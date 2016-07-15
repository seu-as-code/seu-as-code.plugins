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
package de.qaware.seu.as.code.plugins.base

import org.gradle.api.Project

/**
 * The extension to configure actual platform specific behaviour for a project.
 *
 * @author lreimer
 */
class PlatformExtension {
    /**
     * Name of this extension for this plugin.
     */
    static final String NAME = 'platform'

    private final Project project
    private final Platform platform

    /**
     * Initialize the extension instance with the actual project
     * we want to configure.
     *
     * @param project the Gradle project instance
     */
    PlatformExtension(Project project, Platform platform) {
        this.project = project
        this.platform = platform
    }

    /**
     * Evaluates the given closure on the current project of the current platform
     * we are running on is Windows.
     *
     * @param closure the closure to apply to the current project
     */
    def windows(Closure closure) {
        if (platform == Platform.Windows) {
            closure.delegate = project
            closure()
        }
    }

    /**
     * Evaluates the given closure on the current project of the current platform
     * we are running on is MacOs.
     *
     * @param closure the closure to apply to the current project
     */
    def macOs(Closure closure) {
        if (platform == Platform.MacOs) {
            closure.delegate = project
            closure()
        }
    }

    /**
     * Evaluates the given closure on the current project of the current platform
     * we are running on is Unix or Linux.
     *
     * @param closure the closure to apply to the current project
     */
    def unix(Closure closure) {
        if (platform == Platform.Unix) {
            closure.delegate = project
            closure()
        }
    }

    /**
     * Evaluates the given closure on the current project of the current platform
     * we are running on is x86.
     *
     * @param closure the closure to apply to the current project
     */
    def x86(Closure closure) {
        if (!Platform.is64bit()) {
            closure.delegate = project
            closure()
        }
    }

    /**
     * Evaluates the given closure on the current project of the current platform
     * we are running on is x86_64.
     *
     * @param closure the closure to apply to the current project
     */
    def x86_64(Closure closure) {
        if (Platform.is64bit()) {
            closure.delegate = project
            closure()
        }
    }
}
