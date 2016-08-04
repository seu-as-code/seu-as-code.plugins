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
package de.qaware.seu.as.code.plugins.credentials

import org.apache.commons.codec.binary.Base64
import spock.lang.Specification

import java.nio.charset.Charset

/**
 * Spec to check proper functionality of the Credentials class.
 *
 * @author lreimer
 */
class CredentialsSpec extends Specification {
    def "Check toSecret and fromSecret"() {
        given:
        def secret = new Credentials('Max', 'Mustermann').toSecret()

        when:
        def credentials = Credentials.fromSecret(secret)

        then:
        secret == '''{"username":"Max","password":"Mustermann"}'''
        credentials.username == 'Max'
        credentials.password == 'Mustermann'
    }

    def "Check invalid secret representation"() {
        when:
        Credentials.fromSecret("invalid")

        then:
        thrown(CredentialsException)
    }

    def "Check UTF-8 charset"() {
        expect:
        Charset.forName("UTF-8").name() == "UTF-8"
    }

    def "toSecret then fromSecret with Base64"() {
        given:
        def credential = new Credentials('Max', 'Mustermann')
        def secret64 = Base64.encodeBase64String(credential.toSecret().getBytes(Charset.forName("UTF-8")))

        when:
        def decoded = new String(Base64.decodeBase64(secret64), Charset.forName("UTF-8"))
        def fromSecret = Credentials.fromSecret(decoded)

        then:
        fromSecret.username == 'Max'
        fromSecret.password == 'Mustermann'
    }
}
