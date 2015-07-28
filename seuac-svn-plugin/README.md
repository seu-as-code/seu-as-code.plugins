# SEU as Code SVN Plugin

A Gradle plugin for handling SVN repositories. Provides basic tasks to checkout SVN repositories and update 
local directories. The repositories can be configured using an extension.

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
        classpath 'de.qaware.seu:seuac-svn-plugin:2.1.2'
    }
}

apply plugin: 'seuac-svn'
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

```groovy
subversion {
    code {
        url 'https://www.qaware.de/development/BMW-IAP-AIR/codebase/trunk'
        directory file("$seuHome/codebase/")
        username qawareUsername
        password qawarePassword
    }
    docs {
        url 'https://www.qaware.de/development/BMW-IAP-AIR/sebase/trunk'
        directory "$seuHome/docbase/"
        username qawareUsername
        password qawarePassword
    }
}
```

## Maintainer

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
