/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.ninexe.ui.R;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.models.viewholders.ArticleDetailArticleContentSectionViewHolder;
import com.ninexe.ui.models.viewholders.ArticleDetailRelatedArticleSectionViewHolder;
import com.ninexe.ui.models.viewholders.ArticleDetailRelatedArticleTextSectionViewHolder;
import com.ninexe.ui.models.viewholders.ArticleDetailVideoSectionViewHolder;
import com.ninexe.ui.models.viewholders.CommentSectionViewHolder;
import com.ninexe.ui.models.viewholders.ReactometerViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<IArticleDetailRecyclerViewItem> mDataSet;
    private Context mContext;
    private View mView;
    private LayoutInflater mLayoutInflater;
    private static final int COMMENT_VIEW_TAG = 1;
    private static final int REACTOMETER_VIEW_TAG = 2;
    private IArticleDetailRecyclerAdapterInteractionListener mListener;

    public ArticleDetailRecyclerViewAdapter(Context context,
                                            ArrayList<IArticleDetailRecyclerViewItem> articleDetailRecyclerViewItems,
                                            IArticleDetailRecyclerAdapterInteractionListener listener) {
        mContext = context;
        mDataSet = articleDetailRecyclerViewItems;
        mLayoutInflater = LayoutInflater.from(mContext);
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == IArticleDetailRecyclerViewItem.TYPE_VIDEO_SECTION) {
            return getVideoSectionViewHolder(parent);
        } else if (viewType == IArticleDetailRecyclerViewItem.TYPE_REACTOMETER_SECTION) {
            return getReactometerSectionViewHolder(parent);
        } else if (viewType == IArticleDetailRecyclerViewItem.TYPE_ARTICLE_CONTENT_SECTION) {
            return getContentSectionViewHolder(parent);
        } else if (viewType == IArticleDetailRecyclerViewItem.TYPE_COMMENT_SECTION) {
            return getCommentSectionViewHolder(parent);
        } else if (viewType == IArticleDetailRecyclerViewItem.TYPE_RELATED_ARTICLE_TEXT_SECTION) {
            return getRealtedArticleTextSectionViewHolder(parent);
        } else if (viewType == IArticleDetailRecyclerViewItem.TYPE_RELATED_ARTICLE_SECTION) {
            return getRealtedArticleSectionViewHolder(parent);
        }
        throw new RuntimeException("there is no type that matches the type "
                + viewType + " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleDetailVideoSectionViewHolder) {
            ArticleDetailVideoSectionViewHolder videoSectionViewHolder = (ArticleDetailVideoSectionViewHolder) holder;
            videoSectionViewHolder.bindVideoSectionData(mDataSet.get(position));
        } else if (holder instanceof ReactometerViewHolder) {
            ReactometerViewHolder reactometerViewHolder = (ReactometerViewHolder) holder;
            reactometerViewHolder.bindReactometerSectionData(mDataSet.get(position));
        } else if (holder instanceof ArticleDetailArticleContentSectionViewHolder) {
            ArticleDetailArticleContentSectionViewHolder articleContentSectionViewHolder
                    = (ArticleDetailArticleContentSectionViewHolder) holder;
            articleContentSectionViewHolder.bindArticleContentSectionData(mDataSet.get(position));
        } else if (holder instanceof CommentSectionViewHolder) {
            CommentSectionViewHolder commentSectionViewHolder
                    = (CommentSectionViewHolder) holder;
            commentSectionViewHolder.bindCommentSectionData(mDataSet.get(position));
        } else if (holder instanceof ArticleDetailRelatedArticleTextSectionViewHolder) {
            ArticleDetailRelatedArticleTextSectionViewHolder relatedArticleTextSectionViewHolder
                    = (ArticleDetailRelatedArticleTextSectionViewHolder) holder;
            relatedArticleTextSectionViewHolder.bindRelatedArticleTextSectionData(mDataSet.get(position));
        } else if (holder instanceof ArticleDetailRelatedArticleSectionViewHolder) {
            ArticleDetailRelatedArticleSectionViewHolder relatedArticleSectionViewHolder
                    = (ArticleDetailRelatedArticleSectionViewHolder) holder;
            relatedArticleSectionViewHolder.bindRelatedArticleSectionData(mDataSet.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private RecyclerView.ViewHolder getVideoSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.item_article_detail_video, parent, false);
        ArticleDetailVideoSectionViewHolder articleDetailVideoSectionViewHolder
                = new ArticleDetailVideoSectionViewHolder(mView,
                new ArticleDetailVideoSectionViewHolder.IVideoSectionViewHolderClicks() {
                    @Override
                    public void onVideoPlayButtonClick() {
                        //TODO: Handle Video Play Button Click
                    }

                    @Override
                    public void getPlayerInstance(JWPlayerView playerView) {
                        mListener.getVideoPlayerInstance(playerView);
                    }
                });
        return articleDetailVideoSectionViewHolder;
    }

    private RecyclerView.ViewHolder getContentSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.item_article_detail_article_content, parent, false);
        ArticleDetailArticleContentSectionViewHolder articleDetailArticleContentSectionViewHolder
                = new ArticleDetailArticleContentSectionViewHolder(mView,
                new ArticleDetailArticleContentSectionViewHolder.IArticleContentSectionViewHolderClicks() {
                    @Override
                    public void onReadMoreButtonClick() {
                        //TODO: Handle Read More Button Click
                    }

                    @Override
                    public void getWebViewInstance(WebView webView) {
                        mListener.getWebViewInstance(webView);
                    }
                });
        return articleDetailArticleContentSectionViewHolder;
    }

    private RecyclerView.ViewHolder getCommentSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.item_article_detail_comments, parent, false);
        mView.setTag(COMMENT_VIEW_TAG);
        CommentSectionViewHolder commentSectionViewHolder = new CommentSectionViewHolder(mView,
                new CommentSectionViewHolder.ICommentSectionViewHolderClicks() {
                    @Override
                    public void onSubmitButtonClick(String comment) {
                        //TODO: Handle Submit Button Click
                        mListener.OnCommentSubmitButtonClick(comment);
                    }

                    @Override
                    public void onViewMoreButtonClick() {
                        mListener.onViewMoreButtonClick();
                    }
                });
        return commentSectionViewHolder;
    }

    private RecyclerView.ViewHolder getRealtedArticleSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.item_article_detail_related_article, parent, false);
        ArticleDetailRelatedArticleSectionViewHolder articleDetailRelatedArticleSectionViewHolder
                = new ArticleDetailRelatedArticleSectionViewHolder(mView,
                new ArticleDetailRelatedArticleSectionViewHolder.IRelatedArticleViewHolderClicks() {
                    @Override
                    public void onRelatedArticleSelection(Article article) {
                        if (null != mListener) {
                            mListener.onRelatedArticleSelection(article);
                        }
                    }
                });
        return articleDetailRelatedArticleSectionViewHolder;
    }

    private RecyclerView.ViewHolder getRealtedArticleTextSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.item_related_article_text, parent, false);
        ArticleDetailRelatedArticleTextSectionViewHolder relatedArticleTextSectionViewHolder
                = new ArticleDetailRelatedArticleTextSectionViewHolder(mView);
        return relatedArticleTextSectionViewHolder;
    }

    private RecyclerView.ViewHolder getReactometerSectionViewHolder(ViewGroup parent) {
        mView = mLayoutInflater.inflate(R.layout.reactometer, parent, false);
        mView.setTag(REACTOMETER_VIEW_TAG);
        ReactometerViewHolder reactometerViewHolder = new ReactometerViewHolder(mView,
                new ReactometerViewHolder.IReactometerViewHolderClicks() {
                    @Override
                    public void onShareButtonClick() {
                        mListener.onShareButtonClick();
                    }

                    @Override
                    public void onReacted(String reaction) {
                        mListener.onReacted(reaction);
                    }
                });
        return reactometerViewHolder;
    }

    public boolean isCurrentItemRealtedArticle(int position) {
        return getItemViewType(position) == IArticleDetailRecyclerViewItem.TYPE_RELATED_ARTICLE_SECTION;
    }


    public interface IArticleDetailRecyclerAdapterInteractionListener {
        void onRelatedArticleSelection(Article article);

        void OnCommentSubmitButtonClick(String comment);

        void onShareButtonClick();

        void onViewMoreButtonClick();

        void getWebViewInstance(WebView webView);

        void onReacted(String reaction);

        void getVideoPlayerInstance(JWPlayerView playerView);
    }
}
