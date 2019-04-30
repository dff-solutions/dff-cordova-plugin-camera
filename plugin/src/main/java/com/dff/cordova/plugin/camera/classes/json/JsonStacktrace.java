package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonStacktrace {
    private JsonFactory jsonFactory;

    @Inject
    public JsonStacktrace(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public JSONArray toJson(StackTraceElement[] stackTrace) {
        JSONArray jsonStackTrace = null;

        if (stackTrace != null) {
            jsonStackTrace  = jsonFactory.getJSONArray();

            for (StackTraceElement ste : stackTrace) {
                jsonStackTrace.put(ste.toString());
            }
        }

        return jsonStackTrace;

    }
}
