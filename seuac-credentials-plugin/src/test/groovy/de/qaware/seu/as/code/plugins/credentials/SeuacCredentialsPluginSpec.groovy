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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Basic test specification for the {@link SeuacCredentialsPlugin}.
 *
 * @author clboettcher
 */
class SeuacCredentialsPluginSpec extends Specification {

    private Project project
    private SeuacCredentialsPlugin plugin

    def setup() {
        this.project = ProjectBuilder.builder().build()
        this.plugin = new SeuacCredentialsPlugin()
    }

    def "Apply the plugin"() {
        when: "the plugin is applied"
        this.plugin.apply(this.project)

        then: "we expect to find the tasks and the extension to be configured"
        SetCredentialsTask setCredentialsTask = (SetCredentialsTask) project.tasks.findByName('setCredentials')
        ClearCredentialsTask clearCredentialsTask = (ClearCredentialsTask) project.tasks.findByName('clearCredentials')
        def extension = project.getExtensions().getExtraProperties().get('credentials')

        expect setCredentialsTask, notNullValue()
        expect setCredentialsTask.getIoSupport(), notNullValue()
        expect setCredentialsTask.getCredentials(), notNullValue()

        expect clearCredentialsTask, notNullValue()
        expect clearCredentialsTask.getIoSupport(), notNullValue()
        expect clearCredentialsTask.getCredentials(), notNullValue()

        expect extension, notNullValue()
    }
}
