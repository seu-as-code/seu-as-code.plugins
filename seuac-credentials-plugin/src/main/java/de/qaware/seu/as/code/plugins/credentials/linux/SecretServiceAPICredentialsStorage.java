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
package de.qaware.seu.as.code.plugins.credentials.linux;

import de.qaware.seu.as.code.plugins.credentials.Credentials;
import de.qaware.seu.as.code.plugins.credentials.CredentialsException;
import de.qaware.seu.as.code.plugins.credentials.CredentialsStorage;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * This is a simple initial implementation that relies on the secret-tool being
 * installed on a user's machine. This command line tool will then use the
 * libsecret library to talk via dbus and the secret service API to the
 * installed keyring system (if any is installed). Currently at least
 * gnome-keyring and kwallet support this API.
 * <p>
 * To install the secret-tool, run: sudo apt-get install libsecret-tool
 * <p>
 * There are more elaborate options that could improve this version of the
 * credential storage:
 * <ul>
 * <li>1) Using JNA to directly talk to the libsecret library.
 * https://wiki.gnome.org/Projects/Libsecret</li>
 * <li>2) Directly talking to the secret service API via dbus, there is a dbus
 * adapter available for Java: https://dbus.freedesktop.org/doc/dbus-java/
 * https://dbus.freedesktop.org/doc/dbus-java/dbus-java/dbus-javase14.html
 * https://cgit.freedesktop.org/dbus/dbus-java/ And the Secret Service API
 * sepcification:
 * https://specifications.freedesktop.org/secret-service/index.html</li>
 * <li>2 b) There is already a Java implementation that does exactly this, but
 * it's unclear if the code is free (license-wise): The code:
 * https://github.com/ffw/lantern/tree/master/src/main/java/org/freedesktop/Secret
 * Usage: https://searchcode.com/codesearch/view/68310988/</li>
 * </ul>
 */
public class SecretServiceAPICredentialsStorage implements CredentialsStorage {
    private static final String SYSTEMPREFIX = "QAcredential";
    private static final String SERVICEID_KEY = SYSTEMPREFIX + ":serviceId";

    @Override
    public void clearCredentials(String service) throws CredentialsException {
        ProcessBuilder pb = new ProcessBuilder("secret-tool", "clear", SERVICEID_KEY, service);

        try {
            pb.start();
        } catch (IOException e) {
            throw new CredentialsException(
                    "A problem occurred when attempting to delete credentials for service " + service + ":", e);
        }
    }

    @Override
    public void setCredentials(String service, String username, char[] password) throws CredentialsException {
        Credentials creds = new Credentials(username, String.copyValueOf(password));
        setCredentials(service, creds);
    }

    @Override
    public void setCredentials(String service, Credentials credentials) throws CredentialsException {
        // The label is just a comment, to be used, for instance, by UI tools
        // that allow human inspection of the keystore
        String label = SYSTEMPREFIX + ":" + service;
        ProcessBuilder pb = new ProcessBuilder("secret-tool", "store", "--label", label, SERVICEID_KEY, service);

        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            throw new CredentialsException("Error storing secure credential properties.", e);
        }

        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
            out.write(credentials.toSecret());
            out.close();
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new CredentialsException("Error storing secure credential properties.", e);
        }

    }

    @Override
    public Credentials findCredentials(String service) throws CredentialsException {

        ProcessBuilder pb = new ProcessBuilder("secret-tool", "lookup", SERVICEID_KEY, service);
        Process p;

        try {
            p = pb.start();
        } catch (IOException e) {
            throw new CredentialsException("Could not retrieve credentials from store", e);
        }

        Credentials cred = null;
        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {

            p.waitFor();
            String secret = input.readLine();
            if (!StringUtils.isBlank(secret)) {
                cred = Credentials.fromSecret(secret);
            }
        } catch (IOException | InterruptedException e) {
            throw new CredentialsException("Could not retrieve credentials from store", e);
        }

        return cred;
    }

}
