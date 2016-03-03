/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ninexe.ui.R;
import com.ninexe.ui.custom.TouchImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 14/11/15.
 */
public class PhotoGalleryFullScreenContentFragment extends BaseFragment {

    private static String ARG_IMAGE_URL = "arg_image_url";
    private String mImageUrl;
    private OnZoomListener mListener;

    @Bind(R.id.photo)
    TouchImageView mPhoto;

    public static PhotoGalleryFullScreenContentFragment newInstance(String imageUrl) {
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        PhotoGalleryFullScreenContentFragment fragment = new PhotoGalleryFullScreenContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            setImageUrl(getArguments().getString(ARG_IMAGE_URL));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (null != mPhoto)
                if (mPhoto.isZoomed())
                    mPhoto.resetZoom();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_photo_gallery_photo, container, false);
        ButterKnife.bind(this, view);
        mPhoto.setEnabled(false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(getActivity())
                .load(getImageUrl())
                .placeholder(R.drawable.placeholder_featured_images)
                .dontAnimate()
                .into(mPhoto);


        mPhoto.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mListener.isImageZoomed(mPhoto.isZoomed());
                return false;
            }
        });

        mPhoto.setOnGenericMotionListener(new View.OnGenericMotionListener() {
            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                return false;
            }
        });
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public boolean isImageZoomed() {
        return mPhoto.isZoomed();
    }


    public interface OnZoomListener {
        void isImageZoomed(boolean value);
    }

    public void setImageZoomListener(OnZoomListener onZoomListener) {
        mListener = onZoomListener;
    }

    public void zoomOutImage() {
        mPhoto.resetZoom();
    }
}
