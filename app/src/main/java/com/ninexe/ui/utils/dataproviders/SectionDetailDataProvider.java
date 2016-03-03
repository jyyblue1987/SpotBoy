/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.FeaturedArticle;
import com.ninexe.ui.models.GridRecentArticle;
import com.ninexe.ui.models.HomeRecentArticle;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;

import java.util.ArrayList;

/**
 * Created by nagesh on 19/10/15.
 */
public class SectionDetailDataProvider {

    private static final SectionDetailDataProvider SECTION_DETAIL_DATA_PROVIDER
            = new SectionDetailDataProvider();

    private ArticleResponse articleResponse;


    public SectionDetailDataProvider() {
    }

    public void setArticleResponse(ArticleResponse articleResponse) {
        this.articleResponse = articleResponse;
    }

    public ArticleResponse getArticleResponse() {
        return articleResponse;
    }


    public ArrayList<ITabSectionRecyclerViewItem> getGridRecentArticles() {
        ArrayList<ITabSectionRecyclerViewItem> mGridRecentArticleList
                = new ArrayList<>();

        if (null != articleResponse && !articleResponse.getArticles().isEmpty()) {

            if (1 == articleResponse.getMeta().getPage()) {
                FeaturedArticle featuredArticle = new FeaturedArticle(articleResponse.getArticles().get(0));
                articleResponse.getArticles().remove(0);
                mGridRecentArticleList.add(featuredArticle);
            }


            if (!articleResponse.getArticles().isEmpty()) {
                for (Article article : articleResponse.getArticles()) {
                    GridRecentArticle gridRecentArticle = new GridRecentArticle(article);
                    mGridRecentArticleList.add(gridRecentArticle);
                }
            }

        }
        return mGridRecentArticleList;
    }


    public ArrayList<ITabSectionRecyclerViewItem> getHomeRecentArticles() {
        ArrayList<ITabSectionRecyclerViewItem> mHomeRecentArticleList
                = new ArrayList<>();

        if (null != articleResponse && !articleResponse.getArticles().isEmpty()) {

            if (1 == articleResponse.getMeta().getPage()) {
                FeaturedArticle featuredArticle = new FeaturedArticle(articleResponse.getArticles().get(0));
                articleResponse.getArticles().remove(0);
                mHomeRecentArticleList.add(featuredArticle);
            }


            if (!articleResponse.getArticles().isEmpty()) {
                for (Article article : articleResponse.getArticles()) {
                    HomeRecentArticle homeRecentArticle = new HomeRecentArticle(article);
                    mHomeRecentArticleList.add(homeRecentArticle);
                }
            }

        }
        return mHomeRecentArticleList;
    }
}
