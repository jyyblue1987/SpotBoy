/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.models.ArticleDetailRelatedArticleTextSection;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 16/10/15.
 */
public class ArticleDetailRelatedArticleTextSectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.related_article_text)
    TextView relatedArticleText;

    private ArticleDetailRelatedArticleTextSection relatedArticleTextSection;

    public ArticleDetailRelatedArticleTextSectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindRelatedArticleTextSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ArticleDetailRelatedArticleTextSection) {
            relatedArticleTextSection = (ArticleDetailRelatedArticleTextSection) articleDetailRecyclerViewItem;
            ViewUtils.setText(relatedArticleText, relatedArticleTextSection.getRelatedArticleText());
        }
    }
}
