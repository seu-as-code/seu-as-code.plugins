[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-git-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-git-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-git-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-git-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code Git Plugin

A Gradle plugin for handling Git repositories. Provides basic tasks to init and clone Git repositories.
The repositories can be configured using an extension.

## Usage

Build script snippet for use in all Gradle versions, using the Bintray Maven repository:
```groovy
buildscript {
    repositories {
        mavenCentral()
        maven {
            url 'https://dl.bintray.com/seu-as-code/gradle-plugins'
        }
    }
    dependencies {
        classpath 'de.qaware.seu.as.code:seuac-git-plugin:2.3.0.RC2'
    }
}

apply plugin: 'de.qaware.seu.as.code.git'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.git' version '2.3.0.RC2'
}
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`gitInitAll`| all `gitInit<RepositoryName>` tasks | - | Performs a Git init for all defined repositories.
`gitCloneAll` | all `gitClone<RepositoryName>` tasks | - | Performs a Git clone for all defined repositories.
`gitPushAll` | all `gitPush<RepositoryName>` tasks | - | Performs a Git push for all defined repositories.
`gitPullAll` | all `gitPull<RepositoryName>` tasks | - | Performs a Git pull for all defined repositories.
`gitStatusAll` | all `gitStatus<RepositoryName>` tasks | - | Performs a Git status for all defined repositories.
`gitInit<RepositoryName>` | - | GitInitTask | Performs a Git init for the named Git repository.
`gitClone<RepositoryName>` | - | GitCloneTask | Performs a Git clone for the named Git repository.
`gitStatus<RepositoryName>` | - | GitStatusTask | Performs a Git status for the named Git repository.
`gitCommit<RepositoryName>` | - | GitCommitTask | Performs a Git commit for the named Git repository. Override message project property.
`gitPush<RepositoryName>` | - | GitPushTask | Performs a Git push for the named Git repository to remote origin.
`gitPull<RepositoryName>` | - | GitPullTask | Performs a Git pull for the named Git repository from remote origin.

## Extension Properties

The plugin defines the following extension properties in the `git` closure:

Property name | Type | Default value | Description
--- | --- | --- | ---
`git` | NamedDomainObjectContainer<GitRepository> | - | Contains the named Git repository definitions.
`url` | String | - | The URL of the named Git repository. Include username and password in the URL.
`directory` | File | - | The local directory of the named Git repository.
`branch` | String | - | The branch name to use. Defaults to HEAD. If `singleBranch` is `true` this must be a valid refspec like `refs/heads/BRANCHNAME`.
`username` | String | - | The username used for authentication.
`password` | String | - | The password used for authentication.
`options` | GitOptions | - | The Git command options.

### Example

The following example defines the Git repository of the SEU-as-code plugins. The example did not hardcode the
username and password properties, instead you should use project properties or the credentials plugin.
```groovy
git {
    SeuAsCodePlugins {
        url 'https://github.com/seu-as-code/seu-as-code.plugins.git'
        directory file("$seuHome/codebase/seu-as-code.plugins/")
        branch 'HEAD'
        username gitUsername
        password gitPassword
        
        options {
            clone {
                singleBranch = false
                cloneSubmodules = true
                noCheckout = false
                timeout = 300
            }
            pull {
                rebase = true
                timeout = 600
            }
            push {
                dryRun = true
                pushAll = true
                pushTags = true
                timeout = 200
                force = true
            }
        }
    }
}
```

## Maintainer

M.-Leander Reimer (@lreimer)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
