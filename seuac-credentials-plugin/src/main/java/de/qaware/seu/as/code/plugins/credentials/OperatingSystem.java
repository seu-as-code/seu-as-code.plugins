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
package de.qaware.seu.as.code.plugins.credentials;

/**
 * Enum to represent the supported operating systems. Gradle has an internal API we
 * could have used but we didn' want to so we implemented it ourselves.
 *
 * @author lreimer
 */
public enum OperatingSystem {
    WINDOWS, MAC_OS;

    /**
     * Returns the current operating system we are running on.
     *
     * @return the current Operating system
     */
    public static OperatingSystem current() {
        return forOsName(System.getProperty("os.name"));
    }

    /**
     * Get the matching {@link OperatingSystem} instance for the given name.
     * If the name is not supported than this method returns NULL.
     *
     * @param name the OS name
     * @return the OperatingSystem or NULL
     */
    public static OperatingSystem forOsName(final String name) {
        String osName = name.toLowerCase();
        if (osName.contains("windows")) {
            return WINDOWS;
        } else if (osName.contains("mac os x") || osName.contains("darwin") || osName.contains("osx")) {
            return MAC_OS;
        } else {
            return null;
        }
    }
}
