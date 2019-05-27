package com.dff.cordova.plugin.camera.classes.json;

import org.json.JSONObject;

/**
 * Interface for a class that parses data transfer object (dto) into json.
 *
 * @param <T> data transfer object to parse into json.
 */
public interface JsonDto<T> {
    JSONObject toJson(T object) throws Exception;
}
