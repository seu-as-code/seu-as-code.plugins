/*
 *    Copyright (C) 2018 QAware GmbH
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
package de.qaware.seu.as.code.plugins.chocolatey

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static de.qaware.seu.as.code.plugins.base.Platform.current
import static de.qaware.seu.as.code.plugins.base.Platform.isWindows

/**
 * The SEU-as-code Chocolatey plugin extends the software installation mechanism to use chocolatey packages instead self
 * created packages.
 */
class ChocolateyPlugin implements Plugin<Project> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChocolateyPlugin.class);

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

        if (!isWindows()) {
            LOGGER.info("No Windows, currently running on {}! Did not activate Chocolatey plugin.", current());
            return;
        }

        project.afterEvaluate {
            Task installChoco = project.task('installChoco', type: InstallChocolateyTask) {
                chocolateyBasePath = new File("${project.seuAsCode.layout.software}", 'chocolatey')
            }
            installChoco.mustRunAfter 'createSeuacLayout'
            project.tasks['bootstrapSeu'].dependsOn installChoco

            Task installNetFramework = project.task('installNetFrameworkIfMissing', type: InstallNetFrameworkIfMissingTask)
            installChoco.dependsOn installNetFramework

            Task applyChocoSoftware = project.task('applyChocoSoftware', type: ApplyChocolateySoftwareTask) {
                chocolateyBasePath = new File("${project.seuAsCode.layout.software}", 'chocolatey')
                choco = project.configurations.choco
                datastore = project.seuAsCode.datastore
            }
            applyChocoSoftware.dependsOn installChoco
            project.tasks['applySoftware'].dependsOn applyChocoSoftware

            Task storeSeuacDb = project.task('storeChocoSeuacDb', type: StoreChocolateySeuacDbTask, dependsOn: project.tasks['storeSeuacDb']) {
                chocolateyBasePath = new File("${project.seuAsCode.layout.software}", 'chocolatey')
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
        def chocolateyConfig = project.configurations.create('choco')
        chocolateyConfig.transitive = false
    }
}
