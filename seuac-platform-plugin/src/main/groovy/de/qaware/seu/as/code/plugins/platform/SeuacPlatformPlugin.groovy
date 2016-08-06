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
package de.qaware.seu.as.code.plugins.platform

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The SEU-as-code platform plugin allows platform specific project configuration.
 *
 * @author lreimer
 */
class SeuacPlatformPlugin implements Plugin<Project> {
    /**
     * Name of the osFamily extra property.
     */
    static final String OS_FAMILY = 'osFamily'
    /**
     * Name of the osClassifier extra property.
     */
    static final String OS_CLASSIFIER = 'osClassifier'
    /**
     * Name of the osArch extra property.
     */
    static final String OS_ARCH = 'osArch'

    @Override
    void apply(Project project) {
        def platform = Platform.current()

        // register extra properties for OS family, classifier and architecture
        project.extensions.extraProperties.set(OS_FAMILY, platform.osFamily)
        project.extensions.extraProperties.set(OS_CLASSIFIER, platform.osClassifier)
        project.extensions.extraProperties.set(OS_ARCH, platform.osArch)

        // register extension to project
        project.extensions.add(PlatformExtension.NAME, new PlatformExtension(project, platform))
    }
}