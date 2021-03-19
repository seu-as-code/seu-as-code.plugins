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

import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.temporaryDatastore
import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * A test spec for the JDBC H2 datastore provider.
 *
 * @author lreimer
 */
class JdbcH2DatastoreProviderSpec extends Specification {

    Project project
    JdbcH2DatastoreProvider provider
    Dependency dependency
    File testFile

    def setup() {
        project = ProjectBuilder.builder().build()
        dependency = Mock(Dependency)
        testFile = new File(RunHooksTaskSpec.getResource("/seuac-test-1.0.0.zip").toURI())

        def datastore = temporaryDatastore()
        provider = new JdbcH2DatastoreProvider(datastore)
        provider.reset()

        provider.withDb { sql ->
            sql.execute 'insert into dependencies (configuration, dependency, file) values (?, ?, ?)',
                    ['software', 'de.qaware.seu:seuac-base:1.0.0', 'set-env.cmd']
        }

        provider.withDb { sql ->
            sql.execute 'insert into dependencies (configuration, dependency, file) values (?, ?, ?)',
                    ['home', 'de.qaware.seu:seuac-home:1.0.0', '.bashrc']
        }
    }

    def "Check for correct simple dependencyId"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'

        expect: "the correct dependency ID to be returned"
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Check for correct dependencyId with extension and classifier"() {
        setup: "the dependency and project"
        project.configurations.create('software')
        dependency = project.dependencies.create('de.qaware.seu:seuac-test:1.0.0:1.5@zip')

        expect: "the correct dependency ID to be returned"
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-test:1.0.0:1.5@zip')
    }

    def "Check for correct dependencyId with extension"() {
        setup: "the dependency and project"
        project.configurations.create('software')
        dependency = project.dependencies.create('de.qaware.seu:seuac-test:1.0.0@zip')

        expect: "the correct dependency ID to be returned"
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-test:1.0.0@zip')
    }

    def "Check for correct dependencyId with classifier"() {
        setup: "the dependency and project"
        project.configurations.create('software')
        dependency = project.dependencies.create('de.qaware.seu:seuac-test:1.0.0:1.5')

        expect: "the correct dependency ID to be returned"
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-test:1.0.0:1.5@jar')
    }

    def "Store software dependency with extension"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when: "we store the software dependency"
        provider.storeDependency(dependency, [project.zipTree(testFile)], 'software')

        then: "we expect to find 3 files for the ID"
        expect provider.findAllFiles(ids as Set<String>, 'software'), hasSize(3)
    }

    def "Find all files for IDs"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when: "we find alle file for the given software IDs"
        Set<String> files = provider.findAllFiles(ids as Set<String>, 'software')

        then: "we expect the correct files for the dependency"
        expect files, hasSize(1)
        expect files, hasItem('set-env.cmd')
    }

    def "Find all obsolete dependencies"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when: "we find all obsolete dependencies"
        Set<String> deps = provider.findAllObsoleteDeps([dependency] as Set<Dependency>, 'software')

        then: "we expect the old seuac-base version to be found"
        expect deps, hasSize(1)
        expect deps, hasItem('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Find all incoming dependencies"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when: "we find all incoming dependencies"
        Set<Dependency> deps = provider.findAllIncomingDeps([dependency] as Set<Dependency>, 'software')

        then: "we expect the mock dependency to be found"
        expect deps, hasSize(1)
        expect deps, hasItem(dependency)
    }

}
