/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.events.listeners.VideoPlayerEvents;
import com.longtailvideo.jwplayer.media.source.SingleSource;
import com.longtailvideo.jwplayer.media.source.ad.AdBreak;
import com.longtailvideo.jwplayer.media.source.ad.Advertising;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.ArticleDetailVideoSection;
import com.ninexe.ui.models.ArticleMedia;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 14/10/15.
 */
public class ArticleDetailVideoSectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.article_detail_video_thumbnail)
    ImageView articleDetailVideoThumbnailImage;

    @Bind(R.id.jwplayer)
    JWPlayerView mPlayerView;

    private IVideoSectionViewHolderClicks listener;
    private ArticleDetailVideoSection videoSection;
    private Context context;
    private boolean adPlayed = false;
    private VideoPlayerEvents.OnFullscreenListener mFullScreenListener;

    public ArticleDetailVideoSectionViewHolder(View itemView, IVideoSectionViewHolderClicks listenerArg) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        listener = listenerArg;
        listener.getPlayerInstance(mPlayerView);

    }

    @OnClick(R.id.article_detail_video_play_btn)
    void onVideoPlayButtonClick() {
        listener.onVideoPlayButtonClick();
    }

    public void bindVideoSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ArticleDetailVideoSection) {
            videoSection = (ArticleDetailVideoSection) articleDetailRecyclerViewItem;
            String imageType;
            if (context.getResources().getBoolean(R.bool.isTablet)) {
                imageType = Constants.IMAGE_THUMBNAIL;
            } else {
                imageType = Constants.IMAGE_SMALL;
            }
            Glide.with(context)
                    .load(ViewUtils.getThumbnail(videoSection.getThumbnail(), imageType))
                    .placeholder(R.drawable.placeholder_featured_images)
                    .centerCrop()
                    .dontAnimate()
                    .into(articleDetailVideoThumbnailImage);
            if (null != ((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getMedia()) {
                if (((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getMedia().size() > 0 &&
                        null != ((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getMedia().get(0)) {
                    final ArticleMedia articleMedia = ((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getMedia().get(0);
                    if (TextUtils.equals(articleMedia.getType(), Constants.VIDEO_TYPE)) {
                        mPlayerView.setVisibility(View.VISIBLE);

                        SingleSource media = new SingleSource(articleMedia.getUrl());

                        if (null != ((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getThumbnail()) {
                            media.setPosterUrl(((ArticleDetailVideoSection) articleDetailRecyclerViewItem).getThumbnail());
                        } else {
                            media.setPosterLocalDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_featured_images));
                        }

                        AdBreak preRollAdBreak = new AdBreak();
                        preRollAdBreak.setType(AdBreak.PRE_ROLL);
                        preRollAdBreak.setTag(articleMedia.getVast());

                        AdBreak midRollAdBreak = new AdBreak();
                        midRollAdBreak.setType(AdBreak.MID_ROLL);
                        midRollAdBreak.setOffset("50%");
                        midRollAdBreak.setTag(articleMedia.getVast());

                        AdBreak postRollAdBreak = new AdBreak();
                        postRollAdBreak.setType(AdBreak.POST_ROLL);
                        postRollAdBreak.setTag(articleMedia.getVast());

                        ArrayList<AdBreak> adBreaks = new ArrayList<>();
                        adBreaks.add(preRollAdBreak);
                        adBreaks.add(midRollAdBreak);
                        adBreaks.add(postRollAdBreak);

                        Advertising advertising = new Advertising();
                        advertising.setClient(Advertising.VAST_CLIENT);
                        advertising.setListOfBreaks(adBreaks);

                        media.setAdvertising(advertising);
                        articleDetailVideoThumbnailImage.setVisibility(View.GONE);
                        mPlayerView.load(media);

                        mPlayerView.setOnTimeListener(new VideoPlayerEvents.OnTimeListener() {
                            @Override
                            public void onTime(int time, int duration) {
                                if (time >= duration) {
                                    mPlayerView.stop();
                                }
                            }
                        });

                    }
                }
            }

        }
    }

    public interface IVideoSectionViewHolderClicks {
        void onVideoPlayButtonClick();

        void getPlayerInstance(JWPlayerView playerView);
    }
}
