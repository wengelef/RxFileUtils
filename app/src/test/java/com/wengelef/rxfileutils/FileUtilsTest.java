package com.wengelef.rxfileutils;

import android.os.Build;

import com.wengelef.library.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by fwengelewski on 10/26/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = com.wengelef.library.BuildConfig.class, sdk = Build.VERSION_CODES.JELLY_BEAN)
public class FileUtilsTest {

    @Test public void test_readAssetFile() {

    }
}
