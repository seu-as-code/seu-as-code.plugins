# SEU-as-Code Git Plugin

A Gradle plugin for handling Git repositories. Provides basic tasks to init and clone Git repositories.
The repositories can be configured using an extension.

## Usage

To use the plugin, include in your build script:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath 'de.qaware.seu:seuac-git-plugin:2.1.1'
    }
}

apply plugin: 'seuac-git'
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


## Extension properties

The plugin defines the following extension properties in the `git` closure:

Property name | Type | Default value | Description
--- | --- | --- | ---
`git` | NamedDomainObjectContainer<GitRepository> | - | Contains the named Git repository definitions.
`url` | String | - | The URL of the named Git repository. Include username and password in the URL.
`directory` | File | - | The local directory of the named Git repository.
`branch` | String | - | The branch name to use. Defaults to HEAD.
`username` | String | - | The username used for authentication.
`password` | String | - | The password used for authentication.

### Example

```groovy
git {
    seuAsCode {
        url 'https://github.com/qaware/QAseuac.git'
        branch 'HEAD'
        username 'yourGitUsername'
        password 'yourGitPassword'
        directory file("$seuHome/codebase/seu-as-code/")
    }    
    wiki {
        url 'https://username:password@github.com/qaware/QAseuac.wiki.git'
        directory "$seuHome/docbase/wiki/"
    }
}
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
