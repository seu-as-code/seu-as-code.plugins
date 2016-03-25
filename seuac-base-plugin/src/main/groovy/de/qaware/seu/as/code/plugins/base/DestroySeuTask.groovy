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
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 * A simple tasks to destroy the SEU. This will delete all the directories from
 * the SEU layout.
 *
 * @author lreimer
 */
class DestroySeuTask extends DefaultTask {

    SeuacLayout layout
    SeuacDatastore datastore

    /**
     * Initialize the group and description for this task.
     */
    DestroySeuTask() {
        group = 'SEU-as-code'
        description = 'Destroy the complete SEU. Be careful!'
    }

    /**
     * Removes all the directories from the layout and resets the dependencies DB.
     */
    @TaskAction
    void destroy() {
        try {
            // remove all directories for the layout
            layout.rmdirs()
        } catch (IOException e) {
            throw new GradleException("Error destroying SEU directory layout.", e)
        }

        // remove all dependencies from the DB
        DatastoreProvider provider = DatastoreProviderFactory.instance.get(datastore)
        provider.reset()
    }
}
