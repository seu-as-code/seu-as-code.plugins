package de.qaware.seu.as.code.plugins.homebrew

import de.qaware.seu.as.code.plugins.base.DatastoreProvider
import de.qaware.seu.as.code.plugins.base.JdbcH2DatastoreProvider
import de.qaware.seu.as.code.plugins.base.SeuacDatastore
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

import static de.qaware.seu.as.code.plugins.base.SeuacDatastore.defaultDatastore

class StoreBrewSeuacDbTaskTest extends Specification {

    Project project
    SeuacDatastore defaultDatastore
    DatastoreProvider provider
    File home

    void setup() {
        project = ProjectBuilder.builder().build()
        home = File.createTempDir()

        def maven3Dir = new File(home, 'homebrew/Cellar/maven3/')
        maven3Dir.mkdirs()
        new File(maven3Dir, 'test').createNewFile()

        defaultDatastore = defaultDatastore()
        defaultDatastore.url = 'jdbc:h2:./build/seuac'
        provider = new JdbcH2DatastoreProvider(defaultDatastore)
    }

    def "StoreBrewSeuacDb"() {
        setup: "the plugin, apply it and configure the convention"
        project.apply plugin: 'seuac-homebrew'
        project.dependencies.add('brew', ':maven3:')

        StoreBrewSeuacDbTask task = project.task("storeBrewSeuacDb", type: StoreBrewSeuacDbTask) {
            homebrewBasePath = new File(home, 'homebrew')
            datastore = defaultDatastore
        }
        when: "we evaluate the task"
        task.storeSeuacDb()

        then: "the DB should contain all files for all configurations"
        notThrown(Exception)

        provider.findAllFiles(['null:maven3:null'] as Set, 'brew').size() == 1
    }
}
