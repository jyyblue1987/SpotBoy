/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.models.ArticleMedia;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 21/10/15.
 */
public class PhotoGalleryPagerAdapter extends PagerAdapter {
    private ArrayList<ArticleMedia> mMediaArrayList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private PhotoGalleryPagerAdapterInteractionListener mListener;

    @Bind(R.id.photo)
    ImageView photo;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.title_container)
    View titleContainer;

    @Bind(R.id.photo_gallery_photo_container)
    View photoContainer;

    private int mPhotoGalleryPagerAdapterContainerType = -1;

    public interface PhotoGalleryPagerAdapterContainerType {
        int TYPE_PHOTO_GALLERY = 0;
        int TYPE_PHOTO_GALLERY_DETAIL = 1;
    }

    public PhotoGalleryPagerAdapter(Context context, ArrayList<ArticleMedia> mediaArrayList,
                                    PhotoGalleryPagerAdapterInteractionListener listener,
                                    int photoGalleryPagerAdapterContainerType) {
        this.mContext = context;
        this.mMediaArrayList = mediaArrayList;
        mLayoutInflater = LayoutInflater.from(mContext);
        mListener = listener;
        mPhotoGalleryPagerAdapterContainerType = photoGalleryPagerAdapterContainerType;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ViewGroup viewGroup = (ViewGroup) mLayoutInflater.inflate(R.layout.item_photo_gallery_photo, container, false);
        ButterKnife.bind(this, viewGroup);
        ArticleMedia articleMedia = mMediaArrayList.get(position);
        Glide.with(container.getContext())
                .load(articleMedia.getFile())
                .placeholder(R.drawable.placeholder_featured_images)
                .crossFade()
                .into(photo);
        if (mPhotoGalleryPagerAdapterContainerType == PhotoGalleryPagerAdapterContainerType.TYPE_PHOTO_GALLERY) {
            title.setText(articleMedia.getTitle());
        } else if (mPhotoGalleryPagerAdapterContainerType == PhotoGalleryPagerAdapterContainerType.TYPE_PHOTO_GALLERY_DETAIL) {
            titleContainer.setVisibility(View.GONE);
        }
        photoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onPhotoSelection(position);
                }
            }
        });
        container.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public int getCount() {
        return mMediaArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface PhotoGalleryPagerAdapterInteractionListener {
        void onPhotoSelection(int viewPagerCurrentPosition);
    }
}
