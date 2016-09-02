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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 *
 * @author christian.fritz
 */
class InstallHomebrewTask extends DefaultTask {
    @Input
    File target

    InstallHomebrewTask() {
        group = 'SEU-as-code'
        description = 'Installs homebrew into the SEU.'
    }

    @TaskAction
    void doInstall() {
        if (target.exists()) {
            // update homebrew
            return
        }
        target.mkdirs()
        project.exec {
            commandLine 'curl', '-L', 'https://github.com/Homebrew/brew/tarball/master', '|',
                    'tar', 'xz', '--strip', '1', '-C', 'homebrew'
        }
    }
}
