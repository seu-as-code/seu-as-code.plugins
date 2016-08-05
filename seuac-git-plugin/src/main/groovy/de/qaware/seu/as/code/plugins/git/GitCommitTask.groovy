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

import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.api.Git
import org.gradle.api.internal.tasks.options.Option
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git commit.
 *
 * @author lreimer
 */
class GitCommitTask extends AbstractGitTask {

    @Option(option = "message", description = "The commit message.")
    String message = ''

    boolean all = true
    boolean noVerify
    boolean amend

    GitUser committer
    GitUser author

    @TaskAction
    def doCommit() {
        Git gitRepo = null;
        withExceptionHandling('Could not commit changes to Git repository.') {
            gitRepo = Git.open(directory);

            CommitCommand commit = gitRepo.commit();
            commit.setMessage(message)

            // set committer and author if set
            if (committer != null) {
                commit.setCommitter(committer.username, committer.email)
            }
            if (author != null) {
                commit.setAuthor(author.username, author.email)
            }

            // set the options
            commit.setAll(all).setAmend(amend).setNoVerify(noVerify)

            commit.call()
        } always {
            if (gitRepo) {
                gitRepo.close()
            }
        }
    }

    /**
     * Apply the task specific options to this instance.
     *
     * @param options the task options
     */
    void applyOptions(GitCommitOptions options) {
        this.message = options.message
        this.all = options.all
        this.noVerify = options.noVerify
        this.amend = options.amend
        this.committer = options.committer
        this.author = options.author
    }

}
