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
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.process.internal.ExecAction
import org.gradle.process.internal.ExecActionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.nio.file.Paths

/**
 * Applies the current home chocolatey configuration within the seu.
 *
 * It will first uninstall removed chocolatey software.
 * Finally it will install all new added chocolatey software.
 *
 * @author christian.fritz
 */
class ApplyChocolateySoftwareTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyChocolateySoftwareTask.class);

    @Input
    Configuration choco

    File chocolateyBasePath
    SeuacDatastore datastore

    /**
     * Initializes the apply chocolatey software task.
     */
    ApplyChocolateySoftwareTask() {
        group = 'SEU-as-code'
        description = 'Install Chocolatey packages into the SEU'
    }

    @Inject
    protected ExecActionFactory getExecActionFactory() {
        throw new UnsupportedOperationException();
    }

    /**
     * Apply the configuration source to the target directory. Checks with the datastore for any obsolete
     * dependencies, these will be removed. Then it finds all newly incoming deps and unpacks these to the
     * configured target directory.
     */
    @TaskAction
    void exec() {
        DatastoreProvider provider = DatastoreProviderFactory.instance.get(datastore)
        provider.init()

        choco.transitive = false

        // first we find all obsolete dependencies and remove associated files
        Set<String> obsoleteDeps = provider.findAllObsoleteDeps(choco.dependencies, choco.name)
        uninstallOldPackages(obsoleteDeps, choco)
        Set<Dependency> incomingDeps = provider.findAllIncomingDeps(choco.dependencies, choco.name)
        installNewPackages(incomingDeps)
    }

    /**
     * Removes the given chocolatey packages.
     *
     * @param uninstallDeps a set of home chocolatey package names to remove
     * @param configuration the configuration of the dependencies
     */
    def uninstallOldPackages(Set<String> uninstallDeps, Configuration configuration) {
        if (uninstallDeps.isEmpty()) {
            return
        }

        LOGGER.info 'Uninstall the removed chocolatey packages: {}', uninstallDeps

        uninstallDeps.forEach({ d ->
            def dependency = d.split(":", 3)
            def chocoTool = createChocoCommand()
            chocoTool.commandLine += ['uninstall', dependency[1]]
            if (dependency[2] != 'null') {
                chocoTool.commandLine += ['--version', dependency[2]]
            }
            chocoTool.execute()
        })

    }

    /**
     * Installs the given set of dependencies using chocolatey
     *
     * @param dependencies The dependencies to install.
     */
    def installNewPackages(Set<Dependency> dependencies) {
        LOGGER.info 'Install the new chocolatey packages'
        dependencies.forEach({ d ->
            def installTool = createChocoCommand()
            installTool.commandLine += ['install', d.name, '-y']
            if (d.version != null) {
                installTool.commandLine += ['--version', d.version]
            }
            installTool.execute()

            LOGGER.info 'Finished installing chocolatey package: {}', d.name
        })
    }

    /**
     * Create a new {@link ExecAction} for the chocolatey installation in the current seu.
     *
     * @return A new exec action.
     */
    protected ExecAction createChocoCommand() {
        def action = getExecActionFactory().newExecAction()
        action.commandLine(Paths.get(chocolateyBasePath.path, 'bin', 'choco.exe').toString())
        action.workingDir = chocolateyBasePath
        action.environment([
                ChocolateyInstall      : chocolateyBasePath,
                ChocolateyBinRoot      : chocolateyBasePath.getParent(),
                ChocolateyToolsLocation: chocolateyBasePath.getParent()
        ])
        return action
    }
}
