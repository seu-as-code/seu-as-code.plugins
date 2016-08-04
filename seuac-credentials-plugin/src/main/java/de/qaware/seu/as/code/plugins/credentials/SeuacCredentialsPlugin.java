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

import de.qaware.seu.as.code.plugins.credentials.mac.KeychainCredentialsStorage;
import de.qaware.seu.as.code.plugins.credentials.win.PropertyCredentialsStorage;
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
        CredentialsStorage storage = createCredentialsStorage(project);
        SystemConsole console = new SystemConsole();

        // register the extra credential property to access credentials in the build script
        project.getExtensions().getExtraProperties().set(CredentialsProperty.NAME, new CredentialsProperty(storage));

        SetCredentialsTask setCredentialsTask = project.getTasks().create("setCredentials", SetCredentialsTask.class);
        initTask(setCredentialsTask, storage, console);

        ClearCredentialsTask clearCredentialsTask = project.getTasks().create("clearCredentials", ClearCredentialsTask.class);
        initTask(clearCredentialsTask, storage, console);

        DisplayCredentialsTask displayCredentialsTask = project.getTasks().create("displayCredentials", DisplayCredentialsTask.class);
        initTask(displayCredentialsTask, storage, console);
    }

    private void initTask(AbstractCredentialsTask task, CredentialsStorage storage, SystemConsole console) {
        task.setStorage(storage);
        task.setConsole(console);
    }

    private CredentialsStorage createCredentialsStorage(Project project) {
        CredentialsStorage storage;

        if (OperatingSystem.isWindows()) {
            storage = new PropertyCredentialsStorage(project);
        } else if (OperatingSystem.isMacOs()) {
            storage = new KeychainCredentialsStorage();
        } else {
            project.getLogger().warn("Unsupported OS. All credential tasks will be disabled.");
            storage = new CredentialsStorage.None();
        }
        return storage;
    }
}
