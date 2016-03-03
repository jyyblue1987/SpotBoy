/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.analytics;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.ninexe.ui.R;

/**
 * Created by mukesh on 12/10/15.
 */
public class GoogleAnalyticsManager {

    private static final String STAGING_PROPERTY_ID = "UA-69815103-1";
    private static final String GOOGLE_ANALYTICS_PROPERTY_ID = STAGING_PROPERTY_ID;

    private static Tracker mAppTracker;

    public static void init(Context context) {
        initTracker(context);
    }

    private static void initTracker(Context context) {
        if (mAppTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            mAppTracker = analytics.newTracker(context.getString(R.string.google_analytics_property_id));
            mAppTracker.enableAdvertisingIdCollection(true);
        }
    }

    public static void logEvent(String category, String eventAction, String eventLabel) {
        if (mAppTracker != null) {
            mAppTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(eventAction)
                    .setLabel(eventLabel)
                    .build());
        }
    }

}
