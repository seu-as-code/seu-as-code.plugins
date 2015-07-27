/*
 *    Copyright 2015 QAware GmbH
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
package de.qaware.seu.as.code.plugins.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.InitCommand
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git init.
 *
 * @author mario-leander.reimer
 */
class GitInitTask extends AbstractGitTask {
    @TaskAction
    def doInit() {
        withExceptionHandling('Could not initialize Git repository.') {
            InitCommand init = Git.init();
            init.setBare(false).setDirectory(directory);
            init.call();
        }
    }
}
