/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.ninexe.ui.R;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.NotificationsSettingsFragment;
import com.ninexe.ui.fragments.SettingsFragment;
import com.ninexe.ui.fragments.TextSizeFragment;
import com.ninexe.ui.fragments.WebViewFragment;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.SendNotificationSubscriptionResponseModel;
import com.ninexe.ui.models.StaticUrlData;
import com.ninexe.ui.models.StaticUrlResponse;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.EmailUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.Theme;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;
import com.ninexe.ui.utils.dataproviders.SettingsDataProvider;

/**
 * Created by nagesh on 20/10/15.
 */
public class SettingsActivity extends BaseActivity
        implements SettingsFragment.SettingsInteractionListener {

    StaticUrlData mStaticUrlData;
    private boolean mVisible;
    private static final int READ_PHONE_STATE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (null == SettingsDataProvider.getInstance().getStaticUrlResponse()) {
            getStaticUrls();
        } else {
            mStaticUrlData = SettingsDataProvider.getInstance().getStaticUrlResponse().getData();
        }
        loadSettingsFragment();
    }


    private void getStaticUrls() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            ResponseCallback<StaticUrlResponse> responseCallback = new ResponseCallback<StaticUrlResponse>() {
                @Override
                public void success(StaticUrlResponse staticUrlResponse) {
                    if (null != SettingsActivity.this) {
                        SettingsDataProvider.getInstance().setStaticUrlResponse(staticUrlResponse);
                        mStaticUrlData = SettingsDataProvider.getInstance().getStaticUrlResponse().getData();
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (null != SettingsActivity.this) {

                    }
                }
            };
            NetworkAdapter.get(this).getStaticUrls(responseCallback);
        } else {

        }
    }


    @Override
    public void onNotificationsClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onNotificationsClick");

        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            loadNotificationsSettingsFragment();
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    @Override
    public void onThemeToggleSelection(boolean isChecked) {
        LogUtils.LOGD(Constants.APP_TAG, "onThemeToggleSelection");
        Theme theme;
        if (!isChecked) {
            theme = Theme.Blue;
        } else {
            theme = Theme.Red;
        }
        PreferenceManager.setCurrentTheme(this, theme);

        SettingsFragment settingsFragment =
                (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.SETTINGS_FRAGMENT);
        settingsFragment.updateTheme();
    }

    @Override
    public void onTextSizeClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onTextSizeClick");
        loadTextSizeFragment();
    }

    @Override
    public void onOfflineCachingSelection(boolean isChecked) {
        LogUtils.LOGD(Constants.APP_TAG, "onOfflineCachingSelection");
        OfflineArticleDataHandler.toggleOfflineCacheMode(SettingsActivity.this, isChecked);
    }

    @Override
    public void onFeedbackClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onFeedbackClick");
        sendFeedback();
    }

    @Override
    public void onRateAndReviewClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onRateAndReviewClick");
        NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_RATE_REVIEW, null);
        rateUs();
    }

    @Override
    public void onTermsAndConditionsClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onTermsAndConditionsClick");
        if (null != mStaticUrlData && null != mStaticUrlData.getTermsAndConditionsUrl()) {
            loadWebViewFragment(mStaticUrlData.getTermsAndConditionsUrl(), getString(R.string.settings_terms_conditions));
        }
    }

    @Override
    public void onPrivacyPolicyClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onPrivacyPolicyClick");
        if (null != mStaticUrlData && null != mStaticUrlData.getPrivacyPolicyUrl()) {
            loadWebViewFragment(mStaticUrlData.getPrivacyPolicyUrl(), getString(R.string.settings_privacy_policy));
        }
    }

    @Override
    public void onAboutUsClick() {
        LogUtils.LOGD(Constants.APP_TAG, "onAboutUsClick");
        if (null != mStaticUrlData && null != mStaticUrlData.getAboutUsUrl()) {
            loadWebViewFragment(mStaticUrlData.getAboutUsUrl(), getString(R.string.settings_about_us));
        }
    }

    @Override
    public void onContactUsClick() {
        if (null != mStaticUrlData && null != mStaticUrlData.getContactusUrl()) {
            loadWebViewFragment(mStaticUrlData.getContactusUrl(), getString(R.string.settings_contact_us));
        }
    }

    @Override
    public void onDisclaimerClick() {
        if (null != mStaticUrlData && null != mStaticUrlData.getDisclaimerUrl()) {
            loadWebViewFragment(mStaticUrlData.getDisclaimerUrl(), getString(R.string.settings_disclaimer));
        }
    }

    private void sendFeedback() {
        NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_FEEDBACK, null);
        if (null != SettingsDataProvider.getInstance().getStaticUrlResponse()) {
            EmailUtils.sendEmail(this,
                    SettingsDataProvider.getInstance().getStaticUrlResponse().getData().getFeedbackEmail()
                    , getString(R.string.feedback_email_subject),
                    getString(R.string.email_chooser_text));
        }
    }

    private void rateUs() {
        String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.
                    parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.
                    parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void loadNotificationsSettingsFragment() {
        NotificationsSettingsFragment notificationsSettingsFragment =
                NotificationsSettingsFragment.newInstance();
        loadFragment(R.id.fragment_container, notificationsSettingsFragment,
                NotificationsSettingsFragment.NOTIFICATIONS_SETTINGS_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_REPLACE);
    }

    private void loadSettingsFragment() {
        SettingsFragment settingsFragment = SettingsFragment.newInstance();
        loadFragment(R.id.fragment_container, settingsFragment, SettingsFragment.SETTINGS_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }

    private void loadTextSizeFragment() {
        TextSizeFragment textSizeFragment = TextSizeFragment.newInstance();
        loadFragment(R.id.fragment_container, textSizeFragment, TextSizeFragment.TEXT_SIZE_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_REPLACE);
    }

    private void loadWebViewFragment(String url, String title) {
        WebViewFragment webViewFragment = WebViewFragment.newInstance(url, title);
        loadFragment(R.id.fragment_container, webViewFragment, WebViewFragment.WEB_VIEW_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD_TO_BACK_STACK_AND_REPLACE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVisible = false;
    }

    @Override
    public void onBackPressed() {
        NotificationsSettingsFragment notificationsSettingsFragment =
                (NotificationsSettingsFragment) getSupportFragmentManager().
                        findFragmentByTag(NotificationsSettingsFragment.NOTIFICATIONS_SETTINGS_FRAGMENT);
        if (null != notificationsSettingsFragment) {
            DialogUtils.showCustomProgressDialog(SettingsActivity.this, true);
            ResponseCallback<GenericResponse<SendNotificationSubscriptionResponseModel>>
                    responseCallback = new ResponseCallback<GenericResponse<SendNotificationSubscriptionResponseModel>>() {
                @Override
                public void success(GenericResponse<SendNotificationSubscriptionResponseModel> sendNotificationSubscriptionResponseModelGenericResponse) {
                    DialogUtils.cancelProgressDialog();
                    LogUtils.LOGD(Constants.APP_TAG, "Notification Settings saved successfully");
                    if (mVisible)
                        getSupportFragmentManager().popBackStack();

                }

                @Override
                public void failure(RestError error) {
                    DialogUtils.cancelProgressDialog();
                    LogUtils.LOGD(Constants.APP_TAG, "Notification Settings save failure");
                    if (mVisible)
                        getSupportFragmentManager().popBackStack();
                }
            };
            NetworkAdapter.get(SettingsActivity.this).sendNotificationSubscriptions(notificationsSettingsFragment.getNotificationSubscriptions(), responseCallback);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_PHONE_STATE_PERMISSION:
                NotificationsSettingsFragment notificationsSettingsFragment =
                        (NotificationsSettingsFragment) getSupportFragmentManager().
                                findFragmentByTag(NotificationsSettingsFragment.NOTIFICATIONS_SETTINGS_FRAGMENT);
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PreferenceManager.storeShowNotificationsEnableStatus(this, true);
                    notificationsSettingsFragment.subscribeForNotifications();
                }
                notificationsSettingsFragment.setRecyclerItems();
                break;
        }
    }
}
