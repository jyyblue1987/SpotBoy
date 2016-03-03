/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Settings;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.Theme;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 12/10/15.
 */
public class SettingsViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.settings_item_container)
    View settingsContainer;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.btn_image)
    ImageButton arrowIcon;

    @Bind(R.id.btn_toggle)
    ToggleButton toggleButton;

    private Context context;
    private Settings settings;
    private SettingsViewHolderClicks listener;

    public SettingsViewHolder(View itemView, SettingsViewHolderClicks settingsViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        listener = settingsViewHolderClicks;
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switch (settings.getTitle()) {
                    case Constants.SETTINGS_THEME:
                        listener.onThemeToggleSelection(isChecked);
                        break;
                    case Constants.SETTINGS_OFFLINE_CACHE:
                        listener.onOfflineCachingSelection(isChecked);
                        break;
                }

            }
        });
    }

    public void bindSettings(Settings settingsArg) {
        settings = settingsArg;
        title.setText(settings.getTitle());

        switch (settings.getTitle()) {
            case Constants.SETTINGS_THEME:
                setThemeToggleButtonStatus();
                break;
            case Constants.SETTINGS_OFFLINE_CACHE:
                setOfflineToggleButtonStatus();
                break;
        }

        if (settings.hasToggle()) {
            arrowIcon.setVisibility(View.GONE);
            toggleButton.setVisibility(View.VISIBLE);
            int paddingTop = context.getResources().getDimensionPixelOffset(R.dimen.padding_15);
            int paddingLeft = context.getResources().getDimensionPixelOffset(R.dimen.padding_15);

            switch (settings.getTitle()) {
                case Constants.SETTINGS_THEME:
                    toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_theme_toggle));
                    settingsContainer.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                    break;
                case Constants.SETTINGS_OFFLINE_CACHE:
                    toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
                    settingsContainer.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
                    break;
            }
        }

        if (settings.hasFwdArrow()) {
            toggleButton.setVisibility(View.GONE);
            arrowIcon.setVisibility(View.VISIBLE);
        }

        if (getAdapterPosition() % 2 == 0) {
            settingsContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_light_grey));
        } else {
            settingsContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
        }
    }

    private void setOfflineToggleButtonStatus() {
        if (OfflineArticleDataHandler.isOfflineCacheEnabled(context)) {
            toggleButton.setChecked(true);
        } else {
            toggleButton.setChecked(false);
        }
    }

    private void setThemeToggleButtonStatus() {
        final Theme currentTheme = PreferenceManager.getCurrentTheme(context);
        switch (currentTheme) {
            case Blue:
                toggleButton.setChecked(false);
                break;
            case Red:
                toggleButton.setChecked(true);
                break;

        }
    }

    @OnClick(R.id.settings_item_container)
    void OnSettingsItemClick() {
        switch (settings.getTitle()) {
            case Constants.SETTINGS_NOTIFICATION:
                listener.onNotificationsClick();
                break;
            case Constants.SETTINGS_TEXT_SIZE:
                listener.onTextSizeClick();
                break;
            case Constants.SETTINGS_FEEDBACK:
                listener.onFeedbackClick();
                break;
            case Constants.SETTINGS_RATE_REVIEW:
                listener.onRateAndReviewClick();
                break;
            case Constants.SETTINGS_TERMS_CONDITIONS:
                listener.onTermsAndConditionsClick();
                break;
            case Constants.SETTINGS_PRIVACY_POLICY:
                listener.onPrivacyPolicyClick();
                break;
            case Constants.SETTINGS_ABOUT_US:
                listener.onAboutUsClick();
                break;
            case Constants.SETTINGS_CONTACT_US:
                listener.onContactUsClick();
                break;
            case Constants.SETTINGS_DISCLAIMER:
                listener.onDisclaimerClick();
                break;
        }
    }

    public interface SettingsViewHolderClicks {
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
