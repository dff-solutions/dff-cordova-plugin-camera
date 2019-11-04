# CHANGELOG

# [4.0.0] 2019-06-XX
- build(Camera2): update the plugin to [camera2 API](https://developer.android.com/guide/topics/media/camera)
- docs: update `README`, start using github wiki, add `CHANGELOG` and `USAGE`

# [3.0.6] 
- Fix: supporting cordova version 7+
# [3.0.1] 
- Feat: Software Architecture with DI - Dagger2 
- Fix: @activity context 
- Feat: draw rec surface on autofocus
# [3.0.0] 
- Ref: converted the Camera plugin as well as android module library @Instrumental Tests! RELEASE 3.0.0
# [2.3.0] 
- Ref: requesting permissions will be performed by the common plugin @TargetAPI(21)
# [2.2.2] 
- Fix: flash button is set as View.GONE after animations
# [2.2.1] 
- Fix: Handling runtimeException on setting parameters to the camera while the orientation is on change (@Target: front-camera)
# [2.2.0]
- FEAT: @Target Android version starting with API 23: Requesting Camera Permission!
# [2.1.0]
- FEAT: The ability to preview a taken image
# [2.0.2]
- FIX: handling camera activity and preview if the app does not have the right permissions
# [2.0.1]
- FIX: handling runtime exceptions while taking a picture | the duration of the ratotion of the icons has been decreased to 250
# [2.0.0]
- FEAT: implementation of manual focus
# [1.0.7]
- FEAT: take a picture and forward it to js as BASE64
