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
package de.qaware.seu.as.code.plugins.homebrew

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.JdbcH2DatastoreProvider
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore

class StoreBrewSeuacDbTaskTest extends Specification {

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider
    File home

    void setup() {
        project = ProjectBuilder.builder().build()
        home = File.createTempDir()

        def maven3Dir = new File(home, 'homebrew/Cellar/maven3/')
        maven3Dir.mkdirs()
        new File(maven3Dir, 'test').createNewFile()

        defaultDatastore = defaultDatastore()
        defaultDatastore.url = 'jdbc:h2:./build/seuac'
        provider = new JdbcH2DatastoreProvider(defaultDatastore)
    }

    def "StoreBrewSeuacDb"() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-homebrew'
        project.dependencies.add('brew', ':maven3:')

        StoreBrewSeuacDbTask task = project.task("storeBrewSeuacDb", type: StoreBrewSeuacDbTask) {
            homebrewBasePath = new File(home, 'homebrew')
            datastore = defaultDatastore
        }
        when: "we evaluate the task"
        task.storeSeuacDb()

        then: "the DB should contain all files for all configurations"
        notThrown(Exception)

        provider.findAllFiles(['null:maven3:null'] as Set, 'brew').size() == 1
    }
}
