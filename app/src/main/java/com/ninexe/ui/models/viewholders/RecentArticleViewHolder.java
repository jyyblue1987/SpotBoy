/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.RecentArticle;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 7/10/15.
 */
public class RecentArticleViewHolder extends ArticleViewHolder {

    private Context context;
    private RecentArticle recentArticle;
    private IArticleViewHolderClicks mListener;


    public RecentArticleViewHolder(View itemView, IArticleViewHolderClicks listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindRecentArticle(ITabSectionRecyclerViewItem article) {
        if (article instanceof RecentArticle) {
            recentArticle = (RecentArticle) article;
            Glide.with(context)
                    .load(recentArticle.getThumbnail())
                    .placeholder(R.drawable.placeholder_home_searchresult)
                    .centerCrop()
                    .crossFade()
                    .into(thumbnail);
            ViewUtils.setText(title, recentArticle.getTitle());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(recentArticle.getPublishedAt()));
        }
    }

    @OnClick(R.id.recent_article_container)
    void onRecentArticleClick() {
        mListener.onArticleClick(recentArticle);
    }

}
