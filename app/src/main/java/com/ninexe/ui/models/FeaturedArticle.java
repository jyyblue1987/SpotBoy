/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;

/**
 * Created by nagesh on 7/10/15.
 */
public class FeaturedArticle extends Article implements ITabSectionRecyclerViewItem {

    protected FeaturedArticle(Parcel in) {
        super(in);
    }

    public FeaturedArticle(Article article) {
        this._id = article.getId();
        this.title = article.getTitle();
        this.thumbnail = article.getThumbnail();
        this.publishedAt = article.getPublishedAt();
        this.shortBody = article.getShortBody();
        this.type = article.getType();
        this.views = article.getViews();
        this.authorName = article.getAuthorName();
    }

    @Override
    public int getItemType() {
        return ITabSectionRecyclerViewItem.TYPE_FEATURED_ARTICLE;
    }
}
