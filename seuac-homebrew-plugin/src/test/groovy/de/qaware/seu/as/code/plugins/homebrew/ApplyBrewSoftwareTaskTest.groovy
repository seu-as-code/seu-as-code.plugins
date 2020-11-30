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
package de.qaware.seu.as.code.plugins.homebrew

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.JdbcH2DatastoreProvider
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import de.qaware.seu.as.code.plugins.base.SeuacLayout
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Requires
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.temporaryDatastore
import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout

/**
 * Unit test for the {@link ApplyBrewSoftwareTask}.
 */
@Requires({ os.macOs })
class ApplyBrewSoftwareTaskTest extends Specification {

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider
    SeuacLayout defaultLayout
    File home
    private Dependency xzDependency


    void setup() {
        project = ProjectBuilder.builder().build()

        home = folder.newFolder()
        defaultLayout = defaultLayout(home)

        project.apply plugin: 'seuac-homebrew'
        xzDependency = project.dependencies.add('brew', ':xz:')

        defaultDatastore = temporaryDatastore()
        provider = new JdbcH2DatastoreProvider(defaultDatastore)

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }
        project.task("installHomebrewTask", type: InstallHomebrewTask) {
            homebrewBasePath = new File(home, 'homebrew')
        }.doInstall()
    }

    def "Exec"() {
        setup:
        ApplyBrewSoftwareTask task = project.task("applyBrewSoftwareTask", type: ApplyBrewSoftwareTask) {
            homebrewBasePath = new File(home, 'homebrew')
            datastore = defaultDatastore
            brew = project.configurations.brew
            cask = project.configurations.cask
        } as ApplyBrewSoftwareTask
        when:
        task.exec()
        provider.storeDependency(xzDependency, [project.fileTree(new File("$home/homebrew/Cellar/xz/"))], 'brew')

        then:
        assert new File(home, 'homebrew/bin/xz').exists()
        when:
        project.configurations.brew.dependencies.remove(xzDependency)
        task.exec()
        then:
        assert !new File(home, 'homebrew/bin/xz').exists()
    }
}
