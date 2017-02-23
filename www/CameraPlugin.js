/**
 * Created by anahas on 05.01.2017.
 */

/**
 * JS in order to open and use the camera activity
 *
 * @author Anthony Nahas
 * @version 2.1.0
 * @since 5.1.2017
 */

const exec = require('cordova/exec');

const FEATURE = "CameraPlugin";

const ACTION_TAKE_PHOTO = "takephoto";


function CameraPlugin() {
    console.log("CameraPlugin.js has been created");
}


/**
 * Open the camera activity in order to take a photo.
 *
 * @param success - the success callback function.
 * @param error - the error callback function.
 * @param withPreview - whether a preview for the taken image is to provide
 */
CameraPlugin.prototype.takePhoto = function (success, error, withPreview) {
    exec(success, error, FEATURE, ACTION_TAKE_PHOTO, [withPreview]);
};


module.exports = new CameraPlugin();