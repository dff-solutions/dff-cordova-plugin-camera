package com.dff.cordova.plugin.camera.classes.json;

import com.dff.cordova.plugin.camera.helpers.JsonFactory;

import org.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JsonStacktraceUnitTest {
    private StackTraceElement[] stackTrace;

    @Mock
    private JsonFactory mJsonFactory;

    @Mock
    private JSONArray mJSONArray;

    @InjectMocks
    private
    JsonStacktrace jsonStacktrace;

    @BeforeEach
    public void setup() {
        // not mockable, final class
        final StackTraceElement ste1 = new StackTraceElement(
            "clazz1",
            "method1",
            "file1",
            1
        );

        final StackTraceElement ste2 = new StackTraceElement(
            "clazz2",
            "method2",
            "file2",
            2
        );

        stackTrace = new StackTraceElement[] { ste1, ste2};
    }

    @Test
    public void toJson_checkArgs() {
        JSONArray result = jsonStacktrace.toJson(null);
        assertNull(result);
    }

    @Test
    public void toJson_convertArg() {
        doReturn(mJSONArray).when(mJsonFactory).getJSONArray();

        JSONArray result = jsonStacktrace.toJson(stackTrace);

        assertNotNull(result);
        verify(mJSONArray).put(stackTrace[0].toString());
        verify(mJSONArray).put(stackTrace[1].toString());
    }
}
