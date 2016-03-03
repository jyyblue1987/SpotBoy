/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailRelatedArticleSection implements IArticleDetailRecyclerViewItem {

    Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_RELATED_ARTICLE_SECTION;
    }
}
