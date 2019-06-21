package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.dff.cordova.plugin.camera.classes.json.JsonThrowable.JSON_ARG_CAUSE;
import static com.dff.cordova.plugin.camera.classes.json.JsonThrowable.JSON_ARG_CLASSNAME;
import static com.dff.cordova.plugin.camera.classes.json.JsonThrowable.JSON_ARG_MESSAGE;
import static com.dff.cordova.plugin.camera.classes.json.JsonThrowable.JSON_ARG_STACKTRACE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JsonThrowableUnitTest {
    @Mock
    private JsonStacktrace jsonStacktrace;

    @Mock
    private Throwable throwable;

    @Mock
    private JsonFactory mJsonFactory;

    @Mock
    private JSONObject mJSONObject;

    @InjectMocks
    private JsonThrowable jsonThrowable;

    @Test
    public void toJson_shouldCheckArg() throws JSONException {
        assertNull(jsonThrowable.toJson(null));
    }

    @Test
    public void toJson_shouldConvert() throws JSONException {
        doReturn(mJSONObject).when(mJsonFactory).getJSONObject();

        JSONObject result = jsonThrowable.toJson(throwable);

        assertNotNull(result);
        verify(jsonStacktrace, times(1))
            .toJson(null);
        verify(throwable, times(1))
            .getMessage();
        verify(throwable, times(1))
            .getStackTrace();
        verify(throwable, times(1))
            .getCause();

        verify(mJSONObject).put(JSON_ARG_CLASSNAME, throwable.getClass().getName());
        verify(mJSONObject).put(JSON_ARG_MESSAGE, throwable.getMessage());
        verify(mJSONObject).put(JSON_ARG_STACKTRACE, jsonStacktrace.toJson(throwable.getStackTrace()));
        verify(mJSONObject).put(JSON_ARG_CAUSE, throwable.getCause());

        System.out.println(result);
    }
}
