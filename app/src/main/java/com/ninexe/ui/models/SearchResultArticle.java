/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 26/10/15.
 */
public class SearchResultArticle implements ITabSectionRecyclerViewItem {
    Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return ITabSectionRecyclerViewItem.TYPE_SEARCH_RESULT_ARTICLE;
    }
}
