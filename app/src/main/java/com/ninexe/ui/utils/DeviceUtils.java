/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.util.DisplayMetrics;

import com.ninexe.ui.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by nagesh on 5/11/15.
 */
public class DeviceUtils {

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String getAdDeviceId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return md5(androidId).toUpperCase();
    }

    private static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
        }
        return "";
    }

    public static void  getDeviceDensity(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                LogUtils.LOGD("DeviceDensity","Low");
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                LogUtils.LOGD("DeviceDensity","Medium");
                break;
            case DisplayMetrics.DENSITY_HIGH:
                LogUtils.LOGD("DeviceDensity","High");
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                LogUtils.LOGD("DeviceDensity","X High");
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                LogUtils.LOGD("DeviceDensity","XX High");
                break;
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
