/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.PreferenceManager;

public class RegistrationIntentService extends IntentService {
    public static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
        LogUtils.LOGD(TAG, "start");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    InstanceID instanceID = InstanceID.getInstance(RegistrationIntentService.this);
                    String token = instanceID.getToken(getString(R.string.gcm_SenderId),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    LogUtils.LOGD(TAG, "GCM Registration Token: " + token);
                    PreferenceManager.storeGCMToken(RegistrationIntentService.this, token);
                } catch (Exception e) {
                    LogUtils.LOGD(TAG, "Failed to complete token refresh" + e.toString());
                }
                Intent registrationComplete = new Intent(Constants.GCM_REGISTRATION_COMPLETE);
                LocalBroadcastManager.getInstance(RegistrationIntentService.this).sendBroadcast(registrationComplete);
            }
        }).start();
    }


}