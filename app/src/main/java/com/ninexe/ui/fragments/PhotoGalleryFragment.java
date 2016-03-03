/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.CoverFlowAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.ArticleDetailData;
import com.ninexe.ui.models.ArticleDetailResponse;
import com.ninexe.ui.models.ArticleMedia;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.ReactionModel;
import com.ninexe.ui.models.ReactionResponseModel;
import com.ninexe.ui.models.ReactometerSection;
import com.ninexe.ui.models.viewholders.ReactometerViewHolder;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DeviceUtils;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ShareUtils;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.ArticleDetailDataProvider;
import com.ninexe.ui.utils.dataproviders.PhotoGalleryDataProvider;
import com.ninexe.ui.utils.dataproviders.ReactionDataHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by nagesh on 20/10/15.
 */
public class PhotoGalleryFragment extends BaseFragment {

    public final static String PHOTO_GALLERY_FRAGMENT = "photo_gallery_fragment";
    private static String ARG_ARTICLE_ID = "arg_article_id";

    private String mArticleId;
    private OnPhotoGalleryFragmentInteractionListener mListener;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    @Bind(R.id.article_title)
    TextView mTitle;

    @Bind(R.id.author)
    TextView mAuthor;

    @Bind(R.id.divider_author)
    View mDividerAuthor;

    @Bind(R.id.published_at)
    TextView mPublishedAt;

    @Bind(R.id.views)
    TextView mViews;

    @Bind(R.id.image_slide_count)
    TextView mImageSlideCount;

    @Bind(R.id.reactometer_container)
    LinearLayout mReactometerContainer;

    @Bind(R.id.coverflow)
    FeatureCoverFlow mCoverFlow;

    @Bind(R.id.layoutContainer)
    LinearLayout mLayoutContainer;

    @Bind(R.id.empty_text)
    TextView mEmptyText;

    @Bind(R.id.single_image_layout)
    View mSingleImageLayout;

    @Bind(R.id.image)
    ImageView mSingleImage;

    @Bind(R.id.label)
    TextView mSingleLabel;

    @Bind(R.id.title_container)
    View mTitleContainer;


    private CoverFlowAdapter mAdapter;
    private ArrayList<ArticleMedia> mData = new ArrayList<>(0);
    private ReactometerViewHolder mReactometerViewHolder;


    public static PhotoGalleryFragment newInstance(String articleId) {
        Bundle args = new Bundle();
        args.putString(ARG_ARTICLE_ID, articleId);
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setArticleId(String articleId) {
        this.mArticleId = articleId;
    }

    public String getArticleId() {
        return mArticleId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnPhotoGalleryFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnPhotoGalleryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            setArticleId(getArguments().getString(ARG_ARTICLE_ID));
            PreferenceManager.storePhotoGalleryDetailCurrentPosition(Constants.PHOTO_GALLERY_DUMMY, getActivity());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        ButterKnife.bind(this, view);
        mReactometerContainer.setBackgroundColor(getResources().getColor(R.color.photo_gallery_grey));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        if (!ReactionDataHandler.isArticleReacted(getArticleId(), getActivity()))
            initReactometer();
        fetchArticleDetail();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mCoverFlow
                && null != mAdapter && Constants.PHOTO_GALLERY_DUMMY
                != PreferenceManager.getPhotoGalleryDetailCurrentPosition(getActivity())) {
            mCoverFlow.scrollToPosition(mData.size() + PreferenceManager.getPhotoGalleryDetailCurrentPosition(getActivity()));
        }
    }

    private void initReactometer() {
        getReactometerViewHolder().bindReactometerSectionData(new ReactometerSection());
    }

    private void initReactometer(ReactionResponseModel reactionResponseModel) {
        getReactometerViewHolder().bindReactometerSectionData(new ReactometerSection(), reactionResponseModel);
    }

