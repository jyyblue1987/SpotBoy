/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.INotificationSettingsRecyclerViewItem;
import com.ninexe.ui.models.NotificationCategoryResponse;
import com.ninexe.ui.models.NotificationSettings;
import com.ninexe.ui.models.NotificationSettingsCategoryText;
import com.ninexe.ui.models.NotificationTopic;
import com.ninexe.ui.utils.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by nagesh on 8/11/15.
 */
public class NotificationSettingsDataProvider {
    private static final NotificationSettingsDataProvider NOTIFICATION_SETTINGS_DATA_PROVIDER
            = new NotificationSettingsDataProvider();

    private NotificationCategoryResponse notificationCategoryResponse;

    private NotificationSettingsDataProvider() {

    }

    public static NotificationSettingsDataProvider getInstance() {
        return NOTIFICATION_SETTINGS_DATA_PROVIDER;
    }

    public void setNotificationCategoryResponse(NotificationCategoryResponse notificationCategoryResponse) {
        this.notificationCategoryResponse = notificationCategoryResponse;
    }

    public NotificationCategoryResponse getNotificationCategoryResponse() {
        return notificationCategoryResponse;
    }

    public ArrayList<INotificationSettingsRecyclerViewItem> getNotificationSettingsList(Context context) {
        ArrayList<INotificationSettingsRecyclerViewItem> notificationSettingsList = new ArrayList<>();


        //notificationSettingsList.add(getNotificationsItem());
        notificationSettingsList.add(getDoNotDisturbItem(context));
        notificationSettingsList.add(getNotificationSettingsCategoryText());

        for (NotificationTopic notificationTopic : getNotificationCategoryResponse().getTopics()) {
            notificationSettingsList.add(getNotificationSettingsItem(notificationTopic));
        }

        return notificationSettingsList;
    }

    private INotificationSettingsRecyclerViewItem getDoNotDisturbItem(Context context) {
        NotificationSettings notificationSettings = new NotificationSettings();
        NotificationTopic notificationTopic = new NotificationTopic();
        notificationTopic.setSubscribed(PreferenceManager.isNotificationDNDEnabled(context));
        notificationTopic.setTitle(Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB);
        notificationSettings.setNotificationTopic(notificationTopic);
        return notificationSettings;
    }

    public INotificationSettingsRecyclerViewItem getNotificationsItem() {
        NotificationSettings notificationSettings = new NotificationSettings();
        NotificationTopic notificationTopic = new NotificationTopic();
        notificationTopic.setTitle(Constants.SETTINGS_NOTIFICATION);
        notificationSettings.setNotificationTopic(notificationTopic);
        return notificationSettings;
    }

    public INotificationSettingsRecyclerViewItem getNotificationSoundItem() {
        NotificationSettings notificationSettings = new NotificationSettings();
        NotificationTopic notificationTopic = new NotificationTopic();
        notificationTopic.setTitle(Constants.SETTINGS_NOTIFICATION_SOUND);
        notificationSettings.setNotificationTopic(notificationTopic);
        return notificationSettings;
    }

/*    private boolean isAllNotificationCategorySelected() {
        boolean isAllSelected = true;

        if (null != getNotificationCategoryResponse() && !getNotificationCategoryResponse().getTopics().isEmpty()) {
            for (NotificationTopic notificationTopic : getNotificationCategoryResponse().getTopics()) {
                if (!notificationTopic.isSubscribed()) {
                    isAllSelected = false;
                    break;
                }
            }
        }

        return isAllSelected;
    }*/

    private INotificationSettingsRecyclerViewItem getNotificationSettingsCategoryText() {
        return new NotificationSettingsCategoryText();
    }

    private INotificationSettingsRecyclerViewItem
    getNotificationSettingsItem(NotificationTopic notificationTopic) {
        NotificationSettings notificationSettings = new NotificationSettings();
        notificationSettings.setNotificationTopic(notificationTopic);
        return notificationSettings;
    }


}
