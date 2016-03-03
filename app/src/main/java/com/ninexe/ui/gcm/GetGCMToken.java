/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.gcm;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.ninexe.ui.R;
import com.ninexe.ui.utils.LogUtils;

public class GetGCMToken extends AsyncTask<Context, Void, String> {

    public OnGCMTokenListener mListener;

    @Override
    protected String doInBackground(Context... contexts) {
        try {
            Context context = contexts[0];
            try {
                mListener = (OnGCMTokenListener) context;
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            InstanceID instanceID = InstanceID.getInstance(context);
            String token = instanceID.getToken(context.getString(R.string.gcm_SenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            LogUtils.LOGD("gcm", "GCM Registration Token: " + token);
            return token;
        } catch (Exception e) {
            LogUtils.LOGD("gcm", "Failed to complete token refresh" + e.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String token) {
        super.onPostExecute(token);
        mListener.onGCMTokenFetched(token);
    }

    public interface OnGCMTokenListener {
        void onGCMTokenFetched(String token);
    }
}
