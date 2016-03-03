/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class ReactionDataHandler extends OfflineDataHandler {

    private static JSONObject mJsonObject;

    public static void addReaction(String articleId, String reaction, Context context) {
        addContent(context, articleId, reaction, Constants.SP_REACTION_MAP);
    }


    public static void printReactionMap(Context context) {
        String wrapperStr = PreferenceManager.getString(context, Constants.SP_REACTION_MAP, "");
    }

    public static boolean isArticleReacted(String articleId, Context context) {
        mJsonObject = getContent(context, Constants.SP_REACTION_MAP);
        if (mJsonObject == null)
            return false;
        try {
            mJsonObject.get(articleId);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }


    public static String getReaction(String articleId, Context context) {
        if (isArticleReacted(articleId, context)) {
            mJsonObject = getContent(context, Constants.SP_REACTION_MAP);
            try {
                return mJsonObject != null ? mJsonObject.getString(articleId) : null;
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }
}
