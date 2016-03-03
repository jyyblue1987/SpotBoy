/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;

/**
 * Created by nagesh on 7/10/15.
 */
public class RecentArticle extends Article implements ITabSectionRecyclerViewItem {

    protected RecentArticle(Parcel in) {
        super(in);
    }

    @Override
    public int getItemType() {
        return ITabSectionRecyclerViewItem.TYPE_RECENT_ARTICLE;
    }


}
