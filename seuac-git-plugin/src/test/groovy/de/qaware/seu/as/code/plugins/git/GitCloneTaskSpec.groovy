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
 * Basic test specification for the GitCloneTask.
 *
 * @author lreimer
 */
class GitCloneTaskSpec extends Specification {
    static final String TEST_GIT_CLONE = 'testGitClone'

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = folder.newFolder()
    }

    def "Define GitCloneTask"() {
        given:
        def options = new GitCloneOptions()

        expect: "the clone task to be undefined"
        that project.tasks.findByName(TEST_GIT_CLONE), is(nullValue())

        when: "we defined and configure the clone task"
        def task = (GitCloneTask) project.task(TEST_GIT_CLONE, type: GitCloneTask) {
            url = "https://github.com/qaware/QAseuac.git"
            directory = new File(this.directory, "QAseuac")
            branch = 'TEST'
            username = 'user'
            password = 'secret'
        }
        task.applyOptions(options)
        task = (GitCloneTask) project.tasks.findByName(TEST_GIT_CLONE)

        then: "we expect to find the task correctly configured"

        expect task, notNullValue()
        expect task.group, equalTo('Version Control')
        expect task.url, equalTo('https://github.com/qaware/QAseuac.git')
        expect task.branch, equalTo('TEST')
        expect task.username, equalTo('user')
        expect task.password, equalTo('secret')
        expect task.directory, notNullValue()

        task.singleBranch == options.singleBranch
        task.cloneAllBranches == options.cloneAllBranches
        task.cloneSubmodules == options.cloneSubmodules
        task.noCheckout == options.noCheckout
        task.gitTimeout == options.timeout
    }

    def "Invoke doClone"() {
        expect: "the clone task to be undefined"
        that project.tasks.findByName(TEST_GIT_CLONE), is(nullValue())

        when: "we create the task and invoke clone"
        project.configurations.create('jgit')
        GitCloneTask task = project.task(TEST_GIT_CLONE, type: GitCloneTask) {
            url = "https://github.com/qaware/QAseuac.git"
            directory = new File(this.directory, "QAseuac")
        } as GitCloneTask
        task.doClone()

        then: "the task is defined but threw an exception"
        expect project.tasks.testGitClone, notNullValue()
        thrown(GradleException)
    }

    def "Invoke doClone with single task"() {
        expect:
        that project.tasks.findByName(TEST_GIT_CLONE), is(nullValue())

        when:
        project.configurations.create('jgit')
        GitCloneTask task = project.task(TEST_GIT_CLONE, type: GitCloneTask) {
            url = "https://github.com/qaware/QAseuac.git"
            directory = new File(this.directory, "QAseuac")
            singleBranch = true
            branch = "refs/heads/base-plugin"
        } as GitCloneTask
        task.doClone()

        then:
        expect project.tasks.testGitClone, notNullValue()
        thrown(GradleException)
    }
}
