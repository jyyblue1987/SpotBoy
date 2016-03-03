/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;

public class RateAppUtil {

    private static int viewedArticleCount;
    private static int articleCountLimit = Constants.NO_ARTICLE_RATE;
    private static boolean isDialogShown = false;

    public static void incrementViewedArticles(Context context) {
        viewedArticleCount = PreferenceManager.getInt(context, Constants.SP_NO_VIEWED_ARTICLES, 0);
        viewedArticleCount++;
        PreferenceManager.putInt(context, Constants.SP_NO_VIEWED_ARTICLES, viewedArticleCount);
    }

    public static void showRateDialog(final Context context) {
        viewedArticleCount = PreferenceManager.getInt(context, Constants.SP_NO_VIEWED_ARTICLES, 0);
        boolean showPopup = PreferenceManager.getBoolean(context, Constants.SP_SHOW_RATE_POPUP, true);
        if (viewedArticleCount >= articleCountLimit && showPopup && !isDialogShown) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.rate_app));
            builder.setMessage(context.getString(R.string.rate_message));

            builder.setPositiveButton(context.getString(R.string.rate_spotboye), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isDialogShown = false;
                    dialog.dismiss();
                    PreferenceManager.putBoolean(context, Constants.SP_SHOW_RATE_POPUP, false);
                    String appPackageName = context.getPackageName();
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.
                                parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException e) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.
                                parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });

            builder.setNegativeButton(context.getString(R.string.rate_never), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.putBoolean(context, Constants.SP_SHOW_RATE_POPUP, false);
                    isDialogShown = false;
                    dialog.dismiss();
                }
            });

            builder.setNeutralButton(context.getString(R.string.rate_remind_later), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.putInt(context, Constants.SP_NO_VIEWED_ARTICLES, 0);
                    isDialogShown = false;
                    dialog.dismiss();
                }
            });

            builder.setCancelable(false);
            builder.create();
            builder.show();
            isDialogShown = true;
        }
    }

    public boolean isDialogShown() {
        return isDialogShown;
    }
}
