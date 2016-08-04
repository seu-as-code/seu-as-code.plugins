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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple credentials POJO that contains username and password.
 *
 * @author reimer
 */
public final class Credentials {
    /**
     * An empty credentials instance with no username and no password.
     */
    public static final Credentials EMPTY = new Credentials("", "");

    private final String username;
    private final String password;

    /**
     * Initialize the instance with a username and password value.
     *
     * @param username the username
     * @param password the password
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Special method to get a Credential representation to be used as secret when
     * securely storing the data.
     *
     * @return the secret representation
     */
    public String toSecret() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"username\":\"").append(username).append("\"");
        sb.append(',');
        sb.append("\"password\":\"").append(password).append("\"");
        sb.append('}');
        return sb.toString();
    }

    /**
     * Factory method to create Credentials from their secret representation.
     *
     * @param secret the secret representation
     * @return the Credential
     */
    public static Credentials fromSecret(String secret) {
        Pattern pattern = Pattern.compile("\\{\"username\":\"(.*)\",\"password\":\"(.*)\"\\}");
        Matcher matcher = pattern.matcher(secret);

        if (matcher.matches()) {
            return new Credentials(matcher.group(1), matcher.group(2));
        } else {
            throw new CredentialsException("No valid secret representation.");
        }
    }

}
