/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 10/11/15.
 */
public class HomeRecentArticle implements ITabSectionRecyclerViewItem {
    Article article;

    public HomeRecentArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }


    @Override
    public int getItemType() {
        return ITabSectionRecyclerViewItem.TYPE_HOME_RECENT_ARTICLE;
    }
}
