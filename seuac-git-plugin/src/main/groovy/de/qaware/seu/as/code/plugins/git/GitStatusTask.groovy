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
package de.qaware.seu.as.code.plugins.git

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.StatusCommand
import org.eclipse.jgit.lib.TextProgressMonitor
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git status operation.
 *
 * @author lreimer
 */
class GitStatusTask extends AbstractGitTask {

    @TaskAction
    def doStatus() {
        Git gitRepo = null
        withExceptionHandling('Could not get status for Git repository.') {
            gitRepo = Git.open(directory)

            StatusCommand status = gitRepo.status()
            status.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
            status.call()
        } always {
            if (gitRepo) {
                gitRepo.close()
            }
        }
    }
}
