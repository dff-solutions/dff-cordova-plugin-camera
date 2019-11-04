# Contributing

This plugin is designed as an
[android library](https://developer.android.com/studio/projects/android-library).
Hence it can be easy developed using [Android Studio](https://developer.android.com/studio/).
Including unit tests makes development of this plugin easier since it has
not to be installed on a device before execution.

<!-- TOC depthFrom:2 -->

- [Project structure](#project-structure)
- [Deployment](#deployment)
    - [Update plugin.xml](#update-pluginxml)
        - [git Hooks](#git-hooks)
    - [Continuous Integration](#continuous-integration)
- [Development](#development)
    - [JavaDoc](#javadoc)
    - [Frameworks](#frameworks)
        - [Cordova Platform](#cordova-platform)
        - [Implementation](#implementation)
        - [Test Implementation](#test-implementation)
    - [Testing](#testing)
    - [Static code analysis](#static-code-analysis)

<!-- /TOC -->

## Project structure

As an android library there is a gradle config on the root level
- [settings.gradle](../settings.gradle)
- [build.gradle](../build.gradle)

Java code is placed in a android module named [plugin](../plugin).
This module has its own gradle config
- [build.gradle](../plugin/build.gradle) used during development
- [build-camera-plugin.gradle](../plugin/build-camera-plugin.gradle)
  copied to cordova platform when plugin is installed into
  a cordova app

The [first one](../plugin/build.gradle) has to config everything
that is needed to build and test the plugin as an android library.
The [second one](../plugin/build-camera-plugin.gradle) contains
only a subset that is needed by cordova.

To use the `build-camera-plugin.gradle` during the build of a
cordova app the following tag is included within the [plugin.xml](../plugin.xml):

```xml
<framework custom="true" src="plugin/build-camera-plugin.gradle" type="gradleReference"/>
```

## Deployment

As described in the
[Plugin Development Guide](https://cordova.apache.org/docs/en/8.x/guide/hybrid/plugins)
the [plugin.xml](../plugin.xml) describes how the plugin is installed.

### Update plugin.xml

All __`source-file`__ tags can be automatically
updated using the npm script [update-pluginxml.js](../scripts/update-pluginxml.js):

```sh
$ npm run plugin-xml-update
```

#### git Hooks
To avoid failing installations of the plugin due to a missing update of
[plugin.xml](../plugin.xml) a `pre-commit` [hook for git](https://git-scm.com/book/de/v1/Git-individuell-einrichten-Git-Hooks)
is installed.
This is done with [husky](https://www.npmjs.com/package/husky), declaring
a node script that is run before commits. Please have a look at [package.json](../package.json)
and the scripts
- [update.js](../scripts/update-pluginxml.js)
- [gitCheckUpdateXml.js](../scripts/gitCheckUpdateXml.js)

### Continuous Integration

CI checks are executed on the dff TeamCity server using the build
configuration [dff-cordova-plugin-camera](http://sylt.dff-solutions.local/viewType.html?buildTypeId=CordovaPlugins_DffCordovaPlugincamera).
Feature branchen are only allowed to merge when all checks are passed.

## Development

### JavaDoc

[JavaDoc](https://dff-solutions.github.io/dff.CordovaPlugin.camera/javadoc)
can be found at GitHub Pages.
It should be updated at least for every new release.
This can be done using gradle task `javadoc`.

Please write useful comments to enhance source code documentation.

### Frameworks

This plugin uses several frameworks. Please have a look and make yourself
familiar with them before developing this plugin.

#### Cordova Platform

During development a dependency to the cordova platform
[`cordova-android`](https://github.com/apache/cordova-android)
is needed.
Those sources are provided by cordova when building your app.
But for development the dependency has to be added.
The dependency can be added by using bintray https://bintray.com/cordova/maven/cordova-android.

#### Implementation

- [Dagger](https://google.github.io/dagger/)
- [Camera2 API](https://developer.android.com/reference/android/hardware/camera2/package-summary)
- [Eventbus](http://greenrobot.org/eventbus/)
- [Logback](https://logback.qos.ch/)

#### Test Implementation

- [JUnit](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [PowerMock](https://github.com/powermock/powermock)
- [Robolectric](http://robolectric.org/)

### Testing

### Static code analysis

- [checkstyle](https://checkstyle.org) is used to check Java code for
coding standards. Configuration is based on [Google Java Style Guide](https://checkstyle.org/styleguides/google-java-style-20170228.html)

