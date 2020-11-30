[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-base-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-base-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-base-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-base-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code Base Plugin

A Gradle plugin to create SEU installations from code. Provides basic tasks to bootstrap and update the SEU and to
install and update the software packages. The SEU can be configured and customized using the `seuAsCode` extension.

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
        classpath 'de.qaware.seu.as.code:seuac-base-plugin:2.6.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.base'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.base' version '2.6.0'
}
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`bootstrapSeu`| `createSeuacLayout`, `installSoftware` | - | Initial tasks to create the SEU from scratch.
`updateSeu` | `installSoftware` | - | Updates a the SEU installation and applies the latest configuration.
`destroySeu` | - | DestroySeuTask | Destroy the complete SEU and deletes all directories. So be careful!
`createSeuacLayout` | - | CreateSeuacLayoutTask | Creates the basic directory layout for the SEU.
`applySoftware` | - | ApplyConfigurationTask | Apply the software configuration and install packages. New dependencies are installed, obsolete software will be removed.
`applyHome` | - | ApplyConfigurationTask | Apply the home configuration and install packages. New dependencies are installed, obsolete software will be removed.
`runSoftwareHooks` | - | RunHooksTask | Runs any software hooks after the installation.
`runHomeHooks` | - | RunHooksTask | Runs any home hooks after the installation.
`createAsciiBanner` | - | CreateAsciiBannerTask | Creates the ASCII banner file.
`storeSeuacDb` | - | StoreSeuacDbTask | Store the current SEU software package configuration.

## Configurations

The plugin defines the following configurations:

Task name | Description
--- | ---
`seuac`| Used for dependencies that need to be put in the root classloader, e.g. SQL drivers
`software` | Used for software dependencies that will be installed in the software directory.
`home` | Used for dependencies that will be installed in the home directory.

## Extra Properties

The plugin defines the following extra properties, that may be used for platform specific behaviour:

Task name | Description
--- | ---
`osFamily`| The OS family, either `windows`, `macos`, `unix` or `unknown`
`osClassifier` | The OS classifier, either `win`, `mac`, `unix` or `???`
`osArch` | The OS architecture, either `x86_64` or `x86`

### Dependencies

The configurations are used for the software package dependencies. You also have to specify a Maven repository where
the dependencies are located, in this case we used Bintray.
```groovy
repositories {
	maven { url 'https://dl.bintray.com/seu-as-code/maven' }
	jcenter()
}
dependencies {
    // mandatory dependencies for basic SEU setup
    home 'de.qaware.seu.as.code:seuac-home:2.0.0'
    software 'de.qaware.seu.as.code:seuac-environment:2.0.0:jdk8'

    // additional dependencies for a Groovy development environment
    software 'org.gradle:gradle:2.5'
    software 'org.groovy-lang:groovy:2.4.4'
}
platform {
    win { // add Windows specific code like dependencies or tasks here }
    mac { // add MacOS specific code like dependencies or tasks here }
    unix { // add Unix or Linux specific stuff like dependencies or tasks here }
    x86 { // add 32-bit specific stuff like dependencies or tasks here }
    x86_64 { // add 64-bit specific code like dependencies or tasks here }
}
```

## Extension Properties

The plugin defines the following extension properties in the `seuAsCode` closure:

Property name | Type | Default value | Description
--- | --- | --- | ---
`seuHome` | String | - | The home directory for this SEU. Can be a VHD or any other valid directory.
`projectName` | String | - | The project name for this SEU.
`layout` | SeuacLayout | - | Optional. Defines the directory layout for this SEU.
`datastore` | SeuacDatastore | - | Optional. Defines the datastore used to persist the SEU configuration. Currently H2 (use jdbc:h2:seuac as URL) and MapDB (use file:mapdb:seuac as URL) are supported.
`banner` | SeuacBanner | - | Optional. Defines the ASCII banner configuration.

### Example

The following example show the full extension configuration:
```groovy
seuAsCode {
    seuHome 'S:'
    projectName 'SEU-as-code'
    layout {
        codebase 'S:/codebase/'
        docbase 'S:/docbase/'
        home 'S:/home/'
        repository 'S:/repository/'
        software 'S:/software/'
        temp 'S:/temp/'
    }
    datastore {
        // this is for backwards compatibility to H2 1.3.x
        // for latest H2 1.4.x you can enable the MVStore
        url 'jdbc:h2:./seuac;mv_store=false'

        // use this URL if you want to store things with MapDB
        // url 'file:mapdb:./seuac'

        user 'sa'
        password 'sa'
    }
    banner {
        font 'slant'
        reflection 'no'
        adjustment 'center'
        stretch 'yes'
        width 80
    }
}
```

## Maintainer

M.-Leander Reimer (@lreimer)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
