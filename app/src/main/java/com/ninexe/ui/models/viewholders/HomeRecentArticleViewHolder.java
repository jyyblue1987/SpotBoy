/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.HomeRecentArticle;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 18/10/15.
 */
public class HomeRecentArticleViewHolder extends ArticleViewHolder {

    private IArticleViewHolderClicks listener;
    private HomeRecentArticle homeRecentArticle;
    private Context context;

    public HomeRecentArticleViewHolder(View itemView, IArticleViewHolderClicks articleViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        listener = articleViewHolderClicks;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindRecentArticle(ITabSectionRecyclerViewItem article) {
        if (article instanceof HomeRecentArticle) {
            homeRecentArticle = (HomeRecentArticle) article;
            String imageType = Constants.IMAGE_SMALL;
            Glide.with(context)
                    .load(ViewUtils.getThumbnail(homeRecentArticle.getArticle().getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_home_searchresult)
                    .centerCrop()
                    .crossFade()
                    .into(thumbnail);
            ViewUtils.setText(title, homeRecentArticle.getArticle().getTitle());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(homeRecentArticle.getArticle().getPublishedAt()));
        }
    }

    @OnClick(R.id.recent_article_container)
    void onHomeRecentArticleClick() {
        listener.onArticleClick(homeRecentArticle.getArticle());
    }


}
