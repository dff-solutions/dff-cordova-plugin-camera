# Usage

<!-- TOC depthFrom:2 -->

- [Actions](#actions)
    - [takePhoto](#takephoto)

<!-- /TOC -->

This plugin is available via the global `CameraPlugin`.

For more details read the [CameraPlugin Wiki](https://github.com/dff-solutions/dff-cordova-plugin-camera/wiki).

## Actions

All actions are queued and executed in a single thread one after the other.
Before an action is executed permissions are checked.
If not all required permissions are granted the error callback is called with
an exception.
To avoid dataloss the using app of the plugin has to prepare for those and other
errors. It should rollback or provide a backup for affected data.

### takePhoto

```ts
/**
 * Take a photo.
 *
 * @param {CameraOptions} args for optional function.
 * @return {string} image as base64 string.
 */
Shuttleworker2Plugin
    .countShuttles(
        (success) => console.info(!!success),
        console.error,
        {
            withPreview: false
        }
    );
```
