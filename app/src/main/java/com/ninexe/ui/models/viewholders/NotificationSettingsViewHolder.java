/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.NotificationSettings;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.widgets.ProgramaticallyCheckabeToggle;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationSettingsViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.btn_toggle)
    ProgramaticallyCheckabeToggle toggleButton;

    @Bind(R.id.dnd_time)
    TextView dndTime;

    @Bind(R.id.notification_settings_item_container)
    View notificationSettingsItemContainer;

    private INotificationSettingsViewHolderClicks listener;
    private Context context;
    private NotificationSettings notificationSettings;

    public NotificationSettingsViewHolder(View itemView, INotificationSettingsViewHolderClicks
            notificationSettingsViewHolderClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }

        if (null == listener) {
            listener = notificationSettingsViewHolderClickListener;
        }
    }

    public void bindNotificationSetting(NotificationSettings notificationSettingsArg) {
        notificationSettings = notificationSettingsArg;
        ViewUtils.setText(title, notificationSettings.getNotificationTopic().getTitle());

        switch (notificationSettings.getNotificationTopic().getTitle()) {
            case Constants.SETTINGS_NOTIFICATION_SOUND:
                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
                toggleButton.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_width);
                toggleButton.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_height);
                dndTime.setVisibility(View.GONE);
                notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
                break;
            case Constants.SETTINGS_NOTIFICATION:
                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
                toggleButton.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_width);
                toggleButton.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_height);
                dndTime.setVisibility(View.GONE);
                notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
                break;
            case Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB:
                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
                toggleButton.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_width);
                toggleButton.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_height);
                ViewUtils.setText(dndTime, Constants.DNDTIME);
                notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_light_grey));
                break;
            default:
                dndTime.setVisibility(View.GONE);
                notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
                toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_notification_settings_toggle));
                break;
        }

/*        if (notificationSettings.getNotificationTopic().getTitle().equalsIgnoreCase(Constants.SETTINGS_NOTIFICATION)) {
            toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
            toggleButton.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_width);
            toggleButton.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_height);
            notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
        } else if (notificationSettings.getNotificationTopic().getTitle().equalsIgnoreCase(Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB)) {
            toggleButton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selector_offline_caching_toggle));
            toggleButton.getLayoutParams().width = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_width);
            toggleButton.getLayoutParams().height = context.getResources().getDimensionPixelSize(R.dimen.btn_toggle_height);
            notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_light_grey));
        } else {
            notificationSettingsItemContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
        }*/

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                switch (notificationSettings.getNotificationTopic().getTitle()) {
                    case Constants.SETTINGS_NOTIFICATION:
                        listener.onAllNotificationSelection(isChecked);
                        break;
                    case Constants.NOTIFICATION_SETTINGS_DO_NOT_DISTURB:
                        listener.onDoNotDisturbSelection(isChecked);
                        break;
                    case Constants.SETTINGS_NOTIFICATION_SOUND:
                        listener.onNotificationSoundSelection(isChecked);
                        break;
                    default:
                        listener.onNotificationCategorySelection(notificationSettings, isChecked,getAdapterPosition());
                        break;

                }
            }
        });

        toggleButton.setCheckedProgrammatically(notificationSettings.getNotificationTopic().isSubscribed());
    }

    public interface INotificationSettingsViewHolderClicks {
        void onAllNotificationSelection(boolean isChecked);

        void onDoNotDisturbSelection(boolean isChecked);

        void onNotificationSoundSelection(boolean isChecked);

        void onNotificationCategorySelection(NotificationSettings notificationSettings, boolean isChecked,int position);
    }
}
