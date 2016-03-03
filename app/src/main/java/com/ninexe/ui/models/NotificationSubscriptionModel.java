/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 8/11/15.
 */
public class NotificationSubscriptionModel {

    String deviceId;
    String deviceToken;
    String platform;

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
