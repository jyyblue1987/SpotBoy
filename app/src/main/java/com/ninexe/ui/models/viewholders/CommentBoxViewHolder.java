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
import android.widget.EditText;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.CommentBox;
import com.ninexe.ui.models.IViewMoreCommentsRecyclerViewItem;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 20/10/15.
 */
public class CommentBoxViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.article_question)
    TextView articleQuestion;

    @Bind(R.id.edit_comment)
    EditText commentEditText;

    @Bind(R.id.char_count)
    TextView charCountText;

    @Bind(R.id.number_of_comments)
    TextView numComments;

    private Context context;
    private CommentBox commentBox;
    private final String MAX_CHAR_LIMIT;
    private ICommentBoxViewHolderClicks listener;


    public CommentBoxViewHolder(View itemView,
                                ICommentBoxViewHolderClicks commentBoxViewHolderClickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        MAX_CHAR_LIMIT = String.valueOf(context.getResources().getInteger(R.integer.comment_max_length));
        listener = commentBoxViewHolderClickListener;
    }

    public void bindCommentBoxData(IViewMoreCommentsRecyclerViewItem viewMoreCommentsRecyclerViewItem) {
        if (viewMoreCommentsRecyclerViewItem instanceof CommentBox) {
            commentBox = (CommentBox) viewMoreCommentsRecyclerViewItem;

            if (null != commentBox) {
                ViewUtils.setText(articleQuestion, commentBox.getArticleQuestion());
                commentEditText.setText(commentBox.getComment());
                if (1 == commentBox.getNumberOfComments()) {
                    numComments.setText(String.format(context.getString(R.string.number_of_comment), commentBox.getNumberOfComments()));
                } else {
                    numComments.setText(String.format(context.getString(R.string.number_of_comments), commentBox.getNumberOfComments()));
                }
                addCommentEditTextWatcher();
            }
        }
    }

    private void addCommentEditTextWatcher() {
        final TextWatcher COMMENT_EDIT_TEXT_WATCHER = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    charCountText.setText(MAX_CHAR_LIMIT);
                } else {
                    // TODO: use string formatter
                    charCountText.setText(s.length() + "/" + MAX_CHAR_LIMIT);
                    commentBox.setComment(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        };

        commentEditText.addTextChangedListener(COMMENT_EDIT_TEXT_WATCHER);
    }

    private String getEditTextComment() {
        return commentEditText.getText().toString();
    }

    @OnClick(R.id.btn_submit)
    void onCommentSubmitButtonClick() {
        LogUtils.LOGD(Constants.APP_TAG, "Clicked on Submit Button");
        listener.onCommentSubmitButtonClick(getEditTextComment());
        //commentEditText.setText(null);
    }

    public interface ICommentBoxViewHolderClicks {
        void onCommentSubmitButtonClick(String comment);
    }
}
