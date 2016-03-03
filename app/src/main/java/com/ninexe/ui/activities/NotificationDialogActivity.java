/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.PollDataHandler;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;

import java.util.logging.Handler;

public class NotificationDialogActivity extends AppCompatActivity {

    private String title,message;
    private String mArticleID;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_dialog);

        title = getIntent().getStringExtra(Constants.EXTRA_ARTICLE_TITLE);
        mArticleID = getIntent().getStringExtra(Constants.EXTRA_ARTICLE_ID);
        type = getIntent().getStringExtra(Constants.EXTRA_ARTICLE_TYPE);
        message = getIntent().getStringExtra(Constants.EXTRA_ARTICLE_MESSAGE);

        DialogUtils.showAlertDialogWithCallBack(this, Constants.SETTINGS_NOTIFICATION, message, getString(R.string.show_alertdialog), getResources().getString(R.string.cancel),
                true, new DialogUtils.DialogInterfaceCallBack() {
                    @Override
                    public void positiveButtonClick(DialogInterface dialog) {
                        startArticleDetailActivity();
                    }

                    @Override
                    public void negativeButtonClick(DialogInterface dialog) {
                        finish();
                    }
                }, false);
    }

    private void startArticleDetailActivity() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            if (null != type && type.equalsIgnoreCase(Constants.PHOTO)) {
                NavigationUtils.startPhotoGalleryActivityForResult(this, mArticleID);
            } else if (null != type && (type.equalsIgnoreCase(Constants.QUIZ) || type.equalsIgnoreCase(Constants.PERSONALITY_TEST) ||
                    type.equalsIgnoreCase(Constants.CONTEST) || type.equalsIgnoreCase(Constants.POLL))) {
                if (type.equalsIgnoreCase(Constants.POLL)) {
                    if (PollDataHandler.isPollAttempted(mArticleID, this)) {
                        DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.poll_attemted), getString(R.string.ok), new DialogUtils.SingleButtonDialogListener() {
                            @Override
                            public void onButtonClick() {
                                finish();
                            }
                        });
                        return;
                    }
                }
                NavigationUtils.startQuizActivityForResult(this, mArticleID, type);
            } else {
                NavigationUtils.startArticleDetailActivityForResult(this, mArticleID, title);
            }
        } else {
            if (OfflineArticleDataHandler.isArticleCached(mArticleID, this)) {
                NavigationUtils.startArticleDetailActivityForResult(this, mArticleID, title);
            } else {
                DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.no_network), getString(R.string.ok), new DialogUtils.SingleButtonDialogListener() {
                    @Override
                    public void onButtonClick() {
                        finish();
                    }
                });
                return;
            }
        }
        finish();
    }


}
