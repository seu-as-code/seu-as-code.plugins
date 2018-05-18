/*
 *    Copyright (C) 2015 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.seu.as.code.plugins.chocolatey

import de.undercouch.gradle.tasks.download.DownloadAction
import groovy.xml.XmlUtil
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.WorkResult
import org.gradle.process.internal.ExecActionFactory
import org.gradle.process.internal.ExecException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.nio.file.Paths

/**
 * A simple task thats installs the latest version of <a href="https://chocolatey.org/">Chocolatey</a> into the SEU.
 *
 * The target path can be configured.
 */
class InstallChocolateyTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallChocolateyTask.class);

    @Input
    File chocolateyBasePath

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException();
    }

    InstallChocolateyTask() {
        group = 'SEU-as-code'
        description = 'Installs Chocolatey into the SEU.'
    }

    @OutputFile
    public File getOutputFile() {
        // set-env-choco.cmd is created as last step during installation
        // It can be used to check wheter an valid installation exists or not
        return new File(chocolateyBasePath.getParent(),"set-env-choco.cmd")
    }

    @TaskAction
    void doInstall() {
        LOGGER.info('Installing chocolatey into {}', chocolateyBasePath)
        if (chocolateyBasePath.exists()) {
            LOGGER.warn('Skip install of chocolatey. {} exists', chocolateyBasePath)
            return
        }
        chocolateyBasePath.mkdirs()

        File tmpZip = downloadChocolatey()
        File libDirectory = installIntoSoftwareDirectory(tmpZip)
        installChocolateyPackage(tmpZip, libDirectory)
        initializeChocolatey()
        disableNonElevatedRightsWarning()
        copySetEnvFile()
    }

    private WorkResult copySetEnvFile() {
        project.copy {
            from project.resources.text.fromArchiveEntry(project.buildscript.configurations.classpath.find {
                it.name.contains 'seuac-chocolatey-plugin'
            }, 'set-env-choco.cmd').asFile()
            into chocolateyBasePath.getParent()
        }
    }


    private File installIntoSoftwareDirectory(tmpZip) {
        // copy everthing inside of tools/chocolateyInstall/ into chocolateyBasePath at top level
        project.copy {
            from project.zipTree(tmpZip)
            includeEmptyDirs = false
            into chocolateyBasePath
            eachFile { FileCopyDetails fcp ->
                if (fcp.relativePath.pathString.startsWith('tools/chocolateyInstall/')) {
                    // remap the file to the root
                    def segments = fcp.relativePath.segments
                    def pathsegments = segments[2..-1] as String[]
                    fcp.relativePath = new RelativePath(!fcp.file.isDirectory(), pathsegments)
                } else {
                    fcp.exclude()
                }
            }
        }

        // copy redirects into dir bin
        project.copy {
            from new File(chocolateyBasePath, 'redirects')
            include '*.exe'
            include '*.cmd'
            includeEmptyDirs = false
            into new File(chocolateyBasePath, 'bin')
        }

        // create dir lib
        def libDirectory = new File(chocolateyBasePath, 'lib')
        libDirectory.mkdir()
        libDirectory
    }

    private File downloadChocolatey() {
        File tmpZip = File.createTempFile('chocolatey', '.zip')
        def download = new DownloadAction(project)
        download.src 'https://chocolatey.org/api/v2/package/chocolatey/'
        download.dest tmpZip
        download.execute()
        tmpZip
    }

    private WorkResult installChocolateyPackage(File tmpZip, File libDirectory) {
        project.copy {
            from tmpZip
            into libDirectory
            rename (tmpZip.getName(), 'chocolatey.nupkg')
        }
    }

    private void initializeChocolatey() {
        // first execution of chocolatey: check if .NET-Framework is installed, and writes config files
        def action = getExecActionFactory().newExecAction()
        action.commandLine(Paths.get(chocolateyBasePath.path, 'bin', 'choco.exe').toString())
        action.commandLine += ['-v']
        action.environment([
                ChocolateyInstall      : chocolateyBasePath,
                ChocolateyBinRoot      : chocolateyBasePath.getParent(),
                ChocolateyToolsLocation: chocolateyBasePath.getParent()
        ])
        action.workingDir = chocolateyBasePath
        try {
            action.execute()
        } catch (ExecException e) {
            LOGGER.error("Unable to run Chocolately at this time.  It is likely that .Net Framework is not installed or the installation requires a system reboot.")
            throw e
        }
    }

    private void disableNonElevatedRightsWarning() {
        def configFile = new File(chocolateyBasePath, 'config/chocolatey.config')
        def chocolatey = new XmlSlurper().parse(configFile);
        def nonElevatedWarnings = chocolatey.features.feature.find {
            it.@name="showNonElevatedWarnings"
        }
        nonElevatedWarnings.@enabled = "false"
        nonElevatedWarnings.@setExplicitly = "true"
        def writer = new FileWriter(configFile)
        XmlUtil.serialize(chocolatey, writer)
        writer.close()
    }
}
