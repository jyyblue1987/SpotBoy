/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 19/10/15.
 */
public class ArticleResponse {
    ArticleMeta meta;
    ArrayList<Article> data;

    public ArticleMeta getMeta() {
        return meta;
    }

    public ArrayList<Article> getArticles() {
        return data;
    }
}
