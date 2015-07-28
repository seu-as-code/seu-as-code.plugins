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

import spock.lang.Specification

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Test specification for the SeuacExtension. Especially for all the magic closures.
 *
 * @author lreimer
 */
class SeuacExtensionSpec extends Specification {
    SeuacExtension extension

    void setup() {
        extension = new SeuacExtension(seuHome: 'S:', projectName: 'Spock SEU')
    }

    def "Default extension initialization"() {
        expect: "that the default values are OK"
        that extension.seuHome, equalTo('S:')
        that extension.projectName, equalTo('Spock SEU')
        that extension.layout, notNullValue()
        that extension.datastore, notNullValue()

        that extension.layout.codebase.absolutePath, equalTo(new File('S:/codebase/').absolutePath)
        that extension.layout.docbase.absolutePath, equalTo(new File('S:/docbase/').absolutePath)
        that extension.layout.home.absolutePath, equalTo(new File('S:/home/').absolutePath)
        that extension.layout.repository.absolutePath, equalTo(new File('S:/repository/').absolutePath)
        that extension.layout.software.absolutePath, equalTo(new File('S:/software/').absolutePath)
        that extension.layout.temp.absolutePath, equalTo(new File('S:/temp/').absolutePath)
    }

    def "Initialize extension by setter"() {
        when: "we initialize the extension manually via setters"
        extension.seuHome = 'S:'
        extension.projectName = 'By method name'
        extension.layout = new SeuacLayout()
        extension.datastore = new SeuacDatastore()

        then: "we expect the correct extension values"
        that extension.seuHome, equalTo('S:')
        that extension.projectName, equalTo('By method name')
        that extension.layout, notNullValue()
        that extension.layout.codebase, nullValue()
        that extension.datastore, notNullValue()
    }

    def "Initialize extension by method"() {
        when: "we initialize the extension manually via method"
        extension.seuHome 'S:'
        extension.projectName 'By method name'
        extension.layout new SeuacLayout()
        extension.datastore new SeuacDatastore()

        then: "we expect the correct extension values"
        that extension.seuHome, equalTo('S:')
        that extension.projectName, equalTo('By method name')
        that extension.layout, notNullValue()
        that extension.datastore, notNullValue()
    }

    def "Initialize layout with closure"() {
        when: "we initialize the extension layout via closure"
        extension.layout {
            codebase new File("codebase")
            docbase new File("docbase")
            home new File("home")
            repository new File("repository")
            software new File("software")
            temp new File("temp")
        }

        then: "all values should be set correctly"
        expect extension.layout, notNullValue()
        expect extension.layout.codebase, notNullValue()
        expect extension.layout.docbase, notNullValue()
        expect extension.layout.home, notNullValue()
        expect extension.layout.repository, notNullValue()
        expect extension.layout.software, notNullValue()
        expect extension.layout.temp, notNullValue()
    }

    def "Initialize datastore with closure"() {
        when: "we initialize the extension datastore via closure"
        extension.datastore {
            url 'aUrl'
            user 'aUser'
            password 'aPassword'
        }

        then: "all values should be set correctly"
        expect extension.datastore, notNullValue()
        expect extension.datastore.url, equalTo('aUrl')
        expect extension.datastore.user, equalTo('aUser')
        expect extension.datastore.password, equalTo('aPassword')
    }
}
