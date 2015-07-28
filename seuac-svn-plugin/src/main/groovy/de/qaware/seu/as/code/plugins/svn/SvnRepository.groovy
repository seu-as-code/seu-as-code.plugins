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
package de.qaware.seu.as.code.plugins.svn

/**
 * The SvnRepository class is the value holder object for named SVN domain objects.
 *
 * @author lreimer
 */
class SvnRepository {
    final String name
    String url
    File directory
    String username
    String password

    /**
     * Required to construct by name.
     *
     * @param name the repo name
     */
    SvnRepository(String name) {
        this.name = name
    }

    /**
     * Short hand method to assign the URL value.
     *
     * @param aUrl the URL
     */
    void url(String aUrl) {
        this.url = aUrl
    }

    /**
     * Short hand method to assign the directory name value.
     *
     * @param aDirectory the directory
     */
    void directory(String aDirectory) {
        this.directory = new File(aDirectory)
    }

    /**
     * Short hand method to assign the directory value.
     *
     * @param aDirectory the directory
     */
    void directory(File aDirectory) {
        this.directory = aDirectory
    }

    /**
     * Short hand method to assign the username value.
     *
     * @param aUsername the username
     */
    def void username(String aUsername) {
        this.username = aUsername
    }

    /**
     * Short hand method to assign the password value.
     *
     * @param aPassword the password
     */
    def void password(String aPassword) {
        this.password = aPassword
    }
}
