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

import com.ninexe.ui.R;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.viewholders.ArticleViewHolder;
import com.ninexe.ui.models.viewholders.FeaturedArticleViewHolder;
import com.ninexe.ui.models.viewholders.GridRecentArticleViewHolder;
import com.ninexe.ui.models.viewholders.HomeRecentArticleViewHolder;
import com.ninexe.ui.models.viewholders.RecentArticleViewHolder;
import com.ninexe.ui.models.viewholders.SearchResultArticleViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class TabSectionRecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<ITabSectionRecyclerViewItem> mDataSet;
    private Context mContext;
    private OnTabSectionRecyclerAdapterInteractionListener mListener;

    public TabSectionRecyclerViewAdapter(Context context,
                                         ArrayList<ITabSectionRecyclerViewItem> tabSectionArrayList,
                                         OnTabSectionRecyclerAdapterInteractionListener listener) {
        mContext = context;
        mDataSet = tabSectionArrayList;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (viewType == ITabSectionRecyclerViewItem.TYPE_FEATURED_ARTICLE) {
            view = inflater.inflate(R.layout.item_featured_article, parent, false);
            return getFeaturedArticleViewHolder(view);
        } else if (viewType == ITabSectionRecyclerViewItem.TYPE_RECENT_ARTICLE) {
            view = inflater.inflate(R.layout.item_recent_article, parent, false);
            return getRecentArticleViewHolder(view);
        } else if (viewType == ITabSectionRecyclerViewItem.TYPE_GRID_RECENT_ARTICLE) {
            view = inflater.inflate(R.layout.item_grid_recent_article, parent, false);
            return getRelatedArticleViewHolder(view);
        } else if (viewType == ITabSectionRecyclerViewItem.TYPE_SEARCH_RESULT_ARTICLE) {
            view = inflater.inflate(R.layout.item_recent_article, parent, false);
            return geSearchResultArticleViewHolder(view);
        } else if (viewType == ITabSectionRecyclerViewItem.TYPE_HOME_RECENT_ARTICLE) {
            view = inflater.inflate(R.layout.item_recent_article, parent, false);
            return getHomeRecentArticleViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type "
                + viewType + " make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FeaturedArticleViewHolder) {
            FeaturedArticleViewHolder featuredArticleViewHolder = (FeaturedArticleViewHolder) holder;
            featuredArticleViewHolder.bindFeaturedArticle(mDataSet.get(position));
        } else if (holder instanceof RecentArticleViewHolder) {
            RecentArticleViewHolder recentArticleViewHolder = (RecentArticleViewHolder) holder;
            recentArticleViewHolder.bindRecentArticle(mDataSet.get(position));
        } else if (holder instanceof GridRecentArticleViewHolder) {
            GridRecentArticleViewHolder gridRecentArticleViewHolder = (GridRecentArticleViewHolder) holder;
            gridRecentArticleViewHolder.bindRecentArticle(mDataSet.get(position));
        } else if (holder instanceof SearchResultArticleViewHolder) {
            SearchResultArticleViewHolder searchResultArticleViewHolder = (SearchResultArticleViewHolder) holder;
            searchResultArticleViewHolder.bindRecentArticle(mDataSet.get(position));
        } else if (holder instanceof HomeRecentArticleViewHolder) {
            HomeRecentArticleViewHolder homeRecentArticleViewHolder = (HomeRecentArticleViewHolder) holder;
            homeRecentArticleViewHolder.bindRecentArticle(mDataSet.get(position));
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

    public boolean isCurrentItemRelatedArticle(int position) {
        return getItemViewType(position) == ITabSectionRecyclerViewItem.TYPE_GRID_RECENT_ARTICLE;
    }

    private RecyclerView.ViewHolder getFeaturedArticleViewHolder(View view) {
        FeaturedArticleViewHolder featuredArticleViewHolder =
                new FeaturedArticleViewHolder(view, new ArticleViewHolder.IArticleViewHolderClicks() {
                    @Override
                    public void onArticleClick(Article article) {
                        if (null != mListener) {
                            mListener.onArticleSelection(article);
                        }
                    }
                });
        return featuredArticleViewHolder;
    }

    private RecyclerView.ViewHolder getHomeRecentArticleViewHolder(View view) {
        HomeRecentArticleViewHolder homeRecentArticleViewHolder =
                new HomeRecentArticleViewHolder(view,
                        new ArticleViewHolder.IArticleViewHolderClicks() {
                            @Override
                            public void onArticleClick(Article article) {
                                if (null != mListener) {
                                    mListener.onArticleSelection(article);
                                }
                            }
                        });
        return homeRecentArticleViewHolder;
    }

    private RecyclerView.ViewHolder getRecentArticleViewHolder(View view) {
        RecentArticleViewHolder recentArticleViewHolder =
                new RecentArticleViewHolder(view, new ArticleViewHolder.IArticleViewHolderClicks() {
                    @Override
                    public void onArticleClick(Article article) {
                        if (null != mListener) {
                            mListener.onArticleSelection(article);
                        }
                    }
                });
        return recentArticleViewHolder;

    }

    private RecyclerView.ViewHolder getRelatedArticleViewHolder(View view) {
        GridRecentArticleViewHolder gridRecentArticleViewHolder =
                new GridRecentArticleViewHolder(view, new ArticleViewHolder.IArticleViewHolderClicks() {
                    @Override
                    public void onArticleClick(Article article) {
                        if (null != mListener) {
                            mListener.onArticleSelection(article);
                        }
                    }
                });
        return gridRecentArticleViewHolder;

    }

    private RecyclerView.ViewHolder geSearchResultArticleViewHolder(View view) {
        SearchResultArticleViewHolder searchResultArticleViewHolder =
                new SearchResultArticleViewHolder(view, new ArticleViewHolder.IArticleViewHolderClicks() {
                    @Override
                    public void onArticleClick(Article article) {
                        if (null != mListener) {
                            mListener.onArticleSelection(article);
                        }
                    }
                });
        return searchResultArticleViewHolder;
    }

    public interface OnTabSectionRecyclerAdapterInteractionListener {
        void onArticleSelection(Article article);
    }
}
