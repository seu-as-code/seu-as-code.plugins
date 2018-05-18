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
package de.qaware.seu.as.code.plugins.chocolatey

import com.sun.media.jfxmedia.logging.Logger
import de.qaware.seu.as.code.plugins.base.SeuacLayout
import groovy.xml.XmlUtil
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Requires
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout

/**
 * Unit test for the {@link InstallChocolateyTaskTest}
 */
@Requires({ os.windows })
class InstallChocolateyTaskTest extends Specification {

    File home
    SeuacLayout defaultLayout
    Project project

    void setup() {
        project = ProjectBuilder.builder().build()

        home = File.createTempDir()
        defaultLayout = defaultLayout(home)

        project.apply plugin: 'seuac-chocolatey'
        project.dependencies.add('choco', ':kubernetes-helm:')

        project.seuAsCode {
            seuHome = home
            projectName = 'Test SEU'
            layout = defaultLayout
        }
    }

    def "Install Chocolatey"() {
        setup:
        InstallChocolateyTask task = project.task("installChocoTask", type: InstallChocolateyTask) {
            chocolateyBasePath = new File(home, 'chocolatey')
        }
        when:
        doInstallSave(task)
        then:
        assert new File(home, 'chocolatey/lib/chocolatey.nupkg').exists()
        assert isElevatedRightwarningDisabled()
    }

    def "Try to install but Chocolatey is already installed"() {
        setup:
        def chocolateyPath = new File(home, 'chocolatey')
        chocolateyPath.mkdirs()
        InstallChocolateyTask task = project.task("installChocoTask", type: InstallChocolateyTask) {
            chocolateyBasePath = new File(home, 'chocolatey')
        }
        when:
        doInstallSave(task)
        then:
        assert !new File(home, 'chocolatey/lib/chocolatey.nupkg').exists()
    }

    private void doInstallSave(InstallChocolateyTask task) {
        try {
            task.doInstall()
        } catch (org.gradle.api.resources.ResourceException e) {
            // copying the the set-env-choco.cmd from classpath, currently fails in test
        }
    }

    private boolean isElevatedRightwarningDisabled() {
        def configFile = new File(home, 'chocolatey/config/chocolatey.config')
        def chocolatey = new XmlSlurper().parse(configFile);
        def nonElevatedWarnings = chocolatey.features.feature.find {
            it.@name="showNonElevatedWarnings"
        }
        return nonElevatedWarnings.@enabled == "false" && nonElevatedWarnings.@enabled
    }
}
