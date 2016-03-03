/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.ArticleDetailData;
import com.ninexe.ui.models.ArticleMedia;

import java.util.ArrayList;

/**
 * Created by nagesh on 21/10/15.
 */
public class PhotoGalleryDataProvider {

    private static final PhotoGalleryDataProvider PHOTO_GALLERY_DATA_PROVIDER
            = new PhotoGalleryDataProvider();

    public static PhotoGalleryDataProvider getInstance() {
        return PHOTO_GALLERY_DATA_PROVIDER;
    }

    public ArrayList<ArticleMedia> getMediaList() {
        return ArticleDetailDataProvider.getArticleDetailData().getMedia();
    }

    public ArticleDetailData getArticleData(){
        return ArticleDetailDataProvider.getArticleDetailData();
    }
}
