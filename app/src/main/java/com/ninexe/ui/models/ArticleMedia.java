/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 21/10/15.
 */
public class ArticleMedia {
    String _id;
    String file;
    String title;
    String description;
    String url;
    String vast;
    String type;

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public String getVast() {
        return vast;
    }

    public String getFile() {
        return file;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
