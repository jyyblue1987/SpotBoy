/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.google.android.gms.ads.AdView;
import com.longtailvideo.jwplayer.media.PlayerState;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.ArticleDetailFragment;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.RateAppUtil;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailActivity extends BaseActivity
        implements ArticleDetailFragment.IArticleDetailFragmentInteractionListener {

    private VideoPlayerEvents.OnFullscreenListener mEventHandler;
    private JWPlayerView mPlayerView;
    private ArticleDetailFragment articleDetailFragment;
    private static final int FULL_SCREEN = 1;
    private static final int PORTRAIT_SCREEN = 2;
    private int orientation;
    private static final int LAND = 2;
    private static final int PORT = 1;

    @Bind(R.id.adView)
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        ButterKnife.bind(this);
        loadAd(mAdView);
        loadArticleDetailFragment(getArticleId(), getArticleTitle());

    }

    public AdView getAdView() {
        return mAdView;
    }

    private void loadArticleDetailFragment(String articleId, String articleTitle) {
        articleDetailFragment = ArticleDetailFragment.newInstance(articleId, articleTitle);
        loadFragment(R.id.fragment_container, articleDetailFragment,
                ArticleDetailFragment.ARTICLE_DETAIL_FRAGMENT, 0, 0, BaseFragment.FragmentTransactionType.REPLACE);


    }

    private String getArticleId() {
        String articleId = null;
        if (null != getIntent().getExtras()) {
            articleId = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_ID);
        }
        return articleId;
    }

    private String getArticleTitle() {
        String articleTitle = null;
        if (null != getIntent().getExtras()) {
            articleTitle = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_TITLE);
        }
        return articleTitle;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        try {
            if (null != mPlayerView && mPlayerView.getVisibility() == View.VISIBLE) {
                if (orientation == PORT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    mPlayerView.setFullscreen(false, false);
                    handleFullscreen(false);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    mPlayerView.setFullscreen(true, false);
                    handleFullscreen(true);
                }

            }
        } catch (Exception exp){
            DialogUtils.showSingleButtonAlertDialog(this, null,"Unable to play video", "OK");
        }
    }


    @Override
    public void onBackPressed() {
        try {
            if (null != mPlayerView && mPlayerView.getVisibility() == View.VISIBLE)
                if (mPlayerView.getFullscreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    orientation = PORT;
                    mPlayerView.setFullscreen(false, false);
                    return;
                }
            if (articleDetailFragment.getWebviewVideoFullScreenState() == PORTRAIT_SCREEN) {
                finish();
                disableWebView();
            } else if (articleDetailFragment.getWebviewVideoFullScreenState() == FULL_SCREEN) {
                return;
            }
        } catch (Exception exp) {
            DialogUtils.showSingleButtonAlertDialog(this, null,"Unable to play video", "OK");
        }

        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        try {
            if (null != mPlayerView)
                mPlayerView.onPause();
            pauseWebview();
        } catch (Exception exp){
            DialogUtils.showSingleButtonAlertDialog(this, null,"Unable to play video", "OK");
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            if (null != mPlayerView)
                mPlayerView.onResume();
            resumeWebview();
        } catch (Exception exp) {
            DialogUtils.showSingleButtonAlertDialog(this, null,"Unable to play video", "OK");
        }
        super.onResume();

        RateAppUtil.showRateDialog(this);
    }

    private void disableWebView() {
        ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment)
                getSupportFragmentManager().findFragmentByTag(ArticleDetailFragment.ARTICLE_DETAIL_FRAGMENT);
        articleDetailFragment.disableWebView();
    }

    private void pauseWebview() {
        ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment)
                getSupportFragmentManager().findFragmentByTag(ArticleDetailFragment.ARTICLE_DETAIL_FRAGMENT);
        if (articleDetailFragment != null && articleDetailFragment.getWebview() != null)
            articleDetailFragment.getWebview().onPause();
    }

    private void resumeWebview() {
        ArticleDetailFragment articleDetailFragment = (ArticleDetailFragment)
                getSupportFragmentManager().findFragmentByTag(ArticleDetailFragment.ARTICLE_DETAIL_FRAGMENT);
        if (articleDetailFragment != null && articleDetailFragment.getWebview() != null)
            articleDetailFragment.getWebview().onResume();
    }


    @SuppressLint("NewApi")
    private void handleFullscreen(boolean fullscreen) {

        View decorView = getWindow().getDecorView();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

        if (fullscreen) {
            // Detecting Navigation Bar
            if (!hasBackKey && !hasHomeKey) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (null != articleDetailFragment.getToolbar())
                articleDetailFragment.getToolbar().setVisibility(View.GONE);
            articleDetailFragment.hideViews();
            mAdView.setVisibility(View.GONE);
        } else {
            // Detecting Navigation Bar
            if (!hasBackKey && !hasHomeKey) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (null != articleDetailFragment.getToolbar())
                articleDetailFragment.getToolbar().setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.VISIBLE);
            articleDetailFragment.resetRecyclerViewData();
            articleDetailFragment.resetVideoPlayerHeight();
            articleDetailFragment.setUserComment();
        }


    }

    @Override
    public void getVideoPlayerInstance(JWPlayerView jwPlayerView) {
        try {
            mEventHandler = new VideoPlayerEvents.OnFullscreenListener() {
                @Override
                public void onFullscreen(boolean fullScreen, boolean userRequest) {
                    ViewUtils.hideSoftKeyboard(ArticleDetailActivity.this);
                    if (userRequest) {
                        if (fullScreen)
                            orientation = LAND;
                        else
                            orientation = PORT;
                        handleFullscreen(fullScreen);
                    }
                }
            };
            mPlayerView = jwPlayerView;
            if (null != mPlayerView) {
                mPlayerView.setOnFullscreenListener(mEventHandler);
            }
        } catch (Exception exp) {
            DialogUtils.showSingleButtonAlertDialog(this, null,"Unable to play video", "OK");
        }

    }


}
