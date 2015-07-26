/*
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

/**
 * Simple data type to represent the datastore configuration.
 *
 * @author mario-leander.reimer
 */
class SeuacDatastore {

    String url
    String user
    String password

    /**
     * Short hand method to set the URL via method.
     *
     * @param aUrl a datastore provider URL
     */
    void url(String aUrl) {
        this.url = aUrl
    }

    /**
     * Short hand method to set the datastore user. Might be NULL.
     *
     * @param aUser a datastore user if required
     */
    void user(String aUser) {
        this.user = aUser
    }

    /**
     * Short hand method to set the datastore password. Might be NULL.
     *
     * @param aPassword a datastore password if required
     */
    void password(String aPassword) {
        this.password = aPassword
    }

    /**
     * Convenience factory method to create the default datastore (H2) instance.
     *
     * @return a datastore configuration instance
     */
    static SeuacDatastore defaultDatastore() {
        new SeuacDatastore(url: 'jdbc:h2:seuac', user: 'sa', password: 'sa')
    }
}
