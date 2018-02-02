[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-credentials-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-credentials-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-credentials-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-credentials-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code Credentials Plugin

A Gradle plugin for the secure storage of your credentials using the Windows Data Protection API (DPAPI) 
or the macOS key store.

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
        classpath 'de.qaware.seu.as.code:seuac-credentials-plugin:2.6.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.credentials'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.credentials' version '2.6.0'
}
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`setCredentials`| - | SetCredentialsTask | Sets the credentials for a service. Invoke with `--service [Name of service]` parameter.
`displayCredentials` | - | DisplayCredentialsTask | Displays the credentials for a service of a credential. Invoke with `--service [Name of service]` parameter.
`clearCredentials`| - | ClearCredentialsTask | Clears the credentials for a service. Invoke with `--service [Name of service]` parameter.

## Extension Properties

The plugin defines the following extra extension properties:

Property name | Type | Default value | Description
--- | --- | --- | ---
`credentials` | Credentials | - | Object to query credentials. Invoke the `String get(String service)` method to get the credentials with the service name `service`.

### Example

First add the credentials for the `nexus` service by invoking one of the following Gradle tasks, you will be asked for the
username and password on the Console:
```shell
$ ./gradlew setCredentials --service nexus
$ ./gradlew setCredentials --service nexus --username fooUser
```

Now you can use this credential information in your build script, e.g. in the repositories section, as follows:
```groovy
    repositories {
        mavenCentral()
        maven {
            url nexusUrl
            credentials {
                // use array type access to credentials via service name
                username project.credentials['nexus'].username
                password project.credentials['nexus'].password

                // use getter access to credentials via service name
                username project.credentials.get('nexus').username
                password project.credentials.get('nexus').password

                // or use string interpolation
                username "${credentials['nexus'].username}"
                password "${credentials['nexus'].password}"
            }
        }
    }
```

## How does this work?

The plugin currently supports Windows and macOS as operating systems. The plugin uses the platform mechanisms to
encrypt and decrypt sensitive data by calling the responsible native libraries using JNA bindings.

On Windows the plugin creates a property file named `secure-credentials.properties` in your Gradle home directory
(defaults to ~/.gradle). In that property file the credentials are securely stored. The key of the credential is stored in
plaintext, while the value of the credential is encrypted using the Windows Data Protection API (DPAPI).

On macOS the plugin securely stores the credentials using the default key store mechanism.

## Maintainer

Moritz Kammerer (@phxql)

M.-Leander Reimer (@lreimer)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
