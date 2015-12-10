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
package de.qaware.seu.as.code.plugins.git

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The SEU-as-Code Git plugin. Registers the git extension and creates init and clone
 * tasks for all defined repos.
 *
 * @author lreimer
 */
class GitPlugin implements Plugin<Project> {
    /**
     * The extension name for the Git repository configuration.
     */
    static final String EXTENSION_NAME = 'git'

    @Override
    void apply(Project project) {
        NamedDomainObjectContainer<GitRepository> repos = project.container(GitRepository)
        project.extensions.git = repos

        project.configurations.create('jgit')
        project.dependencies.add('jgit', 'org.eclipse.jgit:org.eclipse.jgit:3.7.1.201504261725-r')
        project.dependencies.add('jgit', 'org.eclipse.jgit:org.eclipse.jgit.ant:3.7.1.201504261725-r')
        project.dependencies.add('jgit', 'com.jcraft:jsch:0.1.53')

        project.task('gitInitAll', description: 'Init all Git repositories', group: 'Version Control')
        project.task('gitCloneAll', description: 'Clone all Git repositories', group: 'Version Control')
        project.task('gitPullAll', description: 'Pull all Git repositories', group: 'Version Control')
        project.task('gitPushAll', description: 'Push all Git repositories', group: 'Version Control')
        project.task('gitStatusAll', description: 'Status all Git repositories', group: 'Version Control')

        NamedDomainObjectContainer<GitRepository> extension = project.git
        configureGitTasks(project, extension)
    }

    /**
     * Configures all Git tasks for the given project and Git repository container.
     *
     * @param project the project
     * @param repositories a name repository container
     */
    def void configureGitTasks(Project project, NamedDomainObjectContainer<GitRepository> repositories) {
        project.afterEvaluate {
            repositories.each { GitRepository r ->
                def capitalized = r.name.capitalize()

                // we could use convention mapping as well, does work but not public
                configureGitInitTasks(project, capitalized, r)
                configureGitCloneTasks(project, capitalized, r)
                configureGitCommitTasks(project, capitalized, r)
                configureGitPullTasks(project, capitalized, r)
                configureGitPushTasks(project, capitalized, r)
                configureGitStatusTasks(project, capitalized, r)
            }
        }
    }

    private configureGitStatusTasks(Project project, repoName, repo) {
        def gitStatus = project.task("gitStatus${repoName}", type: GitStatusTask) {
            description = "Status the ${repoName} Git repository"
            directory = repo.directory
            username = repo.username
            password = repo.password
        }
        project.tasks.gitStatusAll.dependsOn gitStatus
    }

    private configureGitPushTasks(Project project, repoName, repo) {
        def gitPush = project.task("gitPush${repoName}", type: GitPushTask) {
            description = "Push the ${repoName} Git repository"
            directory = repo.directory
            username = repo.username
            password = repo.password
        }
        project.tasks.gitPushAll.dependsOn gitPush
    }

    private configureGitPullTasks(Project project, repoName, repo) {
        def gitPull = project.task("gitPull${repoName}", type: GitPullTask) {
            description = "Pull the ${repoName} Git repository"
            directory = repo.directory
            username = repo.username
            password = repo.password
        }
        project.tasks.gitPullAll.dependsOn gitPull
    }

    private configureGitCommitTasks(Project project, repoName, repo) {
        project.task("gitCommit${repoName}", type: GitCommitTask) {
            description = "Commit changes for ${repoName} Git repository"
            directory = repo.directory
        }
    }

    private configureGitCloneTasks(Project project, repoName, repo) {
        def gitClone = project.task("gitClone${repoName}", type: GitCloneTask) {
            description = "Clone the ${repoName} Git repository"
            directory = repo.directory
            url = repo.url
            branch = repo.branch
            username = repo.username
            password = repo.password
            singleBranch = repo.singleBranch
        }
        project.tasks.gitCloneAll.dependsOn gitClone
    }

    private configureGitInitTasks(Project project, repoName, repo) {
        def gitInit = project.task("gitInit${repoName}", type: GitInitTask) {
            description = "Init the ${repoName} Git repository"
            directory = repo.directory
        }
        project.tasks.gitInitAll.dependsOn gitInit
    }
}
