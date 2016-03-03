/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.ViewMoreCommentsRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.common.Notification;
import com.ninexe.ui.managers.NotificationManager;
import com.ninexe.ui.models.ArticleDetailResponse;
import com.ninexe.ui.models.ArticleMeta;
import com.ninexe.ui.models.Comment;
import com.ninexe.ui.models.CommentBox;
import com.ninexe.ui.models.CommentResponse;
import com.ninexe.ui.models.EndlessRecyclerOnScrollListener;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.IViewMoreCommentsRecyclerViewItem;
import com.ninexe.ui.models.SubmitCommentBody;
import com.ninexe.ui.models.ViewMoreCommentsResponse;
import com.ninexe.ui.models.events.CommentNumberChangeEvent;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.ViewMoreCommentsDataProviders;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 20/10/15.
 */
public class ViewMoreCommentsFragment extends BaseFragment
        implements ViewMoreCommentsRecyclerViewAdapter.IViewMoreCommentsRecyclerViewAdapterInteractionListener {

    public static final String VIEW_MORE_COMMENTS_FRAGMENT = "view_more_comments_fragment";
    private static String ARG_ARTICLE_ID = "arg_article_id";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.view_more_comments_recyclerview)
    RecyclerView mViewMoreCommentsRecyclerView;

    private static String mCommentString;

    private int mUpdatedCommentNumber;
    private String mArticleId;
    private int mCurrentPage = 1;
    private ViewMoreCommentsRecyclerViewAdapter mViewMoreCommentsRecyclerViewAdapter;
    private ArrayList<IViewMoreCommentsRecyclerViewItem> mViewMoreCommentsDataSet
            = new ArrayList<>();

    public static ViewMoreCommentsFragment newInstance(String articleId) {
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE_ID, articleId);
        ViewMoreCommentsFragment fragment = new ViewMoreCommentsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            setArticleId(getArguments().getString(ARG_ARTICLE_ID));
            PreferenceManager.storePhotoGalleryDetailCurrentPosition(Constants.PHOTO_GALLERY_DUMMY, getActivity());
        }
    }

    public void setArticleId(String mArticleId) {
        this.mArticleId = mArticleId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_more_comments, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initViewMoreCommentsRecyclerView();
        fetchArticle();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewUtils.setThemeBackground(mToolbar);
        if (null != mCommentString && !TextUtils.isEmpty(mCommentString.trim())) {
            setUserComment(mCommentString);
        }
    }

    private void setUserComment(String commentString) {
        CommentBox commentBox = null;
        for (IViewMoreCommentsRecyclerViewItem item : mViewMoreCommentsDataSet) {
            if (item instanceof CommentBox) {
                commentBox = (CommentBox) item;
                break;
            }
        }
        if (null != commentBox) {
            commentBox.setComment(commentString);
            mViewMoreCommentsRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void fetchArticle() {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            ResponseCallback<ArticleDetailResponse> responseCallback = new ResponseCallback<ArticleDetailResponse>() {
                @Override
                public void success(ArticleDetailResponse articleDetailResponse) {
                    if (getActivity() != null && isAdded()) {
                        ViewMoreCommentsDataProviders.getInstance().setArticleDetailData(articleDetailResponse.getData());
                        fetchArticleComments(getArticleId(), mCurrentPage, Constants.COMMENTS_PER_PAGE);
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        //showRetryFrame(getString(R.string.server_error));
                        // TODO : handle failure response
                    }
                }
            };
            NetworkAdapter.get(getContext()).getArticleScreenDetail(getArticleId(), responseCallback, getActivity());
        } else {
            //showRetryFrame(getString(R.string.no_network));
            // TODO : handle no network
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    private void fetchArticleComments(String articleId, int currentPage, int commentsPerPage) {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            ResponseCallback<ViewMoreCommentsResponse> responseCallback = new ResponseCallback<ViewMoreCommentsResponse>() {
                @Override
                public void success(ViewMoreCommentsResponse viewMoreCommentsResponse) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                        ViewMoreCommentsDataProviders.getInstance().setViewMoreCommentsResponse(viewMoreCommentsResponse);
                        setViewMoreCommentsRecyclerViewData();
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                        // TODO : handle failure response
                    }
                }
            };
            NetworkAdapter.get(getActivity()).viewMoreComments(articleId, currentPage, commentsPerPage, responseCallback);
        } else {
            // TODO : handle no network
        }
    }

    private String getArticleId() {
        return mArticleId;
    }


    private void initViewMoreCommentsRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mViewMoreCommentsRecyclerView.setHasFixedSize(true);
        mViewMoreCommentsRecyclerView.setLayoutManager(linearLayoutManager);
        mViewMoreCommentsRecyclerViewAdapter =
                new ViewMoreCommentsRecyclerViewAdapter(mViewMoreCommentsDataSet, this);
        mViewMoreCommentsRecyclerView.setAdapter(mViewMoreCommentsRecyclerViewAdapter);
        mViewMoreCommentsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreComments(current_page);
            }
        });
    }

    private void loadMoreComments(int currentPage) {
        ArticleMeta articleMeta = ViewMoreCommentsDataProviders.getInstance().getViewMoreCommentsResponse().getMeta();
        if (null != articleMeta) {
            if (currentPage <= articleMeta.getPages())
                fetchArticleComments(getArticleId(), currentPage, Constants.COMMENTS_PER_PAGE);

        }
    }

    private void setViewMoreCommentsRecyclerViewData() {
        if (!isCommentBoxAdded()) {
            mViewMoreCommentsDataSet.add(0, ViewMoreCommentsDataProviders.getInstance().getCommentBox());
        }
        mViewMoreCommentsDataSet.addAll(ViewMoreCommentsDataProviders.getInstance().getViewMoreCommentsData());
        mViewMoreCommentsRecyclerViewAdapter.notifyDataSetChanged();
    }

    private boolean isCommentBoxAdded() {
        boolean isCommentBoxAdded = false;
        if (!mViewMoreCommentsDataSet.isEmpty() &&
                mViewMoreCommentsDataSet.get(0) instanceof CommentBox) {
            isCommentBoxAdded = true;
        }
        return isCommentBoxAdded;
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.comments));
        enableBackButton();
        onSearchClick();
    }

    @Override
    public void onCommentSubmit(String comment) {
        if (!PreferenceManager.getBoolean(getContext(), Constants.SP_LOGGED_IN, false)) {
            NavigationUtils.startLoginActivity(getContext());
            mCommentString = comment;
        } else {
            if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
                mCommentString = null;
                if (isValidComment(comment)) {
                    DialogUtils.showCustomProgressDialog(getContext(), true);
                    SubmitCommentBody submitCommentBody = new SubmitCommentBody();
                    submitCommentBody.setComment(comment);
                    NetworkAdapter.get(getContext()).comment(getArticleId(), submitCommentBody,
                            new ResponseCallback<GenericResponse<CommentResponse>>() {
                                @Override
                                public void success(GenericResponse<CommentResponse> commentResponse) {
                                    DialogUtils.cancelProgressDialog();
                                    updateArticleComment(commentResponse);
                                    LogUtils.LOGD(Constants.APP_TAG, commentResponse.getData().getCommentResponse().getUserName());
                                }

                                @Override
                                public void failure(RestError error) {
                                    DialogUtils.cancelProgressDialog();
                                    LogUtils.LOGD(Constants.APP_TAG, "failure");
                                }
                            });
                } else {
                    // TODO : Handl invalid comment
                    DialogUtils.showSingleButtonAlertDialog(getActivity(),
                            null, getString(R.string.msg_invalid_comment), getString(R.string.ok));
                }


            } else {
                // TODO : Handl no network connection
            }
        }
    }

    private void updateArticleComment(GenericResponse<CommentResponse> commentResponse) {
        CommentBox commentBox = (CommentBox) mViewMoreCommentsDataSet.get(0);
        commentBox.setComment(null);
        mViewMoreCommentsDataSet.add(1, commentResponse.getData().getCommentResponse());
        commentBox.setNumberOfComments(commentBox.getNumberOfComments() + 1);
        setUpdatedCommentNumber(commentBox.getNumberOfComments());
        mViewMoreCommentsRecyclerViewAdapter.notifyDataSetChanged();

        Comment comment1 = null;
        Comment comment2 = null;

        if (mViewMoreCommentsDataSet.size() >= 2 && mViewMoreCommentsDataSet.get(1) instanceof Comment) {
            comment1 = (Comment) mViewMoreCommentsDataSet.get(1);
        }

        if (mViewMoreCommentsDataSet.size() >= 3 && mViewMoreCommentsDataSet.get(2) instanceof Comment) {
            comment2 = (Comment) mViewMoreCommentsDataSet.get(2);
        }

        Bundle data = new Bundle();
        data.putParcelable(Constants.EXTRA_COMMENT_CHANGE, getCommentChangeEvent(getUpdatedCommentNumber(), comment1, comment2));
        NotificationManager.getInstance().notifyObservers(Notification.COMMENT_UPDATE, data);
    }

    private CommentNumberChangeEvent getCommentChangeEvent(int updatedCommentNumber, Comment comment1, Comment comment2) {
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        return new CommentNumberChangeEvent(updatedCommentNumber, comments);
    }

    private boolean isValidComment(String comment) {
        boolean isValidComment = false;
        if (!TextUtils.isEmpty(comment.trim())) {
            isValidComment = true;
        }
        return isValidComment;
    }


    public void setUpdatedCommentNumber(int updatedCommentNumber) {
        this.mUpdatedCommentNumber = updatedCommentNumber;
    }

    public int getUpdatedCommentNumber() {
        return mUpdatedCommentNumber;
    }

}
