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

import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Unroll

/**
 * The Spock spec of the OperatingSystem enum class.
 *
 * @author lreimer
 */
class OperatingSystemSpec extends Specification {
    @Requires({ os.macOs })
    def "Check current on Mac OS"() {
        expect:
        OperatingSystem.isMacOs()
        OperatingSystem.isSupported()
    }

    @Requires({ os.windows })
    def "Check current on Windows"() {
        expect:
        OperatingSystem.isWindows()
        OperatingSystem.isSupported()
    }

    @Requires({ os.linux })
    def "Check current on Linux"() {
        expect:
        OperatingSystem.isLinux()
        OperatingSystem.isSupported()
    }

    @Unroll
    def "Check for OS #name"() {
        expect:
        OperatingSystem.forOsName(name) == os

        where:
        name         || os
        'Windows 98' || OperatingSystem.WINDOWS
        'Linux'      || OperatingSystem.LINUX
        'Mac OS X'   || OperatingSystem.MAC_OS
        'Darwin'     || OperatingSystem.MAC_OS
        'OSX'        || OperatingSystem.MAC_OS
    }
}
