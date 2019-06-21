package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class converts a Throwable into a JSONObject.
 *
 * @see <a href="https://docs.oracle.com/javase/9/docs/api/java/lang/Throwable.html"
 *      >https://docs.oracle.com/javase/9/docs/api/java/lang/Throwable.html</a>
 */
@Singleton
public class JsonThrowable {
    public static final String JSON_ARG_CLASSNAME = "className";
    public static final String JSON_ARG_MESSAGE = "message";
    public static final String JSON_ARG_CAUSE = "cause";
    public static final String JSON_ARG_STACKTRACE = "stackTrace";

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
    
    /**
     * Converts a throwable into a JSONObject.
     *
     * @param e superclass of all errors and exceptions
     * @return parsed JSONObject or null
     * @throws JSONException Exception for parsing JSON
     */
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
