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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect

/**
 * The test specification for the GitPlugin behaviour.
 *
 * @author lreimer
 */
class GitPluginSpec extends Specification {
    Project project
    File directory

    def setup() {
        project = ProjectBuilder.builder().build()
        directory = File.createTempDir()
    }

    void cleanup() {
        directory.deleteDir()
    }

    def "Apply Git plugin to project"() {
        when: "we apply and configure the plugin via its convention"
        project.apply plugin: 'seuac-git'
        project.git {
            code {
                url = "https://github.com/qaware/QAseuac.git"
                directory = new File(this.directory, "code")
                branch = 'TEST'
                username = 'lreimer'
                password = 'secret'
                options {
                    clone {
                        singleBranch = true
                        cloneSubmodules = true
                        noCheckout = true
                        timeout = 300
                    }
                }
            }
            wiki {
                url "https://github.com/qaware/QAseuac.wiki.git"
                directory = new File(this.directory, "wiki")
            }
        }
        project.evaluate()

        then: "the extension is registered and all tasks are defined correctly"
        expect project.extensions.findByName(GitPlugin.EXTENSION_NAME), notNullValue()

        Task gitInitAll = project.tasks.gitInitAll
        expect gitInitAll, notNullValue()
        expect gitInitAll.group, equalTo('Version Control')

        Task gitCloneAll = project.tasks.gitCloneAll
        expect gitCloneAll, notNullValue()
        expect gitCloneAll.group, equalTo('Version Control')

        Task gitPushAll = project.tasks.gitPushAll
        expect gitPushAll, notNullValue()
        expect gitPushAll.group, equalTo('Version Control')

        Task gitPullAll = project.tasks.gitPullAll
        expect gitPullAll, notNullValue()
        expect gitPullAll.group, equalTo('Version Control')

        Task gitStatusAll = project.tasks.gitStatusAll
        expect gitStatusAll, notNullValue()
        expect gitStatusAll.group, equalTo('Version Control')

        def gitCloneCode = (GitCloneTask) project.tasks.gitCloneCode
        expect gitCloneCode, notNullValue()
        expect gitCloneCode.description, equalTo('Clone the Code Git repository')
        expect gitCloneCode.url, equalTo('https://github.com/qaware/QAseuac.git')
        expect gitCloneCode.branch, equalTo('TEST')
        expect gitCloneCode.username, equalTo('lreimer')
        expect gitCloneCode.password, equalTo('secret')
        expect gitCloneCode.directory, notNullValue()

        expect gitCloneCode.singleBranch, is(true)
        expect gitCloneCode.cloneSubmodules, is(true)
        expect gitCloneCode.noCheckout, is(true)
        expect gitCloneCode.timeout, is(300)

        def gitCloneWiki = project.tasks.gitCloneWiki
        expect gitCloneWiki, notNullValue()
        expect gitCloneWiki.description, equalTo('Clone the Wiki Git repository')
        expect gitCloneWiki.url, equalTo('https://github.com/qaware/QAseuac.wiki.git')
        expect gitCloneWiki.branch, equalTo('HEAD')
        expect gitCloneWiki.username, nullValue()
        expect gitCloneWiki.password, nullValue()
        expect gitCloneWiki.directory, notNullValue()

        expect gitInitAll.dependsOn, hasSize(3)
        expect gitInitAll.dependsOn, hasItem(project.tasks.gitInitCode)
        expect gitInitAll.dependsOn, hasItem(project.tasks.gitInitWiki)

        expect gitCloneAll.dependsOn, hasSize(3)
        expect gitCloneAll.dependsOn, hasItem(gitCloneCode)
        expect gitCloneAll.dependsOn, hasItem(gitCloneWiki)

        expect gitPullAll.dependsOn, hasSize(3)
        expect gitPullAll.dependsOn, hasItem(project.tasks.gitPullCode)
        expect gitPullAll.dependsOn, hasItem(project.tasks.gitPullWiki)

        expect gitPushAll.dependsOn, hasSize(3)
        expect gitPushAll.dependsOn, hasItem(project.tasks.gitPushCode)
        expect gitPushAll.dependsOn, hasItem(project.tasks.gitPushWiki)

        expect gitStatusAll.dependsOn, hasSize(3)
        expect gitStatusAll.dependsOn, hasItem(project.tasks.gitStatusCode)
        expect gitStatusAll.dependsOn, hasItem(project.tasks.gitStatusWiki)

        expect project.tasks.gitCommitCode, notNullValue()
        expect project.tasks.gitCommitWiki, notNullValue()
    }
}
