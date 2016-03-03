/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 2/11/15.
 */
public class ViewMoreCommentsResponse {
    private ArrayList<Comment> data;
    private ArticleMeta meta;

    public ArrayList<Comment> getData() {
        return data;
    }

    public ArticleMeta getMeta() {
        return meta;
    }
}
