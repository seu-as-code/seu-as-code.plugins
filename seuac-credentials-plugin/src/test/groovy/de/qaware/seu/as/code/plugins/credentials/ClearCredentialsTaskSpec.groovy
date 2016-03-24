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

import de.qaware.seu.as.code.plugins.credentials.impl.ConsoleReader
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Basic test specification for the {@link ClearCredentialsTask}.
 *
 * @author clboettcher
 */
class ClearCredentialsTaskSpec extends Specification {

    private static final String TEST_CLEAR_CREDENTIALS = 'testClearCredentials'
    private Project project
    private Credentials credentials
    private ConsoleReader consoleReader

    def setup() {
        this.project = ProjectBuilder.builder().build()
        this.credentials = Mock(Credentials)
        this.consoleReader = Mock(ConsoleReader)
    }


    def "Define ClearCredentials task"() {
        expect: "the ClearCredentialsTask to be undefined"
        that project.tasks.findByName(TEST_CLEAR_CREDENTIALS), is(nullValue())

        when: "we defined and configure the ClearCredentialsTask task"
        project.task(TEST_CLEAR_CREDENTIALS, type: ClearCredentialsTask) {
            key "myKey"
            credentials this.credentials
            consoleReader this.consoleReader
        }

        then: "we expect to find the task correctly configured"
        ClearCredentialsTask task = project.tasks.findByName(TEST_CLEAR_CREDENTIALS)

        expect task, notNullValue()
        expect task.key, equalTo('myKey')
        expect task.group, equalTo('SEU-as-code')
        expect task.description, not(isEmptyOrNullString())
        expect task.getCredentials(), notNullValue()
        expect task.getConsoleReader(), notNullValue()
    }

    def "Invoke ClearCredentials task with stored key"() {
        setup: "we define the task to remove a stored credential"
        // Simulate user confirms deletion
        consoleReader.readLine() >> 'y'

        ClearCredentialsTask task = project.task(TEST_CLEAR_CREDENTIALS, type: ClearCredentialsTask) {
            key = "toRemove"
            credentials = this.credentials
            consoleReader = this.consoleReader
        }

        when: "the task runs"
        task.onAction()

        then: "that entry is checked and removed"
        1 * this.credentials.get('toRemove') >> 'value'
        1 * this.credentials.remove('toRemove')
        1 * this.credentials.save()
    }

    def "Invoke ClearCredentials task with unknown key"() {
        setup: "we define the task to remove an a not stored credential"
        // Simulate user confirms deletion
        consoleReader.readLine() >> 'y'

        ClearCredentialsTask task = project.task(TEST_CLEAR_CREDENTIALS, type: ClearCredentialsTask) {
            key = "unknown"
            credentials = this.credentials
            consoleReader = this.consoleReader
        }

        when: "the task runs"
        task.onAction()

        then: "that entry does not exist. The task action is aborted."
        1 * this.credentials.get('unknown') >> null
        0 * this.credentials.remove(_)
        0 * this.credentials.save()
    }

    def "Invoke ClearCredentials task without a key"() {
        setup: "we define the task to remove all credentials"
        // Simulate user confirms deletion
        consoleReader.readLine() >> 'y'

        ClearCredentialsTask task = project.task(TEST_CLEAR_CREDENTIALS, type: ClearCredentialsTask) {
            credentials = this.credentials
            consoleReader = this.consoleReader
        }

        when: "the task runs"
        task.onAction()

        then: "all entries are cleared"
        1 * this.credentials.clear()
        1 * this.credentials.save()
    }

    def "Invoke ClearCredentials task and user declines deletion"() {
        setup: "we define the task to remove all credentials and the user to decline the deletion"
        // Simulate user declines deletion
        consoleReader.readLine() >> 'n'

        ClearCredentialsTask task = project.task(TEST_CLEAR_CREDENTIALS, type: ClearCredentialsTask) {
            credentials = this.credentials
            consoleReader = this.consoleReader
        }

        when: "the task runs"
        task.onAction()

        then: "the task is aborted. No entries are cleared"
        0 * this.credentials.clear()
        0 * this.credentials.save()
    }
}
