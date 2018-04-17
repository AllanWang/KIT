# KIT: gradle-plugin

Version updates for dependencies

## Contents
* [Versions](#versions)
* [Plugins](#plugins)
* [Dependencies](#dependencies)
* [Changelog Generator](#changelog-generator)

## Usage

Firstly, add the plugin to the buildscript:

```gradle
buildscript {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }

    dependencies {
        ...
        classpath "ca.allanwang:kit:$KIT"
    }
}
```

Then where necessary, apply the plugin using

```gradle
apply plugin: 'ca.allanwang.kit'
```

Versions will then be accessible via `kit.[name]`