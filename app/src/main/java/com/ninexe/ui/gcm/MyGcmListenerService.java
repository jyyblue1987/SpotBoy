/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.gcm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.ninexe.ui.R;
import com.ninexe.ui.activities.HomeActivity;
import com.ninexe.ui.activities.SplashActivity;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NotificationUtils;
import com.ninexe.ui.utils.PreferenceManager;

public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
// [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        String articleId = data.getString("id");
        String title = data.getString("section");
        String type = data.getString("type");
        String sound = data.getString("sound");
        String imageUrl = data.getString("image");


        LogUtils.LOGD(TAG, "From: " + from);
        LogUtils.LOGD(TAG, "Message: " + message);
        sendNotification(message, articleId, title, type, sound, imageUrl);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String articleId, String title, String type, String sound, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(this);
        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra(Constants.EXTRA_DEEPLINK, true);
        resultIntent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        resultIntent.putExtra(Constants.EXTRA_ARTICLE_TYPE, type);
        resultIntent.putExtra(Constants.EXTRA_ARTICLE_TITLE, title);
        resultIntent.putExtra(Constants.EXTRA_ARTICLE_MESSAGE, message);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (!PreferenceManager.isNotificationDNDEnabled(this)) {
            notificationUtils.showNotification(MyGcmListenerService.this, getString(R.string.app_name), message, resultIntent, sound, imageUrl);
        } else {
            if (!DateTimeUtils.isTimeWithInNotificationDNDRange()) {
                notificationUtils.showNotification(MyGcmListenerService.this, getString(R.string.app_name), message, resultIntent, sound, imageUrl);
            }
        }
    }
}