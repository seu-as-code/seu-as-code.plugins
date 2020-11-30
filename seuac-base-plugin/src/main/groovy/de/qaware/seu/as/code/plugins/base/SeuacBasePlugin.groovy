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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * The SEU-as-code base plugin takes care of creating and updating an SEU installation.
 *
 * @author lreimer
 */
class SeuacBasePlugin implements Plugin<Project> {

    /**
     * Apply all SEU-as-code tasks to the project as well as the extension and configurations.
     *
     * @param project the current project
     */
    @Override
    void apply(Project project) {
        // basic plugin specific setup of project
        createConfigurations(project)

        // register extension properties for OS family, classifier and architecture
        def platform = Platform.current()
        setExtraProperties(project, platform)

        // create the extensions for this project
        project.extensions.add(PlatformExtension.NAME, new PlatformExtension(project, platform))
        project.extensions.create(SeuacExtension.NAME, SeuacExtension)
        SeuacExtension seuAsCode = project.seuAsCode
        if (seuAsCode.datastore == null) {
            seuAsCode.datastore = new SeuacDatastore(url: "jdbc:h2:${project.buildDir}/seuac;mv_store=false", user: 'sa', password: 'sa')
        }

        project.afterEvaluate {
            Task createSeuacLayout = project.task('createSeuacLayout', type: CreateSeuacLayoutTask) {
                layout = seuAsCode.layout
                directories = seuAsCode.layout.missingDirectories
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
                seuLayout = seuAsCode.layout
                projectName = seuAsCode.projectName
                target = seuAsCode.layout.software
            }

            Task runHomeHooks = project.task('runHomeHooks', type: RunHooksTask) {
                seuHome = seuAsCode.seuHome
                seuLayout = seuAsCode.layout
                projectName = seuAsCode.projectName
                target = seuAsCode.layout.home
            }

            Task createAsciiBanner = project.task('createAsciiBanner', type: CreateAsciiBannerTask) {
                projectName = seuAsCode.projectName
                bannerFile = new File(seuAsCode.layout.software, DEFAULT_FILENAME)
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

            Task bootstrapSeu = project.task('bootstrapSeu', group: 'SEU-as-code', description: 'Bootstrap the complete SEU.')
            bootstrapSeu.dependsOn createSeuacLayout, applySoftware, applyHome
            bootstrapSeu.finalizedBy storeSeuacDb

            Task updateSeu = project.task('updateSeu', group: 'SEU-as-code', description: 'Update the complete SEU.')
            updateSeu.dependsOn applySoftware, applyHome
            updateSeu.finalizedBy storeSeuacDb

            project.task('destroySeu', type: DestroySeuTask) {
                layout = seuAsCode.layout
                datastore = seuAsCode.datastore
            }
        }
    }

    private static void setExtraProperties(Project project, Platform platform) {
        project.extensions.extraProperties.set('osFamily', platform.osFamily)
        project.extensions.extraProperties.set('osClassifier', platform.osClassifier)
        project.extensions.extraProperties.set('osArch', platform.osArch)
    }

    private static void createConfigurations(Project project) {
        project.configurations.create('software')
        project.configurations.create('home')
    }
}