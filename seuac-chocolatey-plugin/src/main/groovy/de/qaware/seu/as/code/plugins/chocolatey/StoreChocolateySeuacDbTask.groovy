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
import de.qaware.seu.as.code.plugins.base.DatastoreProviderFactory
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * This task will store the SEU-as-code configuration to the configured storage.
 */
class StoreChocolateySeuacDbTask extends DefaultTask {

    @OutputDirectory
    File chocolateyBasePath
    @Nested
    SeuacDatastore datastore

    /**
     * Initialize the group and description for this task.
     */
    StoreChocolateySeuacDbTask() {
        group = 'SEU-as-code'
        description = 'Store the current SEU chocolatey software package configuration.'
    }

    /**
     * Stores the SEU-as-code database for the chocolatey software configuration.
     */
    @TaskAction
    void storeSeuacDb() {
        DatastoreProvider provider = DatastoreProviderFactory.instance.get(datastore)
        provider.init()

        project.configurations.choco.dependencies.each { Dependency d ->
            provider.storeDependency(d, [project.fileTree(new File("${chocolateyBasePath}/lib/${d.name}/"))], 'choco')
        }
    }
}
