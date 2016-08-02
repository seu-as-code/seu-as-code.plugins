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
 * Spec to check proper functionality of the Credentials class.
 *
 * @author lreimer
 */
class CredentialsSpec extends Specification {
    def "Check toString and fromString"() {
        given:
        def toString = new Credentials('Max', 'Mustermann').toString()

        when:
        def credentials = new Credentials(toString)

        then:
        credentials.username == 'Max'
        credentials.password == 'Mustermann'
    }

    def "Check invalid Credentials representation"() {
        when:
        new Credentials("invalid")

        then:
        thrown(CredentialsException)
    }
}
