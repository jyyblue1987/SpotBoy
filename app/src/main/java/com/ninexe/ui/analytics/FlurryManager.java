/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.analytics;

import android.content.Context;

import com.flurry.android.FlurryAgent;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;

import java.util.Map;


public class FlurryManager {

    private static final String FLURRY_API_KEY_DEV = "MPQCZTVT39YKPZRCCFQ4";

    public static void init(Context context) {
        if (!context.getResources().getBoolean(R.bool.isProduction)) {
            FlurryAgent.setLogEnabled(Constants.DEV_ENABLE_LOG);
        }
        FlurryAgent.init(context, context.getString(R.string.flurry_api_key));
    }

    public static void logEvent(String eventName, Map<String, String> eventData) {
        FlurryAgent.logEvent(eventName, eventData);
    }

    public static void logEvent(String eventName) {
        FlurryAgent.logEvent(eventName);
    }
}
