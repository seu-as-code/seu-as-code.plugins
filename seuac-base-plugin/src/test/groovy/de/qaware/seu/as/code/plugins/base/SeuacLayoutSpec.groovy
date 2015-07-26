/*
 *
 *    Copyright 2015 QAware GmbH
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

import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacLayout.defaultLayout
import static org.hamcrest.Matchers.is
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Test specification for SeuacLayout class.
 *
 * @author mario-leander.reimer
 */
class SeuacLayoutSpec extends Specification {
    File seuHome
    SeuacLayout layout

    def setup() {
        seuHome = File.createTempDir()
        layout = defaultLayout(seuHome)
    }

    void cleanup() {
        seuHome.deleteDir()
    }

    def "Mkdirs for default SEU layout"() {
        when:
        layout.mkdirs()

        then:
        notThrown(IOException)
        expect layout.codebase.exists(), is(true)
        expect layout.docbase.exists(), is(true)
        expect layout.home.exists(), is(true)
        expect layout.repository.exists(), is(true)
        expect layout.software.exists(), is(true)
        expect layout.temp.exists(), is(true)
    }

    def "Check the default SEU layout"() {
        expect:
        that layout, notNullValue()
        that layout.codebase, notNullValue()
        that layout.docbase, notNullValue()
        that layout.home, notNullValue()
        that layout.repository, notNullValue()
        that layout.software, notNullValue()
        that layout.temp, notNullValue()
    }

}
