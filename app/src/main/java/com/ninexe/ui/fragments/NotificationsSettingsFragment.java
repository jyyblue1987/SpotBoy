/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.NotificationSettingsRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.INotificationSettingsRecyclerViewItem;
import com.ninexe.ui.models.NotificationCategoryResponse;
import com.ninexe.ui.models.NotificationSettings;
import com.ninexe.ui.models.NotificationSubscriptionModel;
import com.ninexe.ui.models.NotificationSubscriptionResponse;
import com.ninexe.ui.models.NotificationTopic;
import com.ninexe.ui.models.SendNotificationSubscriptionModel;
import com.ninexe.ui.models.SendNotificationSubscriptionResponseModel;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.NotificationSettingsDataProvider;

import java.util.ArrayList;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationsSettingsFragment extends BaseFragment
        implements NotificationSettingsRecyclerViewAdapter.INotificationSettingsRecyclerViewAdapterListener {


    public static final String NOTIFICATIONS_SETTINGS_FRAGMENT = "notifications_settings_fragment";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.notification_settings_recycler_view)
    RecyclerView mNotificationSettingsRecyclerView;

    private NotificationSettingsRecyclerViewAdapter mAdapter;
    private ArrayList<INotificationSettingsRecyclerViewItem> mDataSet = new ArrayList<>();

    private static final int READ_PHONE_STATE_PERMISSION = 1;

    public static NotificationsSettingsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsSettingsFragment fragment = new NotificationsSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        setRecyclerItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewUtils.setThemeBackground(mToolbar);
    }

    public void setRecyclerItems() {
        NotificationSettings notificationSettings =
                (NotificationSettings) NotificationSettingsDataProvider.getInstance().getNotificationsItem();
        notificationSettings.getNotificationTopic()
                .setSubscribed(PreferenceManager.isShowToNotificationsEnabled(getActivity()) &&
                        PreferenceManager.isSubscribedToNotifications(getActivity()));

        NotificationSettings notificationSoundSettings = (NotificationSettings) NotificationSettingsDataProvider.getInstance().getNotificationSoundItem();
        notificationSoundSettings.getNotificationTopic()
                .setSubscribed(PreferenceManager.isNotificationSoundEnabled(getActivity()));


        mDataSet.clear();
        if (PreferenceManager.isShowToNotificationsEnabled(getActivity()) && PreferenceManager.isSubscribedToNotifications(getActivity())) {
            mDataSet.add(notificationSettings);
            if (PreferenceManager.isShowToNotificationsEnabled(getActivity()))
                mDataSet.add(notificationSoundSettings);
            fecthNotificationCategories();
        } else {
            mDataSet.add(notificationSettings);
            if (PreferenceManager.isShowToNotificationsEnabled(getActivity()) && PreferenceManager.isSubscribedToNotifications(getActivity()))
                mDataSet.add(notificationSoundSettings);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void fecthNotificationCategories() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            String deviceId = PreferenceManager.getGCMDeviceId(getActivity());
            ResponseCallback<GenericResponse<NotificationCategoryResponse>> responseCallback =
                    new ResponseCallback<GenericResponse<NotificationCategoryResponse>>() {
                        @Override
                        public void success(GenericResponse<NotificationCategoryResponse>
                                                    notificationCategoryResponse) {
                            DialogUtils.cancelProgressDialog();
                            // TODO: Handle Success
                            NotificationSettingsDataProvider.getInstance()
                                    .setNotificationCategoryResponse(notificationCategoryResponse
                                            .getData());
                            updateNotificationSettingsRecyclerView(NotificationSettingsDataProvider
                                    .getInstance().getNotificationSettingsList(getActivity()));

                        }

                        @Override
                        public void failure(RestError error) {
                            DialogUtils.cancelProgressDialog();
                            // TODO: Handle Failure
                        }
                    };
            NetworkAdapter.get(getActivity()).getNotificationCatgeories(deviceId, responseCallback);
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    private void updateNotificationSettingsRecyclerView(ArrayList<INotificationSettingsRecyclerViewItem>
                                                                notificationSettingsList) {
        mDataSet.addAll(notificationSettingsList);
        mAdapter.notifyDataSetChanged();
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        displaySearch(false);
        setToolbarTitle(getString(R.string.title_notification_settings));
        enableBackButton();
    }

    private void initRecyclerView() {
        mAdapter = new NotificationSettingsRecyclerViewAdapter(mDataSet, this);
        mNotificationSettingsRecyclerView.setHasFixedSize(true);
        mNotificationSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotificationSettingsRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onAllNotificationSelection(boolean isChecked) {

        if (!PreferenceManager.isSubscribedToNotifications(getActivity()) && isChecked) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_DENIED) {
                subscribeForNotifications();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        READ_PHONE_STATE_PERMISSION);
            }
        } else {
            PreferenceManager.storeShowNotificationsEnableStatus(getActivity(), isChecked);
            setRecyclerItems();
        }
    }

    @Override
    public void onDoNotDisturbSelection(boolean isChecked) {
        PreferenceManager.storeIsNotificationDNDEnabled(getActivity(), isChecked);
        NotificationSettings notificationSettingsDND = null;
        for (INotificationSettingsRecyclerViewItem item : mDataSet) {
            NotificationSettings notificationSettings = (NotificationSettings) item;
            if (notificationSettings.getNotificationTopic()
                    .getTitle().equals(Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB)) {
                notificationSettingsDND = notificationSettings;
                break;
            }
        }
        if (null != notificationSettingsDND) {
            notificationSettingsDND.getNotificationTopic()
                    .setSubscribed(PreferenceManager.isNotificationDNDEnabled(getActivity()));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNotificationCategorySelection(NotificationSettings notificationSettings, boolean isChecked, int position) {
        notificationSettings.getNotificationTopic().setSubscribed(isChecked);
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onNotificationSoundSelection(boolean isChecked) {
        PreferenceManager.setNotificationSoundSetting(getActivity(), isChecked);
    }


    public SendNotificationSubscriptionModel getNotificationSubscriptions() {
        String deviceId = PreferenceManager.getGCMDeviceId(getActivity());
        ArrayList<String> subscriptionList = new ArrayList<>();
        for (INotificationSettingsRecyclerViewItem item : mDataSet) {
            if (item instanceof NotificationSettings) {
                NotificationSettings notificationSettings = (NotificationSettings) item;
                NotificationTopic notificationTopic = notificationSettings.getNotificationTopic();
                if (!notificationTopic.getTitle().equalsIgnoreCase(Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB) &&
                        !notificationTopic.getTitle().equalsIgnoreCase(Constants.SETTINGS_NOTIFICATION)) {
                    if (notificationTopic.isSubscribed()) {
                        subscriptionList.add(notificationTopic.get_id());
                    }
                }
            }
        }
        SendNotificationSubscriptionModel sendNotificationSubscriptionModel = new SendNotificationSubscriptionModel();
        sendNotificationSubscriptionModel.setDeviceId(deviceId);
        sendNotificationSubscriptionModel.setSubscribedTopics(subscriptionList);
        sendNotificationSubscriptionModel.setDnd(PreferenceManager.isNotificationDNDEnabled(getActivity()));
        return sendNotificationSubscriptionModel;
    }

    @Override
    public void onStop() {
        //saveNotificationsSubscriptions();
        super.onStop();
    }


    public void saveNotificationsSubscriptions() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            String deviceId = PreferenceManager.getGCMDeviceId(getActivity());
            ArrayList<String> subscriptionList = new ArrayList<>();
            for (INotificationSettingsRecyclerViewItem item : mDataSet) {
                if (item instanceof NotificationSettings) {
                    NotificationSettings notificationSettings = (NotificationSettings) item;
                    NotificationTopic notificationTopic = notificationSettings.getNotificationTopic();
                    if (!notificationTopic.getTitle().equalsIgnoreCase(Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB) &&
                            !notificationTopic.getTitle().equalsIgnoreCase(Constants.SETTINGS_NOTIFICATION)) {
                        if (notificationTopic.isSubscribed()) {
                            subscriptionList.add(notificationTopic.get_id());
                        }
                    }
                }
            }
            SendNotificationSubscriptionModel sendNotificationSubscriptionModel = new SendNotificationSubscriptionModel();
            sendNotificationSubscriptionModel.setDeviceId(deviceId);
            sendNotificationSubscriptionModel.setSubscribedTopics(subscriptionList);
            sendNotificationSubscriptionModel.setDnd(PreferenceManager.isNotificationDNDEnabled(getActivity()));

            ResponseCallback<GenericResponse<SendNotificationSubscriptionResponseModel>>
                    responseCallback = new ResponseCallback<GenericResponse<SendNotificationSubscriptionResponseModel>>() {
                @Override
                public void success(GenericResponse<SendNotificationSubscriptionResponseModel> sendNotificationSubscriptionResponseModelGenericResponse) {
                    DialogUtils.cancelProgressDialog();
                    LogUtils.LOGD(Constants.APP_TAG, "Notification Settings saved successfully");
                    getActivity().onBackPressed();
                }

                @Override
                public void failure(RestError error) {
                    DialogUtils.cancelProgressDialog();
                    LogUtils.LOGD(Constants.APP_TAG, "Notification Settings save failure");
                    getActivity().onBackPressed();
                }
            };
            NetworkAdapter.get(getActivity()).sendNotificationSubscriptions(sendNotificationSubscriptionModel, responseCallback);
        }
    }

    public void subscribeForNotifications() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity()) && !PreferenceManager.isSubscribedToNotifications(getActivity())) {
            String deviceId;
            String gcmToken = PreferenceManager.getGCMToken(getActivity());

            TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();

            PreferenceManager.storeGCMDeviceId(getActivity(), deviceId);


            if (null != gcmToken && null != deviceId) {
                DialogUtils.showCustomProgressDialog(getActivity(), true);
                NotificationSubscriptionModel notificationSubscriptionModel = new NotificationSubscriptionModel();
                notificationSubscriptionModel.setDeviceId(deviceId);
                notificationSubscriptionModel.setDeviceToken(gcmToken);
                notificationSubscriptionModel.setPlatform(Constants.PLATFORM_ANDROID);

                ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>
                        responseCallback = new ResponseCallback<GenericResponse<NotificationSubscriptionResponse>>() {
                    @Override
                    public void success(GenericResponse<NotificationSubscriptionResponse> notificationSubscriptionResponseGenericResponse) {
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration successful");
                        DialogUtils.cancelProgressDialog();
                        PreferenceManager.storeIsSubscribedToNotifications(getActivity(), true);
                        PreferenceManager.storeShowNotificationsEnableStatus(getActivity(), true);
                        setRecyclerItems();
                    }

                    @Override
                    public void failure(RestError error) {
                        DialogUtils.cancelProgressDialog();
                        LogUtils.LOGD(Constants.APP_TAG, "GCM registration failure");
                        PreferenceManager.storeIsSubscribedToNotifications(getActivity(), false);
                        setRecyclerItems();
                    }
                };
                NetworkAdapter.get(getActivity()).subscribeForNotifications(notificationSubscriptionModel, responseCallback);

            }

        }
    }

}
