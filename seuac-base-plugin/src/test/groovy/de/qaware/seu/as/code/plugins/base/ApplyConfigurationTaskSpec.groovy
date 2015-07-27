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

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore

/**
 * Test specification for the ApplyConfigurationTask functionality.
 *
 * @author lreimer
 */
class ApplyConfigurationTaskSpec extends Specification {

    Project project
    File seuHome
    SeuacDatastore defaultDatastore
    File repo
    DatastoreProvider provider

    def setup() {
        project = ProjectBuilder.builder().build()
        project.configurations.create('software')

        repo = new File(RunHooksTaskSpec.getResource("/").toURI())
        project.repositories.flatDir {
            dirs repo
        }
        project.dependencies.add('software', ':seuac-test:1.0.0@zip')

        seuHome = File.createTempDir()

        defaultDatastore = defaultDatastore()
        defaultDatastore.url = 'jdbc:h2:build/seuac'

        provider = new JdbcH2DatastoreProvider(defaultDatastore)
        provider.reset()
    }

    def "Define ApplyConfigurationTask and doApply"() {
        given:
        ApplyConfigurationTask task = project.task("applySoftware", type: ApplyConfigurationTask) {
            source = project.configurations.software
            target = seuHome
            datastore = defaultDatastore
            withEmptyDirs = false
        }

        when:
        task.doApply()

        then:
        notThrown(Exception)
        fileExists('ascii-art.txt')
        fileExists('set-env.cmd')
        fileExists('start-console.bat')
    }

    private boolean fileExists(String filename) {
        return new File(seuHome, filename).exists()
    }
}
