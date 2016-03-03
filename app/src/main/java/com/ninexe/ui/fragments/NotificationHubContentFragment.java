/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.NotificationHubRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleMeta;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.EndlessRecyclerOnScrollListener;
import com.ninexe.ui.models.INotificationHubRecyclerViewItem;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.dataproviders.NotificationHubDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 29/10/15.
 */
public class NotificationHubContentFragment extends BaseFragment implements NotificationHubRecyclerViewAdapter.INotificationHubAdapterListener {

    private static String ARG_SECTION_ID = "arg_section_id";

    @Bind(R.id.notification_hub_content_recycler_view)
    RecyclerView mNotificationHubContentRecyclerView;

    @Bind(R.id.progress_bar_container)
    View mProgressBarContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.data_container)
    View mDataContainer;

    private NotificationHubRecyclerViewAdapter mAdapter;
    private ArrayList<INotificationHubRecyclerViewItem> mDataSet = new ArrayList<>();
    private String mSectionId;
    private int mCurrentPage = 1;

    public static NotificationHubContentFragment newInstance(String sectionId) {
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_ID, sectionId);
        NotificationHubContentFragment fragment = new NotificationHubContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mSectionId = getArguments().getString(ARG_SECTION_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_hub_content, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        showProgress();
        fetchSectionArticles(getSectionId(), mCurrentPage, Constants.ARTICLES_PER_PAGE);
    }

    private String getSectionId() {
        return mSectionId;
    }

    private void fetchSectionArticles(final String sectionId, int currentPage, int articlesPerPage) {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            ResponseCallback<ArticleResponse> responseCallback = new ResponseCallback<ArticleResponse>() {
                @Override
                public void success(ArticleResponse articleResponse) {
                    if (getActivity() != null && isAdded()) {

                        if (null != articleResponse && articleResponse.getMeta().getPage() == 1
                                && articleResponse.getArticles().isEmpty()) {
                            showEmptyContainer(getString(R.string.msg_empty_notifications));
                        } else {
                            showSectionData();
                            NotificationHubDataProvider.getInstance().setArticleResponse(articleResponse);
                            updateRecyclerViewData(NotificationHubDataProvider.getInstance().getNotificationHubSectionList());
                        }

                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        showRetryFrame(getString(R.string.server_error));
                    }
                }
            };
            NetworkAdapter.get(getContext()).getArticles(sectionId, currentPage, articlesPerPage, responseCallback);
        } else {
            showRetryFrame(getString(R.string.no_network));
        }
    }

    private void initRecyclerView() {
        mAdapter = new NotificationHubRecyclerViewAdapter(mDataSet, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mNotificationHubContentRecyclerView.setLayoutManager(linearLayoutManager);
        mNotificationHubContentRecyclerView.setAdapter(mAdapter);
        mNotificationHubContentRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                    loadMoreArticles(currentPage);
                }
            }
        });
    }

    private void loadMoreArticles(int currentPage) {
        ArticleMeta articleMeta = NotificationHubDataProvider.getInstance().getArticleResponse().getMeta();
        if (null != articleMeta) {
            if (currentPage <= articleMeta.getPages() && ((currentPage * Constants.ARTICLES_PER_PAGE) <= Constants.MAX_ARTICLES)) {
                fetchSectionArticles(getSectionId(), currentPage, Constants.ARTICLES_PER_PAGE);
            }
        }
    }

    private void updateRecyclerViewData(ArrayList<INotificationHubRecyclerViewItem>
                                                notificationHubRecyclerViewItems) {
        mDataSet.addAll(notificationHubRecyclerViewItems);
        mAdapter.notifyDataSetChanged();
    }

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
        getView().findViewById(R.id.btn_retry).setVisibility(View.VISIBLE);
        setRetryMessage(message);
    }

    private void showSectionData() {
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mDataContainer.setVisibility(View.VISIBLE);
    }

    private void showEmptyContainer(String message) {
        mDataContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        getView().findViewById(R.id.btn_retry).setVisibility(View.GONE);
        setRetryMessage(message);
    }

    public void setRetryMessage(String message) {
        this.mRetryMessage.setText(message);
    }

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        fetchSectionArticles(getSectionId(), mCurrentPage, Constants.ARTICLES_PER_PAGE);
    }

    /**
     * End of Screen Display related methods
     */

    @Override
    public void onArticleClick(Article article) {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            NavigationUtils.startArticleDetailActivity(getActivity(), article.getId());
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }

    }
}
