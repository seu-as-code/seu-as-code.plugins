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

import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Test and check the Platform enum behaviour.
 *
 * @author lreimer
 */
class PlatformSpec extends Specification {

    @Requires({ os.macOs })
    def "Check current on Mac OS"() {
        expect:
        Platform.current() == Platform.MacOs
        Platform.isMacOs()
    }

    @Requires({ os.windows })
    def "Check current on Windows"() {
        expect:
        Platform.current() == Platform.Windows
        Platform.isWindows()
    }

    @Unroll
    def "Check Platform from OS #name"() {
        expect:
        Platform.fromOsName(name) == os

        where:
        name         || os
        'Windows 98' || Platform.Windows
        'Linux'      || Platform.Unix
        'Mac OS X'   || Platform.MacOs
        'Darwin'     || Platform.MacOs
        'OSX'        || Platform.MacOs
        'Android'    || Platform.Unknown
    }
}
