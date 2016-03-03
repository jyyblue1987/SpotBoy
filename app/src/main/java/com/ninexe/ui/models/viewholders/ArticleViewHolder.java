/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.models.Article;

import butterknife.Bind;

/**
 * Created by nagesh on 8/10/15.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.thumbnail)
    ImageView thumbnail;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.published_at)
    TextView publishedAt;


    public ArticleViewHolder(View itemView) {
        super(itemView);
    }

    public interface IArticleViewHolderClicks {
        void onArticleClick(Article article);
    }
}
