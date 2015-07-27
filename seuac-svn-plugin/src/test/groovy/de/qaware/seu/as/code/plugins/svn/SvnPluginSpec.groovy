/*
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
package de.qaware.seu.as.code.plugins.svn

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect

/**
 * The test specification for the SvnPlugin behaviour.
 *
 * @author lreimer
 */
class SvnPluginSpec extends Specification {
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Apply plugin to project"() {
        when:
        project.apply plugin: 'seuac-svn'
        project.subversion {
            test1 {
                url = 'http://localhost'
                directory = new File('build')
                username = 'user'
                password = 'pwd'
            }
            test2 {
                url 'http://localhost'
                directory new File('build')
                username 'user'
                password 'pwd'
            }
        }
        project.evaluate()

        then:
        expect project.extensions.findByName(SvnPlugin.EXTENSION_NAME), notNullValue()

        def svnCheckoutAll = project.tasks.svnCheckoutAll
        expect svnCheckoutAll, notNullValue()
        expect svnCheckoutAll.group, equalTo('Version Control')

        def svnUpdateAll = project.tasks.svnUpdateAll
        expect svnUpdateAll, notNullValue()
        expect svnUpdateAll.group, equalTo('Version Control')

        def svnCheckoutTest = project.tasks.svnCheckoutTest1
        expect svnCheckoutTest, notNullValue()
        expect svnCheckoutTest.description, notNullValue()
        expect svnCheckoutTest.url, equalTo('http://localhost')
        expect svnCheckoutTest.directory, equalTo(new File('build'))
        expect svnCheckoutTest.username, equalTo('user')
        expect svnCheckoutTest.password, equalTo('pwd')

        def svnUpdateTest = project.tasks.svnUpdateTest2
        expect svnUpdateTest, notNullValue()
        expect svnUpdateTest.description, notNullValue()
        expect svnUpdateTest.directory, equalTo(new File('build'))
        expect svnUpdateTest.username, equalTo('user')
        expect svnUpdateTest.password, equalTo('pwd')

        expect svnCheckoutAll.dependsOn, hasSize(3)
        expect svnCheckoutAll.dependsOn, hasItem(svnCheckoutTest)
        expect svnCheckoutAll.dependsOn, hasItem(project.tasks.svnCheckoutTest2)

        expect svnUpdateAll.dependsOn, hasSize(3)
        expect svnUpdateAll.dependsOn, hasItem(svnUpdateTest)
        expect svnUpdateAll.dependsOn, hasItem(project.tasks.svnUpdateTest1)
    }
}
