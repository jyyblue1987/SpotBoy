/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.gms.ads.AdView;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.source.SingleSource;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.SubMenu;
import com.ninexe.ui.utils.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LiveTVActivity extends BaseActivity {

    @Bind(R.id.jwplayer)
    JWPlayerView mPlayerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.adView)
    AdView mAdView;

    private VideoPlayerEvents.OnFullscreenListener mEventHandler;
    private boolean orientationChanged = false;
    private int orientation;
    private static final int LAND = 2;
    private static final int PORT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_tv);
        ButterKnife.bind(this);
        loadAd(mAdView);
        mPlayerView.setFullscreen(false, false);

        mEventHandler = new VideoPlayerEvents.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean fullScreen, boolean userRequest) {
                if (userRequest) {
                    if (fullScreen)
                        orientation = LAND;
                    else
                        orientation = PORT;
                    handleFullscreen(fullScreen);
                }
            }
        };

        Bundle bundle = getIntent().getExtras();
        SubMenu subMenu = bundle.getParcelable(Constants.EXTRA_SUB_MENU);
        if (null != subMenu) {
            SingleSource media = new SingleSource(subMenu.getActionUrl());
            mPlayerView.load(media);
            mPlayerView.play();
            mPlayerView.setOnFullscreenListener(mEventHandler);
            iniToolBar(subMenu.getTitle());
            mPlayerView.setEnableSetSystemUiVisibility(false);
        }
    }


    private void iniToolBar(String title) {
        setSupportActionBar(mToolbar);
        setToolbar(mToolbar);
        setToolbarTitle(title);
        displaySearch(false);
        enableBackButton();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (orientation == PORT)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        mPlayerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPlayerView.onPause();
        super.onPause();
    }

    @SuppressLint("NewApi")
    private void handleFullscreen(boolean fullscreen) {
        View decorView = getWindow().getDecorView();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        if (fullscreen) {
            // Detecting Navigation Bar
            //mPlayerView.setFullscreen(true, false);
            if (!hasBackKey && !hasHomeKey) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (null != getSupportActionBar())
                getSupportActionBar().hide();
            mAdView.setVisibility(View.GONE);
        } else {
            //mPlayerView.setFullscreen(false, false);
            // Detecting Navigation Bar
            if (!hasBackKey && !hasHomeKey) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (null != getSupportActionBar())
                getSupportActionBar().show();
            mAdView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Exit fullscreen when the user pressed the Back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mPlayerView.getFullscreen()) {
                // mPlayerView.setFullscreen(false, false);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                orientation = PORT;
                mPlayerView.setFullscreen(false, false);
                handleFullscreen(false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
