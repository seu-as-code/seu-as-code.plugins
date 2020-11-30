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
package de.qaware.seu.as.code.plugins.homebrew

import de.undercouch.gradle.tasks.download.DownloadAction
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.file.Paths

/**
 * A simple task thats installs the latest version of <a href="http://brew.sh/">Homebrew</a> into the SEU.
 *
 * The target path can be configured.
 */
class InstallHomebrewTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallHomebrewTask.class)

    @Input
    File homebrewBasePath

    InstallHomebrewTask() {
        group = 'SEU-as-code'
        description = 'Installs Homebrew into the SEU.'
    }

    @TaskAction
    void doInstall() {
        if (homebrewBasePath.exists()) {
            LOGGER.warn('Skip install of homebrew. {} exists', homebrewBasePath)
            return
        }
        LOGGER.info('Installing homebrew into {}', homebrewBasePath)
        homebrewBasePath.mkdirs()

        def tmpTar = File.createTempFile('homebrewInst', '.zip')
        tmpTar.deleteOnExit()

        def download = new DownloadAction(project)
        download.src 'https://github.com/Homebrew/brew/archive/master.zip'
        download.dest tmpTar
        download.execute()

        project.copy {
            from project.zipTree(tmpTar)
            includeEmptyDirs = false
            into homebrewBasePath
            eachFile { FileCopyDetails details ->
                def path = Paths.get details.path
                if (path.getNameCount() > 1) {
                    details.path = details.path - (path.subpath(0, 1).toString() + '/')
                }
            }
        }
    }
}
