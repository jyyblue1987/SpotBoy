/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.LoginDataModel;
import com.ninexe.ui.utils.PreferenceManager;

public class UserDataProvider {
    private static UserDataProvider Instance = new UserDataProvider();

    public static UserDataProvider getInstance() {
        return Instance;
    }

    private UserDataProvider() {
    }

    private String mName;

    public String getProfilePictureUrl(Context context) {
        return PreferenceManager.getString(context, Constants.SP_USER_PROFILE_PIC, null);
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        mProfilePictureUrl = profilePictureUrl;
    }

    public String getName(Context context) {
        return PreferenceManager.getString(context, Constants.SP_USER_NAME, null);
    }

    public String getAccessToken(Context context) {
        return PreferenceManager.getString(context, Constants.SP_USER_ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    private String mProfilePictureUrl;
    private String mAccessToken;


    public void storeUserDetails(LoginDataModel loginDataModel, Context context) {
        mName = loginDataModel.getName();
        mAccessToken = loginDataModel.getAuthToken();
        mProfilePictureUrl = loginDataModel.getProfilePic();

        PreferenceManager.putString(context, Constants.SP_USER_NAME, mName);
        PreferenceManager.putString(context, Constants.SP_USER_PROFILE_PIC, mProfilePictureUrl);
        PreferenceManager.putString(context, Constants.SP_USER_ACCESS_TOKEN, mAccessToken);
    }

    public boolean isLoggedIn(Context context) {
        return (PreferenceManager.getBoolean(context, Constants.SP_LOGGED_IN, false));
    }


}
