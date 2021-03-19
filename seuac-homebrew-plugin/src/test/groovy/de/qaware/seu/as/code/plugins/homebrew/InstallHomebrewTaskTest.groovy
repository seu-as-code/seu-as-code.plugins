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

import de.qaware.seu.as.code.plugins.base.SeuacLayout
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Requires
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout

/**
 * Unit test for the {@link InstallHomebrewTask}
 */
@Requires({ os.macOs })
class InstallHomebrewTaskTest extends Specification {

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    File home
    SeuacLayout defaultLayout
    Project project

    void setup() {
        project = ProjectBuilder.builder().build()

        home = folder.newFolder()
        defaultLayout = defaultLayout(home)

        project.apply plugin: 'seuac-homebrew'
        project.dependencies.add('brew', ':maven3:')

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }
    }

    def "Install HomeBrew"() {
        setup:
        InstallHomebrewTask task = project.task("installHomebrewTask", type: InstallHomebrewTask) {
            homebrewBasePath = new File(home, 'homebrew')
        } as InstallHomebrewTask
        when:
        task.doInstall()
        then:
        assert new File(home, 'homebrew/bin/brew').exists()
    }

    def "Try to install but HomeBrew is already installed"() {
        setup:
        def homebrewPath = new File(home, 'homebrew')
        homebrewPath.mkdirs()
        InstallHomebrewTask task = project.task("installHomebrewTask", type: InstallHomebrewTask) {
            homebrewBasePath = homebrewPath
        } as InstallHomebrewTask
        when:
        task.doInstall()
        then:
        assert !new File(home, 'homebrew/bin/brew').exists()
    }
}
