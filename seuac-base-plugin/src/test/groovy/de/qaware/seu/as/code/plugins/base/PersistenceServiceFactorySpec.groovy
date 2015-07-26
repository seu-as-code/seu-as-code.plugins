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

import org.gradle.api.GradleException
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore
import static org.hamcrest.Matchers.instanceOf
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

/**
 * Test specification for the PersistenceServiceFactory.
 *
 * @author mario-leander.reimer
 */
class PersistenceServiceFactorySpec extends Specification {
    SeuacDatastore datastore

    void setup() {
        datastore = new SeuacDatastore()
    }

    def "Create a JDBC H2 PersistenceService instance"() {
        given:
        datastore = defaultDatastore()
        datastore.url = 'jdbc:h2:build/seuac'

        when:
        DatastoreProvider service = DatastoreProviderFactory.instance.get(datastore)

        then:
        expect service, notNullValue()
        expect service, instanceOf(JdbcH2DatastoreProvider)
    }

    def "Create a MapDB PersistenceService instance"() {
        given:
        datastore.url = 'file:mapdb:build/seuac.dbmap'

        when:
        DatastoreProvider service = DatastoreProviderFactory.instance.get(datastore)

        then:
        expect service, notNullValue()
        expect service, instanceOf(MapDbDatastoreProvider)
    }

    def "Create unknown PersistenceService instance"() {
        given:
        datastore.url = 'unknown:db:seuac'

        when:
        DatastoreProviderFactory.instance.get(datastore)

        then:
        thrown(GradleException)
    }
}
