[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-svn-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-svn-plugin/_latestVersion)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-Code SVN Plugin

A Gradle plugin for handling SVN repositories. Provides basic tasks to checkout SVN repositories and update 
local directories. The repositories can be configured using an extension.

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
        classpath 'de.qaware.seu.as.code:seuac-svn-plugin:2.1.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.svn'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.svn' version '2.1.0'
}
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`svnCheckoutAll`| all `svnCheckout<RepositoryName>` tasks | - | Performs a SVN checkout of all defined repositories.
`svnUpdateAll` | all `svnUpdate<RepositoryName>` tasks | - | Performs a SVN update of all defined repositories.
`svnCheckout<RepositoryName>` | - | SvnCheckoutTask | Performs a SVN checkout of the named SVN repository.
`svnUpdate<RepositoryName>` | - | SvnUpdateTask | Performs a SVN update of the named SVN repository.

## Extension properties

The plugin defines the following extension properties in the `subversion` closure:

Property name | Type | Default value | Description
--- | --- | --- | ---
`subversion` | NamedDomainObjectContainer<SvnRepository> | - | Contains the named SVN repository definitions.
`url` | String | - | The URL of the named SVN repository.
`directory` | File | - | The local checkout directory of the named SVN repository.
`username` | String | - | The username used to authenticate.
`password` | String | - | The password used to authenticate.

### Example

The following example defines the SVN repository of the SEU-as-Code plugins. The example did not hardcode the
username and password properties, instead you should use project properties or the credentials plugin.
```groovy
subversion {
    SeuAsCodePlugins {
        url 'https://github.com/seu-as-code/seu-as-code.plugins'
        directory file("$seuHome/codebase/seu-as-code.plugins/")
        username svnUsername
        password svnPassword
    }
}
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
