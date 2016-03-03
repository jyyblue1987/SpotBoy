/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class OfflineDataHandler {

    private static JSONObject mJsonObject;

    protected static void addContent(Context context, String key, String response, String prefKey) {
        mJsonObject = getContent(context, prefKey);
        if (mJsonObject == null)
            mJsonObject = new JSONObject();
        try {
            mJsonObject.put(key, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PreferenceManager.putString(context, prefKey, mJsonObject.toString());
    }

    protected static void addContent(Context context, String key, boolean response, String prefKey) {
        mJsonObject = getContent(context, prefKey);
        if (mJsonObject == null)
            mJsonObject = new JSONObject();
        try {
            mJsonObject.put(key, response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PreferenceManager.putString(context, prefKey, mJsonObject.toString());
    }

    protected static JSONObject getContent(Context context, String prefKey) {
        try {
            return new JSONObject(PreferenceManager.getString(context, prefKey, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static boolean isContentCached(Context context,String contentKey, String prefKey) {
        mJsonObject = getContent(context, prefKey);
        if (mJsonObject == null)
            return false;
        try {
            mJsonObject.get(contentKey);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
