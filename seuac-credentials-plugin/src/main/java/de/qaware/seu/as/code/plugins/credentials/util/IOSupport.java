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
package de.qaware.seu.as.code.plugins.credentials.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Supports I/O opperations.
 */
public class IOSupport {

    /**
     * The reader to read user input.
     */
    private BufferedReader reader;

    /**
     * Constructor initializing the reader.
     */
    public IOSupport() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads a line form the console.
     *
     * @return The read line.
     * @throws IOException if an I/O error occurrs.
     */
    public String readLine() throws IOException {
        return reader.readLine();
    }
}
