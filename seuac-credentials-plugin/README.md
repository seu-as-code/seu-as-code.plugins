# SEU-as-Code Credentials Plugin

A Gradle plugin for the secure storage of your credentials using the Windows Data Protection API (DPAPI).

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
        classpath 'de.qaware.seu:seuac-credentials-plugin:2.0.0'
    }
}

apply plugin: 'seuac-credentials'
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`setCredentials`| - | - | Sets a credential. Invoke with `--key [Key of the credentials]` parameter.

## Extension properties

The plugin defines the following extension properties:

Property name | Type | Default value | Description
--- | --- | --- | ---
`credentials` | Credentials | - | Object to query credentials. Invoke the `String get(String key)` method to get the credentials with the key `key`.

## Example

First add the credentials with the key `nexusUsername` and `nexusPassword` by invoking
`gradle setCredentials --key nexusUsername` and `gradle setCredentials --key nexusPassword`.

```groovy
    repositories {
        mavenCentral()
        maven {
            credentials {
                username credentials.get('nexusUsername')
                password credentials.get('nexusPassword')
            }
        }
    }
```

See the `example` folders for an example project.

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
