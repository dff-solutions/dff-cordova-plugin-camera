package com.dff.cordova.plugin.camera.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class provides new JSONObjects or JSONArrays.
 * It can be mocked and makes testing easier.
 */
@Singleton
public class JsonFactory {
    @Inject
    public JsonFactory() {}

    public JSONObject getJSONObject() {
        return new JSONObject();
    }

    public JSONObject getJSONObject(String json) throws JSONException {
        return new JSONObject(json);
    }

    public JSONArray getJSONArray() {
        return new JSONArray();
    }

    public JSONArray getJSONArray(String json) throws JSONException {
        return new JSONArray(json);
    }
}
