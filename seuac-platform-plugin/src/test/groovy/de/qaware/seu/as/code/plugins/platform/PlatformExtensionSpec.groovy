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
import spock.lang.Specification
import spock.lang.Unroll

/**
 * The Spock specification to check the proper closure behaviour for a
 * given project and platform.
 *
 * @author lreimer
 */
class PlatformExtensionSpec extends Specification {

    Project project

    void setup() {
        project = Mock(Project)
    }

    @Unroll
    def "Apply #platform specific project dependencies when on #platform is OK"() {
        given: 'we are on windows and an extension instance'
        def extension = new PlatformExtension(project, platform)

        when: 'we apply some dependencies via the windows closure'
        extension.win {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }
        extension.mac {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }
        extension.unix {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }

        then: 'we expect one dependencies invocation'
        1 * project.dependencies { _ }

        where:
        platform << [Platform.Windows, Platform.MacOs, Platform.Unix]
    }

    def "Apply other platform dependencies when on Windows should not work"() {
        given: 'we are on windows and an extension instance'
        def platform = Platform.Windows
        def extension = new PlatformExtension(project, platform)

        when: 'we apply some dependencies via the the non windows closures'
        extension.mac {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }
        extension.unix {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }

        then: 'we expect no dependency invocations'
        0 * project.dependencies { _ }
    }

    def "Apply other platform dependencies when on MacOS should not work"() {
        given: 'we are on MacOs and an extension instance'
        def platform = Platform.MacOs
        def extension = new PlatformExtension(project, platform)

        when: 'we apply some dependencies via the the non MacOs closures'
        extension.win {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }
        extension.unix {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }

        then: 'we expect no dependency invocations'
        0 * project.dependencies { _ }
    }

    def "Apply platform architecture specific project dependencies"() {
        given: 'we are on windows and an extension instance'
        def platform = Platform.Unknown
        def extension = new PlatformExtension(project, platform)

        when: 'we apply some dependencies via the 2 architecture closures'
        extension.x86 {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }
        extension.x86_64 {
            dependencies {
                software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'
            }
        }

        then: 'we expect exactly one dependencies invocation'
        1 * project.dependencies { _ }
    }
}
