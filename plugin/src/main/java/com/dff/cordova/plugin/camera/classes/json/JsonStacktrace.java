package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONArray;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * This class converts an Array of StackTraceElements into a JSONArray.
 *
 * @see <a href="https://docs.oracle.com/javase/9/docs/api/java/lang/StackTraceElement.html"
 *      >https://docs.oracle.com/javase/9/docs/api/java/lang/StackTraceElement.html</a>
 */
@Singleton
public class JsonStacktrace {
    private JsonFactory jsonFactory;

    @Inject
    public JsonStacktrace(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
    
    /**
     * Converts an array of StackTraceElement into a JSONArray.
     *
     * @param stackTrace Array of StackTraceElement
     * @return JSONArray which contains all the elements of the stacktrace
     */
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
