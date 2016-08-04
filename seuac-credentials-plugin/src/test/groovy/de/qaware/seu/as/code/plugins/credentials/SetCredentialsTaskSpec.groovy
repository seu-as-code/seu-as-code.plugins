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
package de.qaware.seu.as.code.plugins.credentials

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Basic test specification for the {@link SetCredentialsTask}.
 *
 * @author clboettcher
 */
class SetCredentialsTaskSpec extends Specification {

    private static final String TEST_SET_CREDENTIALS = 'testSetCredentials'

    private Project project
    private CredentialsStorage storage
    private SystemConsole console

    def setup() {
        this.project = ProjectBuilder.builder().build()
        this.storage = Mock(CredentialsStorage)
        this.console = Mock(SystemConsole)
    }

    def "Define SetCredentials task"() {
        expect: "the SetCredentials to be undefined"
        that project.tasks.findByName(TEST_SET_CREDENTIALS), is(nullValue())

        when: "we defined and configure the SetCredentials task"
        project.task(TEST_SET_CREDENTIALS, type: SetCredentialsTask) {
            service "nexus"
            storage this.storage
            console this.console
        }

        then: "we expect to find the task correctly configured"
        SetCredentialsTask task = project.tasks.findByName(TEST_SET_CREDENTIALS)

        expect task, notNullValue()
        expect task.service, equalTo('nexus')
        expect task.group, equalTo('Security')
        expect task.description, not(isEmptyOrNullString())
        expect task.getStorage(), notNullValue()
        expect task.getConsole(), notNullValue()
    }

    def "Invoke SetCredentialsTask with service and no credentials"() {
        setup: "the mocked responses"
        console.readLine("Enter username:") >> 'Max'
        console.readPassword("Enter password:") >> 'Mustermann'.toCharArray()

        SetCredentialsTask task = project.task(TEST_SET_CREDENTIALS, type: SetCredentialsTask) {
            service = "nexus"
            storage = this.storage
            console = this.console
        }

        when: "the task runs"
        task.onAction()

        then: "that credentials are set via the storage"
        1 * this.storage.setCredentials('nexus', 'Max', 'Mustermann'.toCharArray())
    }

    def "Invoke SetCredentialsTask with service and username"() {
        setup: "the mocked responses"
        console.readPassword("Enter password:") >> 'Mustermann'.toCharArray()

        SetCredentialsTask task = project.task(TEST_SET_CREDENTIALS, type: SetCredentialsTask) {
            service = "nexus"
            username = 'John'
            storage = this.storage
            console = this.console
        }

        when: "the task runs"
        task.onAction()

        then: "that credentials are set via the storage"
        1 * this.storage.setCredentials('nexus', 'John', 'Mustermann'.toCharArray())
    }
}
