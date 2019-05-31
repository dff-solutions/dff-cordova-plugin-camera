package com.dff.cordova.plugin.camera.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;

public class PackageManagerHelperUnitTest {
    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Mock
    private Context mContext;

    @Mock
    private ApplicationInfo mApplicationInfo;

    @InjectMocks
    private PackageManagerHelper mPackageManagerHelper;

    @Before
    public void setup() {
        Mockito.doReturn(mApplicationInfo).when(mContext).getApplicationInfo();
    }

    @Test
    public void isDebuggable_whenNoFlagReturnFalse() {
        Assertions.assertEquals(0, mApplicationInfo.flags);
        Assertions.assertFalse(mPackageManagerHelper.isDebuggable());
    }

    @Test
    public void isDebuggable_whenFlagDebuggableReturnTrue() {
        mApplicationInfo.flags = ApplicationInfo.FLAG_DEBUGGABLE;
        Assertions.assertTrue(mPackageManagerHelper.isDebuggable());
    }

    @Test
    public void isDebuggable_whenMoreThanFlagDebuggableReturnTrue() {
        mApplicationInfo.flags = ApplicationInfo.FLAG_DEBUGGABLE | ApplicationInfo.FLAG_HAS_CODE;
        Assertions.assertTrue(mPackageManagerHelper.isDebuggable());
    }

    @Test
    public void getDataDir_shouldReturnDataDir() {
        final String dataDir = "foobar";
        mApplicationInfo.dataDir = dataDir;
        Assertions.assertSame(dataDir, mPackageManagerHelper.getDataDir());
    }
}
