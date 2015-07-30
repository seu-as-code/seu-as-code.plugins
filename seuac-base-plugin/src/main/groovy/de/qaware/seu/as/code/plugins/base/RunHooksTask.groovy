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

import groovy.io.FileType
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * This task will run all provided hooks that might be available after
 * installing software packages.
 *
 * @author lreimer
 */
class RunHooksTask extends DefaultTask {
    @Input
    String seuHome

    String projectName

    SeuacLayout seuLayout

    @InputDirectory
    File target

    boolean deleteHooks = true

    /**
     * Initialize the group and description for this task.
     */
    RunHooksTask() {
        group = 'SEU-as-Code'
        description = 'Run hooks for installed software packages.'
    }

    /**
     * Run all software hooks found in the target directory. If configured, the hooks directory
     * will be removed afterwards.
     */
    @TaskAction
    void runHooks() {
        File hooks = new File(target, 'META-INF/hooks/')
        if (hooks.exists()) {
            Binding binding = new Binding();
            binding.setVariable('seuHome', seuHome)
            binding.setVariable('seuLayout', seuLayout)
            binding.setVariable('projectName', projectName)
            binding.setVariable('directory', target)
            binding.setVariable('logger', logger)

            def shell = new GroovyShell(binding)
            hooks.eachFileMatch(FileType.FILES, ~/.*\.groovy/) {
                logger.debug "Running software hook $it"
                try {
                    shell.evaluate(it)
                } catch (any) {
                    logger.warn("Error running software hook $it", any)
                }
            }
        }

        // delete the whole META-INF/ directory
        if (deleteHooks) {
            hooks.getParentFile().deleteDir()
        }
    }
}
