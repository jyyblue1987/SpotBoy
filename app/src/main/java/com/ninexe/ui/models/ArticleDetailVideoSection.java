/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailVideoSection implements IArticleDetailRecyclerViewItem {
    String thumbnail;
    ArrayList<ArticleMedia> media;

    public ArrayList<ArticleMedia> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<ArticleMedia> media) {
        this.media = media;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_VIDEO_SECTION;
    }
}
