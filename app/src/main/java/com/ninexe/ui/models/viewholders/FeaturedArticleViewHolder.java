/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.FeaturedArticle;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DeviceUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 7/10/15.
 */
public class FeaturedArticleViewHolder extends ArticleViewHolder {

    @Bind(R.id.short_body)
    TextView shortBody;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.views)
    TextView views;

    private FeaturedArticle featuredArticle;
    private Context context;
    private IArticleViewHolderClicks mListener;

    public FeaturedArticleViewHolder(View itemView, IArticleViewHolderClicks listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mListener = listener;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindFeaturedArticle(ITabSectionRecyclerViewItem article) {
        if (article instanceof FeaturedArticle) {
            featuredArticle = (FeaturedArticle) article;
            String imageType;
            if (context.getResources().getBoolean(R.bool.isTablet)) {
                imageType = Constants.IMAGE_THUMBNAIL;
            } else {
                imageType = Constants.IMAGE_SMALL;
            }

            Glide.with(context)
                    .load(ViewUtils.getThumbnail(featuredArticle.getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_featured_images)
                    .centerCrop()
                    .crossFade()
                    .into(thumbnail);
            ViewUtils.setText(title, featuredArticle.getTitle());
            ViewUtils.setText(shortBody, featuredArticle.getShortBody());
            if (TextUtils.isEmpty(featuredArticle.getAuthorName())) {
                ViewUtils.hideView(author);
            }
            ViewUtils.setText(author, featuredArticle.getAuthorName());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(featuredArticle.getPublishedAt()));
            views.setText(String.valueOf(ViewUtils.getNumberWithSuffix(featuredArticle.getViews())));
        }
    }

    @OnClick(R.id.featured_article_container)
    void onFeaturedArticleClick() {
        mListener.onArticleClick(featuredArticle);
    }
}
