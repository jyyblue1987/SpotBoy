/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;

import com.ninexe.ui.R;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.NotificationHubFragment;

/**
 * Created by nagesh on 10/11/15.
 */
public class NotificationHubActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_hub);
        loadNotificationHubFragment();
    }

    private void loadNotificationHubFragment() {
        NotificationHubFragment notificationHubFragment = NotificationHubFragment.newInstance();
        loadFragment(R.id.fragment_container, notificationHubFragment, NotificationHubFragment.NOTIFICATION_HUB_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }
}
