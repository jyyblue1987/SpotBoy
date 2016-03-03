/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.utils.AdsUtil;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;

/**
 * Created by nagesh on 7/10/15.
 */
public class BaseFragment extends Fragment {
    protected Toolbar mToolbar;
    protected AdView mAdView;

    public enum FragmentTransactionType {
        ADD, REPLACE, ADD_TO_BACK_STACK_AND_ADD, ADD_TO_BACK_STACK_AND_REPLACE, POP_BACK_STACK_AND_REPLACE, CLEAR_BACK_STACK_AND_REPLACE
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

    protected void enableBackButton() {
        if (null == mToolbar) return;
        mToolbar.findViewById(R.id.navigation_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    protected void onSearchClick() {
        mToolbar.findViewById(R.id.right_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                    NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SEARCH, null);
                    NavigationUtils.startSearchActivity(getActivity());
                } else {
                    DialogUtils.showNoNetworkDialog(getActivity());
                }
            }
        });
    }

    /**
     * End of Toolbar Related Methods
     */

    protected void loadAd(AdView adView) {
        if (null == mAdView) return;
        mAdView = adView;
        AdsUtil.loadBannerAd(getActivity(), mAdView);
    }
}
