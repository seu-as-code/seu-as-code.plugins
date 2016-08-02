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
import org.gradle.api.tasks.TaskAction;

/**
 * Task to to clear the stored credentials for a given service.
 * <p>
 * Invoke with 'gradle clearCredentials --service [name of service]' or alternatively with
 * 'gradle setCredentials --service [name of service] --username [the username]'
 * <p/>
 *
 * @author clboettcher
 */
public class ClearCredentialsTask extends AbstractCredentialsTask {

    /**
     * Constructor initializing the tasks meta data.
     */
    public ClearCredentialsTask() {
        this.setDescription("Clears the credentials.");
    }

    /**
     * Is executed from gradle when running the 'clearCredentials' task.
     */
    @TaskAction
    public void onAction() {
        String service = getService();
        if (StringUtils.isNotBlank(service)) {
            String answer = getConsole().readLine("Clear credentials for service %s (y/N)?", service);
            if (StringUtils.equalsIgnoreCase(answer, "y")) {
                getStorage().clearCredentials(service);
            }
        }
    }
}
