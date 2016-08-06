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
 * Basic test specification for the GitPushTask.
 *
 * @author lreimer
 */
class GitPushTaskSpec extends Specification {
    static final String TEST_GIT_PUSH = 'testGitPush'
    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = File.createTempDir()
    }

    void cleanup() {
        directory.deleteDir()
    }

    def "Define GitPushTask"() {
        given:
        def options = new GitPushOptions()

        expect: "the push task to be undefined"
        that project.tasks.findByName(TEST_GIT_PUSH), is(nullValue())

        when: "we define and configure the task for the project"
        def task = (GitPushTask) project.task(TEST_GIT_PUSH, type: GitPushTask) {
            directory = this.directory
            username = 'user'
            password = 'secret'
            remote = 'test'
        }
        task.applyOptions(options)
        task = (GitPushTask) project.tasks.findByName(TEST_GIT_PUSH)

        then: "we expect to find the task correctly configured"

        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.remote, equalTo('test')
        expect task.username, equalTo('user')
        expect task.password, equalTo('secret')
        expect task.directory, notNullValue()

        task.force == options.force
        task.dryRun == options.dryRun
        task.pushAll == options.pushAll
        task.pushTags == options.pushTags
        task.timeout == options.timeout
    }

    def "Invoke doPush"() {
        expect: "the push task to be undefined"
        that project.tasks.findByName(TEST_GIT_PUSH), is(nullValue())

        when: "we define and invoke the task on the project"
        GitPushTask task = project.task(TEST_GIT_PUSH, type: GitPushTask) {
            directory = this.directory
        }
        task.doPush()

        then: "the task is defined by threw an exception"
        expect project.tasks.testGitPush, notNullValue()
        thrown(GradleException)
    }
}
