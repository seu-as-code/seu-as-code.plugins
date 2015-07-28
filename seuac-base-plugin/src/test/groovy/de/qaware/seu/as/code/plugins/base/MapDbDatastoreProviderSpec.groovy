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

import static org.hamcrest.Matchers.*
import static spock.util.matcher.HamcrestSupport.expect
import static spock.util.matcher.HamcrestSupport.that

/**
 * The test specification for the MapDbDatastoreProvider.
 *
 * @author lreimer
 */
class MapDbDatastoreProviderSpec extends Specification {

    Project project
    MapDbDatastoreProvider provider
    Dependency dependency
    File testFile

    def setup() {
        project = ProjectBuilder.builder().build()
        dependency = Mock(Dependency)
        testFile = new File(RunHooksTaskSpec.getResource("/seuac-test-1.0.0.zip").toURI())

        provider = (MapDbDatastoreProvider) DatastoreProviderFactory.instance.get(new SeuacDatastore(url: 'file:mapdb:build/seuac', password: 'test'))
        provider.reset()

        def et = new MapDbDatastoreProvider.DependencyEt(dependency: 'de.qaware.seu:seuac-base:1.0.0', file: 'set-env.cmd')
        provider.database.getHashSet('software').add(et)

        et = new MapDbDatastoreProvider.DependencyEt(dependency: 'de.qaware.seu:seuac-home:1.0.0', file: '.bashrc')
        provider.database.getHashSet('home').add(et)
        provider.database.commit()
    }

    def "Init datastore"() {
        when: "we call init"
        provider.init()

        then: "we do not expect any interactions"
        0 * _._
    }

    def "Clear datastore"() {
        when: "we call clear"
        provider.clear()

        then: "then we expect the DB to be empty"
        provider.database.getHashSet('software').isEmpty()
        provider.database.getHashSet('home').isEmpty()
    }

    def "Check for correct dependencyId"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'

        expect: "we expect the correct dependency ID"
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Store software dependency"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when: "we store the software dependency"
        provider.storeDependency(dependency, [project.zipTree(testFile)], 'software')

        then: "we expect to find 3 files"
        expect provider.findAllFiles(ids as Set<String>, 'software'), hasSize(3)
    }

    def "Find all obsolete dependencies"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when: "we find all obsolete dependencies"
        Set<String> deps = provider.findAllObsoleteDeps([dependency] as Set<Dependency>, 'software')

        then: "we expect one dependency to be found"
        expect deps, hasSize(1)
        expect deps, hasItem('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Find all files for IDs"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when: "we find all files for the dependencies"
        Set<String> files = provider.findAllFiles(ids as Set<String>, 'software')

        then: "we expect 1 file to be found"
        expect files, hasSize(1)
        expect files, hasItem('set-env.cmd')
    }

    def "Find all incoming dependencies"() {
        setup: "the mock behaviour"
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when: "we find all incoming dependencies"
        Set<Dependency> deps = provider.findAllIncomingDeps([dependency] as Set<Dependency>, 'software')

        then: "we expect one dependency to be found"
        expect deps, hasSize(1)
        expect deps, hasItem(dependency)
    }
}
