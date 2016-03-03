/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ninexe.ui.R;
import com.ninexe.ui.models.Comment;
import com.ninexe.ui.models.IViewMoreCommentsRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nagesh on 20/10/15.
 */
public class CommentViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.profile_image)
    ImageView profileImage;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.created_at)
    TextView createdAt;

    @Bind(R.id.comment)
    TextView commentTextView;

    private Context context;
    private Comment comment;
    private BitmapPool bitmapPool;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
            bitmapPool = Glide.get(context).getBitmapPool();
        }
    }

    public void bindCommentData(IViewMoreCommentsRecyclerViewItem viewMoreCommentsRecyclerViewItem) {
        if (viewMoreCommentsRecyclerViewItem instanceof Comment) {
            comment = (Comment) viewMoreCommentsRecyclerViewItem;
            if (null != comment) {

                if (null != comment.getUserProfilePic()) {
                    Glide.with(context)
                            .load(comment.getUserProfilePic())
                            .bitmapTransform(new CropCircleTransformation(bitmapPool))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.drawable.placeholder_pro_pic)
                            .crossFade()
                            .into(profileImage);
                }
                author.setText(comment.getUserName());
                ViewUtils.setText(createdAt, DateTimeUtils.getDate(comment.getCreatedAt()));
                ViewUtils.setText(commentTextView, comment.getComment());
            }
        }
    }
}
