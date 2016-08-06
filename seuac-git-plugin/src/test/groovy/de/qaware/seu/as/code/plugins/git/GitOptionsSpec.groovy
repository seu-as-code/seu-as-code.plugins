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

import de.qaware.seu.as.code.plugins.git.GitPullOptions.MergeStrategy
import spock.lang.Specification

/**
 * Basic test spec to check proper DSL behaviour when configuring the options.
 *
 * @author lreimer
 */
class GitOptionsSpec extends Specification {
    def "Check pull options closure"() {
        given:
        def options = new GitOptions()

        when:
        options.pull {
            rebase = true
            strategy = MergeStrategy.THEIRS
            timeout = 300
        }

        then:
        options.pull.rebase
        options.pull.strategy == MergeStrategy.THEIRS
        options.pull.timeout == 300
    }

    def "Check push options closure"() {
        given:
        def options = new GitOptions()

        when:
        options.push {
            dryRun = true
            force = true
            pushAll = true
            pushTags = true
            timeout = 200
        }

        then:
        options.push.dryRun
        options.push.force
        options.push.pushAll
        options.push.pushTags
        options.push.timeout == 200
    }

    def "Check clone options closure"() {
        given:
        def options = new GitOptions()

        when:
        options.clone {
            singleBranch = true
            cloneAllBranches = true
            cloneSubmodules = true
            noCheckout = true
            timeout = 600
        }

        then:
        options.clone.singleBranch
        options.clone.cloneAllBranches
        options.clone.cloneSubmodules
        options.clone.noCheckout
        options.clone.timeout == 600
    }

    def "Check commit options closure"() {
        given:
        def options = new GitOptions()

        expect:
        options.commit.author == null
        options.commit.committer == null

        when:
        options.commit {
            message = 'Added new files'
            all = false
            noVerify = true
            amend = true
            committer {
                username = 'seu-as-code'
                email = 'seu-as-code@qaware.de'
            }
            author {
                username = 'lreimer'
                email = 'lreimer@qaware.de'
            }
        }

        then:
        options.commit.message == 'Added new files'
        !options.commit.all
        options.commit.noVerify
        options.commit.amend
        options.commit.committer.username == 'seu-as-code'
        options.commit.committer.email == 'seu-as-code@qaware.de'
        options.commit.author.username == 'lreimer'
        options.commit.author.email == 'lreimer@qaware.de'
    }
}
