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

import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.FileTree

/**
 * The base class for different SEU as Code data store providers. The providers
 * are responsible to persist and query the SEU configuration.
 *
 * @author lreimer
 */
abstract class DatastoreProvider {
    final String url
    final String user
    final String password

    /**
     * Convenience constructor via a SeuacDatastore instance.
     *
     * @param ds the datastore configuration
     */
    DatastoreProvider(SeuacDatastore ds) {
        url = ds.url
        user = ds.user
        password = ds.password
    }

    /**
     * Clear this data store.
     */
    abstract void clear()

    /**
     * Initialize the data store.
     */
    abstract void init()

    /**
     * Resets this data store. Short hand for clear() and init().
     */
    void reset() {
        clear()
        init()
    }

    /**
     * Stores the given dependency with its files for the given configuration
     *
     * @param dependency the dependency
     * @param files the files as fileTrees
     * @param configuration the configuration
     */
    abstract void storeDependency(Dependency dependency, List<FileTree> files, String configuration)

    /**
     * Finds all obsolete dependencies in the given Set of current dependencies and configuration.
     *
     * @param dependencies the curent dependencies
     * @param configuration the configuratoin name
     * @return the set of obsolete dependencies as ID strings
     */
    abstract Set<String> findAllObsoleteDeps(Set<Dependency> dependencies, String configuration)

    /**
     * Find all files associated with the given dependency IDs and configuration.
     *
     * @param dependencyIds the dependency IDs
     * @param configuration the configuration
     * @return a set of all files
     */
    abstract Set<String> findAllFiles(Set<String> dependencyIds, String configuration)

    /**
     * Find all incoming dependencies from the current set of dependencies for the given configuration.
     *
     * @param dependencies the current set of dependencies
     * @param configuration the configuration name
     * @return the set of newly incoming dependencies
     */
    abstract Set<Dependency> findAllIncomingDeps(Set<Dependency> dependencies, String configuration)

    /**
     * Returns the ID for the given dependency.
     *
     * @param d the dep
     * @return the ID
     */
    String getDependencyId(Dependency d) {
        [d.group, d.name, d.version].join(':')
    }

}