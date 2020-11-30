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

import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.options.Option;

/**
 * Abstract base class for storage tasks.
 *
 * @author clboettcher
 */
public abstract class AbstractCredentialsTask extends DefaultTask {

    private String service;
    private String username;

    private CredentialsStorageFactory storageFactory;
    private SystemConsole console;

    /**
     * Initialize the console reader.
     */
    protected AbstractCredentialsTask() {
        this.setGroup("Security");
        this.setEnabled(OperatingSystem.isSupported());
    }

    /**
     * Sets the service name. This method may also be called when
     * the service name is set via the command line option.
     *
     * @param service the service nam.
     */
    @Option(option = "service", description = "The service the credentials are for.")
    public void setService(String service) {
        this.service = service;
    }

    /**
     * This method will return the service name, either fro mthe command line option or
     * it will be read from the command line.
     *
     * @return the service name
     */
    public String getService() {
        if (StringUtils.isEmpty(service)) {
            service = console.readLine("%nEnter service:");
        }
        return service;
    }

    /**
     * This method will return the username, either from the command line option or
     * it will be read from the command line.
     *
     * @return the username
     */
    public String getUsername() {
        if (StringUtils.isEmpty(username)) {
            username = console.readLine("%nEnter username:");
        }
        return username;
    }

    /**
     * Sets the username. This method may also be calles when the
     * username is set via the command line options.
     *
     * @param username the username
     */
    @Option(option = "username", description = "The username for the credentials.")
    public void setUsername(String username) {
        this.username = username;
    }

    protected CredentialsStorage getStorage() {
        return storageFactory.create();
    }

    CredentialsStorageFactory getStorageFactory() {
        return storageFactory;
    }

    void setStorageFactory(CredentialsStorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    protected SystemConsole getConsole() {
        return console;
    }

    void setConsole(SystemConsole console) {
        this.console = console;
    }

    /**
     * This method will read the password from the console and return it.
     *
     * @return the password
     */
    protected char[] getPassword() {
        return console.readPassword("Enter password:");
    }
}
