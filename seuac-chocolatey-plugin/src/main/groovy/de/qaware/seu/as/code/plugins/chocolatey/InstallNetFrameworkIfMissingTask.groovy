/*
 *    Copyright (C) 2018 QAware GmbH
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

import de.qaware.seu.as.code.plugins.base.Platform
import de.undercouch.gradle.tasks.download.DownloadAction
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecResult
import org.gradle.process.internal.ExecActionFactory
import org.gradle.process.internal.ExecException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject

/**
 * A simple task that installs the latest version of DotNET into the system root directory.
 */
class InstallNetFrameworkIfMissingTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallNetFrameworkIfMissingTask.class);
    public static
    final String NET_INSTALLER_URL = 'https://download.microsoft.com/download/9/5/A/95A9616B-7A37-4AF6-BC36-D6EA96C8DAAE/dotNetFx40_Full_x86_x64.exe'


    File netInstallationPath = new File("${System.env.SystemRoot}" +
            "/Microsoft.Net/Framework${Platform.is64bit() ? '64' : ''}/v4.0.30319");

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException();
    }

    InstallNetFrameworkIfMissingTask() {
        group = 'SEU-as-code'
        description = 'Installs Chocolatey into the SEU.'
    }

    @OutputDirectory
    public File getOutputDirectory() {
        return netInstallationPath
    }

    @TaskAction
    void doInstall() {
        if (netInstallationPath.exists()) {
            LOGGER.info('.NetFramework already installed at {}', netInstallationPath)
            return
        }
        LOGGER.info('.NetFramework not found. It will be installed now.')
        File installerFile = downloadInstaller()
        installNetFramework(installerFile)
    }

    private void installNetFramework(File installerFile) {
        def action = getExecActionFactory().newExecAction()
        action.commandLine('cmd', '/c', installerFile)
        action.commandLine += ['/q', '/norestart', '/repair']
        action.ignoreExitValue

        ExecResult result
        try {
            result = action.execute()
        } catch (ExecException e) {
            LOGGER.error("Unable to run .NetFramework installer.")
            throw e
        }

        int exitValue = result.exitValue
        if (exitValue == 0 || exitValue == 3010) {
            LOGGER.info("Installed .NetFramework successfully.")
            if (exitValue == 3010) {
                LOGGER.warn("Installation of .NetFramework requires restart!.")
            }
        } else {
            LOGGER.error("Failed to install .NetFramework. Error Code: {}", exitValue)
            LOGGER.error("For more details on the error code see {}",
                    "https://msdn.microsoft.com/library/ee942965(v=VS.100).aspx#troubleshooting")
            throw new GradleException("Failed to install .NetFramework")
        }

    }

    private File downloadInstaller() {
        File installerFile = File.createTempFile('dotNetFx40_Client_x86_x64', '.exe')
        def download = new DownloadAction(project)
        download.src NET_INSTALLER_URL
        download.dest installerFile
        download.execute()
        LOGGER.info('Downloading {} to {} - the installer is 40+ MBs, so this could take a while on a slow connection.',
                NET_INSTALLER_URL, installerFile)
        return installerFile
    }

}
