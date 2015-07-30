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

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

/**
 * Test specification to check correct execution of hooks.
 *
 * @author lreimer
 */
class RunHooksTaskSpec extends Specification {
    Project project
    File classesDir

    def setup() {
        project = ProjectBuilder.builder().build()
        classesDir = new File(RunHooksTaskSpec.getResource("/").toURI())
    }

    def "Define RunSoftwareHooksTask and runHooks"() {
        given: "a configured RunHooksTask"
        RunHooksTask task = project.task("runSoftwareHooks", type: RunHooksTask) {
            seuHome = 'S:'
            seuLayout = SeuacLayout.defaultLayout('S:')
            projectName = 'Base Plugin Test'
            target = classesDir
            deleteHooks = false
        }

        when: "we run the hooks"
        task.runHooks()

        then: "we expect no exceptions and an existing hooks dir"
        notThrown(Exception)
        assert new File(classesDir, 'META-INF/hooks/').exists()
    }
}
