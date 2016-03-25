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
package de.qaware.seu.as.code.plugins.base

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore
import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.is
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Basic test specification for the DestroySeuTask.
 *
 * @author lreimer
 */
class DetroySeuTaskSpec extends Specification {
    static final String TEST_DESTROY_SEU = 'testDestroySeu'

    Project project
    File seuHome

    SeuacLayout testLayout
    SeuacDatastore testDatastore

    def setup() {
        project = ProjectBuilder.builder().build()
        seuHome = File.createTempDir()

        testLayout = defaultLayout(seuHome)
        testLayout.mkdirs()

        testDatastore = defaultDatastore()
        testDatastore.url = 'jdbc:h2:./build/destroy'
    }

    def "Define DestroySeuTask and destroy a SEU layout"() {
        given: "a configured DestroySeuTask"
        DestroySeuTask task = project.task(TEST_DESTROY_SEU, type: DestroySeuTask) {
            layout = testLayout
            datastore = testDatastore
        }

        when: "we destroy the SEU"
        task.destroy()

        then: "we expect no directories to exist any more"
        notThrown(IOException)

        expect testLayout.codebase.exists(), is(false)
        expect testLayout.docbase.exists(), is(false)
        expect testLayout.home.exists(), is(false)
        expect testLayout.repository.exists(), is(false)
        expect testLayout.software.exists(), is(false)
        expect testLayout.temp.exists(), is(false)
    }
}
