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

import org.gradle.api.tasks.TaskAction;

/**
 * Task to display the credentials for a given service.
 * <p>
 * Invoke with 'gradle displayCredentials --service [name of service]'
 * <p/>
 *
 * @author lreimer
 */
public class DisplayCredentialsTask extends AbstractCredentialsTask {

    /**
     * Constructor initializing the tasks meta data.
     */
    public DisplayCredentialsTask() {
        this.setDescription("Displays the credentials.");
    }

    /**
     * Is executed from gradle when running the 'setStorage' task.
     */
    @TaskAction
    public void onAction() {
        String service = getService();
        Credentials credentials = getStorage().findCredentials(service);
        if (credentials != null) {
            getConsole().format("Current credentials for service '%s' -> {'%s', '%s'}.%n", service,
                    credentials.getUsername(), credentials.getPassword());
        } else {
            getConsole().format("No credentials found for service '%s'.%n", service);
        }
    }
}
