# dff-cordova-plugin-camera

###Outlook
1. open camera
2. take photo (and Preview)
3. return photo as base64


## Supported platforms

- Android 

## Plugin@latest

- Android: 3.0.5

## Releases:
- 3.0.6: Fix: supporting cordova version 7+
- 3.0.1: Feat: Software Architecture with DI - Dagger2 | Fix: @activity context | Feat: draw rec surface on autofocus
- 3.0.0: Ref: converted the Camera plugin as well as android module library @Instrumental Tests! RELEASE 3.0.0
- 2.3.0: Ref: requesting permissions will be performed by the common plugin @TargetAPI(21)
- 2.2.2: Fix: flash button is set as View.GONE after animations
- 2.2.1: Fix: Handling runtimeException on setting parameters to the camera while the orientation is on change (@Target: front-camera)
- 2.2.0: FEAT: @Target Android version starting with API 23: Requesting Camera Permission!
- 2.1.0: FEAT: The ability to preview a taken image
- 2.0.2: FIX: handling camera activity and preview if the app does not have the right permissions
- 2.0.1: FIX: handling runtime exceptions while taking a picture | the duration of the ratotion of the icons has been decreased to 250
- 2.0.0: FEAT: implementation of manual focus
- 1.0.7: FEAT: take a picture and forward it to js as BASE64

## Usage

The plugin is available via the global variable `**CameraPlugin**`

Please make sure that you have cordova and ionic installed on your machine

```ssh
$ npm i cordova && ionic
```

##Installation

- cordova plugin add [https://github.com/dff-solutions/dff-cordova-plugin-camera.git]()
- or
- ionic plugin add [https://github.com/dff-solutions/dff-cordova-plugin-camera.git]()

## [Usage](./docs/USAGE.md)

Please read [Wiki](https://github.com/dff-solutions/dff-cordova-plugin-camera/wiki)
before [Usage](./docs/USAGE.md).

For Ionic/Angular projects use [dff.ionic-native](https://github.com/dff-solutions/dff.ionic-native)
as wrapper.

## [Contributing](./docs/CONTRIBUTING.md)

As a developer please see [Contributing](./docs/CONTRIBUTING.md)
when making changes to the plugin.

## Issues

Issues are tracked by [YouTrack](https://dff.myjetbrains.com/youtrack/issues/DCP?q=plugin:Camera%20).
