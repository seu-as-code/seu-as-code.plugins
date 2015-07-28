# SEU-as-Code Base Plugin

A Gradle plugin to create SEU installations from code. Provides basic tasks to bootstrap and update the SEU and to
install and update the software packages. The SEU can be configured and customized using the `seuAsCode` extension.

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
        classpath 'de.qaware.seu:seuac-base-plugin:2.1.0'
    }
}

apply plugin: 'seuac-base'
```

## Tasks

The plugin defines the following tasks:

Task name | Depends on | Type | Description
--- | --- | --- | ---
`bootstrapSeu`| `createSeuacLayout`, `installSoftware` | - | Initial tasks to create the SEU from scratch.
`updateSeu` | `installSoftware` | - | Updates a the SEU installation and applies the latest configuration.
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

### Example

```groovy
dependencies {
    seuac 'org.codehaus.groovy.modules.scriptom:scriptom:1.6.0'
    seuac 'com.h2database:h2:1.3.176'

    home 'de.qaware.seu:seuac-home:1.1'
    software 'de.qaware.seu:seuac-base:1.5'
    
	software 'de.qaware.seu:intellij:13.1.3'
	software 'de.qaware.seu:jdk:1.7.0_60'
}
```

## Extension properties

The plugin defines the following extension properties in the `seuAsCode` closure:

Property name | Type | Default value | Description
--- | --- | --- | ---
`seuHome` | String | - | The home directory for this SEU. Can be a VHD or any other valid directory.
`projectName` | String | - | The project name for this SEU.
`layout` | SeuacLayout | - | Optional. Defines the directory layout for this SEU.
`datastore` | SeuacDatastore | - | Optional. Defines the datastore used to persist the SEU configuration. Currently H2 (use jdbc:h2:seuac as URL) and MapDB (use file:mapdb:seuac as URL) are supported.
`banner` | SeuacBanner | - | Optional. Defines the ASCII banner configuration.

### Example

```groovy
seuAsCode {
    seuHome 'S:'
    projectName 'SEU-as-Code'
    layout {
        codebase "S:/codebase/"
        docbase "S:/docbase/"
        home "S:/home/"
        repository "S:/repository/"
        software "S:/software/"
        temp "S:/temp/"
    }
    datastore {
        url 'jdbc:h2:seuac'
        // url 'file:mapdb:seuac'
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

M.-Leander Reimer (@lreimer), <mario-leander.reimer@qaware.de>

## License

This software is provided under the Apache License, Version 2.0 license. See the `LICENSE` file for details.
