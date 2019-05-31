package com.dff.cordova.plugin.camera.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@PrepareForTest({
    ContextCompat.class,
    ActivityCompat.class
})
public class PermissionHelperUnitTest {
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Rule
    public PowerMockRule mPowerMockRule = new PowerMockRule();

    @Mock
    private Context mContext;

    @Mock
    private Log mLog;
    
    @Mock
    private Activity mActivity;

    private PermissionHelper mPermissionHelper;

    @Before
    public void setup() {
        mPermissionHelper = new PermissionHelper(mLog, mContext);
    }

    @Test
    public void hasAllPermissions_whenAllGrantedThenReturnTrue() {
        PowerMockito.mockStatic(ContextCompat.class);
        Mockito
            .when(ContextCompat.checkSelfPermission(eq(mContext), anyString()))
            .thenReturn(PERMISSION_GRANTED);

        boolean result = mPermissionHelper.hasAllPermissions(CameraPlugin.PERMISSIONS);
        Assertions.assertTrue(result);

        PowerMockito.verifyStatic(ContextCompat.class, times(1));
        ContextCompat.checkSelfPermission(mContext, CameraPlugin.PERMISSIONS[0]);
    }

    @Test
    public void hasAllPermissions_whenOneNotGrantedThenReturnFalse() {
        PowerMockito.mockStatic(ContextCompat.class);

        Mockito
            .when(ContextCompat.checkSelfPermission(eq(mContext), eq(CameraPlugin.PERMISSIONS[0])))
            .thenReturn(PERMISSION_DENIED);

        boolean result = mPermissionHelper.hasAllPermissions(CameraPlugin.PERMISSIONS);
        Assertions.assertFalse(result);

        PowerMockito.verifyStatic(ContextCompat.class, times(1));
        ContextCompat.checkSelfPermission(mContext, CameraPlugin.PERMISSIONS[0]);
    }

    @Test
    public void shouldShowRequestPermissionRationale_whenOneShouldReturnTrue() {
        PowerMockito.mockStatic(ActivityCompat.class);
        Mockito
            .when(ActivityCompat
                .shouldShowRequestPermissionRationale(
                    eq(mActivity),
                    eq(CameraPlugin.PERMISSIONS[0])
                ))
            .thenReturn(true);

        boolean result = mPermissionHelper
            .shouldShowRequestPermissionRationale(
                mActivity,
                CameraPlugin.PERMISSIONS
            );
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldShowRequestPermissionRationale_whenNoneShouldReturnFalse() {
        PowerMockito.mockStatic(ActivityCompat.class);
        Mockito
            .when(ActivityCompat
                .shouldShowRequestPermissionRationale(
                    eq(mActivity),
                    eq(CameraPlugin.PERMISSIONS[0])
                ))
            .thenReturn(false);

        boolean result = mPermissionHelper
            .shouldShowRequestPermissionRationale(
                mActivity,
                CameraPlugin.PERMISSIONS
            );
        Assertions.assertFalse(result);
    }
}
