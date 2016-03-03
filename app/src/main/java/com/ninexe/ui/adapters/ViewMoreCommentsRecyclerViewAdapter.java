/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.models.IViewMoreCommentsRecyclerViewItem;
import com.ninexe.ui.models.viewholders.CommentBoxViewHolder;
import com.ninexe.ui.models.viewholders.CommentViewHolder;

import java.util.ArrayList;

/**
 * Created by nagesh on 20/10/15.
 */
public class ViewMoreCommentsRecyclerViewAdapter extends RecyclerView.Adapter {
    private ArrayList<IViewMoreCommentsRecyclerViewItem> mDataSet;
    private IViewMoreCommentsRecyclerViewAdapterInteractionListener mListener;

    public ViewMoreCommentsRecyclerViewAdapter(
            ArrayList<IViewMoreCommentsRecyclerViewItem> viewMoreCommentsRecyclerViewItems,
            IViewMoreCommentsRecyclerViewAdapterInteractionListener listener) {
        mDataSet = viewMoreCommentsRecyclerViewItems;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == IViewMoreCommentsRecyclerViewItem.TYPE_COMMENT_BOX) {
            view = inflater.inflate(R.layout.item_comment_box, parent, false);
            return getCommentBoxViewHolder(view);
        } else if (viewType == IViewMoreCommentsRecyclerViewItem.TYPE_COMMENT) {
            view = inflater.inflate(R.layout.comment, parent, false);
            return getCommentViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type "
                + viewType + " make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentBoxViewHolder) {
            ((CommentBoxViewHolder) holder).bindCommentBoxData(mDataSet.get(position));
        } else if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).bindCommentData(mDataSet.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataSet.get(position).getItemType();
    }

    private RecyclerView.ViewHolder getCommentViewHolder(View view) {
        CommentViewHolder commentViewHolder = new CommentViewHolder(view);
        return commentViewHolder;
    }

    private RecyclerView.ViewHolder getCommentBoxViewHolder(View view) {
        CommentBoxViewHolder commentBoxViewHolder =
                new CommentBoxViewHolder(view, new CommentBoxViewHolder.ICommentBoxViewHolderClicks() {
                    @Override
                    public void onCommentSubmitButtonClick(String comment) {
                        if (null != mListener) {
                            mListener.onCommentSubmit(comment);
                        }
                    }
                });
        return commentBoxViewHolder;
    }

    public interface IViewMoreCommentsRecyclerViewAdapterInteractionListener {
        void onCommentSubmit(String comment);
    }

}
