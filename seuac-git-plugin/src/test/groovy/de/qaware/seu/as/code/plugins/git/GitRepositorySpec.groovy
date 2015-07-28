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

import spock.lang.Specification

/**
 * The simple Spock spec for the GitRepository.
 *
 * @author lreimer
 */
class GitRepositorySpec extends Specification {

    GitRepository repository

    def setup() {
        repository = new GitRepository('test')
    }

    def "Initialize URL by setter and method"() {
        when: "we set the URL"
        repository.url = "http://localhost"
        then: "we expect the value"
        repository.url == "http://localhost"

        when: "we assign the URL"
        repository.url "http://localhost"
        then: "we expect the value"
        repository.url == "http://localhost"
    }

    def "Initialize the branch by setter and method"() {
        when: "we set the branch"
        repository.branch = 'HEAD'
        then: "we expect the value"
        repository.branch == 'HEAD'

        when: "we assign the branch"
        repository.branch 'HEAD'
        then: "we expect the value"
        repository.branch == 'HEAD'
    }

    def "Initialize the directory by setter and method"() {
        when: "we set the directory"
        repository.directory = new File('build')
        then: "we expect the value"
        repository.directory == new File('build')

        when: "we assign the directory"
        repository.directory new File('build')
        then: "we expect the value"
        repository.directory == new File('build')
    }

    def "Initialize the username by setter and method"() {
        when: "we set the username"
        repository.username = 'test'
        then: "we expect the value"
        repository.username == 'test'

        when: "we set the username"
        repository.username 'test'
        then: "we expect the value"
        repository.username == 'test'
    }

    def "Initialize the password by setter and method"() {
        when: "we set the password"
        repository.password = 'test'
        then: "we expect the value"
        repository.password == 'test'

        when: "we set the password"
        repository.password 'test'
        then: "we expect the value"
        repository.password == 'test'
    }
}
