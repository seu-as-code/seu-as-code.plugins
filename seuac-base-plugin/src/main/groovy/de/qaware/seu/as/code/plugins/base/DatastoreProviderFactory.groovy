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

/**
 * The factory singleton to get PersistenceService instances for a given SeuacDatastore configuration.
 *
 * @author mario-leander.reimer
 */
@Singleton
class DatastoreProviderFactory {

    private def providers = ['jdbc:h2': JdbcH2DatastoreProvider, 'file:mapdb': MapDbDatastoreProvider]

    private def instances = [:]

    /**
     * Creates a suitable PersistenceService instance for the given data store config.
     *
     * @param datastore the SeuacDatastore configuration
     * @return a suitable persistence service
     */
    DatastoreProvider get(SeuacDatastore datastore) {
        def key = keyFor(datastore)

        if (instances[key] == null) {
            def providerClass = providers[keyFor(datastore)]
            if (providerClass == null) throw new GradleException("Unsupported PersistenceService for $datastore.url")
            instances[key] = providerClass.newInstance(datastore)
        }
        instances[key]
    }

    private String keyFor(SeuacDatastore datastore) {
        def url = datastore.url
        url.substring(0, url.lastIndexOf(':'))
    }
}
