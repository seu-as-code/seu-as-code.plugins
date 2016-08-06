[![Build Status](https://travis-ci.org/seu-as-code/seu-as-code.plugins.svg?branch=master)](https://travis-ci.org/seu-as-code/seu-as-code.plugins)
[![Coverage Status](https://coveralls.io/repos/seu-as-code/seu-as-code.plugins/badge.svg?branch=master&service=github&ts=1)](https://coveralls.io/github/seu-as-code/seu-as-code.plugins?branch=master)
[![Download](https://api.bintray.com/packages/seu-as-code/gradle-plugins/seuac-platform-plugin/images/download.svg) ](https://bintray.com/seu-as-code/gradle-plugins/seuac-platform-plugin/_latestVersion)
[![javadoc.io](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-platform-plugin/badge.svg)](https://javadocio-badges.herokuapp.com/de.qaware.seu.as.code/seuac-platform-plugin)
[![Stories in Ready](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=ready&title=Ready)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Stories in Progress](https://badge.waffle.io/seu-as-code/seu-as-code.plugins.png?label=in%20progress&title=In%20Progress)](https://waffle.io/seu-as-code/seu-as-code.plugins)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/seu-as-code/seu-as-code.plugins/blob/master/LICENSE)

# SEU-as-code Platform Plugin

A basic Gradle plugin to apply platform specific configuration in a Gradle build file.

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
        classpath 'de.qaware.seu.as.code:seuac-platform-plugin:1.0.0'
    }
}

apply plugin: 'de.qaware.seu.as.code.platform'
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```groovy
plugins {
    id 'de.qaware.seu.as.code.platform' version '1.0.0'
}
```

## Extra Properties

The plugin defines the following extra properties, that may be used for platform specific behaviour:

Task name | Description
--- | ---
`osFamily`| The OS family, either `windows`, `macos`, `unix` or `unknown`
`osClassifier` | The OS classifier, either `win`, `mac`, `unix` or `???`
`osArch` | The OS architecture, either `x86_64` or `x86`

## Extension

The plugin defines the following closures in the `platform` extension:

Property name | Type | Default value | Description
--- | --- | --- | ---
`win` | Closure | - | Apply configuration to project if running on Windows.
`mac` | Closure | - | Apply configuration to project if running on MacOS.
`unix` | Closure | - | Apply configuration to project if running on Linux or Unix.
`x86` | Closure | - | Apply configuration to project if running on x86 system.
`x86_64` | Closure | - | Apply configuration to project if running on x86_64 system.

### Example

The following example show the full extension configuration:
```groovy
platform {
    win { // add Windows specific code like dependencies or tasks here }
    mac { // add MacOS specific code like dependencies or tasks here }
    unix { // add Unix or Linux specific stuff like dependencies or tasks here }
    x86 { // add 32-bit specific stuff like dependencies or tasks here }
    x86_64 { // add 64-bit specific code like dependencies or tasks here }
}
```

## Maintainer

M.-Leander Reimer (@lreimer)

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