    private void sendReaction(final String reaction) {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            final ReactionModel reactionModel = new ReactionModel();
            reactionModel.setReaction(reaction);
            NetworkAdapter.get(getContext()).react(getArticleId(), reactionModel, new ResponseCallback<GenericResponse<ReactionResponseModel>>() {
                @Override
                public void success(GenericResponse<ReactionResponseModel> reactionResponse) {
                    DialogUtils.cancelProgressDialog();
                    ReactionDataHandler.addReaction(getArticleId(), reaction, getActivity());
                    reactionResponse.getData().setReaction(reaction);
                    initReactometer(reactionResponse.getData());
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

    private void fetchArticleDetail() {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            ResponseCallback<ArticleDetailResponse> responseCallback = new ResponseCallback<ArticleDetailResponse>() {
                @Override
                public void success(ArticleDetailResponse articleDetailResponse) {
                    if (getActivity() != null && isAdded()) {
                        ArticleDetailDataProvider.setArticleDetailData(articleDetailResponse.getData());
                        if (ReactionDataHandler.isArticleReacted(articleDetailResponse.getData().getId(), getActivity())) {
                            ArticleDetailDataProvider
                                    .setArticleReaction(ReactionDataHandler.getReaction(articleDetailResponse.getData().getId(), getActivity()));
                            articleDetailResponse.getData().getReactionResponseModel().setReaction(ReactionDataHandler.getReaction(getArticleId(), getActivity()));
                            initReactometer(articleDetailResponse.getData().getReactionResponseModel());
                        }
                        initPhotoGalleryPagerContainer();
                        setArticleData();
                        mLayoutContainer.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                    }
                }
            };
            NetworkAdapter.get(getContext()).getArticleScreenDetail(getArticleId(), responseCallback, getActivity());
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    private void setArticleData() {
        ArticleDetailData articleDetailData = PhotoGalleryDataProvider.getInstance().getArticleData();
        ViewUtils.setText(mTitle, articleDetailData.getTitle());
        if (TextUtils.isEmpty(articleDetailData.getAuthorName())) {
            ViewUtils.hideView(mDividerAuthor);
        }
        ViewUtils.setText(mAuthor, articleDetailData.getAuthorName());
        ViewUtils.setText(mPublishedAt, DateTimeUtils.getDate(articleDetailData.getPublishedAt()));
        mViews.setText(String.valueOf(articleDetailData.getViews()));
    }


    private int getImagesCount() {
        return PhotoGalleryDataProvider.getInstance().getMediaList().size();
    }

    private void initPhotoGalleryPagerContainer() {
        ArrayList<ArticleMedia> mediaArrayList = PhotoGalleryDataProvider.getInstance().getMediaList();
        if (null != mediaArrayList && !mediaArrayList.isEmpty()) {
            if (mediaArrayList.size() > 1) {
                mData.addAll(PhotoGalleryDataProvider.getInstance().getMediaList());
                mAdapter = new CoverFlowAdapter(getActivity(), mData);
                mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mListener.onPhotoSelection(position);
                    }
                });

                mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                    @Override
                    public void onScrolledToPosition(int position) {
                        mImageSlideCount.setText(String.format("%d/%d", position + 1, getImagesCount()));
                    }

                    @Override
                    public void onScrolling() {
                        //TODO CoverFlow began scrolling
                        LogUtils.LOGD("scroll", "ss");
                    }
                });
                mCoverFlow.setAdapter(mAdapter);
                mCoverFlow.setShouldRepeat(false);
                mCoverFlow.scrollToPosition(mData.size());
                mEmptyText.setVisibility(View.GONE);
                mCoverFlow.setVisibility(View.VISIBLE);
                mSingleImageLayout.setVisibility(View.GONE);
            } else {
                mSingleImageLayout.setVisibility(View.VISIBLE);
                mEmptyText.setVisibility(View.GONE);
                mCoverFlow.setVisibility(View.GONE);
               /* mSingleLabel.setText(mediaArrayList.get(0).getDescription());*/
                //mSingleLabel.setVisibility(View.GONE);
                ViewUtils.setText(mSingleLabel, mediaArrayList.get(0).getDescription());
                if (TextUtils.isEmpty(mediaArrayList.get(0).getDescription())) {
                    setToolbarTitle(getString(R.string.memes));
                    mTitleContainer.setVisibility(View.GONE);
                }

                if (DeviceUtils.isTablet(getActivity())) {
                    Glide.with(getActivity())
                            .load(mediaArrayList.get(0).getFile())
                            .placeholder(R.drawable.placeholder_featured_images)
                            .override(getActivity().getResources().getDimensionPixelSize(R.dimen.cover_width), getActivity().getResources().getDimensionPixelSize(R.dimen.cover_height))
                            .dontAnimate()
                            .centerCrop()
                            .into(mSingleImage);
                } else {
                    Glide.with(getActivity())
                            .load(ViewUtils.getThumbnail(mediaArrayList.get(0).getFile(), Constants.IMAGE_THUMBNAIL))
                            .placeholder(R.drawable.placeholder_home_searchresult)
                            .override(getActivity().getResources().getDimensionPixelSize(R.dimen.cover_width), getActivity().getResources().getDimensionPixelSize(R.dimen.cover_height))
                            .dontAnimate()
                            .into(mSingleImage);
                }
                mSingleImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onPhotoSelection(0);
                    }
                });
            }
        } else {
            mCoverFlow.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
            mSingleImageLayout.setVisibility(View.GONE);
            mEmptyText.setText(getString(R.string.msg_no_photos));
        }
        DialogUtils.cancelProgressDialog();
    }

    @OnClick(R.id.btn_comment)
    void onCommentButtonClick() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            NavigationUtils.startViewMoreCommentsActivity(getActivity(), getArticleId());
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.photo_gallery));
        enableBackButton();
        onSearchClick();
    }


    public interface OnPhotoGalleryFragmentInteractionListener {
        void onPhotoSelection(int position);
    }

    private ReactometerViewHolder getReactometerViewHolder() {
        if (null == mReactometerViewHolder) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.reactometer, mReactometerContainer, true);
            mReactometerViewHolder = new ReactometerViewHolder(view,
                    new ReactometerViewHolder.IReactometerViewHolderClicks() {
                        @Override
                        public void onShareButtonClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                ShareUtils.share(ArticleDetailDataProvider.getArticleDetailData().getShareURL(), getActivity());
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }

                        @Override
                        public void onReacted(String reaction) {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                sendReaction(reaction);
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }
                    });
        }
        return mReactometerViewHolder;
    }
}
