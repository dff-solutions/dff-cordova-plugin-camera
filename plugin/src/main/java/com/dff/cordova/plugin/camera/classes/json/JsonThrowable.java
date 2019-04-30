package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class JsonThrowable {
    private static final String JSON_ARG_CLASSNAME = "className";
    private static final String JSON_ARG_MESSAGE = "message";
    private static final String JSON_ARG_CAUSE = "cause";
    private static final String JSON_ARG_STACKTRACE = "stackTrace";

    private JsonStacktrace jsonStacktrace;
    private JsonFactory jsonFactory;

    @Inject
    public JsonThrowable(
        JsonStacktrace jsonStacktrace,
        JsonFactory jsonFactory
    ) {
        this.jsonStacktrace = jsonStacktrace;
        this.jsonFactory = jsonFactory;
    }

    public JSONObject toJson(Throwable e) throws JSONException {
        JSONObject jsonThrowable = null;

        if (e != null) {
            jsonThrowable = jsonFactory.getJSONObject();

            jsonThrowable.put(JSON_ARG_CLASSNAME, e.getClass().getName());
            jsonThrowable.put(JSON_ARG_MESSAGE, e.getMessage());
            jsonThrowable.put(JSON_ARG_STACKTRACE, jsonStacktrace.toJson(e.getStackTrace()));
            jsonThrowable.put(JSON_ARG_CAUSE, toJson(e.getCause()));
        }

        return jsonThrowable;
    }
}
