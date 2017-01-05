/**
 * Created by anahas on 05.01.2017.
 */

/**
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


CameraPlugin.prototype.takePhoto = function () {
    exec(function () {
        console.log("on camera success");
    }, function (errMsg) {
        console.log("On camera error: " + errMsg);
    }, FEATURE, ACTION_TAKE_PHOTO, []);
};


module.exports = new CameraPlugin();