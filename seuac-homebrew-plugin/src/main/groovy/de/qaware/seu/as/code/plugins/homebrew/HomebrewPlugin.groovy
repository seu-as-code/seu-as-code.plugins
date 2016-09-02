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
package de.qaware.seu.as.code.plugins.homebrew

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static de.qaware.seu.as.code.plugins.base.Platform.current
import static de.qaware.seu.as.code.plugins.base.Platform.isMacOs

/**
 *
 * @author christian.fritz
 */
class HomebrewPlugin implements Plugin<Project> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HomebrewPlugin.class);
    /**
     * Apply this plugin to the given target object.
     *
     * @param project The target object
     */
    @Override
    void apply(Project project) {
        createConfigurations project

        if (!isMacOs()) {
            LOGGER.info("Did not activate Homebrew Plugin. Did not run on {}", current());
            return;
        }

        project.afterEvaluate {
            Task installBrew = project.task('installBrew', type: InstallHomebrewTask) {
                target = project.seuAsCode.layout.software
            }
            installBrew.mustRunAfter 'createSeuacLayout'
        }
    }

    /**
     * Adds the additional configuration sets.
     * @param project the project.
     */
    private static void createConfigurations(Project project) {
        project.configurations.create('brew')
    }
}
