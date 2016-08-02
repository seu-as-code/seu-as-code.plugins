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

import org.gradle.api.GradleException;

/**
 * A custom Gradle exception to signal something went wrong with managed Credentials.
 *
 * @author lreimer
 */
public class CredentialsException extends GradleException {
    /**
     * Initialize exception with error message.
     *
     * @param message the message
     */
    public CredentialsException(String message) {
        super(message);
    }

    /**
     * Initialize exception with error message and cause.
     *
     * @param message the error message
     * @param cause   the cause
     */
    public CredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
