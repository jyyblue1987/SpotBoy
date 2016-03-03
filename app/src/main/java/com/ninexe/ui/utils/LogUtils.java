/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.util.Log;

import com.ninexe.ui.NixonApplication;
import com.ninexe.ui.R;


/**
 * Created by nagesh on 3/9/15.
 */
public class LogUtils {

    public static void LOGD(final String tag, String message) {
        if (!NixonApplication.getAppContext().getResources().getBoolean(R.bool.isProduction)) {
            Log.d(tag, message);
        }
    }

    public static void LOGV(final String tag, String message) {
        if (!NixonApplication.getAppContext().getResources().getBoolean(R.bool.isProduction)) {
            Log.v(tag, message);
        }
    }

    public static void LOGI(final String tag, String message) {
        if (!NixonApplication.getAppContext().getResources().getBoolean(R.bool.isProduction)) {
            Log.i(tag, message);
        }
    }

    public static void LOGW(final String tag, String message) {
        if (!NixonApplication.getAppContext().getResources().getBoolean(R.bool.isProduction)) {
            Log.w(tag, message);
        }
    }

    public static void LOGE(final String tag, String message) {
        if (!NixonApplication.getAppContext().getResources().getBoolean(R.bool.isProduction)) {
            Log.e(tag, message);
        }
    }

}
