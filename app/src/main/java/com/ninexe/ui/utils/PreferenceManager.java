/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.ninexe.ui.common.Constants;


public class PreferenceManager {


    public static SharedPreferences getSharedPreferences(final Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }


    public static boolean isFirstTime(Context context, String key) {
        if (getBoolean(context, key, false)) {
            return false;
        } else {
            putBoolean(context, key, true);
            return true;
        }
    }


    public static boolean contains(Context context, String key) {
        return PreferenceManager.getSharedPreferences(context).contains(key);
    }

    public static int getInt(final Context context, final String key, final int defaultValue) {
        return PreferenceManager.getSharedPreferences(context).getInt(key, defaultValue);
    }

    public static boolean putInt(final Context context, final String key, final int pValue) {
        final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.putInt(key, pValue);

        return editor.commit();
    }

    public static long getLong(final Context context, final String key, final long defaultValue) {
        return PreferenceManager.getSharedPreferences(context).getLong(key, defaultValue);
    }

    public static Long getLong(final Context context, final String key, final Long defaultValue) {
        if (PreferenceManager.getSharedPreferences(context).contains(key)) {
            return PreferenceManager.getSharedPreferences(context).getLong(key, 0);
        } else {
            return null;
        }
    }


    public static boolean putLong(final Context context, final String key, final long pValue) {
        final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.putLong(key, pValue);

        return editor.commit();
    }

    public static boolean getBoolean(final Context context, final String key, final boolean defaultValue) {
        return PreferenceManager.getSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static boolean putBoolean(final Context context, final String key, final boolean pValue) {
        final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.putBoolean(key, pValue);

        return editor.commit();
    }

    public static String getString(final Context context, final String key, final String defaultValue) {
        return PreferenceManager.getSharedPreferences(context).getString(key, defaultValue);
    }

    public static boolean putString(final Context context, final String key, final String pValue) {
        final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.putString(key, pValue);

        return editor.commit();
    }


    public static boolean remove(final Context context, final String key) {
        final SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();

        editor.remove(key);

        return editor.commit();
    }

    public static Theme getCurrentTheme(Context context) {
        return Theme.valueOf(PreferenceManager.getString(context, "app_theme", Theme.Blue.name()));
    }

    public static void setCurrentTheme(Context context, Theme currentTheme) {
        PreferenceManager.putString(context, "app_theme", currentTheme.name());
    }

    public static void storeWebViewTextSize(Context context, int textSize) {
        putInt(context, Constants.SP_TEXT_SIZE, textSize);
    }

    public static int getWebViewTextSize(Context context) {
        return getInt(context, Constants.SP_TEXT_SIZE, -1);
    }

    public static void clearPreference(Context context) {
        PreferenceManager.getSharedPreferences(context).edit().clear().commit();
    }

    public static void clearField(String key, Context context) {
        PreferenceManager.getSharedPreferences(context).edit().remove(key).apply();
    }

    public static void storePhotoGalleryDetailCurrentPosition(int currentPosition, Context context) {
        putInt(context, Constants.SP_PHOTO_GALLERY_DETAIL_CURRENT_POSITION, currentPosition);
    }

    public static int getPhotoGalleryDetailCurrentPosition(Context context) {
        return getInt(context, Constants.SP_PHOTO_GALLERY_DETAIL_CURRENT_POSITION, -1);
    }

    public static void storeGCMToken(Context context, String token) {
        putString(context, Constants.SP_GCM_TOKEN, token);
    }

    public static String getGCMToken(Context context) {
        return getString(context, Constants.SP_GCM_TOKEN, null);
    }

    public static void storeIsSubscribedToNotifications(Context context, boolean isSubscribedToNotifications) {
        putBoolean(context, Constants.SP_IS_SUBSCRIBED_TO_NOTIFICATIONS, isSubscribedToNotifications);
    }

    public static boolean isSubscribedToNotifications(Context context) {
        return getBoolean(context, Constants.SP_IS_SUBSCRIBED_TO_NOTIFICATIONS, false);
    }

    public static void storeGCMDeviceId(Context context, String deviceId) {
        putString(context, Constants.SP_GCM_DEVICE_ID, deviceId);
    }

    public static String getGCMDeviceId(Context context) {
        return getString(context, Constants.SP_GCM_DEVICE_ID, null);
    }

    public static void storeIsNotificationDNDEnabled(Context context, boolean isDNDEnabled) {
        putBoolean(context, Constants.SP_IS_DND_ENABLED, isDNDEnabled);
    }

    public static boolean isNotificationDNDEnabled(Context context) {
        return getBoolean(context, Constants.SP_IS_DND_ENABLED, false);
    }

    public static void storeShowNotificationsEnableStatus(Context context, boolean showNotificationCategories) {
        putBoolean(context, Constants.SP_SHOW_NOTIFICATIONS_ENABLED, showNotificationCategories);
    }

    public static boolean isShowToNotificationsEnabled(Context context) {
        return getBoolean(context, Constants.SP_SHOW_NOTIFICATIONS_ENABLED, false);
    }

    /*public static boolean isTutorialScreenShown(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
        return sharedpreferences.getBoolean(Constants.SP_WELCOME_SCREEN_SHOWN, false);
    }*/

    public static void setIsTutorialScreenShown(Context context, boolean value) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean(Constants.SP_WELCOME_SCREEN_SHOWN, value).apply();
    }

    public static void setNotificationSoundSetting(Context context,boolean value){
        putBoolean(context, Constants.SP_SHOW_NOTIFICATIONS_SOUND_ENABLED, value);
    }

    public static boolean isNotificationSoundEnabled(Context context){
        return getBoolean(context, Constants.SP_SHOW_NOTIFICATIONS_SOUND_ENABLED, true);
    }
}