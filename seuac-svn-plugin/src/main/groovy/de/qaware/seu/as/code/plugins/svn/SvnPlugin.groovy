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

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The SEU-as-Code subversion plugin. Registers the subversion extension and creates checkout and
 * update tasks for all defined repos.
 *
 * @author lreimer
 */
class SvnPlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'subversion'

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<SvnRepository> repos = project.container(SvnRepository)
        project.extensions.subversion = repos

        project.configurations.create('svnkit')
        project.dependencies.add('svnkit', 'org.tmatesoft.svnkit:svnkit:1.8.10')

        project.task('svnCheckoutAll', description: 'Checkout all SVN repositories', group: 'Version Control')
        project.task('svnUpdateAll', description: 'Update all SVN repositories', group: 'Version Control')

        NamedDomainObjectContainer<SvnRepository> extension = project.subversion
        configureSvnTasks(project, extension)
    }

    /**
     * Configures all SVN tasks for the given project and SCN repository container.
     *
     * @param project the project
     * @param repositories a name repository container
     */
    def void configureSvnTasks(Project project, NamedDomainObjectContainer<SvnRepository> repositories) {
        project.afterEvaluate {
            repositories.each { SvnRepository r ->
                def capitalized = r.name.capitalize()

                // we could use convention mapping as well, does work but not public
                def checkout = project.task("svnCheckout${capitalized}", type: SvnCheckoutTask) {
                    description = "Checkout the ${capitalized} SVN repository"
                    url = r.url
                    directory = r.directory
                    username = r.username
                    password = r.password
                }
                project.tasks.svnCheckoutAll.dependsOn checkout

                def update = project.task("svnUpdate${capitalized}", type: SvnUpdateTask) {
                    description = "Update the ${capitalized} SVN repository"
                    directory = r.directory
                    username = r.username
                    password = r.password
                }
                project.tasks.svnUpdateAll.dependsOn update
            }
        }
    }
}
