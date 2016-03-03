/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.longtailvideo.jwplayer.JWPlayerView;
import com.longtailvideo.jwplayer.media.PlayerState;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.ArticleDetailRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.common.Notification;
import com.ninexe.ui.custom.MarginDecoration;
import com.ninexe.ui.managers.NotificationManager;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleDetailCommentSection;
import com.ninexe.ui.models.ArticleDetailResponse;
import com.ninexe.ui.models.ArticleDetailVideoSection;
import com.ninexe.ui.models.Comment;
import com.ninexe.ui.models.CommentResponse;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.models.ReactionModel;
import com.ninexe.ui.models.ReactionResponseModel;
import com.ninexe.ui.models.ReactometerSection;
import com.ninexe.ui.models.SubmitCommentBody;
import com.ninexe.ui.models.events.CommentNumberChangeEvent;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ShareUtils;
import com.ninexe.ui.utils.dataproviders.ArticleDetailDataProvider;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;
import com.ninexe.ui.utils.dataproviders.ReactionDataHandler;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailFragment extends BaseFragment
        implements ArticleDetailRecyclerViewAdapter.IArticleDetailRecyclerAdapterInteractionListener,
        NotificationManager.Observer {

    public static final String ARTICLE_DETAIL_FRAGMENT = "article_detail_fragment";
    private static final int COMMENT_VIEW_TAG = 1;
    private static final int REACTOMETER_VIEW_TAG = 2;
    private static final String ARG_ARTICLE_ID = "arg_article_id";
    private static final String ARG_ARTICLE_TITLE = "arg_article_title";
    private static final int FULL_SCREEN = 1;
    private static final int POTRAIT_SCREEN = 2;
    public static boolean isWebviewVideoPlaying = false;
    private String mArticleId;
    private String mArticleTitle;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.article_detail_recycler_view)
    RecyclerView mArticleDetailRecyclerView;

    @Bind(R.id.progress_bar_container)
    View mProgressBarContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.data_container)
    View mDataContainer;


    private WebView mArticleWebView;
    private IArticleDetailFragmentInteractionListener mListener;
    private ArticleDetailRecyclerViewAdapter mArticleDetailRecyclerViewAdapter;
    private ArrayList<IArticleDetailRecyclerViewItem> mArticleDetailList = new ArrayList<>();
    public static String mCommentString;
    private JWPlayerView mPlayerView;
    private static int mPlayerState;
    private boolean mIsPlaying;
    private String mShareURL;
    private View mCustomView;
    private int mOriginalOrientation;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mWebviewVideoFullScreenState;
    private boolean isOrientationFromWebview = false;


    public static ArticleDetailFragment newInstance(String articleId, String articleTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE_ID, articleId);
        args.putString(ARG_ARTICLE_TITLE, articleTitle);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Start of Fragment life cycle methods
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (IArticleDetailFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement IArticleDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            setArticleId(getArguments().getString(ARG_ARTICLE_ID));
            setArticleTitle(getArguments().getString(ARG_ARTICLE_TITLE));
        }
        NotificationManager.getInstance().addObserver(Notification.COMMENT_UPDATE, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initRecyclerView();
        showProgress();
        fetchArticleDetailResponse();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mCommentString && !TextUtils.isEmpty(mCommentString.trim())) {
            if (null != mCommentString && !mCommentString.isEmpty())
                setUserComment(mCommentString);
        }
    }

    public void setUserComment() {
        if (null != mCommentString && !TextUtils.isEmpty(mCommentString.trim())) {
            setUserComment(mCommentString);
        }
    }

    private void setUserComment(String commentString) {
        ArticleDetailCommentSection articleDetailCommentSection = null;
        for (IArticleDetailRecyclerViewItem item : mArticleDetailList) {
            if (item instanceof ArticleDetailCommentSection) {
                articleDetailCommentSection = (ArticleDetailCommentSection) item;
                break;
            }
        }
        if (null != articleDetailCommentSection) {
            articleDetailCommentSection.setComment(commentString);
            refreshRecyclerItem(COMMENT_VIEW_TAG);
        }
    }

    private void initRecyclerView() {
        mArticleDetailRecyclerViewAdapter =
                new ArticleDetailRecyclerViewAdapter(getContext(), mArticleDetailList, this);
        mArticleDetailRecyclerView.setHasFixedSize(true);
        mArticleDetailRecyclerView.setLayoutManager(getLayoutManager());
        mArticleDetailRecyclerView.addItemDecoration(new MarginDecoration(getContext()));
        mArticleDetailRecyclerView.setAdapter(mArticleDetailRecyclerViewAdapter);


        mArticleDetailRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = ((GridLayoutManager) mArticleDetailRecyclerView.getLayoutManager());
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                if (mArticleDetailList.get(firstVisiblePosition) instanceof ArticleDetailVideoSection) {

                    if (null != getPlayer() &&
                            (getPlayer().getState() == PlayerState.PLAYING || getPlayer().getState() == PlayerState.BUFFERING)) {

                    } else if (null != getPlayer() && mIsPlaying) {
                        getPlayer().play();
                        if (-1 != mPlayerState && NetworkCheckUtility.isNetworkAvailable(getActivity()))
                            getPlayer().seek(mPlayerState);
                        mPlayerState = getPlayer().getPosition();
                    }
                    LogUtils.LOGD("scroll", "visible");
                } else {
                    if (null != getPlayer() &&
                            (getPlayer().getState() == PlayerState.BUFFERING || getPlayer().getState() == PlayerState.PLAYING)) {
                        mPlayerState = getPlayer().getPosition();
                        mIsPlaying = true;
                        getPlayer().pause();
                    }
                    LogUtils.LOGD("scroll", "invisible");
                }
            }
        });
    }

    private RecyclerView.LayoutManager getLayoutManager() {
        final int GRID_SPAN_COUNT_ONE = 1;
        final int NUMBER_OF_GRIDS = getResources().getInteger(R.integer.number_of_grids);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_GRIDS);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mArticleDetailRecyclerViewAdapter.isCurrentItemRealtedArticle(position) ?
                        GRID_SPAN_COUNT_ONE : gridLayoutManager.getSpanCount();
            }
        });
        return gridLayoutManager;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        NotificationManager.getInstance().removeObserver(this);
    }

    /**
     * End of Fragment life cycle methods
     */

    /**
     * Start of Member Variable related methods
     */

    public void setRetryMessage(String message) {
        this.mRetryMessage.setText(message);
    }

    public void setArticleId(String mArticleId) {
        this.mArticleId = mArticleId;
    }

    public String getArticleId() {
        return mArticleId;
    }

    public void setArticleTitle(String mArticleTitle) {
        this.mArticleTitle = mArticleTitle;
    }

    public String getArticleTitle() {
        return mArticleTitle;
    }

    /**
     * End of Member Variable related methods
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCommentString = null;
    }

    /**
     * Start of RecyclerView related methods
     */

    private void fetchArticleDetailResponse() {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            ResponseCallback<ArticleDetailResponse> responseCallback = new ResponseCallback<ArticleDetailResponse>() {
                @Override
                public void success(ArticleDetailResponse articleDetailResponse) {
                    if (getActivity() != null && isAdded()) {
                        initArticle(articleDetailResponse);
                        mShareURL = articleDetailResponse.getData().getShareURL();
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        showRetryFrame(getString(R.string.server_error));
                    }
                }
            };
            NetworkAdapter.get(getContext()).getArticleScreenDetail(getArticleId(), responseCallback, getActivity());
        } else {
            if (OfflineArticleDataHandler.isArticleCached(getArticleId(), getActivity())) {
                initArticle(OfflineArticleDataHandler.getArticle(getArticleId(), getActivity()));
            } else {
                showRetryFrame(getString(R.string.no_network));
            }
        }
    }

    private void initArticle(ArticleDetailResponse articleDetailResponse) {
        ArticleDetailDataProvider.setArticleDetailData(articleDetailResponse.getData());
        if (ReactionDataHandler.isArticleReacted(articleDetailResponse.getData().getId(), getActivity()))
            ArticleDetailDataProvider.setArticleReaction(ReactionDataHandler.getReaction(articleDetailResponse.getData().getId(), getActivity()));
        showArticleData();
        setArticleDetailRecyclerViewData();
    }


    public void setArticleDetailRecyclerViewData() {
        mArticleDetailList.addAll(ArticleDetailDataProvider.getArticleDetailDataList());
        mArticleDetailRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void resetRecyclerViewData() {
        mArticleDetailList.clear();
        mArticleDetailList.addAll(ArticleDetailDataProvider.getArticleDetailDataList());
        int articleSize = mArticleDetailList.size();
        for (int i = 1; i < articleSize; i++) {
            mArticleDetailRecyclerViewAdapter.notifyItemChanged(i);
        }
    }

    public void resetVideoPlayerHeight() {
        if (null != getPlayer() && getPlayer().getVisibility() == View.VISIBLE) {
            GridLayoutManager layoutManager = ((GridLayoutManager) mArticleDetailRecyclerView.getLayoutManager());
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            if (null != mArticleDetailRecyclerView.getChildAt(firstVisiblePosition)) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mArticleDetailRecyclerView.getChildAt(firstVisiblePosition).getLayoutParams();
                DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = getResources().getDimensionPixelSize(R.dimen.article_detail_video_thumbnail_height);
                params.height = height;
                params.width = width;

                mArticleDetailRecyclerView.getChildAt(firstVisiblePosition).setLayoutParams(params);
            }
        }
    }

    /**
     * End of RecyclerView related methods
     */

    /**
     * Start of Button Click related methods
     */
    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        showProgress();
        fetchArticleDetailResponse();
    }

    /**
     * End of Button Click related methods
     */

    /**
     * Start of Toolbar related methods
     */

    private void initToolbar() {
        setToolbar(mToolbar);
        if (null != getArticleTitle()) {
            setToolbarTitle(getArticleTitle());
        } else {
            setToolbarTitle("News");
        }
        enableBackButton();
        onSearchClick();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    /**
     * End of Toolbar related methods
     */


    /**
     * Start of Screen Display related methods
     */

    private void showProgress() {
        mDataContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    private void showRetryFrame(String message) {
        mDataContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        setRetryMessage(message);
    }

    private void showArticleData() {
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mDataContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRelatedArticleSelection(Article article) {
        //mListener.onRelatedArticleSelection(article);
        loadArticleDetailFragment(article.getId());
        disableWebView();
    }

    @Override
    public void OnCommentSubmitButtonClick(String comment) {
        if (!UserDataProvider.getInstance().isLoggedIn(getActivity())) {
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
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    private boolean isValidComment(String comment) {
        boolean isValidComment = false;
        if (!TextUtils.isEmpty(comment.trim())) {
            isValidComment = true;
        }
        return isValidComment;
    }

    @Override
    public void onShareButtonClick() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            shareArticleData();
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @Override
    public void onViewMoreButtonClick() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            NavigationUtils.startViewMoreCommentsActivity(getContext(),
                    getArticleId());
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @Override
    public void getWebViewInstance(WebView webView) {
        if (null == mArticleWebView) {
            mArticleWebView = webView;

            mArticleWebView.setWebChromeClient(new WebChromeClient() {

                @Override
                public void onShowCustomView(View view,
                                             WebChromeClient.CustomViewCallback callback) {
                    try {
                        // if a view already exists then immediately terminate the new one
                        if (mCustomView != null) {
                            onHideCustomView();
                            return;
                        }
                        isWebviewVideoPlaying = true;
                        mArticleDetailRecyclerView.setVisibility(View.INVISIBLE);
                        // Stash the current state
                        mCustomView = view;
                        mOriginalOrientation = getActivity().getRequestedOrientation();
                        // Stash the custom view callback
                        mCustomViewCallback = callback;
                        hideViews();

                        // Add the custom view to the view hierarchy
                        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
                        mToolbar.setVisibility(View.INVISIBLE);
                        decor.addView(mCustomView, new FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT));

                        View decorView = getActivity().getWindow().getDecorView();
                        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);

                        if (!hasBackKey && !hasHomeKey) {
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        }
                        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        mWebviewVideoFullScreenState = FULL_SCREEN;
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onHideCustomView() {
                    try {
                        mWebviewVideoFullScreenState = POTRAIT_SCREEN;
                        // Remove the custom view
                        FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
                        decor.removeView(mCustomView);
                        mCustomView = null;

                        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
                        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
                        // Restore the state to it's original form
                        View decorView = getActivity().getWindow().getDecorView();
                        if (!hasBackKey && !hasHomeKey) {
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        }
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getActivity().setRequestedOrientation(mOriginalOrientation);

                        mToolbar.setVisibility(View.VISIBLE);

                        mArticleDetailRecyclerView.setVisibility(View.VISIBLE);
                        mCustomViewCallback.onCustomViewHidden();
                        mCustomViewCallback = null;
                        isWebviewVideoPlaying = false;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        mArticleDetailRecyclerView.setVisibility(View.VISIBLE);
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        isWebviewVideoPlaying = false;
                    }
                }


            });
        }
    }


    public boolean isWebviewVideoPlaying() {
        return isWebviewVideoPlaying;
    }

    public int getWebviewVideoFullScreenState() {
        return mWebviewVideoFullScreenState;
    }

    @Override
    public void onReacted(final String reaction) {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            ReactionModel reactionModel = new ReactionModel();
            reactionModel.setReaction(reaction);
            NetworkAdapter.get(getContext()).react(getArticleId(), reactionModel,
                    new ResponseCallback<GenericResponse<ReactionResponseModel>>() {
                        @Override
                        public void success(GenericResponse<ReactionResponseModel> reactionResponse) {
                            DialogUtils.cancelProgressDialog();
                            ReactionDataHandler.addReaction(getArticleId(), reaction, getActivity());
                            updateReaction(reaction, reactionResponse);
                        }

                        @Override
                        public void failure(RestError error) {
                            DialogUtils.cancelProgressDialog();
                            LogUtils.LOGD("", "");
                        }
                    });
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @Override
    public void getVideoPlayerInstance(JWPlayerView playerView) {
        mListener.getVideoPlayerInstance(playerView);
        mPlayerView = playerView;
    }

    private void updateReaction(String reaction, GenericResponse<ReactionResponseModel> reactionResponse) {
        ReactometerSection reactometerSection = null;
        for (IArticleDetailRecyclerViewItem item : mArticleDetailList) {
            if (item instanceof ReactometerSection) {
                reactometerSection = (ReactometerSection) item;
                break;
            }
        }
        if (null != reactometerSection) {
            reactometerSection.setLolValue(reactionResponse.getData().getLOL());
            reactometerSection.setOmgValue(reactionResponse.getData().getOMG());
            reactometerSection.setWowValue(reactionResponse.getData().getWOW());
            reactometerSection.setWtfValue(reactionResponse.getData().getWTF());
            reactometerSection.setReaction(reaction);
            refreshRecyclerItem(REACTOMETER_VIEW_TAG);
        }
    }


    private void loadArticleDetailFragment(String articleId) {
        getActivity().finish();
        NavigationUtils.startArticleDetailActivity(getContext(), articleId);
    }

    public void disableWebView() {
        if (null != mArticleWebView) {
            mArticleWebView.goBack();
        }
    }

    public WebView getWebview() {
        return mArticleWebView;
    }

    private void shareArticleData() {
        if (null != mShareURL) {
            ShareUtils.share(mShareURL, getActivity());
            disableWebView();
        }
    }

    /**
     * End of Screen Display related methods
     */

    public void updateArticleComment(GenericResponse<CommentResponse> commentResponse) {
        ArticleDetailCommentSection articleDetailCommentSection = null;
        ArrayList<Comment> commentArrayList = null;
        for (IArticleDetailRecyclerViewItem item : mArticleDetailList) {
            if (item instanceof ArticleDetailCommentSection) {
                articleDetailCommentSection = (ArticleDetailCommentSection) item;
                commentArrayList = articleDetailCommentSection.getCommentArrayList();
                break;
            }
        }

        if (null != articleDetailCommentSection) {
            if (null != commentArrayList) {
                Comment comment = commentResponse.getData().getCommentResponse();
                commentArrayList.add(0, comment);
            }
            articleDetailCommentSection.setComment(null);
            articleDetailCommentSection.setCommentsCount(articleDetailCommentSection.getCommentsCount() + 1);
            refreshRecyclerItem(COMMENT_VIEW_TAG);
        }

    }

    public void onUpdateCommentNumberEvent(CommentNumberChangeEvent event) {
        ArticleDetailCommentSection articleDetailCommentSection = null;
        for (IArticleDetailRecyclerViewItem item : mArticleDetailList) {
            if (item instanceof ArticleDetailCommentSection) {
                articleDetailCommentSection = (ArticleDetailCommentSection) item;
                break;
            }
        }
        if (null != articleDetailCommentSection) {
            articleDetailCommentSection.setCommentsCount(event.getNumberOfComments());
            articleDetailCommentSection.setCommentArrayList(event.getCommentArrayList());
            // mArticleDetailRecyclerViewAdapter.notifyDataSetChanged();
            refreshRecyclerItem(COMMENT_VIEW_TAG);
        }
    }

    @Override
    public void update(Notification notificationName, Bundle data) {
        CommentNumberChangeEvent event = data.getParcelable(Constants.EXTRA_COMMENT_CHANGE);
        onUpdateCommentNumberEvent(event);
    }


    public interface IArticleDetailFragmentInteractionListener {
        void getVideoPlayerInstance(JWPlayerView jwPlayerView);
    }

    public JWPlayerView getPlayer() {
        return mPlayerView;
    }

    public void hideViews() {
        if (null != getPlayer() && getPlayer().getVisibility() == View.VISIBLE) {
            ArticleDetailVideoSection articleDetailVideoSection = null;
            if (!mArticleDetailList.isEmpty()) {
                if (mArticleDetailList.get(0) instanceof ArticleDetailVideoSection) {
                    articleDetailVideoSection = (ArticleDetailVideoSection) mArticleDetailList.get(0);
                }
            }
            mArticleDetailList.clear();
            mArticleDetailList.add(articleDetailVideoSection);

            GridLayoutManager layoutManager = ((GridLayoutManager) mArticleDetailRecyclerView.getLayoutManager());
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();

            if (null != mArticleDetailRecyclerView && null != mArticleDetailRecyclerView.getChildAt(firstVisiblePosition)) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mArticleDetailRecyclerView.getChildAt(firstVisiblePosition).getLayoutParams();
                DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                int width = displayMetrics.widthPixels;
                int height = displayMetrics.heightPixels;
                params.height = height;
                params.width = width;
                mArticleDetailRecyclerView.getChildAt(firstVisiblePosition).setLayoutParams(params);
            }
        }
    }

    public void refreshRecyclerItem(int viewTag) {
        for (int i = 0; i < mArticleDetailList.size(); i++) {
            GridLayoutManager layoutManager = ((GridLayoutManager) mArticleDetailRecyclerView.getLayoutManager());
            int firstPosition = layoutManager.findFirstVisibleItemPosition();
            int selectedPositionInRecyclerView = i - firstPosition;
            if (selectedPositionInRecyclerView >= 0 && selectedPositionInRecyclerView < layoutManager.getChildCount()) {
                View selectedView = layoutManager.getChildAt(selectedPositionInRecyclerView);
                if (null != selectedView.getTag()) {
                    if ((int) selectedView.getTag() == viewTag) {
                        mArticleDetailRecyclerViewAdapter.notifyItemChanged(i);
                        break;
                    }
                }
            }
        }
    }
}
