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
package de.qaware.seu.as.code.plugins.git

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Basic test specification for the GitStatusTask.
 *
 * @author lreimer
 */
class GitStatusTaskSpec extends Specification {
    static final String TEST_GIT_STATUS = 'testGitStatus'
    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = File.createTempDir()
    }

    void cleanup() {
        directory.deleteDir()
    }

    def "Define GitStatusTask"() {
        expect:
        that project.tasks.findByName(TEST_GIT_STATUS), is(nullValue())

        when:
        project.task(TEST_GIT_STATUS, type: GitStatusTask) {
            directory = this.directory
        }

        then:
        GitStatusTask task = project.tasks.findByName(TEST_GIT_STATUS)
        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.directory, notNullValue()
    }

    def "Invoke doStatus"() {
        expect:
        that project.tasks.findByName(TEST_GIT_STATUS), is(nullValue())

        when:
        GitStatusTask task = project.task(TEST_GIT_STATUS, type: GitStatusTask) {
            directory = this.directory
        }
        task.doStatus()

        then:
        expect project.tasks.testGitStatus, notNullValue()
        thrown(GradleException)
    }
}
