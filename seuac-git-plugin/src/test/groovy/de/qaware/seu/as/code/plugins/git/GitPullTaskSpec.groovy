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
 * Basic test specification for the GitPullTask.
 *
 * @author lreimer
 */
class GitPullTaskSpec extends Specification {
    static final String TEST_GIT_PULL = 'testGitPull'
    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = File.createTempDir()
    }

    void cleanup() {
        directory.deleteDir()
    }

    def "Define GitPullTask"() {
        expect: "the pull task to be undefined"
        that project.tasks.findByName(TEST_GIT_PULL), is(nullValue())

        when: "we we defined and configure the task"
        project.task(TEST_GIT_PULL, type: GitPullTask) {
            directory = this.directory
            username = 'user'
            password = 'secret'
            remote = 'test'
        }

        then: "we expect to find the task correctly configured"
        GitPullTask task = project.tasks.findByName(TEST_GIT_PULL)

        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.remote, equalTo('test')
        expect task.username, equalTo('user')
        expect task.password, equalTo('secret')
        expect task.directory, notNullValue()
    }

    def "Invoke doPull"() {
        expect: "the pull task to be undefined"
        that project.tasks.findByName(TEST_GIT_PULL), is(nullValue())

        when: "we defined and invoke the pull task"
        GitPullTask task = project.task(TEST_GIT_PULL, type: GitPullTask) {
            directory = this.directory
        }
        task.doPull()

        then: "the task is defined but threw an exception"
        expect project.tasks.testGitPull, notNullValue()
        thrown(GradleException)
    }
}
