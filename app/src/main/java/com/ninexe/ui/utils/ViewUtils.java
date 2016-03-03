/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;

/**
 * Created by nagesh on 12/10/15.
 */
public class ViewUtils {

    public static void setText(TextView textView, String text) {
        if (null == text || TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public static String getEllipsizedText(String text, int limitCount) {
        if (TextUtils.isEmpty(text)) return null;
        String resultantString = null;

        if (text.length() <= limitCount) {
            resultantString = text;
        } else {
            resultantString = text.substring(0, (limitCount - 3)).concat("...");
        }
        return resultantString;
    }

    public static String getEllipsizedText(String text) {
        final int DEFAULT_LIMIT_COUNT = 12;
        return getEllipsizedText(text, DEFAULT_LIMIT_COUNT);
    }

    public static void showView(View view) {
        if (null != view && !(view.getVisibility() == View.VISIBLE)) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(View view) {
        if (null != view && (view.getVisibility() == View.VISIBLE)) {
            view.setVisibility(View.GONE);
        }
    }


    public static void setThemeBackground(View view) {
        Context context = view.getContext();
        final Theme currentTheme = PreferenceManager.getCurrentTheme(context);
        switch (currentTheme) {
            case Blue:
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryBlue));
                break;
            case Red:
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryRed));
                break;

        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static boolean isValidEmail(String emailId) {
        boolean isValidEmail = false;
        if (Patterns.EMAIL_ADDRESS.matcher(emailId).matches())
            isValidEmail = true;
        return isValidEmail;
    }

    public static String getThumbnail(String url, String imageType) {
        if (null == url) return null;

        StringBuilder resultantString = new StringBuilder(url);
        int lastPosition = url.lastIndexOf(".");

        switch (imageType) {
            case Constants.IMAGE_THUMBNAIL:
                resultantString.insert(lastPosition, Constants.IMAGE_THUMBNAIL);
                break;
            case Constants.IMAGE_SMALL:
                resultantString.insert(lastPosition, Constants.IMAGE_SMALL);
                break;
            case Constants.IMAGE_TINY:
                resultantString.insert(lastPosition, Constants.IMAGE_TINY);
                break;
        }
        return resultantString.toString();
    }

    public static String getNumberWithSuffix(int count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        try {
            return String.valueOf(Math.round(count / Math.pow(1000, exp))) + "KMBT".charAt(exp - 1);
        } catch (IndexOutOfBoundsException e) {
            return String.valueOf(count);
        }
    }
}
