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
package de.qaware.seu.as.code.plugins.credentials.mac;

import de.qaware.seu.as.code.plugins.credentials.Credentials;
import de.qaware.seu.as.code.plugins.credentials.CredentialsStorage;

/**
 * The platform specific implementation that uses the MacSO keychain mechanism.
 *
 * @author lreimer
 */
public class KeychainCredentialsStorage implements CredentialsStorage {
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
