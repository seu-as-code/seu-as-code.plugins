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

import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Test specification for the SeuacDatastore conventation.
 *
 * @author lreimer
 */
class SeuacDatastoreSpec extends Specification {
    SeuacDatastore datastore

    void setup() {
        datastore = new SeuacDatastore()
    }

    def "Validate default datastore"() {
        given: "a default data store"
        datastore = SeuacDatastore.defaultDatastore()

        expect: "the default values"
        that datastore, notNullValue()
        that datastore.url, equalTo('jdbc:h2:./seuac;mv_store=false')
        that datastore.user, equalTo('sa')
        that datastore.password, equalTo('sa')
    }

    def "Basic property setter assignment"() {
        when: "we initialize the data store using setters"
        datastore.url = 'url'
        datastore.user = 'user'
        datastore.password = 'password'

        then: "we expect the correct values"
        expect datastore.url, equalTo('url')
        expect datastore.user, equalTo('user')
        expect datastore.password, equalTo('password')
    }

    def "Basic property method assignment"() {
        when: "we initialize the data store using methods"
        datastore.url 'url'
        datastore.user 'user'
        datastore.password 'password'

        then: "we expect the correct values"
        expect datastore.url, equalTo('url')
        expect datastore.user, equalTo('user')
        expect datastore.password, equalTo('password')
    }
}
