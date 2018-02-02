[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-homebrew-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-homebrew-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-homebrew-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-homebrew-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code HomeBrew Plugin

A grade plugin for handling home brew packages inside a seu-as-code. Provides basic tasks to apply the current `brew` 
configuation to the seu.

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
        classpath 'de.qaware.seu.as.code:seuac-homebrew-plugin:1.2.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.homebrew'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.homebrew' version '1.2.0'
}
```
## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`installBrew` | - | `InstallHomebrewTask` | Installs the homebrew environment into the seu
`applyBrewSoftware` | `applySoftware` | `ApplyBrewSoftwareTask` | Applies the current brew configuration. This will remove, update and install all brew dependencies.
`storeBrewSeuacDb` | `storeSeuacDb` | `StoreBrewSeuacDbTask` | Store the current HomeBrew SEU software package configuration.

## Example
This short example will install Git SCM, Apache Maven and Apache Subversion into the seu using home brew. 
```groovy
plugins {
    id 'de.qaware.seu.as.code.homebrew' version '1.2.0'
}

dependencies {
    // Install via brew
    brew ':git:'
    brew ':maven:'
    brew ':subversion:'
    
    // Install via brew as cask
    cask ':minishift:'
}
```
## Maintainer

- Christian Fritz (@chrfritz)
- M.-Leander Reimer (@lreimer)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
