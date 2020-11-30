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

import org.gradle.api.GradleException
import spock.lang.Specification
import spock.lang.Unroll

class DatastoreProviderFactorySpec extends Specification {
    @Unroll
    def "Get datastore provider class for #url"() {
        given:
        def datastore = new SeuacDatastore(url: url)

        when:
        def datastoreProviderClass = DatastoreProviderFactory.instance.getProviderClass(datastore)

        then:
        datastoreProviderClass != null
        datastoreProviderClass == expectedProviderClass

        when:
        def datastoreProviderClass2ndCall = DatastoreProviderFactory.instance.getProviderClass(datastore)

        then:
        datastoreProviderClass == datastoreProviderClass2ndCall

        where:
        url                       | expectedProviderClass
        "jdbc:h2:C:\\mydb"        | JdbcH2DatastoreProvider
        "jdbc:h2:./mydb"          | JdbcH2DatastoreProvider
        "jdbc:h2:/home/user/mydb" | JdbcH2DatastoreProvider
        "file:mapdb:mydb"         | MapDbDatastoreProvider
    }

    def "Get unknown datastore provider class"() {
        given:
        def datastore = new SeuacDatastore(url: "invalid")

        when:
        DatastoreProviderFactory.instance.getProviderClass(datastore)

        then:
        thrown(GradleException)
    }
}
