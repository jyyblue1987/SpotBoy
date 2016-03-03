/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 7/10/15.
 */
public interface IArticleDetailRecyclerViewItem extends IRecyclerViewItem {
    int TYPE_VIDEO_SECTION = 0;
    int TYPE_ARTICLE_CONTENT_SECTION = 1;
    int TYPE_COMMENT_SECTION = 2;
    int TYPE_RELATED_ARTICLE_TEXT_SECTION = 3;
    int TYPE_RELATED_ARTICLE_SECTION = 4;
    int TYPE_REACTOMETER_SECTION = 5;
}
