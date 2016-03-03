/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.NotificationHub;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationHubViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.published_at)
    TextView publishedAt;

    @Bind(R.id.notification_hub_container)
    View notificationHubContainer;

    private Context context;
    private NotificationHub notificationHub;
    private INotificationHolderClicks notificationHolderClickListener;

    public NotificationHubViewHolder(View itemView, INotificationHolderClicks listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        notificationHolderClickListener = listener;
    }

    public void bindNotificationHub(NotificationHub notificationHubArg) {
        notificationHub = notificationHubArg;
        ViewUtils.setText(title, notificationHub.getArticle().getTitle());
        ViewUtils.setText(publishedAt, DateTimeUtils.getDate(notificationHub.getArticle().getPublishedAt()));

        if (getAdapterPosition() % 2 == 0) {
            notificationHubContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_light_grey));
        } else {
            notificationHubContainer.setBackgroundColor(context.getResources().getColor(R.color.settings_dark_grey));
        }
    }

    @OnClick(R.id.notification_hub_container)
    void onNotificationClick(){
        notificationHolderClickListener.onArticleClick(notificationHub.getArticle());
    }

    public interface INotificationHolderClicks {
        void onArticleClick(Article article);
    }
}
