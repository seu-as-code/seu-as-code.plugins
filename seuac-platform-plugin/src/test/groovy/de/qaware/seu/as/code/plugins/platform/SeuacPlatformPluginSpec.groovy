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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Test specification for the SeuacPlatformPlugin.
 *
 * @author lreimer
 */
class SeuacPlatformPluginSpec extends Specification {

    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Apply SeuacPlatformPlugin and check tasks"() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-platform'
        project.apply plugin: 'java'

        project.platform {
            win {
                dependencies {
                    testImplementation group: 'junit', name: 'junit', version: '4.12'
                }
            }
            mac {
                dependencies {
                    testImplementation group: 'junit', name: 'junit', version: '4.12'
                }
            }
            unix {
                dependencies {
                    testImplementation group: 'junit', name: 'junit', version: '4.12'
                }
            }
        }

        when: "we evaluate the project"
        project.evaluate()

        then: "the extentions and all tasks are registered"
        expect project.extensions.findByName(PlatformExtension.NAME), notNullValue()

        expect project.extensions.extraProperties.get('osFamily'), equalTo(Platform.current().osFamily)
        expect project.extensions.extraProperties.get('osClassifier'), equalTo(Platform.current().osClassifier)
        expect project.extensions.extraProperties.get('osArch'), equalTo(Platform.current().osArch)
    }
}
