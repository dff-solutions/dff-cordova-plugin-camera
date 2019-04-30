package com.dff.cordova.plugin.camera.classes.json;

import org.json.JSONObject;

public interface JsonDto<T> {
    JSONObject toJson(T object) throws Exception;
}
