/*
 *
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
 * Test specification for the StoreSeuacDbTask.
 *
 * @author mario-leander.reimer
 */
class StoreSeuacDbTaskSpec extends Specification {

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider

    void setup() {
        project = ProjectBuilder.builder().build()
        project.repositories.flatDir {
            dirs new File(RunHooksTaskSpec.getResource("/").toURI())
        }

        project.configurations.create('software')
        project.dependencies.add('software', ':seuac-test:1.0.0@zip')

        project.configurations.create('home')
        project.dependencies.add('home', ':seuac-test:1.0.0@zip')

        defaultDatastore = defaultDatastore()
        defaultDatastore.url = 'jdbc:h2:build/seuac'
        provider = new JdbcH2DatastoreProvider(defaultDatastore)
    }

    def "Define StoreSeuacDbTask and storeSeuacDb"() {
        given:
        StoreSeuacDbTask task = project.task("storeSeuacDb", type: StoreSeuacDbTask) {
            datastore = defaultDatastore
        }

        when:
        task.storeSeuacDb()

        then:
        notThrown(Exception)
        findAllFiles('software').size() == 3
        findAllFiles('home').size() == 3
    }

    Set<String> findAllFiles(String c) {
        provider.findAllFiles(['null:seuac-test:1.0.0'] as Set, c)
    }
}
