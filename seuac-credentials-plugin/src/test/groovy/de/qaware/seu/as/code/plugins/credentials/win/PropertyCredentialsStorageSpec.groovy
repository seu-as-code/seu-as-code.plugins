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
package de.qaware.seu.as.code.plugins.credentials.win

import de.qaware.seu.as.code.plugins.credentials.Credentials
import de.qaware.seu.as.code.plugins.credentials.CredentialsExtension
import de.qaware.seu.as.code.plugins.credentials.Encryptor
import org.apache.commons.codec.binary.Base64
import org.gradle.api.Project
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * Unit spec for the PropertyCredentials.
 *
 * @author lreimer
 */
@Stepwise
class PropertyCredentialsStorageSpec extends Specification {

    @Shared
    File file

    @Shared
    Credentials credentials

    @Shared
    byte[] bytes

    void setupSpec() {
        file = File.createTempFile("seuac-storage", ".properties")
        credentials = new Credentials('Max', 'Mustermann')
        bytes = credentials.toSecret().getBytes('UTF-8')
    }

    void cleanupSpec() {
        file.delete()
    }

    def "1. Set and find credentials properties"() {
        given: "an encryptor stub and an empty crendentials store"
        Encryptor encryptor = Stub(Encryptor)
        encryptor.decrypt(bytes) >> bytes
        encryptor.encrypt(bytes) >> bytes

        PropertyCredentialsStorage props = new PropertyCredentialsStorage(file, encryptor)

        when: "we set a credential for a service"
        props.setCredentials('nexus', credentials);

        then: "there should be a credential stored"
        expect props.findCredentials('nexus').username, is('Max')
        expect props.findCredentials('nexus').password, is('Mustermann')
    }

    def "2. Check credentials in properties are Base64"() {
        given: "a fresh properties loaded from file"
        Properties properties = new Properties()
        FileInputStream fileInputStream = new FileInputStream(file)
        try {
            properties.load(fileInputStream)
        } finally {
            fileInputStream.close()
        }

        expect: "a Base64 encoded value"
        that properties.getProperty('nexus'), is(Base64.encodeBase64String(bytes))
    }

    def "3. Remove another credential"() {
        given: "a storage store"
        def encryptor = Stub(Encryptor)
        encryptor.decrypt(bytes) >> bytes
        encryptor.encrypt(bytes) >> bytes

        def props = new PropertyCredentialsStorage(file, encryptor)
        props.setCredentials('other', credentials)

        when: "we remove one credential"
        props.clearCredentials('other')

        then: "the credential is removed, other storage are not touched, the file still exists"
        expect props.findCredentials('other'), nullValue()
        expect props.findCredentials('nexus'), notNullValue()
        expect file.exists(), is(true)
    }

    def "4. Check custom location"() {
        given:
        def encryptor = Stub(Encryptor)
        encryptor.decrypt(bytes) >> bytes
        encryptor.encrypt(bytes) >> bytes

        def extension = new CredentialsExtension()
        extension.propertyFile = file.getAbsolutePath()
        def project = Stub(Project)
        def props = new PropertyCredentialsStorage(project, extension, encryptor)

        expect:
        props.findCredentials('nexus').username == 'Max'
        props.findCredentials('nexus').password == 'Mustermann'
    }
}
