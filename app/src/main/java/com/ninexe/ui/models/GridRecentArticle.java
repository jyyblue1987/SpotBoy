/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 16/10/15.
 */
public class GridRecentArticle implements ITabSectionRecyclerViewItem {
    Article article;

    public GridRecentArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }


    @Override
    public int getItemType() {
        return ITabSectionRecyclerViewItem.TYPE_GRID_RECENT_ARTICLE;
    }

}
