/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.ninexe.ui.BuildConfig;
import com.ninexe.ui.R;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.HomeData;
import com.ninexe.ui.models.StaticUrlResponse;
import com.ninexe.ui.models.VersionsData;
import com.ninexe.ui.models.VersionsResponseModel;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DeviceUtils;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.dataproviders.HomeDataProvider;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;
import com.ninexe.ui.utils.dataproviders.SettingsDataProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 13/10/15.
 */
public class SplashActivity extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Bind(R.id.progressBar)
    ProgressBar mProgressBar;

    @Bind(R.id.progress_bar_container)
    View mProgressBarContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.splash_video)
    VideoView mSplashVideo;

    private boolean isVideoFinished = false;
    private boolean isDataFetched = false;
    private HomeData mHomeData;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_APP_ICON, null);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        playVideo();
        DeviceUtils.getDeviceDensity(SplashActivity.this);
        getStaticUrls();
    }

    private void fetchSections() {
        if (isVideoFinished)
            showProgressBar();
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            ResponseCallback<HomeData> responseCallback = new ResponseCallback<HomeData>() {
                @Override
                public void success(HomeData homeData) {
                    isDataFetched = true;
                    mHomeData = homeData;
                    if (OfflineArticleDataHandler.isOfflineCacheEnabled(SplashActivity.this)) {
                        OfflineArticleDataHandler.setHomeResponse(SplashActivity.this, homeData);
                    }
                    if (isVideoFinished) {
                        init(homeData);
                        hideProgressBar();
                    }
                }

                @Override
                public void failure(RestError error) {
                    showRetryFrame(getString(R.string.server_error));
                    hideProgressBar();
                }
            };

            NetworkAdapter.get(this).getSections(responseCallback);
        } else {
            if (OfflineArticleDataHandler.isOfflineCacheEnabled(SplashActivity.this) &&
                    OfflineArticleDataHandler.isHomeScreenCached(SplashActivity.this)) {
                mHomeData = OfflineArticleDataHandler.getHomeResponse(SplashActivity.this);
                if (isVideoFinished) {
                    init(OfflineArticleDataHandler.getHomeResponse(SplashActivity.this));
                }
            } else {
                if (isVideoFinished)
                    showRetryFrame(getString(R.string.no_network));
            }
            hideProgressBar();
        }
    }

    private void getStaticUrls() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            if (isVideoFinished)
                showProgressBar();
            ResponseCallback<StaticUrlResponse> responseCallback = new ResponseCallback<StaticUrlResponse>() {
                @Override
                public void success(StaticUrlResponse staticUrlResponse) {
                    SettingsDataProvider.getInstance().setStaticUrlResponse(staticUrlResponse);
                    fetchSections();
                }

                @Override
                public void failure(RestError error) {
                    if (isVideoFinished)
                        showRetryFrame(getString(R.string.server_error));
                    hideProgressBar();

                }
            };
            NetworkAdapter.get(this).getStaticUrls(responseCallback);
        } else {
            fetchSections();
        }
    }

    public void init(HomeData homeData) {
        HomeDataProvider.getInstance().setHomeData(homeData);
        fetchVersions();
    }

    private void fetchVersions() {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            ResponseCallback<VersionsResponseModel> responseCallback = new ResponseCallback<VersionsResponseModel>() {
                @Override
                public void success(VersionsResponseModel versionsResponseModel) {
                    hideProgressBar();
                    showUpdateDialog(versionsResponseModel.getVersionsData().getVersionsAndroidData());
                }

                @Override
                public void failure(RestError error) {
                    hideProgressBar();
                    showRetryFrame(getString(R.string.server_error));
                }
            };
            NetworkAdapter.get(this).getVersions(responseCallback);
        } else {
            showRetryFrame(getString(R.string.server_error));
        }
    }

    private void showUpdateDialog(VersionsData.VersionsAndroidData versionsAndroidData) {
        String minVersionNumStr = versionsAndroidData.getMinimumVersionNumber();
        String curVersionNumStr = versionsAndroidData.getCurrentVersionNumber();
        int minVersionNum = 0;
        int curVersionNum = 0;
        try {
            minVersionNum = Integer.valueOf(minVersionNumStr);
            if (BuildConfig.VERSION_CODE < minVersionNum) {
                showForceUpdateDialog(versionsAndroidData.getForceUpdateString());
                return;
            }
            curVersionNum = Integer.valueOf(curVersionNumStr);
            if (BuildConfig.VERSION_CODE < curVersionNum) {
                showOptionalUpdateDialog(versionsAndroidData.getOptionalUpdateString());
                return;
            }
        } catch (NumberFormatException e) {
            //Nothing to do
        }

        launchLoginOrHomeScreen();
    }

    private void showOptionalUpdateDialog(String optionalUpdateString) {
        DialogUtils.DialogInterfaceCallBack callback = new DialogUtils.DialogInterfaceCallBack() {
            @Override
            public void positiveButtonClick(DialogInterface dialog) {
                goToPlayStore();
            }

            @Override
            public void negativeButtonClick(DialogInterface dialog) {
                launchLoginOrHomeScreen();
            }
        };

        DialogUtils.BackButtonDialogListener backBtnListener = new DialogUtils.BackButtonDialogListener() {
            @Override
            public void onBackButtonClick() {
                finish();
            }
        };

        DialogUtils.showAlertDialogWithCallBackWithPositiveBanNonDismissible(this, getString(R.string.app_name),
                optionalUpdateString, getString(R.string.update_now), getString(R.string.no_thanks), true,
                callback, false, backBtnListener);
    }

    private void showForceUpdateDialog(String forceUpdateString) {

        DialogUtils.SingleButtonDialogListener listener = new DialogUtils.SingleButtonDialogListener() {
            @Override
            public void onButtonClick() {
                goToPlayStore();
            }
        };

        DialogUtils.BackButtonDialogListener backBtnListener = new DialogUtils.BackButtonDialogListener() {
            @Override
            public void onBackButtonClick() {
                finish();
            }
        };

        DialogUtils.showSingleButtonAlertDialogWithPositiveBanNonDismissible(this, getString(R.string.app_name), forceUpdateString,
                getString(R.string.update_now), listener, false, backBtnListener);
    }

    private void goToPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void launchLoginOrHomeScreen() {
        if (isLoggedInOrSkipped()) {
            /*if (PreferenceManager.isTutorialScreenShown(SplashActivity.this)) {*/
            showHomeScreen();
            /*} else {
                NavigationUtils.startWelcomeActivity(this);
                finish();
            }*/
        } else
            showLoginScreen();
        // hideProgressBar();
    }

    private void showHomeScreen() {
        // This method will be executed once the timer is over
        // Start your app main activity
        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    private void showRetryFrame(String message) {
        showRetryContainer();
        mRetryMessage.setText(message);
    }

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        //  fetchHomeScreenData();
        //fetchSections();
        showProgressBar();
        getStaticUrls();
    }

    private void showProgressBarContainer() {
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    private void showRetryContainer() {
        mProgressBarContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSplashVideo.resume();
    }


    private void showProgressBar() {
        showProgressBarContainer();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


    private void showLoginScreen() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isLoggedInOrSkipped() {
        return (PreferenceManager.getBoolean(SplashActivity.this, Constants.SP_LOGGED_IN, false)
                || PreferenceManager.getBoolean(SplashActivity.this, Constants.SP_LOGIN_SKIPPED, false));
    }

    public void playVideo() {
        mProgressBarContainer.setVisibility(View.GONE);
        mediaController = new MediaController(SplashActivity.this);
        mediaController.setAnchorView(mSplashVideo);
        mSplashVideo.setMediaController(mediaController);
        mSplashVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splashvideo));
        mediaController.setVisibility(View.GONE);
        mSplashVideo.setZOrderOnTop(true);
        mSplashVideo.requestFocus();
        mSplashVideo.start();

        mSplashVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (0 == mp.getVideoHeight()) {
                    isVideoFinished = true;
                    mSplashVideo.pause();
                    showProgressBar();
                }
            }
        });

        mSplashVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mSplashVideo.pause();
                isVideoFinished = true;
                showProgressBar();
                return true;
            }
        });

        mSplashVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mSplashVideo.setVisibility(View.GONE);
                isVideoFinished = true;
                if (isDataFetched) {
                    if (mHomeData == null) {
                        showRetryFrame(getString(R.string.no_network));
                    } else {
                        showProgressBar();
                        init(mHomeData);
                    }
                } else {
                    showProgressBar();
                    if (mHomeData == null) {
                        hideProgressBar();
                        showRetryFrame(getString(R.string.no_network));
                    } else {
                        init(mHomeData);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mSplashVideo.stopPlayback();
        super.onDestroy();
    }
}
