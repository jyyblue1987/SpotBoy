/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.NotificationHubPagerAdapter;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.NotificationCategoryResponse;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.dataproviders.NotificationHubDataProvider;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 9/10/15.
 */
public class NotificationHubFragment extends BaseFragment {

    public static final String NOTIFICATION_HUB_FRAGMENT = "notification_hub_fragment";

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.pager)
    ViewPager mPager;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.data_container)
    View mDataContainer;

    @Bind(R.id.empty_container)
    View mEmptyContainer;

    @Bind(R.id.message)
    TextView mEmptyMessage;

    private NotificationHubPagerAdapter mPagerAdapter;

    public static NotificationHubFragment newInstance() {
        Bundle args = new Bundle();
        NotificationHubFragment fragment = new NotificationHubFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_hub, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        return view;
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        enableBackButton();
        displaySearch(false);
        setToolbarTitle(getString(R.string.title_notification_hub));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fecthNotificationCategories();
    }

    private void fecthNotificationCategories() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            String deviceId = PreferenceManager.getGCMDeviceId(getActivity());
            ResponseCallback<GenericResponse<NotificationCategoryResponse>> responseCallback =
                    new ResponseCallback<GenericResponse<NotificationCategoryResponse>>() {
                        @Override
                        public void success(GenericResponse<NotificationCategoryResponse>
                                                    notificationCategoryResponse) {
                            // TODO: Handle Success
                            NotificationHubDataProvider.getInstance()
                                    .setNotificationCategoryResponse(notificationCategoryResponse
                                            .getData());

                            if (null != getActivity()) {

                                if (!NotificationHubDataProvider.getInstance().getNotificationTopics().isEmpty()) {
                                    showNotificationHubData();
                                } else {
                                    showEmptyContainer();
                                }
                            }
                        }

                        @Override
                        public void failure(RestError error) {
                            // TODO: Handle Failure
                        }
                    };
            NetworkAdapter.get(getActivity()).getNotificationCatgeories(deviceId, responseCallback);
        } else {
            // TODO: Handle No Network
        }
    }

    private void showEmptyContainer() {
        mEmptyContainer.setVisibility(View.VISIBLE);
        mEmptyMessage.setText(getString(R.string.msg_not_subscribe_to_notification));
    }

    private void showNotificationHubData() {
        mDataContainer.setVisibility(View.VISIBLE);
        initPagerAdapter();
        setPagerAdapter();
        addTabLayoutPagerInteraction();
    }

    private void addTabLayoutPagerInteraction() {
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    private void setPagerAdapter() {
        mPager.setAdapter(mPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
    }

    private void initPagerAdapter() {
        mPagerAdapter = new NotificationHubPagerAdapter(getFragmentManager(),
                NotificationHubDataProvider.getInstance().getNotificationTopics());
    }
}
