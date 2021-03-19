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
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Basic test specification for the GitCommitTask.
 *
 * @author lreimer
 */
class GitCommitTaskSpec extends Specification {
    static final String TEST_GIT_COMMIT = 'testGitCommit'

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = folder.newFolder()
    }

    def "Define GitCommitTask"() {
        given:
        def options = new GitCommitOptions()

        expect: "the commit task to be undefined"
        that project.tasks.findByName(TEST_GIT_COMMIT), is(nullValue())

        when: "we define and configure the commit task"
        def task = (GitCommitTask) project.task(TEST_GIT_COMMIT, type: GitCommitTask) {
            directory = this.directory
            username = 'user'
            password = 'secret'
        }
        task.applyOptions(options)
        task = (GitCommitTask) project.tasks.findByName(TEST_GIT_COMMIT)

        then: "the we expect to find the task to be correctly configured"

        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.username, equalTo('user')
        expect task.password, equalTo('secret')
        expect task.directory, notNullValue()

        task.amend == options.amend
        task.message == options.message
        task.noVerify == options.noVerify
        task.all == options.all
        task.author == options.author
        task.committer == options.committer
    }

    def "Invoke doCommit"() {
        expect: "the commit task to be undefined"
        that project.tasks.findByName(TEST_GIT_COMMIT), is(nullValue())

        when: "we define and invoke the commit task"
        GitCommitTask task = project.task(TEST_GIT_COMMIT, type: GitCommitTask) {
            directory = this.directory
        } as GitCommitTask
        task.doCommit()

        then: "the task is defined but threw an exception"
        expect project.tasks.testGitCommit, notNullValue()
        thrown(GradleException)
    }
}
