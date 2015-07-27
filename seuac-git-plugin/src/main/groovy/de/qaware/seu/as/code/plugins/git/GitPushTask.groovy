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
import org.eclipse.jgit.api.PushCommand
import org.eclipse.jgit.lib.Constants
import org.gradle.api.tasks.TaskAction

/**
 * The task implementation to perform a Git push operation.
 *
 * @author lreimer
 */
class GitPushTask extends AbstractGitTask {

    String remote = Constants.DEFAULT_REMOTE_NAME
    boolean dryRun = false
    boolean pushAll = false
    boolean pushTags = false

    @TaskAction
    def doPush() {
        Git gitRepo = null;
        withExceptionHandling('Could not push to remote Git repository.') {
            gitRepo = Git.open(directory);

            PushCommand push = gitRepo.push();
            push.setDryRun(dryRun).setRemote(remote).setOutputStream(System.out)
            push.setCredentialsProvider(createCredentialsProvider())
            if (pushAll) {
                push.setPushAll()
            }
            if (pushTags) {
                push.setPushTags()
            }
            push.call()
        } always {
            if (gitRepo) {
                gitRepo.close()
            }
        }
    }
}
