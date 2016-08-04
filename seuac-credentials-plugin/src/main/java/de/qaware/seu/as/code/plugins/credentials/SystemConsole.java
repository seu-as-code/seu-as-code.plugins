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
 * Supports I/O opperations.
 */
public class SystemConsole {

    /**
     * Reads a line from the console.
     *
     * @param prompt the command line prompt
     * @param args   any arguments for the prompt
     * @return the line read from the console
     */
    public String readLine(String prompt, Object... args) {
        return System.console().readLine(prompt, args);
    }

    /**
     * Reads a password from the console.
     *
     * @param prompt the command line prompt
     * @return the password read from the console
     */
    public char[] readPassword(String prompt) {
        return System.console().readPassword(prompt);
    }

    /**
     * Output the given format string to the current system console.
     *
     * @param fmt  the format string to output
     * @param args any message arguments
     */
    public void format(String fmt, Object... args) {
        System.console().format(fmt, args);
    }
}
