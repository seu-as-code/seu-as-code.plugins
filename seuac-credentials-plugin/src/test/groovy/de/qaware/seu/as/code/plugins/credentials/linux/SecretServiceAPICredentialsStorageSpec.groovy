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
package de.qaware.seu.as.code.plugins.credentials.linux

import de.qaware.seu.as.code.plugins.credentials.Credentials
import spock.lang.Requires
import spock.lang.Specification

/**
 * Test spec for the MacOS Keychain credential store.
 *
 * @author lreimer
 */
class SecretServiceAPICredentialsStorageSpec extends Specification {


    @Requires({ os.linux })
    def "Integration test on Linux"() {
        given:
        def storage = new SecretServiceAPICredentialsStorage()
        def credentials = new Credentials('Max', 'Mustermann')

        when:
        storage.setCredentials('spock', credentials)
        def found = storage.findCredentials('spock')
        storage.clearCredentials('spock')

        then:
        found?.username == credentials.username
        found?.password == credentials.password
        storage.findCredentials('spock') == null
    }

}
