/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.DialogUtils;

import java.io.IOException;
import java.net.MalformedURLException;

public class GooglePlusActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;
    private boolean mIsResolving = false;
    private static final int REQUEST_AUTHORIZATION = 201;
    /* A flag indicating that a PendingIntent is in progress and prevents
     * us from starting further intents.
     */
    private boolean mIntentInProgress = false;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_plus);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
        DialogUtils.showCustomProgressDialog(GooglePlusActivity.this, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            new getAccessToken().execute();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution() && !mIsResolving) {
            mIsResolving = true;
            try {
                startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                DialogUtils.cancelProgressDialog();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                mIntentInProgress = false;
                if (resultCode == RESULT_OK) {
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                } else if (resultCode == RESULT_CANCELED) {
                    DialogUtils.cancelProgressDialog();
                    finish();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_CANCELED) {
                    DialogUtils.cancelProgressDialog();
                    finish();
                } else if (resultCode == RESULT_OK) {
                    finishActivity(accessToken);
                }
                break;
        }
    }

    private class getAccessToken extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                accessToken = GoogleAuthUtil.getToken(
                        GooglePlusActivity.this,
                        Plus.AccountApi.getAccountName(mGoogleApiClient) + "",
                        "oauth2:"
                                + Scopes.PLUS_LOGIN);

            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            } catch (GoogleAuthException | IOException e) {
                e.printStackTrace();
            }
            return accessToken;
        }

        @Override
        protected void onPostExecute(String accessToken) {
            super.onPostExecute(accessToken);
            finishActivity(accessToken);
        }
    }

    private void finishActivity(String accessToken) {
        DialogUtils.cancelProgressDialog();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACCESS_TOKEN, accessToken);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            googlePlusLogout();
        }
    }

    private void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }
}
