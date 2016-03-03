/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.Manifest;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.ForgotPasswordFragment;
import com.ninexe.ui.fragments.HomeFragment;
import com.ninexe.ui.fragments.LoginFragment;
import com.ninexe.ui.fragments.RegisterFragment;
import com.ninexe.ui.fragments.WebViewFragment;
import com.ninexe.ui.gcm.RegistrationIntentService;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.NotificationSubscriptionModel;
import com.ninexe.ui.models.NotificationSubscriptionResponse;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DeviceUtils;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.dataproviders.SettingsDataProvider;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import java.util.UUID;

public class LoginActivity extends BaseActivity implements LoginFragment.OnLoginFragmentInteractionListener, RegisterFragment.OnRegisterFragmentListener, ForgotPasswordFragment.OnForgotPasswordInteractionListener {

    private static final int READ_PHONE_STATE_PERMISSION = 1;
    private static final int GPLUS_ACCOUNT_PERMISSION = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadLoginFragment();
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (null != PreferenceManager.getGCMToken(LoginActivity.this)) {

                        if (PackageManager.PERMISSION_DENIED != ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.GET_ACCOUNTS)) {
                            subscribeForNotifications();
                        } else {
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    READ_PHONE_STATE_PERMISSION);
                        }
                    }
                }
            };


            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.GCM_REGISTRATION_COMPLETE));

            if (NetworkCheckUtility.isNetworkAvailable(this) &&
                    !PreferenceManager.isSubscribedToNotifications(this)) {
                DialogUtils.showCustomProgressDialog(LoginActivity.this, true);
                Intent intent = new Intent(LoginActivity.this, RegistrationIntentService.class);
                startService(intent);
            }
        } else {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                LogUtils.LOGD("gcm", "Play services not available");
                if (!UserDataProvider.getInstance().isLoggedIn(this) && !PreferenceManager.getBoolean(this, Constants.SP_LOGIN_SKIPPED, false))
                    apiAvailability.getErrorDialog(this, resultCode, 9000).show();
            }
        }
    }

    private void loadLoginFragment() {
        LoginFragment loginFragment = new LoginFragment();
        loadFragment(R.id.loginFrameContainer, loginFragment, loginFragment.getClass().getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }


    private void loadRegisterFragment() {
        RegisterFragment registerFragment = new RegisterFragment();
        loadFragment(R.id.loginFrameContainer, registerFragment, registerFragment.getClass().getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_ADD);
    }

    private void loadForgotPasswordFragment() {
        ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();
        loadFragment(R.id.loginFrameContainer, forgotPasswordFragment, forgotPasswordFragment.getClass().getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_ADD);
    }

    void loadWebViewFragment(String url, String title) {
        WebViewFragment webViewFragment = WebViewFragment.newInstance(url, title);
        loadFragment(R.id.loginFrameContainer, webViewFragment, webViewFragment.getClass().getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_ADD);
    }


    @Override
    public void onRegisterSelected() {
        loadRegisterFragment();
    }


    @Override
    public void onSkip() {
        PreferenceManager.putBoolean(LoginActivity.this, Constants.SP_LOGIN_SKIPPED, true);
        /*if (PreferenceManager.isTutorialScreenShown(LoginActivity.this)) {*/
            NavigationUtils.startHomeActivity(LoginActivity.this);
        /*} else {
            NavigationUtils.startWelcomeActivity(LoginActivity.this);
        }*/
        finish();
    }

    @Override
    public void onBack() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack();
        else
            finish();
    }

    @Override
    public void onTermsAndConditionsClick() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            if (null != SettingsDataProvider.getInstance().getStaticUrlResponse()) {
                loadWebViewFragment(SettingsDataProvider.getInstance().getStaticUrlResponse().getData().getTermsAndConditionsUrl(),
                        getString(R.string.settings_terms_conditions));
            }
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    @Override
    public void onForgotPassword() {
        loadForgotPasswordFragment();
    }

    @Override
    public void onLogin() {
        PreferenceManager.putBoolean(LoginActivity.this, Constants.SP_LOGGED_IN, true);
        /*if (PreferenceManager.isTutorialScreenShown(LoginActivity.this)) {*/
            if (PreferenceManager.getBoolean(LoginActivity.this, Constants.SP_LOGIN_SKIPPED, false)) {
                finish();
            } else {
                NavigationUtils.startHomeActivity(LoginActivity.this);
                finish();
            }
        /*} else {
            NavigationUtils.startWelcomeActivity(LoginActivity.this);
            finish();
        }*/
    }

    private void subscribeForNotifications() {
        if (NetworkCheckUtility.isNetworkAvailable(this) && !PreferenceManager.isSubscribedToNotifications(this)) {
            String deviceId;
            String gcmToken = PreferenceManager.getGCMToken(this);


            TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();

            PreferenceManager.storeGCMDeviceId(this, deviceId);


            if (null != gcmToken && null != deviceId) {

                NotificationSubscriptionModel notificationSubscriptionModel = new NotificationSubscriptionModel();
                notificationSubscriptionModel.setDeviceId(deviceId);
                notificationSubscriptionModel.setDeviceToken(gcmToken);
                notificationSubscriptionModel.setPlatform(Constants.PLATFORM_ANDROID);

                ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>
                        responseCallback = new ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>() {
                    @Override
                    public void success(GenericResponse<NotificationSubscriptionResponse> notificationSubscriptionResponseGenericResponse) {
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration successful");
                        PreferenceManager.storeIsSubscribedToNotifications(LoginActivity.this, true);
                        PreferenceManager.storeShowNotificationsEnableStatus(LoginActivity.this, true);

                        DialogUtils.cancelProgressDialog();
                    }

                    @Override
                    public void failure(RestError error) {
                        DialogUtils.cancelProgressDialog();
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration failure");
                        PreferenceManager.storeIsSubscribedToNotifications(LoginActivity.this, false);
                    }
                };
                NetworkAdapter.get(this).subscribeForNotifications(notificationSubscriptionModel, responseCallback);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    subscribeForNotifications();
                } else {
                    DialogUtils.cancelProgressDialog();
                }
                break;
            case GPLUS_ACCOUNT_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoginFragment fragment = (LoginFragment) getSupportFragmentManager().findFragmentByTag(LoginFragment.class.getName());
                    fragment.gplusLogin();
                } else {
                    DialogUtils.cancelProgressDialog();
                }
                break;

        }
    }

}
