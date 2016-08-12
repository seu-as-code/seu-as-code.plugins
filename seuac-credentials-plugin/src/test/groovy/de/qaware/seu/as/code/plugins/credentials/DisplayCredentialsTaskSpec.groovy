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
 * Basic test specification for the {@link DisplayCredentialsTask}.
 *
 * @author lreimer
 */
class DisplayCredentialsTaskSpec extends Specification {

    private static final String TEST_DISPLAY_CREDENTIALS = 'testDisplayCredentials'

    private Project project
    private CredentialsStorageFactory storageFactory
    private CredentialsStorage storage
    private SystemConsole console

    def setup() {
        this.project = ProjectBuilder.builder().build()
        this.storageFactory = Mock(CredentialsStorageFactory)
        this.storage = Mock(CredentialsStorage)
        this.console = Mock(SystemConsole)

        this.storageFactory.create() >> storage
    }

    def "Define DisplayCredentials task"() {
        expect: "the DisplayCredentials to be undefined"
        that project.tasks.findByName(TEST_DISPLAY_CREDENTIALS), is(nullValue())

        when: "we defined and configure the DisplayCredentials task"
        project.task(TEST_DISPLAY_CREDENTIALS, type: DisplayCredentialsTask) {
            service "nexus"
            storageFactory this.storageFactory
            console this.console
        }

        then: "we expect to find the task correctly configured"
        DisplayCredentialsTask task = project.tasks.findByName(TEST_DISPLAY_CREDENTIALS)

        expect task, notNullValue()
        expect task.service, equalTo('nexus')
        expect task.group, equalTo('Security')
        expect task.description, not(isEmptyOrNullString())
        expect task.getStorage(), notNullValue()
        expect task.getStorageFactory(), notNullValue()
        expect task.getConsole(), notNullValue()
    }

    def "Invoke DisplayCredentialsTask with service"() {
        given: "the mocked responses"
        storage.findCredentials('nexus') >> new Credentials('Max', 'Mustermann')

        DisplayCredentialsTask task = project.task(TEST_DISPLAY_CREDENTIALS, type: DisplayCredentialsTask) {
            service = 'nexus'
            storageFactory = this.storageFactory
            console = this.console
        }

        when: "the task runs"
        task.onAction()

        then: "that credentials are set via the storage"
        1 * console.format(_, 'nexus', 'Max', 'Mustermann')
    }

    def "Invoke DisplayCredentialsTask for unknown service"() {
        given: "the mocked responses"
        storage.findCredentials('nexus') >> null

        DisplayCredentialsTask task = project.task(TEST_DISPLAY_CREDENTIALS, type: DisplayCredentialsTask) {
            service = 'nexus'
            storageFactory = this.storageFactory
            console = this.console
        }

        when: "the task runs"
        task.onAction()

        then: "that credentials are set via the storage"
        1 * console.format(_, 'nexus')
    }

}
