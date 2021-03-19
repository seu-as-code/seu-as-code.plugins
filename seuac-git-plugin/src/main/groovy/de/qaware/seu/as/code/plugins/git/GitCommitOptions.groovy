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

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Nested

/**
 * Simple data class to model additional options for the GitCommitTask.
 *
 * @author lreimer
 */
class GitCommitOptions {
    @Input
    String message = ''

    @Input
    boolean all = true
    @Input
    boolean noVerify
    @Input
    boolean amend

    @Nested
    private GitUser author
    @Nested
    private GitUser committer

    /**
     * Set the author information.
     *
     * @param closure the configuration closure
     */
    void author(Closure closure) {
        this.author = new GitUser()
        closure.delegate = this.author
        closure()
    }

    /**
     * Return the author.
     *
     * @return the author, may be NULL if unset
     */
    GitUser getAuthor() {
        return this.author
    }

    /**
     * Set the committer information.
     *
     * @param closure the configuration closure
     */
    void committer(Closure closure) {
        this.committer = new GitUser()
        closure.delegate = this.committer
        closure()
    }

    /**
     * Return the committer.
     *
     * @return the committer, may be NULL
     */
    GitUser getCommitter() {
        return committer
    }
}
