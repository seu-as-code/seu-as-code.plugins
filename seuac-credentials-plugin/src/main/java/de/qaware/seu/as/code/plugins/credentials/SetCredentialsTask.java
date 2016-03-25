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

import java.io.IOException;

/**
 * Task to set credentials.
 * <p/>
 * Invoke with 'gradle setCredentials --key [Name of the key]'
 * <p/>
 * The task will query the user on the console to input the value of the credentials.
 *
 * @author phxql
 */
public class SetCredentialsTask extends AbstractCredentialsTask {

    /**
     * Constructor initializing the tasks meta data.
     */
    public SetCredentialsTask() {
        this.setDescription("Stores credentials.");
        this.setGroup("SEU-as-code");
    }

    /**
     * Is executed from gradle when running the 'setCredentials' task.
     *
     * @throws IOException If something went wrong while setting credentials.
     */
    @TaskAction
    public void onAction() throws IOException {
        System.out.print("Enter value for credentials with key '" + getKey() + "': ");
        System.out.flush();

        String value = getConsoleReader().readLine();

        getCredentials().set(getKey(), value);
        getCredentials().save();
    }
}
