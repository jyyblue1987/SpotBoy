/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleDetailRelatedArticleSection;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailRelatedArticleSectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.related_article_thumbnail)
    ImageView relatedArticleThumbnail;

    @Bind(R.id.related_article_title)
    TextView relatedArticleTitle;

    @Bind(R.id.related_article_published_at)
    TextView relatedArticlePublishedAt;

    private Context context;
    private IRelatedArticleViewHolderClicks listener;
    private Article article;

    public ArticleDetailRelatedArticleSectionViewHolder(View itemView,
                                                        IRelatedArticleViewHolderClicks relatedArticleViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        listener = relatedArticleViewHolderClicks;
    }

    @OnClick(R.id.related_article_container)
    void onRelatedArticleSelection() {
        LogUtils.LOGD(Constants.APP_TAG, "Clicked on Related Article");
        listener.onRelatedArticleSelection(article);

    }

    public void bindRelatedArticleSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ArticleDetailRelatedArticleSection) {
            article = ((ArticleDetailRelatedArticleSection) articleDetailRecyclerViewItem).getArticle();
            String imageType = Constants.IMAGE_SMALL;

            Glide.with(context)
                    .load(ViewUtils.getThumbnail(article.getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_home_searchresult)
                    .centerCrop()
                    .crossFade()
                    .into(relatedArticleThumbnail);
            ViewUtils.setText(relatedArticleTitle, article.getTitle());
            ViewUtils.setText(relatedArticlePublishedAt, DateTimeUtils.getDate(article.getPublishedAt()));
        }
    }

    public interface IRelatedArticleViewHolderClicks {
        void onRelatedArticleSelection(Article article);
    }

}
