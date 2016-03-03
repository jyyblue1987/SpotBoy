/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationSettings implements INotificationSettingsRecyclerViewItem {

    NotificationTopic notificationTopic;

    public void setNotificationTopic(NotificationTopic notificationTopic) {
        this.notificationTopic = notificationTopic;
    }

    public NotificationTopic getNotificationTopic() {
        return notificationTopic;
    }

    @Override
    public int getItemType() {
        return INotificationSettingsRecyclerViewItem.TYPE_NOTIFICATION_SETTINGS;
    }
}
