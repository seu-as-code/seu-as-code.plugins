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

import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.api.Git
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git commit.
 *
 * @author mario-leander.reimer
 */
class GitCommitTask extends AbstractGitTask {

    boolean all = true
    String message = ''

    @TaskAction
    def doCommit() {
        Git gitRepo = null;
        withExceptionHandling('Could not commit changes to Git repository.') {
            gitRepo = Git.open(directory);

            CommitCommand commit = gitRepo.commit();
            commit.setMessage(message()).setAll(all)
            commit.call()
        } always {
            if (gitRepo) {
                gitRepo.close()
            }
        }
    }

    private String message() {
        // either use explicit message if set or message project property
        message ?: project.property('message')
    }
}
