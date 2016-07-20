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
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Test specification for the SeuacBasePlugin.
 *
 * @author lreimer
 */
class SeuacBasePluginSpec extends Specification {

    File home
    SeuacLayout defaultLayout
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
        project.repositories.flatDir {
            dirs new File(RunHooksTaskSpec.getResource("/").toURI())
        }

        home = File.createTempDir()
        defaultLayout = defaultLayout(home)
    }

    def "Apply SeuacBasePlugin and check tasks"() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-base'

        project.dependencies.add('seuac', ':h2:1.3.176')
        project.dependencies.add('software', ':seuac-test:1.0.0@zip')

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }

        project.platform {
            win {
                dependencies.add('software', ':seuac-win:1.0.0@zip')
            }
            mac {
                dependencies.add('software', ':seuac-mac:1.0.0@zip')
            }
            unix {
                dependencies.add('software', ':seuac-unix:1.0.0@zip')
            }
        }

        when: "we evaluate the project"
        project.evaluate()

        then: "the extentions and all tasks are registered"
        expect project.extensions.findByName(SeuacExtension.NAME), notNullValue()
        expect project.extensions.findByName(PlatformExtension.NAME), notNullValue()

        expect project.tasks.bootstrapSeu, notNullValue()
        expect project.tasks.updateSeu, notNullValue()
        expect project.tasks.destroySeu, notNullValue()
        expect project.tasks.createSeuacLayout, notNullValue()
        expect project.tasks.applySoftware, notNullValue()
        expect project.tasks.runSoftwareHooks, notNullValue()
        expect project.tasks.applyHome, notNullValue()
        expect project.tasks.runHomeHooks, notNullValue()
        expect project.tasks.storeSeuacDb, notNullValue()

        expect project.extensions.extraProperties.get('osFamily'), equalTo(Platform.current().osFamily)
        expect project.extensions.extraProperties.get('osClassifier'), equalTo(Platform.current().osClassifier)
        expect project.extensions.extraProperties.get('osArch'), equalTo(Platform.current().osArch)
    }
}
