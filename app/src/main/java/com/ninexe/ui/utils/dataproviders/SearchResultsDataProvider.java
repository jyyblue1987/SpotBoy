/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.SearchResultArticle;

import java.util.ArrayList;

/**
 * Created by nagesh on 26/10/15.
 */
public class SearchResultsDataProvider {

    private static final SearchResultsDataProvider SEARCH_RESULTS_DATA_PROVIDER
            = new SearchResultsDataProvider();

    private ArticleResponse articleResponse;

    private SearchResultsDataProvider() {

    }

    public void setArticleResponse(ArticleResponse articleResponse) {
        this.articleResponse = articleResponse;
    }

    public ArticleResponse getArticleResponse() {
        return articleResponse;
    }


    public static SearchResultsDataProvider getInstance() {
        return SEARCH_RESULTS_DATA_PROVIDER;
    }


    public ArrayList<ITabSectionRecyclerViewItem> getSearchResultsArticles() {
        ArrayList<ITabSectionRecyclerViewItem> searchResultsArticleList
                = new ArrayList<>();

        if (null != articleResponse.getArticles() && !articleResponse.getArticles().isEmpty()) {
            for (Article article : articleResponse.getArticles()) {
                SearchResultArticle searchResultArticle = new SearchResultArticle();
                searchResultArticle.setArticle(article);
                searchResultsArticleList.add(searchResultArticle);
            }
        }
        return searchResultsArticleList;
    }

}
