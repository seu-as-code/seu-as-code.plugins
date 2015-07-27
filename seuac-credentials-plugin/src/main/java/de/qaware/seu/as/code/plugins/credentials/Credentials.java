/*
 *    Copyright 2015 QAware GmbH
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

import java.io.IOException;

/**
 * Get and set credentials.
 *
 * @author phxql
 */
public interface Credentials {
    /**
     * Returns the credential with the given key. Returns null, if no credential with the given key could be found.
     *
     * @param key Key.
     * @return Value of the credential. Null, if no credential with the given key could be found.
     */
    String get(String key);

    /**
     * Sets the credential.
     *
     * @param key   Key.
     * @param value Value.
     */
    void set(String key, String value);

    /**
     * Saves the credentials.
     *
     * @throws IOException If something went wrong while saving.
     */
    void save() throws IOException;
}
