/**
 * Created by anahas on 05.01.2017.
 */

/**
 * JS in order to open the camera activity
 *
 * @author Anthony Nahas
 * @version 0.0.2
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
 */
CameraPlugin.prototype.takePhoto = function (success, error) {
    exec(success, error, FEATURE, ACTION_TAKE_PHOTO, []);
};


module.exports = new CameraPlugin();