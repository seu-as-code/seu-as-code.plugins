/*
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
package de.qaware.seu.as.code.plugins.git

/**
 * The GitRepository class is the value holder object for named Git domain objects.
 *
 * @author lreimer
 */
class GitRepository {
    final String name
    String url
    String branch = 'HEAD'
    File directory
    String username
    String password

    /**
     * Required to construct by name.
     *
     * @param name the repo name
     */
    GitRepository(String name) {
        this.name = name
    }

    /**
     * Short hand method to set the Git URL.
     *
     * @param aUrl the Git URL
     */
    void url(String aUrl) {
        this.url = aUrl
    }

    /**
     * Short hand method to set the Git branch to use.
     *
     * @param aBranch the Git branch name
     */
    void branch(String aBranch) {
        this.branch = aBranch
    }

    /**
     * Short hand method to set the directory.
     *
     * @param aDirectory a local directory
     */
    void directory(File aDirectory) {
        this.directory = aDirectory
    }

    /**
     * Short hand method to set the directory name.
     *
     * @param aDirectory a local directory
     */
    void directory(String aDirectory) {
        this.directory = new File(aDirectory)
    }

    /**
     * Short hand method to set the username.
     *
     * @param aUsername the Git username
     */
    void username(String aUsername) {
        this.username = aUsername
    }

    /**
     * Short hand method to set the password.
     *
     * @param aDirectory a local directory
     */
    void password(String aPassword) {
        this.password = aPassword
    }
}
