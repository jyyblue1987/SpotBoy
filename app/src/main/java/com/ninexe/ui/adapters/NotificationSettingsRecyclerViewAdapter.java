/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.models.INotificationSettingsRecyclerViewItem;
import com.ninexe.ui.models.NotificationSettings;
import com.ninexe.ui.models.NotificationSettingsCategoryTextViewHolder;
import com.ninexe.ui.models.viewholders.NotificationSettingsViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 12/10/15.
 */
public class NotificationSettingsRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<INotificationSettingsRecyclerViewItem> mDataSet;
    private INotificationSettingsRecyclerViewAdapterListener mListener;

    public NotificationSettingsRecyclerViewAdapter(
            ArrayList<INotificationSettingsRecyclerViewItem> dataSet,
            INotificationSettingsRecyclerViewAdapterListener listener) {
        mDataSet = dataSet;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == INotificationSettingsRecyclerViewItem.TYPE_NOTIFICATION_SETTINGS) {
            view = inflater.inflate(R.layout.item_notification_setttings, parent, false);
            return getNotificationSettingsViewHolder(view);
        } else if (viewType == INotificationSettingsRecyclerViewItem.TYPE_NOTIFICATION_CATEGORY_TEXT) {
            view = inflater.inflate(R.layout.item_notification_settings_category, parent, false);
            return getNotificationSettingsCategoryViewHoldr(view);
        }
        throw new RuntimeException("there is no type that matches the type "
                + viewType + " make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NotificationSettingsViewHolder) {
            ((NotificationSettingsViewHolder) holder).bindNotificationSetting((NotificationSettings) mDataSet.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getItemType();
    }

    private RecyclerView.ViewHolder getNotificationSettingsViewHolder(View view) {
        return new NotificationSettingsViewHolder(view,
                new NotificationSettingsViewHolder.INotificationSettingsViewHolderClicks() {
                    @Override
                    public void onAllNotificationSelection(boolean isChecked) {
                        mListener.onAllNotificationSelection(isChecked);
                    }

                    @Override
                    public void onDoNotDisturbSelection(boolean isChecked) {
                        mListener.onDoNotDisturbSelection(isChecked);
                    }

                    @Override
                    public void onNotificationSoundSelection(boolean isChecked) {
                        mListener.onNotificationSoundSelection(isChecked);
                    }

                    @Override
                    public void onNotificationCategorySelection(NotificationSettings notificationSettings,
                                                                boolean isChecked, int position) {
                        mListener.onNotificationCategorySelection(notificationSettings, isChecked, position);
                    }

                });
    }

    private RecyclerView.ViewHolder getNotificationSettingsCategoryViewHoldr(View view) {
        return new NotificationSettingsCategoryTextViewHolder(view);
    }

    public interface INotificationSettingsRecyclerViewAdapterListener {
        void onAllNotificationSelection(boolean isChecked);

        void onDoNotDisturbSelection(boolean isChecked);

        void onNotificationCategorySelection(NotificationSettings notificationSettings, boolean isChecked, int position);

        void onNotificationSoundSelection(boolean isChecked);
    }
}
