/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.analytics;

import android.content.Context;

import com.flurry.android.FlurryAgent;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class NixonTracker {

    private static final String EVENT_SCREEN_VISITED = "Screen_Visited_";
    private Context mContext;
    private static NixonTracker sNixonTracker;

    private NixonTracker() {

    }

    public static NixonTracker get() {
        if (sNixonTracker == null) {
            sNixonTracker = new NixonTracker();
        }
        return sNixonTracker;
    }

    public void init(Context applicationContext) {
        mContext = applicationContext;
        GoogleAnalyticsManager.init(applicationContext);
        FlurryManager.init(applicationContext);
    }

    public void logEvent(String category, String eventAction, String eventLabel, Map<String, String> eventData) {
        if (eventData != null) {
            FlurryAgent.logEvent(eventAction, eventData);
            GoogleAnalyticsManager.logEvent(category, eventAction, getMultipleEventLabel(eventData));
        } else {
            FlurryAgent.logEvent(eventAction);
            GoogleAnalyticsManager.logEvent(category, eventAction, eventLabel);
        }
    }

    public void logScreenVisits(String screenName, Map<String, String> eventData) {
        FlurryManager.logEvent(screenName);
        GoogleAnalyticsManager.logEvent(screenName, screenName, screenName);
    }

    private static String getMultipleEventLabel(Map<String, String> eventData) {
        if (eventData != null) {
            String eventLabel = new JSONObject(eventData).toString();
            return eventLabel != null ? eventLabel : null;
        } else {
            return null;
        }
    }
}
