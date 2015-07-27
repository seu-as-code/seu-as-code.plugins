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
import static spock.util.matcher.HamcrestSupport.that

/**
 * Test specification to check the basic SvnUpdateTask behaviour.
 *
 * @author mario-leander.reimer
 */
class SvnUpdateTaskSpec extends Specification {
    static final String TEST_SVN_UPDATE = 'testSvnUpdate'
    Project project

    def setup() {
        project = ProjectBuilder.builder().build()
    }

    def "Define SvnUpdateTask"() {
        expect:
        that project.tasks.findByName(TEST_SVN_UPDATE), is(nullValue())

        when:
        project.task(TEST_SVN_UPDATE, type: SvnUpdateTask) {
            directory = new File("build")
            username = 'user'
            password = 'pwd'
        }.doUpdate()

        then:
        SvnUpdateTask task = project.tasks.findByName(TEST_SVN_UPDATE)
        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.directory, notNullValue()
        expect task.username, equalTo('user')
        expect task.password, equalTo('pwd')
    }

}
