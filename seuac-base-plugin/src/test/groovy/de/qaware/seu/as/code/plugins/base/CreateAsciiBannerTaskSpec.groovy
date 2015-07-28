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

import static de.qaware.seu.as.code.plugins.base.SeuacBanner.defaultBanner

/**
 * Test specification for the CreateAsciiBannerTask functionality.
 *
 * @author lreimer
 */
class CreateAsciiBannerTaskSpec extends Specification {

    Project project
    File seuHome

    void setup() {
        project = ProjectBuilder.builder().build()
        seuHome = File.createTempDir()
    }

    def "Define CreateAsciiBannerTask and doCreateAsciiBanner"() {
        given: "a configured CreateAsciiBannerTask"
        CreateAsciiBannerTask task = project.task("createAsciiBanner", type: CreateAsciiBannerTask) {
            projectName = 'SEU-as-Code'
            bannerFile = new File(seuHome, CreateAsciiBannerTask.DEFAULT_FILENAME)
            settings = defaultBanner()
        }

        when: "we create the ASCII banner"
        task.doCreateAsciiBanner()

        then: "we expect the banner file to exist with the correct content"
        notThrown(Exception)
        fileExists()
        fileEndsWith('QAware GmbH')
    }

    private boolean fileExists() {
        new File(seuHome, CreateAsciiBannerTask.DEFAULT_FILENAME).exists()
    }

    private boolean fileEndsWith(String content) {
        def lines = new File(seuHome, CreateAsciiBannerTask.DEFAULT_FILENAME).readLines()
        lines.get(lines.size() - 1).endsWith(content)
    }
}
