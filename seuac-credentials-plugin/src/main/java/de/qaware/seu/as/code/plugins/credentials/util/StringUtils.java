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
package de.qaware.seu.as.code.plugins.credentials.util;

/**
 * String utils.
 */
public final class StringUtils {

    private StringUtils() {
        // No instances.
    }

    /**
     * Returns whether the given string is not blank.
     * <p/>
     * A string is considered blank if it is null or consists of only whitespace chars.
     *
     * @param s The string to check, may be null.
     * @return true, iff the given string is not blank, false otherwise.
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    /**
     * Returns whether the given string is blank.
     * <p/>
     * A string is considered blank if it is null or consists of only whitespace chars.
     *
     * @param s The string to check, may be null.
     * @return true, if the given string is blank, false otherwise.
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
