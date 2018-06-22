[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-chocolatey-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-chocolatey-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-chocolatey-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-chocolatey-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code Chocolatey Plugin

A grade plugin for handling chocolatey packages inside a SEU-as-code. Provides basic tasks to apply the current `choco` 
configuation to the SEU.

A portable version of chocolatey is installed within the SEU by this plugin. 
This plugin does not touch a possibly exisiting system installation of chocolatey (No user or system environment variables are set).

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
        classpath 'de.qaware.seu.as.code:seuac-chocolatey-plugin:1.0.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.chocolatey'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.chocolatey' version '1.0.0'
}
```
## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`installNetFrameworkIfMissing` | - | `InstallNetFrameworkIfMissingTask` | Installs the .NetFramework if it is missing
`installChoco` | `installNetFrameworkIfMissing`  | `InstallChocolateyTask` | Installs chocolatey into the seu
`applyChocoSoftware` | `applySoftware` | `ApplyChocolateySoftwareTask` | Applies the current chocolatey configuration. This will remove/install all obsolete/new chocolatey dependencies.
`storeChocoSeuacDb` | `storeSeuacDb` | `StoreChocolateySeuacDbTask` | Store the current Chocolatey SEU software package configuration.

## Example
This short example will install helm and kubernetes-cli into the seu using chocolatey. 
```groovy
plugins {
    id 'de.qaware.seu.as.code.chocolatey' version '1.0.0'
}

dependencies {
    // Install helm via chocolatey (latest version) 
    choco ':kubernetes-helm:'
    // Install kubernetes-cli via chocolatey (version 1.10.1) 
    choco ':kubernetes-cli:1.10.1'
}
```

## Limitations
You can only install chocolatey packages that do not require admin rights.

## Maintainer

- Andreas Weber (@andreaswe)


## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
