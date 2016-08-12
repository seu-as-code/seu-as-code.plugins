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
 * An instance of the class will be registered as extra property of the Grtadle project.
 * It mainly serves as a secure wrapper around the credential storage and provides a
 * convenient DSL way to access a credential for a service.
 * <p>
 * <pre>
 * credentials {
 *    // use array type access to credentials via service name
 *    username project.credentials['nexus'].username
 *    password project.credentials['nexus'].password
 *
 *    // use getter access to credentials via service name
 *    username project.credentials.get('nexus').username
 *    password project.credentials.get('nexus').password
 *
 *    // or use string interpolation
 *    username "${credentials['nexus'].username}"
 *    password "${credentials['nexus'].password}"
 * }
 * </pre>
 *
 * @author lreimer
 */
public class CredentialsProperty {
    /**
     * Name of the credentials property in the build script.
     */
    static final String NAME = "credentials";

    private final CredentialsStorageFactory storageFactory;

    /**
     * Initialize the credentials property with a storage factory.
     *
     * @param storageFactory the storage factory
     */
    public CredentialsProperty(CredentialsStorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    /**
     * Returns the credentials with the given service name.
     *
     * @param service the service name
     * @return the credentials or Credentials.EMPTY if not found
     */
    public Credentials get(String service) {
        CredentialsStorage storage = storageFactory.create();
        Credentials credentials = storage.findCredentials(service);
        return (credentials != null) ? credentials : Credentials.EMPTY;
    }
}
