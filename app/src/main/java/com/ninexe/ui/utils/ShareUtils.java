/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ninexe.ui.R;

public class ShareUtils {

    private static String mContentType = "text/plain";

    public static void share(String shareBody, String shareTitle, String contentType, Context context) {
        if (null == shareBody)
            shareBody = "";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        if (null == contentType || TextUtils.isEmpty(contentType))
            contentType = mContentType;
        sharingIntent.setType(contentType);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        if (null == shareTitle)
            shareTitle = context.getString(R.string.share_using);
        context.startActivity(Intent.createChooser(sharingIntent, shareTitle));
    }

    public static void share(String shareBody, Context context) {
        share(shareBody, "", "", context);
    }

    public static void share(String shareBody, String shareTitle, Context context) {
        share(shareBody, shareTitle, "", context);
    }
}
