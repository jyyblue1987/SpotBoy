/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.ArticleDetailFragment;
import com.ninexe.ui.models.ArticleDetailCommentSection;
import com.ninexe.ui.models.Comment;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by nagesh on 15/10/15.
 */
public class CommentSectionViewHolder extends RecyclerView.ViewHolder {

    private final String MAX_CHAR_LIMIT;

    @Bind(R.id.article_question)
    TextView articleQuestion;

    @Bind(R.id.edit_comment)
    EditText commentEditText;

    @Bind(R.id.char_count)
    TextView charCountText;

    @Bind(R.id.number_of_comments)
    TextView numComments;

    @Bind(R.id.comment1)
    View comment1;

    @Bind(R.id.comment2)
    View comment2;

    @Bind(R.id.btn_view_more_comments)
    Button viewMoreCommentsButton;

    @Bind(R.id.btn_submit)
    Button submitButton;

    private BitmapPool bitmapPool;
    private Context context;
    private ArticleDetailCommentSection articleDetailCommentSection;
    private ICommentSectionViewHolderClicks listener;
    private final TextWatcher COMMENT_EDIT_TEXT_WATCHER = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                charCountText.setText(MAX_CHAR_LIMIT);
                //submitButton.setEnabled(false);

            } else {
                // TODO: use string formatter
                charCountText.setText(s.length() + "/" + MAX_CHAR_LIMIT);
                articleDetailCommentSection.setComment(s.toString());
                ArticleDetailFragment.mCommentString = s.toString();
                //submitButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    };

    public CommentSectionViewHolder(View itemView,
                                    ICommentSectionViewHolderClicks commentSectionViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
            bitmapPool = Glide.get(context).getBitmapPool();
        }
        listener = commentSectionViewHolderClicks;
        MAX_CHAR_LIMIT = String.valueOf(context.getResources().getInteger(R.integer.comment_max_length));
    }


    @OnClick(R.id.btn_submit)
    void onSubmitButtonClick() {
        LogUtils.LOGD(Constants.APP_TAG, "Clicked on Submit Button");
        listener.onSubmitButtonClick(getEditTextComment());
    }

    private String getEditTextComment() {
        return commentEditText.getText().toString();
    }

    @OnClick(R.id.btn_view_more_comments)
    void onViewMoreButtonClick() {
        LogUtils.LOGD(Constants.APP_TAG, "Clicked on View More Button");
        listener.onViewMoreButtonClick();
    }

    public void bindCommentSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ArticleDetailCommentSection) {
            articleDetailCommentSection = (ArticleDetailCommentSection) articleDetailRecyclerViewItem;

            if (null != articleDetailCommentSection) {
                ViewUtils.setText(articleQuestion, articleDetailCommentSection.getArticleQuestion());
                commentEditText.setText(articleDetailCommentSection.getComment());

                if (articleDetailCommentSection.getCommentsCount() == 1) {
                    numComments.setText(String.format(context.getString(R.string.number_of_comment),
                            articleDetailCommentSection.getCommentsCount()));
                } else {
                    numComments.setText(String.format(context.getString(R.string.number_of_comments),
                            articleDetailCommentSection.getCommentsCount()));
                }

                addCommentEditTextWatcher();
                showComments();
                //submitButton.setEnabled(false);
            }
        }
    }

    private void addCommentEditTextWatcher() {
        commentEditText.addTextChangedListener(COMMENT_EDIT_TEXT_WATCHER);
    }

    private void showComments() {
        ArrayList<Comment> commentArrayList;
        if (null != articleDetailCommentSection.getCommentArrayList()) {
            commentArrayList = articleDetailCommentSection.getCommentArrayList();
            if (commentArrayList.isEmpty()) {
                ViewUtils.hideView(comment1);
                ViewUtils.hideView(comment2);
            } else if (commentArrayList.size() == 1) {
                ViewUtils.hideView(comment2);
                ViewUtils.showView(comment1);
                setCommentData(comment1, commentArrayList.get(0));
            } else if (commentArrayList.size() == 2) {
                ViewUtils.showView(comment1);
                ViewUtils.showView(comment2);
                setCommentData(comment1, commentArrayList.get(0));
                setCommentData(comment2, commentArrayList.get(1));
            } else if (commentArrayList.size() > 2) {
                ViewUtils.showView(comment1);
                ViewUtils.showView(comment2);
                setCommentData(comment1, commentArrayList.get(0));
                setCommentData(comment2, commentArrayList.get(1));
            }

            if (articleDetailCommentSection.getCommentsCount() > 2) {
                ViewUtils.showView(viewMoreCommentsButton);
            } else {
                ViewUtils.hideView(viewMoreCommentsButton);
            }
        }

    }

    private void setCommentData(View view, Comment commentArg) {
        if (null == view || null == commentArg)
            return;


        ImageView profileImage = (ImageView) view.findViewById(R.id.profile_image);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView createdAt = (TextView) view.findViewById(R.id.created_at);
        TextView comment = (TextView) view.findViewById(R.id.comment);

        if (null != commentArg.getUserProfilePic()) {
            Glide.with(context)
                    .load(commentArg.getUserProfilePic())
                    .bitmapTransform(new CropCircleTransformation(bitmapPool))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.placeholder_pro_pic)
                    .crossFade()
                    .into(profileImage);
        }
        author.setText(commentArg.getUserName());
        ViewUtils.setText(author, commentArg.getUserName());
        ViewUtils.setText(createdAt, DateTimeUtils.getDate(commentArg.getCreatedAt()));
        ViewUtils.setText(comment, commentArg.getComment());
    }

    public interface ICommentSectionViewHolderClicks {
        void onSubmitButtonClick(String comment);

        void onViewMoreButtonClick();
    }
}
