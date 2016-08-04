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

import groovy.transform.PackageScope

/**
 * The options configuration class for the different Git commands the plugin supports.
 * This allows to set additional command specific configuration options.
 *
 * @author lreimer
 */
class GitOptions {

    @PackageScope
    final GitPullOptions pull = new GitPullOptions()

    @PackageScope
    final GitCloneOptions clone = new GitCloneOptions()

    @PackageScope
    final GitPushOptions push = new GitPushOptions()

    /**
     * Apply the closure to the GitPullOptions.
     *
     * @param closure the configuration closure
     */
    void pull(Closure closure) {
        closure.delegate = pull
        closure()
    }

    /**
     * Apply the closure to the GitCloneOptions.
     *
     * @param closure the configuration closure
     */
    void clone(Closure closure) {
        closure.delegate = clone
        closure()
    }

    /**
     * Apply the closure to the GitPushOptions.
     *
     * @param closure the configuration closure
     */
    void push(Closure closure) {
        closure.delegate = push
        closure()
    }
}
