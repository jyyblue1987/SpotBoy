/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationHub implements INotificationHubRecyclerViewItem {
    private Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return INotificationHubRecyclerViewItem.TYPE_NOTIFICATION_HUB;
    }
}
