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
import com.ninexe.ui.models.Settings;
import com.ninexe.ui.models.viewholders.SettingsViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 12/10/15.
 */
public class SettingsRecyclerViewAdapter extends RecyclerView.Adapter<SettingsViewHolder> {
    private ArrayList<Settings> mDataSet;
    private SettingsRecyclerViewAdapterListener mListener;

    public SettingsRecyclerViewAdapter(ArrayList<Settings> settingsArrayList, SettingsRecyclerViewAdapterListener listener) {
        mDataSet = settingsArrayList;
        mListener = listener;
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_settings, parent, false);
        SettingsViewHolder settingsViewHolder
                = new SettingsViewHolder(view, new SettingsViewHolder.SettingsViewHolderClicks() {
            @Override
            public void onNotificationsClick() {
                mListener.onNotificationsClick();
            }

            @Override
            public void onThemeToggleSelection(boolean isChecked) {
                mListener.onThemeToggleSelection(isChecked);
            }

            @Override
            public void onTextSizeClick() {
                mListener.onTextSizeClick();
            }

            @Override
            public void onOfflineCachingSelection(boolean isChecked) {
                mListener.onOfflineCachingSelection(isChecked);
            }

            @Override
            public void onFeedbackClick() {
                mListener.onFeedbackClick();
            }

            @Override
            public void onRateAndReviewClick() {
                mListener.onRateAndReviewClick();
            }

            @Override
            public void onTermsAndConditionsClick() {
                mListener.onTermsAndConditionsClick();
            }

            @Override
            public void onPrivacyPolicyClick() {
                mListener.onPrivacyPolicyClick();
            }

            @Override
            public void onAboutUsClick() {
                mListener.onAboutUsClick();
            }

            @Override
            public void onContactUsClick() {
                mListener.onContactUsClick();
            }

            @Override
            public void onDisclaimerClick() {
                mListener.onDisclaimerClick();
            }
        });
        return settingsViewHolder;
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        Settings settings = mDataSet.get(position);
        holder.bindSettings(settings);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


    public interface SettingsRecyclerViewAdapterListener {
        void onNotificationsClick();

        void onThemeToggleSelection(boolean isChecked);

        void onTextSizeClick();

        void onOfflineCachingSelection(boolean isChecked);

        void onFeedbackClick();

        void onRateAndReviewClick();

        void onTermsAndConditionsClick();

        void onPrivacyPolicyClick();

        void onAboutUsClick();

        void onContactUsClick();

        void onDisclaimerClick();
    }
}
