# dff-cordova-plugin-camera

###Outlook
1. open camera
2. take photo (and Preview)
3. return photo as base64


## Supported platforms

- Android 

## Plugin version

- Android: 2.1.0

## Releases:
- 2.2.1: Fix: Handling runtimeException on setting parameters to the camera while the orientation is on change (@Target: front-camera)
- 2.2.0: FEAT: @Target Android version starting with API 23: Requesting Camera Permission!
- 2.1.0: FEAT: The ability to preview a taken image
- 2.0.2: FIX: handling camera activity and preview if the app does not have the right permissions
- 2.0.1: FIX: handling runtime exceptions while taking a picture | the duration of the ratotion of the icons has been decreased to 250
- 2.0.0: FEAT: implementation of manual focus
- 1.0.7: FEAT: take a picture and forward it to js as BASE64

##Installation

- cordova plugin add [https://github.com/dff-solutions/dff-cordova-plugin-camera.git]()
- or
- ionic plugin add [https://github.com/dff-solutions/dff-cordova-plugin-camera.git]()

## Usage

The plugin is available via the global variable `**CameraPlugin**`


## Methods

###### Assumption

- Success Callback Function
```js
var success = function(base64) {
  // (Y) do what every you want...with the image as base64! 
}
```

- Error Callback Function
```js
var error = function(errorMsg) {
    console.log(errorMsg);
  // (Y) do what every you want...! 
}
```


----

#### takePhoto
```js
/**
 * Call the camera activity in order to take a photo.
 *
 * @param success - Success callback function with an image as base64
 * @param error - Error callback function
 * @param withPreview - whether a preview for the taken image is to provide
 */

CameraPlugin.takePhoto(success,error,withPreview);

