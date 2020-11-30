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

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Test specification for SeuacLayout class.
 *
 * @author lreimer
 */
class SeuacLayoutSpec extends Specification {

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    File seuHome
    SeuacLayout layout

    def setup() {
        seuHome = folder.newFolder()
        layout = defaultLayout(seuHome)
    }

    def "Mkdirs for default SEU layout"() {
        when: "we make all SEU layout directories"
        layout.mkdirs()

        then: "we expect all directories to exist"
        notThrown(IOException)
        expect layout.codebase.exists(), is(true)
        expect layout.docbase.exists(), is(true)
        expect layout.home.exists(), is(true)
        expect layout.repository.exists(), is(true)
        expect layout.software.exists(), is(true)
        expect layout.temp.exists(), is(true)
    }

    def "Check the default SEU layout"() {
        expect: "the default values to be set"
        that layout, notNullValue()
        that layout.codebase, notNullValue()
        that layout.docbase, notNullValue()
        that layout.home, notNullValue()
        that layout.repository, notNullValue()
        that layout.software, notNullValue()
        that layout.temp, notNullValue()
    }

    def "Manually initialize"() {
        given: "a manually initialized layout"
        layout = new SeuacLayout()
        layout.codebase(new File(seuHome, 'code').absolutePath)
        layout.docbase(new File(seuHome, 'docs').absolutePath)
        layout.home(new File(seuHome, 'home').absolutePath)
        layout.repository(new File(seuHome, 'repo').absolutePath)
        layout.software(new File(seuHome, 'programs').absolutePath)
        layout.temp(new File(seuHome, 'temp').absolutePath)

        when: "we create directories"
        layout.mkdirs()

        then: "we want to have 6 dirs"
        layout.directories.size() == 6
    }

    def "No directories for empty layout"() {
        expect: "no directories when uninitialized"
        new SeuacLayout().directories.size() == 0
    }

    def "No directories created"() {
        given: "an empty layout"
        layout = new SeuacLayout()

        when: "creating the layout"
        layout.mkdirs()

        then: "we wont get any exceptions"
        notThrown(IOException)
    }
}
