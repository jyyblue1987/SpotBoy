/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GridRecentArticle;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DrawableUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 18/10/15.
 */
public class GridRecentArticleViewHolder extends ArticleViewHolder {

    @Bind(R.id.icon)
    ImageView icon;

    @Bind(R.id.views)
    TextView views;

    private IArticleViewHolderClicks listener;
    private GridRecentArticle gridRecentArticle;
    private Context context;

    public GridRecentArticleViewHolder(View itemView, IArticleViewHolderClicks articleViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        listener = articleViewHolderClicks;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindRecentArticle(ITabSectionRecyclerViewItem article) {
        if (article instanceof GridRecentArticle) {
            gridRecentArticle = (GridRecentArticle) article;
            String imageType = Constants.IMAGE_SMALL;

            Glide.with(context)
                    .load(ViewUtils.getThumbnail(gridRecentArticle.getArticle().getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_home_searchresult)
                    .centerCrop()
                    .crossFade()
                    .into(thumbnail);
            if (-1 != DrawableUtils.getGridRecentArticleDrawableResource(gridRecentArticle.getArticle().getType())) {
                icon.setImageResource(DrawableUtils.getGridRecentArticleDrawableResource(gridRecentArticle.getArticle().getType()));
            } else {
                icon.setImageDrawable(null);
            }
            ViewUtils.setText(title, gridRecentArticle.getArticle().getTitle());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(gridRecentArticle.getArticle().getPublishedAt()));
            views.setText(String.valueOf(gridRecentArticle.getArticle().getViews()));
        }
    }

    @OnClick(R.id.grid_recent_article_container)
    void onGridRecentArticleClick() {
        listener.onArticleClick(gridRecentArticle.getArticle());
    }

}
