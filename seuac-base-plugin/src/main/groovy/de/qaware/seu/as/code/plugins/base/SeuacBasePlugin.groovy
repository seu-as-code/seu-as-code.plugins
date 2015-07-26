/*
 *
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
package de.qaware.seu.as.code.plugins.base

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * The SEU as Code base plugin takes care of creating and updating an SEU installation.
 *
 * @author mario-leander.reimer
 */
class SeuacBasePlugin implements Plugin<Project> {
    /**
     * Name of the extension for this plugin.
     */
    static final String EXTENSION_NAME = 'seuAsCode'

    /**
     * Apply all SEU as Code tasks to the project as well as the extension and configurations.
     *
     * @param project the current project
     */
    @Override
    void apply(Project project) {
        // basic plugin specific setup of project
        createConfigurations(project)

        // create the extension for this project
        project.extensions.create(EXTENSION_NAME, SeuacExtension)
        SeuacExtension seuAsCode = project.seuAsCode

        project.afterEvaluate {
            initSeuacClassLoader(project)

            Task createSeuacLayout = project.task('createSeuacLayout', type: CreateSeuacLayoutTask) {
                layout = seuAsCode.layout
                directories = seuAsCode.layout.directories
            }

            Task applySoftware = project.task('applySoftware', type: ApplyConfigurationTask) {
                source = project.configurations.software
                target = seuAsCode.layout.software
                datastore = seuAsCode.datastore
                withEmptyDirs = false
            }

            Task applyHome = project.task('applyHome', type: ApplyConfigurationTask) {
                source = project.configurations.home
                target = seuAsCode.layout.home
                datastore = seuAsCode.datastore
            }

            Task runSoftwareHooks = project.task('runSoftwareHooks', type: RunHooksTask) {
                seuHome = seuAsCode.seuHome
                projectName = seuAsCode.projectName
                target = seuAsCode.layout.software
            }

            Task runHomeHooks = project.task('runHomeHooks', type: RunHooksTask) {
                seuHome = seuAsCode.seuHome
                projectName = seuAsCode.projectName
                target = seuAsCode.layout.home
            }

            Task createAsciiBanner = project.task('createAsciiBanner', type: CreateAsciiBannerTask) {
                projectName = seuAsCode.projectName
                bannerFile = new File(seuAsCode.layout.software, CreateAsciiBannerTask.DEFAULT_FILENAME)
                settings = seuAsCode.banner
            }

            Task storeSeuacDb = project.task('storeSeuacDb', type: StoreSeuacDbTask) {
                datastore = seuAsCode.datastore
            }

            applySoftware.finalizedBy runSoftwareHooks
            applySoftware.mustRunAfter createSeuacLayout

            createAsciiBanner.mustRunAfter runSoftwareHooks

            applyHome.finalizedBy runHomeHooks
            applyHome.mustRunAfter createSeuacLayout

            Task bootstrapSeu = project.task('bootstrapSeu', group: 'SEU as Code', description: 'Bootstrap the complete SEU from 0.')
            bootstrapSeu.dependsOn createSeuacLayout, applySoftware, applyHome
            bootstrapSeu.finalizedBy storeSeuacDb

            Task updateSeu = project.task('updateSeu', group: 'SEU as Code', description: 'Update the complete SEU.')
            updateSeu.dependsOn applySoftware, applyHome
            updateSeu.finalizedBy storeSeuacDb
        }
    }

    private void createConfigurations(Project project) {
        project.configurations.create('seuac')
        project.configurations.create('software')
        project.configurations.create('home')
    }

    private void initSeuacClassLoader(Project project) {
        // causes the test to fail, can not be resolved by ivy
        // for now this needs to be set by the user, which is OK
        // project.dependencies.add('seuac', 'com.h2database:h2:1.3.176')

        def classLoader = project.gradle.class.classLoader
        project.configurations.seuac.each { File f -> classLoader.addURL(f.toURI().toURL()) }
    }
}