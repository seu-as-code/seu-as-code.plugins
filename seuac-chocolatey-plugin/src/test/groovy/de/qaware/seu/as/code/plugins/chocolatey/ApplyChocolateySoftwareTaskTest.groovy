package de.qaware.seu.as.code.plugins.chocolatey

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.JdbcH2DatastoreProvider
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import de.qaware.seu.as.code.plugins.base.SeuacLayout
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Requires
import spock.lang.Specification

/**
 * Unit test for the {@link ApplyChocolateySoftwareTask}.
 */
@Requires({ os.windows })
class ApplyChocolateySoftwareTaskTest extends Specification {

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider
    SeuacLayout defaultLayout
    File home
    private Dependency helmDependency
    private Dependency kubernetesDependency


    void setup() {
        project = ProjectBuilder.builder().build()

        home = File.createTempDir()
        defaultLayout = SeuacLayout.defaultLayout(home)

        project.apply plugin: 'seuac-chocolatey'
        // use latest version
        helmDependency = project.dependencies.add('choco', ':kubernetes-helm:')
        // explicit version
        kubernetesDependency = project.dependencies.add('choco', ':kubernetes-cli:1.10.1')

        defaultDatastore = SeuacDatastore.defaultDatastore()
        defaultDatastore.url = 'jdbc:h2:./build/seuac'
        provider = new JdbcH2DatastoreProvider(defaultDatastore)

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }

        try {
            project.task("installChocolateyTask", type: InstallChocolateyTask) {
                chocolateyBasePath = new File(home, 'chocolatey')
            }.doInstall()
        } catch (org.gradle.api.resources.ResourceException e) {
            // copying the the set-env-choco.cmd from classpath, currently fails in test
        }

    }

    void cleanup() {
        provider.clear()
    }

    def "Exec"() {
        setup:
        ApplyChocolateySoftwareTask task = project.task("applyChocolateySoftwareTask", type: ApplyChocolateySoftwareTask) {
            chocolateyBasePath = new File(home, 'chocolatey')
            datastore = defaultDatastore
            choco = project.configurations.choco
        }
        when:
        task.exec()
        provider.storeDependency(helmDependency, [project.fileTree(new File("$home/chocolatey/lib/kubernetes-helm/"))], 'choco')
        provider.storeDependency(kubernetesDependency, [project.fileTree(new File("$home/chocolatey/lib/kubernetes-cli/"))], 'choco')

        then:
        assert new File(home, 'chocolatey/bin/helm.exe').exists()
        assert new File(home, 'chocolatey/bin/kubectl.exe').exists()
        when:
        project.configurations.choco.dependencies.remove(helmDependency)
        project.configurations.choco.dependencies.remove(kubernetesDependency)
        task.exec()
        then:
        assert !new File(home, 'chocolatey/bin/helm.exe').exists()
        assert !new File(home, 'chocolatey/bin/kubectl.exe').exists()
    }
}
