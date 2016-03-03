/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;

/**
 * Created by nagesh on 8/10/15.
 */
public class DrawableUtils {

    public static int getMenuDrawableResource(String drawableName) {
        int resId = -1;
        switch (drawableName.toLowerCase()) {
            case Constants.MENU_HOME:
                resId = R.drawable.home_normal_icn;
                break;
            case Constants.MENU_HELLO_EDITOR:
                resId = R.drawable.hello_editor_normal_icn;
                break;
            case Constants.MENU_NOTIFICATION_HUB:
                resId = R.drawable.notification_normal_icn;
                break;
            case Constants.MENU_NEWS_LETTER:
                resId = R.drawable.newsletter_normal_icn;
                break;
            case Constants.MENU_SHARE_THE_APP:
                resId = R.drawable.share_normal_icn;
                break;
            case Constants.MENU_SETTINGS:
                resId = R.drawable.settings_normal_icn;
                break;
            case Constants.MENU_LOGOUT:
                resId = R.drawable.logout_normal_icn;
                break;
        }
        return resId;
    }

    public static int getGridRecentArticleDrawableResource(String drawableName) {
        int resId = -1;
        switch (drawableName.toLowerCase()) {
            case Constants.ARTICLE:
                break;
            case Constants.PHOTO:
                resId = R.drawable.camera_icn;
                break;
            case Constants.VIDEO:
                resId = R.drawable.play_icn;
                break;
        }
        return resId;
    }
}
