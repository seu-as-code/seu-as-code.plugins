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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Task to to remove stored credentials.
 * <p/>
 * Invoke with 'gradle clearCredentials --key [Name of the key]' to remove a single stored credential. Leave the --key
 * parameter blank to remove all stored credentials
 *
 * @author clboettcher
 */
public class ClearCredentialsTask extends AbstractCredentialsTask {

    /**
     * Is executed from gradle when running the 'clearCredentials' task.
     *
     * @throws IOException If something went wrong while clearing credentials credentials.
     */
    @TaskAction
    public void onAction() throws IOException {
        if (StringUtils.isNotBlank(getKey())) {
            removeCredentialWithKey(getKey());
        } else {
            clearAllCredentials();
        }
    }

    /**
     * Promts the user if all credentials shall be deleted and clears them accordingly.
     *
     * @throws IOException If I/O fails.
     */
    private void clearAllCredentials() throws IOException {
        System.out.print("All stored credentials will be removed. Are you sure (y/n)?: n");
        System.out.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String answer = reader.readLine();
        if (StringUtils.isBlank(answer)) {
            answer = "n";
        }

        if ("y".equalsIgnoreCase(answer)) {
            getCredentials().clear();
            getCredentials().save();
        }
    }

    /**
     * Prompts the user to remove the credential with the given {@code key} and removes it accordingly.
     *
     * @param key The key.
     * @throws IOException If I/O fails.
     */
    private void removeCredentialWithKey(String key) throws IOException {
        System.out.printf("Stored credentials for key '%s' will be removed. Are you sure (y/n)?: n", key);
        System.out.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String answer = reader.readLine();
        if (StringUtils.isBlank(answer)) {
            answer = "n";
        }

        if ("y".equalsIgnoreCase(answer)) {
            getCredentials().remove(key);
            getCredentials().save();
        }
    }
}
