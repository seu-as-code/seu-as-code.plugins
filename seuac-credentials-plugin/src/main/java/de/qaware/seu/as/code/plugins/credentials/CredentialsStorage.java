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

import java.nio.charset.Charset;

/**
 * This is the main interface to store and handle credentials. Platform specific implementations
 * that do the heavy lifting of securely storing the credentials.
 *
 * @author lreimer
 */
public interface CredentialsStorage {
    /**
     * The default charset to use for credential storage.
     */
    Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Find the credentials for the given service name.
     *
     * @param service the service name
     * @return the stored Credentials or NULL if not present
     * @throws CredentialsException error finding the credentials
     */
    Credentials findCredentials(String service) throws CredentialsException;

    /**
     * Set the given username and password as credential for the specified service.
     *
     * @param service  the service name
     * @param username the username to set
     * @param password the password to set
     * @throws CredentialsException error storing the credentials
     */
    void setCredentials(String service, String username, char[] password) throws CredentialsException;

    /**
     * Set the given credentials object for the specified service.
     *
     * @param service     the service name
     * @param credentials the credentials object
     * @throws CredentialsException error storing the credentials
     */
    void setCredentials(String service, Credentials credentials) throws CredentialsException;

    /**
     * Clear the stored credentials for the given service name.
     *
     * @param service the service name
     * @throws CredentialsException error clearing the credentials
     */
    void clearCredentials(String service) throws CredentialsException;

    /**
     * NoOp implementation of a credential storage. Does not store anything.
     */
    class None implements CredentialsStorage {

        @Override
        public Credentials findCredentials(String service) {
            return null;
        }

        @Override
        public void setCredentials(String service, String username, char[] password) {

        }

        @Override
        public void setCredentials(String service, Credentials credentials) {

        }

        @Override
        public void clearCredentials(String service) {
        }
    }
}
