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
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction

/**
 * A simple tasks to create the SEU-as-code directory layout.
 *
 * @author lreimer
 */
class CreateSeuacLayoutTask extends DefaultTask {

    SeuacLayout layout

    @OutputDirectories
    def directories

    /**
     * Initialize the group and description for this task.
     */
    CreateSeuacLayoutTask() {
        group = 'SEU-as-code'
        description = 'Creates the SEU directory layout.'
    }

    /**
     * Create the directory layout and make all required directories. For incremental build support
     * the output will be initialized, except the codebase directory, this caused problems when the
     * SEU itself is located in codebase.
     */
    @TaskAction
    void mkdirs() {
        try {
            layout.mkdirs()
        } catch (IOException e) {
            throw new GradleException("Error creating SEU directory layout.", e)
        }
    }
}
