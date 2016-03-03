/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ninexe.ui.fragments.NotificationHubContentFragment;
import com.ninexe.ui.models.NotificationTopic;

import java.util.ArrayList;

/**
 * Created by nagesh on 10/11/15.
 */
public class NotificationHubPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<NotificationTopic> notificationTopics;

    public NotificationHubPagerAdapter(FragmentManager fm,
                                       ArrayList<NotificationTopic> notificationTopicArrayList) {
        super(fm);
        notificationTopics = notificationTopicArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        fragment = NotificationHubContentFragment.newInstance(notificationTopics.get(position).get_id());
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return notificationTopics.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return notificationTopics.size();
    }
}
