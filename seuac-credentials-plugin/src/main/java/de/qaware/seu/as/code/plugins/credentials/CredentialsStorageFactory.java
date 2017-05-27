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

import de.qaware.seu.as.code.plugins.credentials.linux.SecretServiceAPICredentialsStorage;
import de.qaware.seu.as.code.plugins.credentials.mac.KeychainCredentialsStorage;
import de.qaware.seu.as.code.plugins.credentials.win.PropertyCredentialsStorage;
import org.gradle.api.Project;

/**
 * A factory method implementation to create {@link CredentialsStorage} instances.
 *
 * @author lreimer
 */
interface CredentialsStorageFactory {
    /**
     * Create a configured CredentialsStorage instance for the given project.
     *
     * @return a CredentialsStorage instance
     */
    CredentialsStorage create();

    /**
     * The default storage implementation.
     */
    class Default implements CredentialsStorageFactory {

        private final Project project;

        /**
         * Initialize the factory with a Gradle project.
         *
         * @param project the project
         */
        Default(Project project) {
            this.project = project;
        }

        @Override
        public CredentialsStorage create() {
            CredentialsExtension extension = project.getExtensions().getByType(CredentialsExtension.class);
            project.getLogger().info("Using {}", extension);

            CredentialsStorage storage;
            if (OperatingSystem.isWindows()) {
                storage = new PropertyCredentialsStorage(project, extension);
            } else if (OperatingSystem.isMacOs()) {
                storage = new KeychainCredentialsStorage(extension);
            } else if (OperatingSystem.isLinux()) {
                storage = new SecretServiceAPICredentialsStorage();
            } else {
                project.getLogger().warn("Unsupported OS. All credential tasks will be disabled.");
                storage = new CredentialsStorage.None();
            }
            return storage;
        }
    }
}
