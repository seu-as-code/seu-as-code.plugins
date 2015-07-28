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

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.TaskAction

/**
 * This task will store the SEU as Code configuration to the configured storage.
 */
class StoreSeuacDbTask extends DefaultTask {

    SeuacDatastore datastore

    /**
     * Initialize the group and description for this task.
     */
    StoreSeuacDbTask() {
        group = 'SEU as Code'
        description = 'Store the current SEU software package configuration.'
    }

    /**
     * Stores the SEU as Code database for the current software configuration.
     */
    @TaskAction
    void storeSeuacDb() {
        DatastoreProvider provider = DatastoreProviderFactory.instance.get(datastore)
        provider.reset()

        project.configurations.software.dependencies.each { Dependency d ->
            List<FileTree> files = dependencyFiles(project.configurations.software.files(d))
            provider.storeDependency(d, files, 'software')
        }

        project.configurations.home.dependencies.each { Dependency d ->
            List<FileTree> trees = dependencyFiles(project.configurations.home.files(d))
            provider.storeDependency(d, trees, 'home')
        }
    }

    /**
     * Returns the FileTree of all given dependency files. Unzips all dependencies
     * and filters META-INF/ folders.
     *
     * @param files a set of files
     * @return a filtered FileTree
     */
    List<FileTree> dependencyFiles(Set<File> files) {
        files.collect {
            project.zipTree(it).matching {
                include '*'
                exclude 'META-INF/'
            }
        }
    }
}
