/*
 *    Copyright (C) 2018 QAware GmbH
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
package de.qaware.seu.as.code.plugins.chocolatey

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.JdbcH2DatastoreProvider
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Requires
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.temporaryDatastore

/**
 * Unit test for the {@link StoreChocolateySeuacDbTaskTest}.
 */
@Requires({ os.windows })
class StoreChocolateySeuacDbTaskTest extends Specification {

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider
    File home

    void setup() {
        project = ProjectBuilder.builder().build()
        home = folder.newFolder()

        def helmDir = new File(home, 'chocolatey/lib/kubernetes-helm/')
        helmDir.mkdirs()
        new File(helmDir, 'test').createNewFile()

        defaultDatastore = temporaryDatastore()
        provider = new JdbcH2DatastoreProvider(defaultDatastore)
    }

    def StoreChocolateySeuacDb() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-chocolatey'
        project.dependencies.add('choco', ':kubernetes-helm:')

        StoreChocolateySeuacDbTask task = project.task("storeChocoSeuacDb", type: StoreChocolateySeuacDbTask) {
            chocolateyBasePath = new File(home, 'chocolatey')
            datastore = defaultDatastore
        } as StoreChocolateySeuacDbTask
        when: "we evaluate the task"
        task.storeSeuacDb()

        then: "the DB should contain all files for all configurations"
        notThrown(Exception)

        provider.findAllFiles(['null:kubernetes-helm:null'] as Set, 'choco').size() == 1
    }
}
