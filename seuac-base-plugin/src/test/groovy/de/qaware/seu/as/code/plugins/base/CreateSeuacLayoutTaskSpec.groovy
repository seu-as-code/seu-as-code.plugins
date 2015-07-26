/*
 *    Copyright 2015 QAware GmbH
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

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Basic test specification for the CreateSeuacLayoutTask.
 *
 * @author mario-leander.reimer
 */
class CreateSeuacLayoutTaskSpec extends Specification {
    static final String TEST_CREATE_SEUAC_LAYOUT = 'testCreateSeuacLayout'
    Project project
    File seuHome
    SeuacLayout testLayout

    def setup() {
        project = ProjectBuilder.builder().build()
        seuHome = File.createTempDir()
        testLayout = defaultLayout(seuHome)
    }

    def "Define CreateSeuacLayoutTask and mkdirs"() {
        given:
        CreateSeuacLayoutTask task = project.task(TEST_CREATE_SEUAC_LAYOUT, type: CreateSeuacLayoutTask) {
            layout = testLayout
            directories = testLayout.directories
        }

        when:
        task.mkdirs()

        then:
        notThrown(IOException)
        expect project.tasks.testCreateSeuacLayout, notNullValue()

        expect testLayout.codebase.exists(), is(true)
        expect testLayout.docbase.exists(), is(true)
        expect testLayout.home.exists(), is(true)
        expect testLayout.repository.exists(), is(true)
        expect testLayout.software.exists(), is(true)
        expect testLayout.temp.exists(), is(true)

        expect task.outputs.files.files.size(), is(5)
    }
}
