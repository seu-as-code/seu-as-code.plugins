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
import org.eclipse.jgit.api.PullCommand
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.merge.MergeStrategy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

/**
 * The task implementation to perform a Git pull operation.
 *
 * @author lreimer
 */
class GitPullTask extends AbstractGitTask {

    @Input
    String remote = Constants.DEFAULT_REMOTE_NAME
    @Internal
    MergeStrategy strategy = MergeStrategy.RECURSIVE

    @Option(option = "rebase", description = "Perform rebase after fetching.")
    @Input
    boolean rebase = false

    @TaskAction
    def doPull() {
        Git gitRepo = null
        withExceptionHandling('Could not pull from remote Git repository.') {
            gitRepo = Git.open(directory)

            PullCommand pull = gitRepo.pull()
            pull.setRemote(remote)
            pull.setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)))
            pull.setCredentialsProvider(createCredentialsProvider())

            // set the additional options
            pull.setStrategy(strategy)
            pull.setRebase(rebase)
            pull.setTimeout(gitTimeout)

            pull.call()
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
    void applyOptions(GitPullOptions options) {
        this.rebase = options.rebase
        this.strategy = MergeStrategy.get(options.strategy.name())
        this.gitTimeout = options.timeout
    }
}
