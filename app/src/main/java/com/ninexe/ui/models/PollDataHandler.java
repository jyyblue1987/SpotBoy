/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.dataproviders.OfflineDataHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class PollDataHandler extends OfflineDataHandler {

    private static JSONObject mJsonObject;

    public static void addPollData(Context context, String articleId) {
        addContent(context, articleId, true, Constants.SP_POLL_DATA);
    }

    public static boolean isPollAttempted(String articleId, Context context) {
        mJsonObject = getContent(context, Constants.SP_POLL_DATA);
        if (mJsonObject == null)
            return false;
        try {
            mJsonObject.get(articleId);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

}
