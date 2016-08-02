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

import spock.lang.Specification

/**
 * Basic test spec to check the extension property to access the credentials.
 *
 * @author lreimer
 */
class CredentialsPropertySpec extends Specification {

    CredentialsStorage storage = Mock(CredentialsStorage)

    def "Get Credentials via typed get method"() {
        setup:
        storage.findCredentials('nexus') >> new Credentials('Max', 'Mustermann')
        def extension = new CredentialsProperty(storage)

        when:
        def credentials = extension.get('nexus')

        then:
        credentials?.username == 'Max'
        credentials?.password == 'Mustermann'
    }

    def "Get Credentials via index operator"() {
        setup:
        storage.findCredentials('nexus') >> new Credentials('Max', 'Mustermann')
        def extension = new CredentialsProperty(storage)

        when:
        def credentials = extension['nexus']

        then:
        credentials?.username == 'Max'
        credentials?.password == 'Mustermann'
    }

    def "Get Credentials using GString replacement"() {
        setup:
        storage.findCredentials('nexus') >> new Credentials('Max', 'Mustermann')
        def extension = new CredentialsProperty(storage)

        when:
        def credentials = "${extension['nexus'].username} ${extension['nexus'].password}"

        then:
        credentials == 'Max Mustermann'
    }

    def "Get unknown empty Credentials"() {
        setup:
        storage.findCredentials('nexus') >> null
        def extension = new CredentialsProperty(storage)

        when:
        def credentials = extension.get('nexus')

        then:
        credentials == Credentials.EMPTY
    }
}
