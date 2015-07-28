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
import org.mapdb.DB
import org.mapdb.DBMaker

/**
 * This data store provider used MapDB to persist the SEU configuration.
 *
 * @author alex.christ
 */
class MapDbDatastoreProvider extends DatastoreProvider {

    /**
     * Lazily initialize the MapDB instance for given file.
     */
    @Lazy
    protected DB database = {
        DBMaker.newFileDB(new File(filename)).closeOnJvmShutdown().encryptionEnable(password ?: "").make()
    }()

    private def filename

    /**
     * Convenience constructor via a SeuacDatastore instance.
     *
     * @param ds the datastore configuration
     */
    MapDbDatastoreProvider(SeuacDatastore ds) {
        super(ds)
        filename = ds.url.substring(ds.url.lastIndexOf(':') + 1) + ".mapdb"
    }

    @Override
    void clear() {
        database.delete('software')
        database.delete('home')
    }

    @Override
    void init() {
    }

    @Override
    void storeDependency(Dependency dependency, List<FileTree> files, String configuration) {
        files.each {
            it.visit { element ->
                def id = getDependencyId(dependency)
                def path = element.relativePath.toString()

                // every configuration has its own map of ID and path
                def et = new DependencyEt(dependency: id, file: path)
                database.getHashSet(configuration).add(et)
            }
        }
        database.commit()
    }

    @Override
    Set<String> findAllObsoleteDeps(Set<Dependency> dependencies, String configuration) {
        def deps = database.getHashSet(configuration)
        def obsoleteDeps = deps.collect { DependencyEt et -> et.dependency }

        obsoleteDeps = obsoleteDeps as Set
        dependencies.each { Dependency d -> obsoleteDeps.remove getDependencyId(d) }
        obsoleteDeps
    }

    @Override
    Set<String> findAllFiles(Set<String> dependencyIds, String configuration) {
        def files = []
        def deps = database.getHashSet(configuration)

        dependencyIds.each { String id ->
            Set<DependencyEt> all = findAll(deps, id)
            all.each { files << it.file }
        }

        files
    }

    @Override
    Set<Dependency> findAllIncomingDeps(Set<Dependency> dependencies, String configuration) {
        dependencies.findAll { Dependency d ->
            // incoming if dependency is NOT in the current configuration
            def id = getDependencyId(d)
            def deps = database.getHashSet(configuration)
            findAll(deps, id).isEmpty()
        }
    }

    private Set<DependencyEt> findAll(Set<DependencyEt> deps, String id) {
        deps.findAll { DependencyEt et ->
            et.dependency == id
        }
    }

    /**
     * Dependency entity for storing software dependencies.
     */
    static class DependencyEt implements Serializable {
        String dependency
        String file

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            DependencyEt that = (DependencyEt) o

            if (dependency != that.dependency) return false
            if (file != that.file) return false

            return true
        }

        int hashCode() {
            int result
            result = dependency.hashCode()
            result = 31 * result + file.hashCode()
            return result
        }
    }
}
