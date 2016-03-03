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
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.INotificationHubRecyclerViewItem;
import com.ninexe.ui.models.NotificationHub;
import com.ninexe.ui.models.viewholders.NotificationHubViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 12/10/15.
 */
public class NotificationHubRecyclerViewAdapter extends RecyclerView.Adapter<NotificationHubViewHolder> {

    private ArrayList<INotificationHubRecyclerViewItem> mDataSet;
    private INotificationHubAdapterListener notificationHolderClickListener;

    public NotificationHubRecyclerViewAdapter
            (ArrayList<INotificationHubRecyclerViewItem> notificationHubRecyclerViewItems, INotificationHubAdapterListener listener) {
        mDataSet = notificationHubRecyclerViewItems;
        notificationHolderClickListener = listener;
    }


    @Override
    public NotificationHubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_hub, parent, false);
        return new NotificationHubViewHolder(view, new NotificationHubViewHolder.INotificationHolderClicks() {
            @Override
            public void onArticleClick(Article article) {
                notificationHolderClickListener.onArticleClick(article);
            }
        });
    }

    @Override
    public void onBindViewHolder(NotificationHubViewHolder holder, int position) {
        holder.bindNotificationHub((NotificationHub) mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface INotificationHubAdapterListener {
        void onArticleClick(Article article);
    }
}
