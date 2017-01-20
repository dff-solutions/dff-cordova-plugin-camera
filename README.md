# dff-cordova-plugin-camera

1. open camera
2. take photo
3. return photo as base64


## Supported platforms

- Android 

## Plugin version

- Android: 1.0.7

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


##--------------------------------------------------------------------------------

1. #### takePhoto
```js
/**
 * Call the camera activity in order to take a photo.
 *
 * @param success - Success callback function with an image as base64
 * @param error - Error callback function
 */
CameraSergice.takePhoto(success,error);