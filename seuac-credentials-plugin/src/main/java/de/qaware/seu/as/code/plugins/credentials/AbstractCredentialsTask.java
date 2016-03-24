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

import de.qaware.seu.as.code.plugins.credentials.impl.ConsoleReader;
import org.gradle.api.DefaultTask;
import org.gradle.api.internal.tasks.options.Option;

/**
 * Abstract base class for credentials tasks.
 *
 * @author clboettcher
 */
public abstract class AbstractCredentialsTask extends DefaultTask {

    private String key;

    private Credentials credentials;

    private ConsoleReader consoleReader;

    /**
     * Initialize the console reader.
     */
    protected AbstractCredentialsTask() {
        this.consoleReader = new ConsoleReader();
    }

    /**
     * Sets the credentials.
     *
     * @param credentials Credentials.
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Gets the key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key Key.
     */
    @Option(option = "key", description = "The credentials key.")
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return The {@link Credentials}.
     */
    /* package-private */ Credentials getCredentials() {
        return credentials;
    }

    /**
     * @return The {@link ConsoleReader}.
     */
    /* package-private */ ConsoleReader getConsoleReader() {
        return consoleReader;
    }

    /**
     * Sets the {@link ConsoleReader} instance.
     *
     * @param consoleReader The I/O support.
     */
    public void setConsoleReader(ConsoleReader consoleReader) {
        this.consoleReader = consoleReader;
    }
}
