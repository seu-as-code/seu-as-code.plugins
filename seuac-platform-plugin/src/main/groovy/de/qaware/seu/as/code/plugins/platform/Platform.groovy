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
package de.qaware.seu.as.code.plugins.platform

/**
 * Used to determine the current operating system.
 *
 * @author lreimer
 */
enum Platform {
    Windows('win'), MacOs('mac'), Unix('unix'), Unknown('???');

    final def osClassifier

    private Platform(osClassifier) {
        this.osClassifier = osClassifier
    }

    /**
     * The OS family for the current platform.
     *
     * @return either windows, macos, or unix
     */
    def String getOsFamily() {
        name().toLowerCase(Locale.ENGLISH)
    }

    /**
     * Returns the current platform architecture.
     *
     * @return either x86_64 or x86
     */
    def String getOsArch() {
        is64bit() ? 'x86_64' : 'x86'
    }

    /**
     * Is this platform a 64-bit platform?
     *
     * @return true if so, otherwise false
     */
    static boolean is64bit() {
        (System.getenv("ProgramFiles(x86)") != null) || (System.getProperty("os.arch").indexOf("64") != -1)
    }

    /**
     * Returns the current operating system we are running on.
     *
     * @return the current Operating system
     */
    static Platform current() {
        fromOsName(System.getProperty("os.name"))
    }

    /**
     * Get the matching {@link Platform} instance for the given name.
     * If the name is not supported than this method returns NULL.
     *
     * @param name the OS name
     * @return the OperatingSystem or NULL
     */
    static Platform fromOsName(final String name) {
        String osName = name.toLowerCase()
        if (osName.contains("windows")) {
            Windows
        } else if (osName.contains("mac os x") || osName.contains("darwin") || osName.contains("osx")) {
            MacOs
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            Unix
        } else {
            Unknown
        }
    }

    /**
     * Is current platform Windows?
     *
     * @return true on Windows, else false
     */
    static boolean isWindows() {
        current() == Windows
    }

    /**
     * Is current platform Unix?
     *
     * @return true on Unix, else false
     */
    static boolean isUnix() {
        current() == Unix
    }

    /**
     * Is current plaform MacOs?
     *
     * @return true on MacOs, else false
     */
    static boolean isMacOs() {
        current() == MacOs
    }
}