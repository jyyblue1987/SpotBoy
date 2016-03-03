/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.ninexe.ui.analytics.NixonTracker;

/**
 * Created by nagesh on 5/11/15.
 */
public class NixonApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static int applicationOnPause;
    private final static int BACKGROUND = 1;
    private final static int FOREGROUND = 2;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
        NixonTracker.get().init(getApplicationContext());
        NixonApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return NixonApplication.context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        applicationOnPause = FOREGROUND;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        applicationOnPause = BACKGROUND;
    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    public static int isApplicationInBackground() {
        return NixonApplication.applicationOnPause;
    }
}
