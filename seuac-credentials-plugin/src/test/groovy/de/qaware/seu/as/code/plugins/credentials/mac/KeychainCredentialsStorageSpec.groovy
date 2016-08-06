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
package de.qaware.seu.as.code.plugins.credentials.mac

import de.qaware.seu.as.code.plugins.credentials.Credentials
import de.qaware.seu.as.code.plugins.credentials.CredentialsException
import de.qaware.seu.as.code.plugins.credentials.CredentialsExtension
import org.apache.commons.codec.binary.Base64
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.credentials.CredentialsStorage.UTF_8

/**
 * Test spec for the MacOS Keychain credential store.
 *
 * @author lreimer
 */
class KeychainCredentialsStorageSpec extends Specification {

    static final String SERVICE = 'SEU-as-code'
    static final String ACCOUNT = 'nexus'

    @Shared
    def credentials = new Credentials('Max', 'Mustermann')

    Security security = Mock(Security)
    CoreFoundation coreFoundation = Mock(CoreFoundation)
    String password

    void setup() {
        def secret = credentials.toSecret()
        password = Base64.encodeBase64String(secret.getBytes(UTF_8))
    }

    def "Find no credentials in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        def found = storage.findCredentials(ACCOUNT)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, _, _, _) >> Security.errSecItemNotFound
        found == null
    }

    def "Try to find credentials in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.findCredentials(ACCOUNT)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, _, _, _) >> 42
        thrown(CredentialsException)
    }

    def "Set a new credentials in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(ACCOUNT, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecItemNotFound
        1 * security.SecKeychainAddGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, password.length(), password.bytes, null) >> Security.errSecSuccess
    }

    def "Update an existing credentials in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(ACCOUNT, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecSuccess
        1 * security.SecKeychainItemModifyContent(_, null, password.length(), password.bytes) >> Security.errSecSuccess
        1 * coreFoundation.CFRelease(_)
    }

    def "Try to set credentials in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(ACCOUNT, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> 42
        thrown(CredentialsException)
    }

    def "Clear an unknown credential in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(ACCOUNT)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecItemNotFound
        0 * security.SecKeychainItemDelete(_)
    }

    def "Try to clear a credential in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(ACCOUNT)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> 42
        thrown(CredentialsException)
    }

    def "Clear a credential in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(ACCOUNT)

        then:
        1 * security.SecKeychainFindGenericPassword(_, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecSuccess
        1 * security.SecKeychainItemDelete(null)
        1 * coreFoundation.CFRelease(null)
    }

    @Requires({ os.macOs })
    def "Integration test on MacOS"() {
        given:
        def storage = new KeychainCredentialsStorage(new CredentialsExtension())

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
