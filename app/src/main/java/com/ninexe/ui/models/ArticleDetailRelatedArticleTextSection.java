/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 16/10/15.
 */
public class ArticleDetailRelatedArticleTextSection implements IArticleDetailRecyclerViewItem {

    String relatedArticleText;

    public void setRelatedArticleText(String relatedArticleText) {
        this.relatedArticleText = relatedArticleText;
    }

    public String getRelatedArticleText() {
        return relatedArticleText;
    }


    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_RELATED_ARTICLE_TEXT_SECTION;
    }
}
