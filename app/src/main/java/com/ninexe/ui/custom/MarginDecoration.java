/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ninexe.ui.R;
import com.ninexe.ui.models.viewholders.ArticleDetailRelatedArticleSectionViewHolder;
import com.ninexe.ui.models.viewholders.GridRecentArticleViewHolder;


public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.padding_10);
    }

    public boolean isDecorated(View view, RecyclerView parent) {
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        return holder instanceof ArticleDetailRelatedArticleSectionViewHolder
                || holder instanceof GridRecentArticleViewHolder;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (isDecorated(view, parent)) {
            outRect.set(margin, margin, margin, margin);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}