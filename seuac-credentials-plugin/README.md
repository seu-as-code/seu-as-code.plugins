[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-credentials-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-credentials-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-credentials-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-credentials-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-Code Credentials Plugin

A Gradle plugin for the secure storage of your credentials using the Windows Data Protection API (DPAPI).

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
        classpath 'de.qaware.seu.as.code:seuac-credentials-plugin:2.0.1'
    }
}

apply plugin: 'de.qaware.seu.as.code.credentials'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.credentials' version '2.0.1'
}
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`setCredentials`| - | - | Sets a credential. Invoke with `--key [Key of the credentials]` parameter.
`clearCredentials`|-|-|Clears a credential. Invoke with `--key [Key of the credentials]` parameter. Invoke without `--key` parameter to clear all stored credentials.

## Extension Properties

The plugin defines the following extra extension properties:

Property name | Type | Default value | Description
--- | --- | --- | ---
`credentials` | Credentials | - | Object to query credentials. Invoke the `String get(String key)` method to get the credentials with the key `key`.

### Example

First add the credentials using the key `nexusUsername` and `nexusPassword` by invoking
`gradle setCredentials --key nexusUsername` and `gradle setCredentials --key nexusPassword`.

```groovy
    repositories {
        mavenCentral()
        maven {
            url nexusUrl
            credentials {
                username project.credentials.get('nexusUsername')
                password project.credentials.get('nexusPassword')
            }
        }
    }
```

## How does this work?

The `setCredentials` task creates a property file named `secure-credentials.properties` in your gradle home directory 
(defaults to ~/.gradle). In that property file the credentials are stored. The key of the credential is stored in 
plaintext, while the value of the credential is encrypted using the Windows Data Protection API (DPAPI).

## Limitations

As the plugin uses DPAPI to encrypt the credentials, this plugin only works with Windows.

## Maintainer

Moritz Kammerer (@phxql)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
