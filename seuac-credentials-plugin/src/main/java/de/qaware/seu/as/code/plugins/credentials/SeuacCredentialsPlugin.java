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

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * SEU-as-code Credentials plugin.
 * <p/>
 * Provides the 'credentials' object to get credentials and the 'setStorage' task to set credentials.
 *
 * @author phxql
 */
public class SeuacCredentialsPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        SystemConsole console = new SystemConsole();
        CredentialsStorageFactory factory = new CredentialsStorageFactory.Default(project);

        // register the credentials configuration with the project
        CredentialsExtension extension = new CredentialsExtension();
        project.getExtensions().add(CredentialsExtension.NAME, extension);

        CredentialsProperty property = new CredentialsProperty(factory);
        project.getExtensions().getExtraProperties().set(CredentialsProperty.NAME, property);

        SetCredentialsTask setCredentialsTask = project.getTasks().create("setCredentials", SetCredentialsTask.class);
        initTask(setCredentialsTask, console, factory);

        ClearCredentialsTask clearCredentialsTask = project.getTasks().create("clearCredentials", ClearCredentialsTask.class);
        initTask(clearCredentialsTask, console, factory);

        DisplayCredentialsTask displayCredentialsTask = project.getTasks().create("displayCredentials", DisplayCredentialsTask.class);
        initTask(displayCredentialsTask, console, factory);
    }

    private void initTask(AbstractCredentialsTask task, SystemConsole console, CredentialsStorageFactory factory) {
        task.setConsole(console);
        task.setStorageFactory(factory);
    }
}
