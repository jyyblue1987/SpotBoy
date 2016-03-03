/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

public class OfflineSectionModel {

    FeaturedArticle featuredArticle;
    ArrayList<GridRecentArticle> gridRecentArticles = new ArrayList<>();
    ArrayList<HomeRecentArticle> homeRecentArticles = new ArrayList<>();

    public void addHomeRecentArticle(HomeRecentArticle homeRecentArticle) {
        this.homeRecentArticles.add(homeRecentArticle);
    }

    public ArrayList<HomeRecentArticle> getHomeRecentArticles() {
        return homeRecentArticles;
    }

    public void setGridRecentArticles(ArrayList<GridRecentArticle> gridRecentArticles) {
        this.gridRecentArticles = gridRecentArticles;
    }

    public FeaturedArticle getFeaturedArticle() {
        return featuredArticle;
    }

    public void setFeaturedArticle(FeaturedArticle featuredArticle) {
        this.featuredArticle = featuredArticle;
    }

    public void addGridRecentArticle(GridRecentArticle gridRecentArticle) {
        gridRecentArticles.add(gridRecentArticle);
    }

    public ArrayList<GridRecentArticle> getGridRecentArticles() {
        return gridRecentArticles;
    }


}
