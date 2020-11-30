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

import groovy.sql.Sql
import org.gradle.api.artifacts.Dependency
import org.gradle.api.file.FileTree

/**
 * This data store provider uses SQL and an embedded H2 DB to persist the SEU configuration.
 *
 * @author lreimer
 */
class JdbcH2DatastoreProvider extends DatastoreProvider {

    /**
     * Convenience constructor via a SeuacDatastore instance. Registers a shutdown hook when
     * instance is created that closes. the internal H2 DB automatically.
     *
     * @param ds the datastore configuration
     */
    JdbcH2DatastoreProvider(SeuacDatastore ds) {
        super(ds)
    }

    def withDb(Closure closure) {
        Sql.withInstance(url, user, password, 'org.h2.Driver', closure)
    }

    /**
     * Clear the datastore by dropping the tables.
     */
    @Override
    void clear() {
        withDb { sql -> sql.execute 'drop table if exists dependencies' }
    }

    /**
     * Initialize the datastore by creating the table if required.
     */
    @Override
    void init() {
        withDb { sql -> sql.execute 'create table if not exists dependencies(configuration varchar(255), dependency varchar(255), file varchar(255))' }
    }

    @Override
    void storeDependency(Dependency dependency, List<FileTree> files, String configuration) {
        withDb { sql ->
            files.each { fileTree ->
                fileTree.visit { element ->
                    def params = [configuration, getDependencyId(dependency), element.relativePath.toString()]
                    sql.execute 'insert into dependencies (configuration, dependency, file) values (?, ?, ?)', params
                }
            }
        }
    }

    @Override
    Set<String> findAllObsoleteDeps(Set<Dependency> dependencies, String configuration) {
        def obsoleteDeps = []
        def params = [c: configuration]
        withDb { sql ->
            sql.eachRow('select distinct dependency from dependencies where configuration=:c', params) {
                obsoleteDeps << it.dependency
            }
        }

        obsoleteDeps = obsoleteDeps as Set
        dependencies.each { Dependency d -> obsoleteDeps.remove getDependencyId(d) }
        obsoleteDeps
    }

    @Override
    Set<String> findAllFiles(Set<String> dependencyIds, String configuration) {
        def files = []
        withDb { sql ->
            dependencyIds.each {
                def params = [c: configuration, d: it]
                sql.eachRow('select file from dependencies where configuration=:c and dependency=:d', params) {
                    files << it.file
                }
            }
        }
        files
    }

    @Override
    Set<Dependency> findAllIncomingDeps(Set<Dependency> dependencies, String configuration) {
        def deps = []
        withDb { sql ->
            deps = dependencies.findAll { Dependency d ->
                def params = [c: configuration, d: getDependencyId(d)]
                def rows = sql.rows 'select * from dependencies where configuration=:c and dependency=:d', params
                rows.isEmpty()
            }
        }
        deps
    }
}
