/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.utils.AdsUtil;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.Theme;

/**
 * Created by nagesh on 7/10/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    protected void loadAd(AdView adView) {
        if (null == adView) return;
        AdsUtil.loadBannerAd(this, adView);
    }


    protected void onPreCreate() {
        final Theme currentTheme = PreferenceManager.getCurrentTheme(this);
        switch (currentTheme) {
            case Blue:
                this.setTheme(R.style.BlueTheme);
                break;
            case Red:
                this.setTheme(R.style.RedTheme);
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onPreCreate();
        super.onCreate(savedInstanceState);
    }

    public void loadFragment(int fragmentContainerId, BaseFragment fragment, @Nullable String tag,
                             int enterAnimId, int exitAnimId,
                             BaseFragment.FragmentTransactionType fragmentTransactionType) {

        performFragmentTranscation(fragmentContainerId, fragment, tag,
                (enterAnimId == 0) ? 0 : enterAnimId,
                (exitAnimId == 0) ? 0 : exitAnimId,
                fragmentTransactionType);
    }

    private void performFragmentTranscation(int fragmentContainerId,
                                            Fragment fragment, String tag,
                                            int enterAnimId, int exitAnimId,
                                            BaseFragment.FragmentTransactionType fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case ADD:
                addFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case REPLACE:
                replaceFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case ADD_TO_BACK_STACK_AND_ADD:
                addToBackStackAndAdd(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case ADD_TO_BACK_STACK_AND_REPLACE:
                addToBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case POP_BACK_STACK_AND_REPLACE:
                popBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            case CLEAR_BACK_STACK_AND_REPLACE:
                clearBackStackAndReplace(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
                break;
            default:
                replaceFragment(fragmentContainerId, fragment, tag, enterAnimId, exitAnimId);
        }

    }

    private void addToBackStackAndAdd(int fragmentContainerId, Fragment fragment, String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
                .add(fragmentContainerId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    protected void addFragment(int fragmentContainerId, Fragment fragment, String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .add(fragmentContainerId, fragment, tag)
                .commit();
    }

    private void replaceFragment(int fragmentContainerId, Fragment fragment, @Nullable String tag,
                                 int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void popBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                        @Nullable String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void addToBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                          @Nullable String tag, int enterAnimId, int exitAnimId) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .setCustomAnimations(enterAnimId, 0, 0, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void clearBackStackAndReplace(int fragmentContainerId, Fragment fragment,
                                          @Nullable String tag, int enterAnimId, int exitAnimId) {
        clearBackStack(FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(enterAnimId, exitAnimId)
                .replace(fragmentContainerId, fragment, tag).commit();
    }

    private void clearBackStack(int flag) {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = fm.getBackStackEntryAt(0);
            fm.popBackStack(first.getId(), flag);
        }
    }


    /**
     * Start of Toolbar Related Methods
     */
    protected void setToolbar(Toolbar toolbar) {
        mToolbar = toolbar;
    }

    protected void setNavigationIcon(int imgRes) {
        if (null == mToolbar) return;
        ((ImageButton) mToolbar.findViewById(R.id.navigation_icon)).setImageResource(imgRes);
    }

    protected void setToolbarTitle(String title) {
        if (null == mToolbar) return;
        ((TextView) mToolbar.findViewById(R.id.title)).setText(title);
    }

    protected void displaySearch(boolean shouldDisplay) {
        if (null == mToolbar) return;
        int visibility = shouldDisplay ? View.VISIBLE : View.GONE;
        mToolbar.findViewById(R.id.right_icon).setVisibility(visibility);
    }

    protected void displayHeaderLogo() {
        if (null == mToolbar) return;
        mToolbar.findViewById(R.id.header_logo).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.title).setVisibility(View.GONE);
    }

    protected void enableBackButton() {
        if (null == mToolbar) return;
        mToolbar.findViewById(R.id.navigation_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void onSearchClicked() {
        mToolbar.findViewById(R.id.right_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SEARCH, null);
                NavigationUtils.startSearchActivity(BaseActivity.this);
            }
        });
    }
    /**
     * End of Toolbar Related Methods
     */

}
