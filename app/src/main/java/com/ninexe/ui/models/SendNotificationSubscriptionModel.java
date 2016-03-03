/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 9/11/15.
 */
public class SendNotificationSubscriptionModel {
    String deviceId;
    ArrayList<String> subscribedTopics;
    boolean DND;

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setSubscribedTopics(ArrayList<String> subscribedTopics) {
        this.subscribedTopics = subscribedTopics;
    }

    public void setDnd(boolean dnd) {
        this.DND = dnd;
    }
}
