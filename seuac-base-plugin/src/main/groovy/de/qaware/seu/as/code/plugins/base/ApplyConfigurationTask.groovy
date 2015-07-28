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
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

/**
 * The main task to apply a software configuration it a SEU. This includes installing new
 * and removing old software.
 *
 * @author lreimer
 */
class ApplyConfigurationTask extends DefaultTask {

    @Input
    Configuration source
    File target
    boolean withEmptyDirs = true
    SeuacDatastore datastore

    /**
     * Initialize the group for this task.
     */
    ApplyConfigurationTask() {
        group = 'SEU as Code'
        description = 'Apply the given configuration to the SEU.'
    }

    /**
     * Apply the configuration source to the target directory. Checks with the datastore for any obsolete
     * dependencies, these will be removed. Then it finds all newly incoming deps and unpacks these to the
     * configured target directory.
     */
    @TaskAction
    void doApply() {
        DatastoreProvider provider = DatastoreProviderFactory.instance.get(datastore)
        provider.init()

        // first we find all obsolete dependencies and remove associated files
        Set<String> obsoleteDeps = provider.findAllObsoleteDeps(source.dependencies, source.name)
        Set<String> obsoleteFiles = provider.findAllFiles(obsoleteDeps, source.name)
        deleteObsoleteFile(obsoleteFiles)

        Set<Dependency> incomingDeps = provider.findAllIncomingDeps(source.dependencies, source.name)
        copyIncomingDeps(incomingDeps)
    }

    private void copyIncomingDeps(Set<Dependency> incomingDeps) {
        project.copy {
            from {
                source.copy {
                    incomingDeps.contains(it)
                }.collect {
                    project.zipTree(it)
                }
            }
            into target
            includeEmptyDirs = withEmptyDirs
        }
    }

    private void deleteObsoleteFile(Set<String> obsoleteFiles) {
        obsoleteFiles.each { project.delete "$target/$it" }
    }

}
