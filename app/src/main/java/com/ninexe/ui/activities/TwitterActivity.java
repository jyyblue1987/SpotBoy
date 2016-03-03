/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.LogUtils;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;

public class TwitterActivity extends AppCompatActivity {

    TwitterAuthClient mTwitterAuthClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
        final TwitterCore twitterCore = new TwitterCore(authConfig);
        Fabric.with(this, twitterCore);
        mTwitterAuthClient = new TwitterAuthClient();

        Callback<TwitterSession> twitterSessionCallback = new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String secret = result.data.getAuthToken().secret;
                LogUtils.LOGD("twitterSecret", secret);
                long id = result.data.getUserId();
                LogUtils.LOGD("twitterId", "" + id);
                bundle.putString(Constants.ACCESS_TOKEN, result.data.getAuthToken().token);
                bundle.putString(Constants.TWITTER_SECRET_TOKEN, secret);
                bundle.putString(Constants.TWITTER_USER_ID, "" + id);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(TwitterException e) {
                LogUtils.LOGE(Constants.APP_TAG, e.getMessage());
                finish();
            }
        };
        mTwitterAuthClient.authorize(TwitterActivity.this, twitterSessionCallback);
    }


    @SuppressWarnings("deprecation")
    public void ClearCookies() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(TwitterActivity.this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
