/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 7/10/15.
 */
public interface ITabSectionRecyclerViewItem extends IRecyclerViewItem {
    int TYPE_FEATURED_ARTICLE = 0;
    int TYPE_RECENT_ARTICLE = 1;
    int TYPE_GRID_RECENT_ARTICLE = 2;
    int TYPE_SEARCH_RESULT_ARTICLE = 3;
    int TYPE_HOME_RECENT_ARTICLE = 4;
}
