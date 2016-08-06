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

import org.eclipse.jgit.api.CloneCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Repository
import org.gradle.api.internal.tasks.options.Option
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git clone.
 *
 * @author lreimer
 */
class GitCloneTask extends AbstractGitTask {
    @Input
    String url
    @Input
    String branch
    @Input
    boolean singleBranch

    boolean cloneAllBranches
    boolean cloneSubmodules

    @Option(option = "no-checkout", description = "Do not checkout branch after clone.")
    boolean noCheckout

    @TaskAction
    def doClone() {
        CloneCommand clone = null;
        Repository repository = null;

        withExceptionHandling('Could not clone Git repository.') {
            clone = Git.cloneRepository()

            clone.setURI(url).setDirectory(directory).setBranch(branch).setBare(false)
            clone.setCredentialsProvider(createCredentialsProvider())

            if (singleBranch && branch.startsWith("refs/")) {
                clone.cloneAllBranches = false
                clone.branchesToClone = [branch]
            } else {
                clone.setCloneAllBranches(cloneAllBranches)
            }

            // set the additional options
            clone.setNoCheckout(noCheckout)
            clone.setCloneSubmodules(cloneSubmodules)
            clone.setTimeout(timeout)

            repository = clone.call().getRepository()
        } always {
            if (repository) {
                repository.close()
            }
        }
    }

    /**
     * Apply the task specific options to this instance.
     *
     * @param options the task options
     */
    void applyOptions(GitCloneOptions options) {
        this.singleBranch = options.singleBranch
        this.noCheckout = options.noCheckout
        this.timeout = options.timeout
        this.cloneAllBranches = options.cloneAllBranches
        this.cloneSubmodules = options.cloneSubmodules
    }
}
