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
 * @author mario-leander.reimer
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
        when:
        provider.init()
        then:
        0 * _._
    }

    def "Clear datastore"() {
        when:
        provider.clear()
        then:
        provider.database.getHashSet('software').isEmpty()
        provider.database.getHashSet('home').isEmpty()
    }

    def "Check for correct dependencyId"() {
        setup:
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'

        expect:
        that provider.getDependencyId(dependency), equalTo('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Store software dependency"() {
        setup:
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when:
        provider.storeDependency(dependency, [project.zipTree(testFile)], 'software')

        then:
        expect provider.findAllFiles(ids as Set<String>, 'software'), hasSize(3)
    }

    def "Find all obsolete dependencies"() {
        setup:
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when:
        Set<String> deps = provider.findAllObsoleteDeps([dependency] as Set<Dependency>, 'software')

        then:
        expect deps, hasSize(1)
        expect deps, hasItem('de.qaware.seu:seuac-base:1.0.0')
    }

    def "Find all files for IDs"() {
        setup:
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-base'
        dependency.version >> '1.0.0'
        def ids = [provider.getDependencyId(dependency)]

        when:
        Set<String> files = provider.findAllFiles(ids as Set<String>, 'software')

        then:
        expect files, hasSize(1)
        expect files, hasItem('set-env.cmd')
    }

    def "Find all incoming dependencies"() {
        setup:
        dependency.group >> 'de.qaware.seu'
        dependency.name >> 'seuac-test'
        dependency.version >> '1.0.0'

        when:
        Set<Dependency> deps = provider.findAllIncomingDeps([dependency] as Set<Dependency>, 'software')

        then:
        expect deps, hasSize(1)
        expect deps, hasItem(dependency)
    }
}
