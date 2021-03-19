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
package de.qaware.seu.as.code.plugins.base

import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputDirectory

/**
 * Simple data type to represent the directory layout of a SEU.
 *
 * @author lreimer
 */
class SeuacLayout {
    @OutputDirectory
    File codebase
    @OutputDirectory
    File docbase
    @OutputDirectory
    File home
    @OutputDirectory
    File repository
    @OutputDirectory
    File software
    @OutputDirectory
    File temp

    /**
     * Short hand method to assign the given file as codebase directory.
     *
     * @param aDir the dir
     */
    void codebase(File aDir) {
        this.codebase = aDir
    }

    /**
     * Short hand method to assign the given path as codebase directory.
     *
     * @param aDir the dir
     */
    void codebase(String aDir) {
        this.codebase = new File(aDir)
    }

    /**
     * Short hand method to assign the given file as docbase directory.
     *
     * @param aDir the dir
     */
    void docbase(File aDir) {
        this.docbase = aDir
    }

    /**
     * Short hand method to assign the given path as docbase directory.
     *
     * @param aDir the dir
     */
    void docbase(String aDir) {
        this.docbase = new File(aDir)
    }

    /**
     * Short hand method to assign the given file as home directory.
     *
     * @param aDir the dir
     */
    void home(File aDir) {
        this.home = aDir
    }

    /**
     * Short hand method to assign the given path as home directory.
     *
     * @param aDir the dir
     */
    void home(String aDir) {
        this.home = new File(aDir)
    }

    /**
     * Short hand method to assign the given file as repository directory.
     *
     * @param aDir the dir
     */
    void repository(File aDir) {
        this.repository = aDir
    }

    /**
     * Short hand method to assign the given path as repository directory.
     *
     * @param aDir the dir
     */
    void repository(String aDir) {
        this.repository = new File(aDir)
    }

    /**
     * Short hand method to assign the given file as software directory.
     *
     * @param aDir the dir
     */
    void software(File aDir) {
        this.software = aDir
    }

    /**
     * Short hand method to assign the given path as software directory.
     *
     * @param aDir the dir
     */
    void software(String aDir) {
        this.software = new File(aDir)
    }

    /**
     * Short hand method to assign the given file as temp directory.
     *
     * @param aDir the dir
     */
    void temp(File aDir) {
        this.temp = aDir
    }

    /**
     * Short hand method to assign the given path as temp directory.
     *
     * @param aDir the dir
     */
    void temp(String aDir) {
        this.temp = new File(aDir)
    }

    /**
     * Utility method to create the missing directories for this layout instance.
     *
     * @throws IOException in case if I/O problems
     */
    void mkdirs() throws IOException {
        getDirectories().each { d -> d.mkdirs() }
    }

    /**
     * Utility method to remove the directories for this layout instance.
     *
     * @throws IOException in case if I/O problems
     */
    void rmdirs() throws IOException {
        getDirectories().each { d -> d.deleteDir() }
    }

    /**
     * Factory method to create a defaut SEU-as-code layout for the given SEU home.
     *
     * @param seuHome the SEU home
     * @return the default instance
     */
    static SeuacLayout defaultLayout(def seuHome) {
        new SeuacLayout(
                codebase: new File("$seuHome/codebase/"),
                docbase: new File("$seuHome/docbase/"),
                home: new File("$seuHome/home/"),
                repository: new File("$seuHome/repository/"),
                software: new File("$seuHome/software/"),
                temp: new File("$seuHome/temp/")
        )
    }

    /**
     * Returns the list of directories for this layout.
     *
     * @return the directory list
     */
    @Internal
    def getDirectories() {
        def directories = []

        if (codebase) directories << codebase
        if (docbase) directories << docbase
        if (home) directories << home
        if (repository) directories << repository
        if (software) directories << software
        if (temp) directories << temp

        directories
    }
}
