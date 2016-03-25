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

import de.qaware.seu.as.code.plugins.credentials.impl.DPAPIEncryptor;
import de.qaware.seu.as.code.plugins.credentials.impl.PropertyCredentials;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;
import java.io.IOException;

/**
 * SEU-as-code Credentials plugin.
 * <p/>
 * Provides the 'credentials' object to get credentials and the 'setCredentials' task to set credentials.
 *
 * @author phxql
 */
public class SeuacCredentialsPlugin implements Plugin<Project> {
    /**
     * Name of the properties file.
     */
    private static final String PROPERTIES_FILE = "secure-credentials.properties";
    /**
     * Name of the credentials property in the build script.
     */
    private static final String CREDENTIALS_PROPERTY = "credentials";

    @Override
    public void apply(Project project) {
        Credentials credentials;
        try {
            credentials = new PropertyCredentials(new File(project.getGradle().getGradleUserHomeDir(), PROPERTIES_FILE), new DPAPIEncryptor());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        project.getExtensions().getExtraProperties().set(CREDENTIALS_PROPERTY, credentials);

        SetCredentialsTask setCredentialsTask = project.getTasks().create("setCredentials", SetCredentialsTask.class);
        setCredentialsTask.setCredentials(credentials);

        ClearCredentialsTask clearCredentialsTask = project.getTasks().create("clearCredentials", ClearCredentialsTask.class);
        clearCredentialsTask.setCredentials(credentials);
    }
}
