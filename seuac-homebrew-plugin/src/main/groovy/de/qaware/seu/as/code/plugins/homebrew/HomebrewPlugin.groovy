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
 * The SEU-as-code Homebrew plugin extends the software installation mechanism to use homebrew packages instead self
 * created packages.
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
        if (!project.pluginManager.hasPlugin('de.qaware.seu.as.code.base')) {
            project.pluginManager.apply 'de.qaware.seu.as.code.base'
        }

        createConfigurations project

        if (!macOs) {
            LOGGER.info("No MacOS, currently running on {}! Did not activate Homebrew plugin.", current());
            return;
        }

        project.afterEvaluate {
            Task installBrew = project.task('installBrew', type: InstallHomebrewTask) {
                homebrewBasePath = new File("${project.seuAsCode.layout.software}", 'homebrew')
            }
            installBrew.mustRunAfter 'createSeuacLayout'

            Task applyBrewSoftware = project.task('applyBrewSoftware', type: ApplyBrewSoftwareTask) {
                homebrewBasePath = new File("${project.seuAsCode.layout.software}", 'homebrew')
                brew = project.configurations.brew
                cask = project.configurations.cask
                datastore = project.seuAsCode.datastore
            }
            project.tasks['applySoftware'].dependsOn << applyBrewSoftware

            Task storeSeuacDb = project.task('storeBrewSeuacDb', type: StoreBrewSeuacDbTask, dependsOn: project.tasks['storeSeuacDb']) {
                homebrewBasePath = new File("${project.seuAsCode.layout.software}", 'homebrew')
                datastore = project.seuAsCode.datastore
            }
            project.tasks['storeSeuacDb'].finalizedBy storeSeuacDb
        }
    }

    /**
     * Adds the additional configuration sets.
     * @param project the project.
     */
    private static void createConfigurations(Project project) {
        def brewConfig = project.configurations.create('brew')
        brewConfig.transitive = false

        def caskConfig = project.configurations.create('cask')
        caskConfig.transitive = false
    }
}
