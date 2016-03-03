/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;

/**
 * Created by nagesh on 8/11/15.
 */
public class AdsUtil {
    public static void loadBannerAd(Context context, final AdView adView) {
        AdRequest adRequest = null;
        if (context.getResources().getBoolean(R.bool.isProduction)) {
            adRequest = new AdRequest.Builder().build();
        } else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(DeviceUtils.getAdDeviceId(context))
                    .build();
        }
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }
        });
    }
}
