package de.qaware.seu.as.code.plugins.homebrew

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.DatastoreProviderFactory
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.tasks.AbstractExecTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

import java.nio.file.Paths
import java.util.stream.Collectors

/**
 *
 * @author christian.fritz
 */
class ApplyBrewSoftwareTask extends AbstractExecTask {

    @Input
    Configuration source
    //File target
    File homebrewBasePath
    boolean withEmptyDirs = true
    SeuacDatastore datastore


    ApplyBrewSoftwareTask() {
        super(ApplyBrewSoftwareTask)
        group = 'SEU-as-code'
        description = 'Installs a homebrew package into the seu'
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

        Set<Dependency> incomingDeps = provider.findAllIncomingDeps(source.dependencies, source.name)
        installNewPackages(incomingDeps)
    }

    def uninstallOldPackages(Set<String> uninstallDeps) {
        if (uninstallDeps.isEmpty()) {
            return
        }

        def uninstall = getExecActionFactory().newExecAction()
        uninstall.workingDir = homebrewBasePath

        uninstall.commandLine(Paths.get(homebrewBasePath.path, 'bin', 'brew').toString(), 'uninstall')
        uninstall.commandLine += uninstallDeps

        def execute = uninstall.execute()
        execute.rethrowFailure()
    }


    def installNewPackages(Set<Dependency> dependencies) {
        workingDir = homebrewBasePath

        commandLine(Paths.get(homebrewBasePath.path, 'bin', 'brew').toString(), 'install')
        commandLine += dependencies.stream()
                .map({ d -> d.name })
                .collect(Collectors.toSet())

        super.exec()
    }
}
