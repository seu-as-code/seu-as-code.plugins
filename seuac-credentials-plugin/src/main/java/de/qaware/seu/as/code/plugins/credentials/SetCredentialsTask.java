/**
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

import org.gradle.api.DefaultTask;
import org.gradle.api.internal.tasks.options.Option;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Task to set credentials.
 * <p/>
 * Invoke with 'gradle setCredentials --key [Name of the key]'
 * <p/>
 * The task will query the user on the console to input the value of the credentials.
 */
public class SetCredentialsTask extends DefaultTask {
    /**
     * Credential key.
     */
    private String key;

    /**
     * Credentials.
     */
    private Credentials credentials;

    /**
     * Sets the credentials.
     *
     * @param credentials Credentials.
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * Gets the key.
     *
     * @return Key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key Key.
     */
    @Option(option = "key", description = "The credentials key.")
    public void setKey(String key) {
        this.key = key;
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

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String value = reader.readLine();

        credentials.set(getKey(), value);
        credentials.save();
    }
}
