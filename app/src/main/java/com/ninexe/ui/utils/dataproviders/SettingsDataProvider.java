/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Settings;
import com.ninexe.ui.models.StaticUrlResponse;

import java.util.ArrayList;

/**
 * Created by nagesh on 12/10/15.
 */
public class SettingsDataProvider {

    private final static SettingsDataProvider SETTINGS_DATA_PROVIDER = new SettingsDataProvider();
    private StaticUrlResponse mStaticUrlResponse = null;

    private SettingsDataProvider() {

    }

    public static SettingsDataProvider getInstance() {
        return SETTINGS_DATA_PROVIDER;
    }

    public ArrayList<Settings> getSettingsList() {
        ArrayList<Settings> settingsArrayList = new ArrayList<>();

        Settings notification = new Settings();
        notification.setTitle(Constants.SETTINGS_NOTIFICATION);
        notification.setHasToggle(false);
        notification.setHasFwdArrow(true);
        settingsArrayList.add(notification);

        Settings theme = new Settings();
        theme.setTitle(Constants.SETTINGS_THEME);
        theme.setHasFwdArrow(false);
        theme.setHasToggle(true);
        settingsArrayList.add(theme);

        Settings textSize = new Settings();
        textSize.setTitle(Constants.SETTINGS_TEXT_SIZE);
        textSize.setHasToggle(false);
        textSize.setHasFwdArrow(true);
        settingsArrayList.add(textSize);

        Settings offlineCaching = new Settings();
        offlineCaching.setTitle(Constants.SETTINGS_OFFLINE_CACHE);
        offlineCaching.setHasFwdArrow(false);
        offlineCaching.setHasToggle(true);
        settingsArrayList.add(offlineCaching);

        Settings feedback = new Settings();
        feedback.setTitle(Constants.SETTINGS_FEEDBACK);
        feedback.setHasFwdArrow(false);
        feedback.setHasToggle(false);
        settingsArrayList.add(feedback);

        Settings rateAndReview = new Settings();
        rateAndReview.setTitle(Constants.SETTINGS_RATE_REVIEW);
        rateAndReview.setHasFwdArrow(false);
        rateAndReview.setHasToggle(false);
        settingsArrayList.add(rateAndReview);

        Settings termsAndConditions = new Settings();
        termsAndConditions.setTitle(Constants.SETTINGS_TERMS_CONDITIONS);
        termsAndConditions.setHasFwdArrow(false);
        termsAndConditions.setHasToggle(false);
        settingsArrayList.add(termsAndConditions);

        Settings privacyPolicy = new Settings();
        privacyPolicy.setTitle(Constants.SETTINGS_PRIVACY_POLICY);
        privacyPolicy.setHasFwdArrow(false);
        privacyPolicy.setHasToggle(false);
        settingsArrayList.add(privacyPolicy);

        Settings aboutUs = new Settings();
        aboutUs.setTitle(Constants.SETTINGS_ABOUT_US);
        aboutUs.setHasFwdArrow(false);
        aboutUs.setHasToggle(false);
        settingsArrayList.add(aboutUs);

        Settings contactUs = new Settings();
        contactUs.setTitle(Constants.SETTINGS_CONTACT_US);
        contactUs.setHasFwdArrow(false);
        contactUs.setHasToggle(false);
        settingsArrayList.add(contactUs);

        Settings disclaimer = new Settings();
        disclaimer.setTitle(Constants.SETTINGS_DISCLAIMER);
        disclaimer.setHasFwdArrow(false);
        disclaimer.setHasToggle(false);
        settingsArrayList.add(disclaimer);

        return settingsArrayList;
    }

    public void setStaticUrlResponse(StaticUrlResponse staticUrlResponse) {
        this.mStaticUrlResponse = staticUrlResponse;
    }

    public StaticUrlResponse getStaticUrlResponse() {
        return mStaticUrlResponse;
    }
}
