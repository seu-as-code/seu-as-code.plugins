package de.qaware.seu.as.code.plugins.homebrew

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
 * Applies the current home brew configuration within the seu.
 *
 * It will first uninstall removed brew software. Then it updates brew itself and any installed software.
 * Finally it will install all new added brew software.
 *
 * @author christian.fritz
 */
class ApplyBrewSoftwareTask extends DefaultTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyBrewSoftwareTask.class);

    @Input
    Configuration source
    File homebrewBasePath
    SeuacDatastore datastore

    /**
     * Initializes the apply brew software task.
     */
    ApplyBrewSoftwareTask() {
        group = 'SEU-as-code'
        description = 'Installs a homebrew package into the seu'
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
        source.transitive = false

        // first we find all obsolete dependencies and remove associated files
        Set<String> obsoleteDeps = provider.findAllObsoleteDeps(source.dependencies, source.name)
        Set<String> obsoleteFiles = provider.findAllFiles(obsoleteDeps, source.name)
        uninstallOldPackages(obsoleteFiles)

        updateBrewPackages()

        Set<Dependency> incomingDeps = provider.findAllIncomingDeps(source.dependencies, source.name)
        installNewPackages(incomingDeps, provider)
    }

    /**
     * Updates homebrew it self and then updates all installed home brew software.
     */
    def updateBrewPackages() {
        LOGGER.info 'Updates brew itself'
        def update = createBrewCommand()
        update.commandLine += 'update'
        update.execute()

        LOGGER.info 'Updates all installed brew packages'
        def upgrade = createBrewCommand()
        upgrade.commandLine += 'upgrade'
        upgrade.execute()
    }

    /**
     * Removes the given brew packages.
     *
     * @param uninstallDeps a set of home brew package names to remove
     */
    def uninstallOldPackages(Set<String> uninstallDeps) {
        if (uninstallDeps.isEmpty()) {
            return
        }

        LOGGER.info 'Uninstall the removed brew packages: {}', uninstallDeps

        def uninstall = createBrewCommand()
        uninstall.commandLine += 'uninstall'
        uninstall.commandLine += uninstallDeps

        def execute = uninstall.execute()
        execute.rethrowFailure()
    }

    /**
     * Installs the given set of dependencies using brew and store them within the seu management database using
     * the given provider.
     *
     * @param dependencies The dependencies to install.
     * @param provider The provider to store the installed dependencies in
     */
    def installNewPackages(Set<Dependency> dependencies, DatastoreProvider provider) {
        LOGGER.info 'Install the new brew packages'
        dependencies.forEach({ d ->
            def installTool = createBrewCommand()
            installTool.commandLine += ['install', d.name]
            installTool.execute()
            provider.storeDependency(d, null, 'brew')

            LOGGER.info 'Finished installing brew package: {}', d.name
        })
    }

    /**
     * Create a new {@link ExecAction} for the brew installation in the current seu.
     *
     * @return A new exec action.
     */
    protected ExecAction createBrewCommand() {
        def action = getExecActionFactory().newExecAction()
        action.commandLine(Paths.get(homebrewBasePath.path, 'bin', 'brew').toString())
        action.workingDir = homebrewBasePath
        return action
    }
}
