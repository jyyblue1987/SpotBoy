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
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.GridRecentArticle;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.SearchResultArticle;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DrawableUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 26/10/15.
 */
public class SearchResultArticleViewHolder extends ArticleViewHolder {

    private IArticleViewHolderClicks listener;
    private SearchResultArticle searchResultArticle;
    private Context context;

    public SearchResultArticleViewHolder(View itemView, IArticleViewHolderClicks articleViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        listener = articleViewHolderClicks;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindRecentArticle(ITabSectionRecyclerViewItem article) {
        if (article instanceof SearchResultArticle) {
            searchResultArticle = (SearchResultArticle) article;
            String imageType = Constants.IMAGE_SMALL;
            Glide.with(context)
                    .load(ViewUtils.getThumbnail(searchResultArticle.getArticle().getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_home_searchresult)
                    .centerCrop()
                    .crossFade()
                    .into(thumbnail);
            ViewUtils.setText(title, searchResultArticle.getArticle().getTitle());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(searchResultArticle.getArticle().getPublishedAt()));
        }
    }

    @OnClick(R.id.recent_article_container)
    void onGridRecentArticleClick() {
        listener.onArticleClick(searchResultArticle.getArticle());
    }

}
