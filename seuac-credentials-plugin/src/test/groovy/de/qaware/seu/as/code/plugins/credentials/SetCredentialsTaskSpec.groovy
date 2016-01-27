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

import de.qaware.seu.as.code.plugins.credentials.util.IOSupport
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
    private Credentials credentials
    private IOSupport ioSupport

    def setup() {
        this.project = ProjectBuilder.builder().build()
        this.credentials = Mock(Credentials)
        this.ioSupport = Mock(IOSupport)
    }

    def "Define SetCredentials task"() {
        expect: "the SetCredentials to be undefined"
        that project.tasks.findByName(TEST_SET_CREDENTIALS), is(nullValue())

        when: "we defined and configure the SetCredentials task"
        project.task(TEST_SET_CREDENTIALS, type: SetCredentialsTask) {
            key "myKey"
            credentials this.credentials
            ioSupport this.ioSupport
        }

        then: "we expect to find the task correctly configured"
        SetCredentialsTask task = project.tasks.findByName(TEST_SET_CREDENTIALS)

        expect task, notNullValue()
        expect task.key, equalTo('myKey')
        expect task.group, equalTo('SEU-as-Code')
        expect task.description, not(isEmptyOrNullString())
        expect task.getCredentials(), notNullValue()
        expect task.getIoSupport(), notNullValue()
    }

    def "Invoke SetCredentialsTask with key"() {
        setup: "we define the task to remove a stored credential"
        // Simulate user input
        ioSupport.readLine() >> 'value'

        SetCredentialsTask task = project.task(TEST_SET_CREDENTIALS, type: SetCredentialsTask) {
            key = "key"
            credentials = this.credentials
            ioSupport = this.ioSupport
        }

        when: "the task runs"
        task.onAction()

        then: "that entry is set and saved"
        1 * this.credentials.set('key', 'value')
        1 * this.credentials.save()
    }
}
