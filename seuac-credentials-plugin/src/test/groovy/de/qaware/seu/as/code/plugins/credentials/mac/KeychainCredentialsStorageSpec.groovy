package de.qaware.seu.as.code.plugins.credentials.mac

import de.qaware.seu.as.code.plugins.credentials.Credentials
import de.qaware.seu.as.code.plugins.credentials.CredentialsException
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

    static final String SERVICE = 'nexus'
    static final String ACCOUNT = 'seu-as-code'

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
        def found = storage.findCredentials(SERVICE)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, _, _, _) >> Security.errSecItemNotFound
        found == null
    }

    def "Try to find credentials in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.findCredentials(SERVICE)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, _, _, _) >> 42
        thrown(CredentialsException)
    }

    def "Set a new credentials in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(SERVICE, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecItemNotFound
        1 * security.SecKeychainAddGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, password.length(), password.bytes, null) >> Security.errSecSuccess
    }

    def "Update an existing credentials in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(SERVICE, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecSuccess
        1 * security.SecKeychainItemModifyContent(null, null, password.length(), password.bytes) >> Security.errSecSuccess
        1 * coreFoundation.CFRelease(null)
    }

    def "Try to set credentials in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.setCredentials(SERVICE, credentials)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> 42
        thrown(CredentialsException)
    }

    def "Clear an unknown credential in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(SERVICE)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecItemNotFound
        0 * security.SecKeychainItemDelete(_)
    }

    def "Try to clear a credential in keystore with error"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(SERVICE)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> 42
        thrown(CredentialsException)
    }

    def "Clear a credential in keystore"() {
        given:
        def storage = new KeychainCredentialsStorage(security, coreFoundation)

        when:
        storage.clearCredentials(SERVICE)

        then:
        1 * security.SecKeychainFindGenericPassword(null, SERVICE.length(), SERVICE.bytes, ACCOUNT.length(), ACCOUNT.bytes, null, null, _) >> Security.errSecSuccess
        1 * security.SecKeychainItemDelete(null)
        1 * coreFoundation.CFRelease(null)
    }

    @Requires({ os.macOs })
    def "Integration test on MacOS"() {
        given:
        def storage = new KeychainCredentialsStorage()

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
