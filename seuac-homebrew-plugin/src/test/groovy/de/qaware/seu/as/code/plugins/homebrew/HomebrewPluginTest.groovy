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

import de.qaware.seu.as.code.plugins.base.SeuacExtension
import de.qaware.seu.as.code.plugins.base.SeuacLayout
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Requires
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Unit test for the {@link HomebrewPlugin}.
 */
@Requires({ os.macOs })
class HomebrewPluginTest extends Specification {

    File home
    SeuacLayout defaultLayout
    Project project

    void setup() {
        project = ProjectBuilder.builder().build()

        home = File.createTempDir()
        defaultLayout = defaultLayout(home)
    }

    def "Apply SeuacHomeBrewPlugin and check tasks"() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-homebrew'

        project.dependencies.add('brew', ':maven3:')

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }
        when: "we evaluate the project"
        project.evaluate()

        then: "the extentions and all tasks are registered"
        expect project.extensions.findByName(SeuacExtension.NAME), notNullValue()

        expect project.tasks.applySoftware, notNullValue()
        expect project.tasks.storeSeuacDb, notNullValue()

        expect project.tasks.installBrew, notNullValue()
        expect project.tasks.applyBrewSoftware, notNullValue()
        expect project.tasks.storeBrewSeuacDb, notNullValue()
    }
}
