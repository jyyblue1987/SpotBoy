/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.INotificationHubRecyclerViewItem;
import com.ninexe.ui.models.NotificationCategoryResponse;
import com.ninexe.ui.models.NotificationHub;
import com.ninexe.ui.models.NotificationTopic;

import java.util.ArrayList;

/**
 * Created by nagesh on 10/11/15.
 */
public class NotificationHubDataProvider {
    private static final NotificationHubDataProvider NOTIFICATION_HUB_DATA_PROVIDER
            = new NotificationHubDataProvider();

    private NotificationCategoryResponse notificationCategoryResponse;
    private ArticleResponse articleResponse;

    private NotificationHubDataProvider() {

    }

    public static NotificationHubDataProvider getInstance() {
        return NOTIFICATION_HUB_DATA_PROVIDER;
    }

    public void setNotificationCategoryResponse(NotificationCategoryResponse notificationCategoryResponse) {
        this.notificationCategoryResponse = notificationCategoryResponse;
    }

    public NotificationCategoryResponse getNotificationCategoryResponse() {
        return notificationCategoryResponse;
    }

    public void setArticleResponse(ArticleResponse articleResponse) {
        this.articleResponse = articleResponse;
    }

    public ArticleResponse getArticleResponse() {
        return articleResponse;
    }

    public ArrayList<NotificationTopic> getNotificationTopics(){
        ArrayList<NotificationTopic> notificationTopics = new ArrayList<>();
        for (NotificationTopic notificationTopic :
                getInstance().getNotificationCategoryResponse().getTopics()) {
            if (notificationTopic.isSubscribed()) {
                notificationTopics.add(notificationTopic);
            }
        }
        return notificationTopics;
    }

    public ArrayList<INotificationHubRecyclerViewItem> getNotificationHubSectionList() {
        ArrayList<INotificationHubRecyclerViewItem> notificationHubRecyclerViewItems
                = new ArrayList<>();
        if (null != getInstance().getArticleResponse()) {
            for (Article article : getInstance().getArticleResponse().getArticles()) {
                NotificationHub notificationHub = new NotificationHub();
                notificationHub.setArticle(article);
                notificationHubRecyclerViewItems.add(notificationHub);
            }
        }
        return notificationHubRecyclerViewItems;
    }

}
