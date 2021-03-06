/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.TabSectionRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.custom.MarginDecoration;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleMeta;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.EndlessRecyclerOnScrollListener;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.SubMenu;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;
import com.ninexe.ui.utils.dataproviders.SectionDetailDataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 19/10/15.
 */
public class SectionDetailFragment extends BaseFragment
        implements TabSectionRecyclerViewAdapter.OnTabSectionRecyclerAdapterInteractionListener {

    public static String SECTION_DETAIL_FRAGMENT = "section_detail_fragment";
    private static final String ARG_SECTION_ID = "arg_section_id";
    private static final String ARG_SUB_MENU = "arg_sub_menu";

    @Bind(R.id.section_detail_recycler_view)
    RecyclerView mSectionDetailRecyclerView;

    @Bind(R.id.progress_bar_container)
    View mProgressBarContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.data_container)
    SwipeRefreshLayout mSwipeContainer;

    @Bind(R.id.btn_retry)
    View mRetryButton;

    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = null;
    private OnSectionDetailFragmentInteractionListener mListener;
    private TabSectionRecyclerViewAdapter mTabSectionRecyclerViewAdapter;
    private ArrayList<ITabSectionRecyclerViewItem> mSectionDetailList = new ArrayList<>();
    private int mCurrentPage = 1;
    private SubMenu mSubMenu;
    private SectionDetailDataProvider mSectionDetailDataProvider;

    public static SectionDetailFragment newInstance(SubMenu subMenu) {
        Bundle args = new Bundle();
        //args.putString(ARG_SECTION_ID, sectionId);
        args.putParcelable(ARG_SUB_MENU, subMenu);
        SectionDetailFragment fragment = new SectionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getSectionId() {
        return mSubMenu.getId();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnSectionDetailFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnSectionDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            //mSectionId = getArguments().getString(ARG_SECTION_ID);
            mSubMenu = getArguments().getParcelable(ARG_SUB_MENU);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section_detail, container, false);
        ButterKnife.bind(this, view);
        initSectionDetailRecyclerView();
        showProgress();
        fetchArticles(getSectionId(), mCurrentPage, Constants.ARTICLES_PER_PAGE);
        setSwipeToRefresh();
        return view;
    }

    private void setSwipeToRefresh() {
        mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
                    clearRecyclerData();
                    mCurrentPage = 1;
                    fetchArticles(getSectionId(), mCurrentPage, Constants.ARTICLES_PER_PAGE);
                } else {
                    mSwipeContainer.setRefreshing(false);
                    DialogUtils.showNoNetworkDialog(getActivity());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mEndlessRecyclerOnScrollListener) {
            mEndlessRecyclerOnScrollListener.reset(0, true);
        }
    }

    private void fetchArticles(final String sectionId, int currentPage, int articlesPerPage) {
        if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
            if (!mSectionDetailList.isEmpty() && mSectionDetailList.size() > Constants.ARTICLES_PER_PAGE) {
                DialogUtils.showCustomProgressDialog(getActivity(), true);
            }
            ResponseCallback<ArticleResponse> responseCallback = new ResponseCallback<ArticleResponse>() {
                @Override
                public void success(ArticleResponse articleResponse) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                        mSwipeContainer.setRefreshing(false);
                        if (null != articleResponse && articleResponse.getMeta().getPage() == 1
                                && articleResponse.getArticles().isEmpty()) {
                            showEmptyContainer(getString(R.string.msg_empty_articles));
                        } else {
                            initSection(articleResponse);
                        }
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                        mSwipeContainer.setRefreshing(false);
                        showRetryFrame(getString(R.string.server_error));
                    }
                }
            };
            if (null != getQueryModifier()) {
                NetworkAdapter.get(getContext())
                        .getArticlesForQueryModifier(getQueryModifierUrl(getQueryModifier()),
                                getQueryModifierOptions(getQueryModifier()), currentPage,
                                articlesPerPage, responseCallback);
            } else {
                NetworkAdapter.get(getContext()).getArticles(sectionId, currentPage, articlesPerPage, responseCallback);
            }
        } else {
            if (OfflineArticleDataHandler.isSectionCached(getActivity(), sectionId)) {
                showSectionData();
                setRecyclerData(OfflineArticleDataHandler.getSectionData(getActivity(), getSectionId()));
            } else {
                showRetryFrame(getString(R.string.no_network));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            if (OfflineArticleDataHandler.isOfflineCacheEnabled(getActivity())) {
                OfflineArticleDataHandler.saveSectionData(getActivity(), getSectionId(), mSectionDetailList);
            }
        }
    }

    private String getQueryModifierUrl(String queryModifier) {
        String url = null;
        if (null != queryModifier) {
            String[] urlSplit = queryModifier.split("\\?");
            url = urlSplit[0];
        }
        return url;
    }

    private Map<String, String> getQueryModifierOptions(String queryModifier) {
        String queryParameter = null;
        String[] queryParameterArray = null;
        String[] tmpArray;
        Map<String, String> options = null;
        if (null != queryModifier) {
            String[] urlSplit = queryModifier.split("\\?");
            queryParameter = urlSplit[1];
            queryParameterArray = queryParameter.split("&");
            options = new HashMap<>();
            for (String str : queryParameterArray) {
                tmpArray = str.split("=");
                options.put(tmpArray[0], tmpArray[1]);
            }
        }
        return options;
    }

    public String getQueryModifier() {
        return mSubMenu.getQueryModifier();
    }

    private void initSection(ArticleResponse articleResponse) {
        showSectionData();
        if (null != articleResponse && articleResponse.getMeta().getPage() == 1) {
            mSectionDetailList.clear();
        }
        mSectionDetailDataProvider = new SectionDetailDataProvider();
        mSectionDetailDataProvider.setArticleResponse(articleResponse);
        setRecyclerData(mSectionDetailDataProvider.getGridRecentArticles());
    }

    private void clearRecyclerData() {
        mEndlessRecyclerOnScrollListener.reset(0, true);
        mSectionDetailList.clear();
        mTabSectionRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void setRecyclerData(ArrayList<ITabSectionRecyclerViewItem> gridRecentArticles) {
        mSectionDetailList.addAll(gridRecentArticles);
        mTabSectionRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initSectionDetailRecyclerView() {
        mSectionDetailRecyclerView.setHasFixedSize(true);
        setGridLayoutManager();
        mTabSectionRecyclerViewAdapter =
                new TabSectionRecyclerViewAdapter(getContext(), mSectionDetailList, this);
        mSectionDetailRecyclerView.setAdapter(mTabSectionRecyclerViewAdapter);
    }

    private void setGridLayoutManager() {
        final int GRID_SPAN_COUNT_ONE = 1;
        final int NUMBER_OF_GRIDS = getResources().getInteger(R.integer.number_of_grids);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_GRIDS);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mTabSectionRecyclerViewAdapter.isCurrentItemRelatedArticle(position) ?
                        GRID_SPAN_COUNT_ONE : gridLayoutManager.getSpanCount();
            }
        });
        mSectionDetailRecyclerView.setLayoutManager(gridLayoutManager);
        mSectionDetailRecyclerView.addItemDecoration(new MarginDecoration(getContext()));
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                    loadMoreArticles(currentPage);
                }
            }
        };
        mSectionDetailRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    private void loadMoreArticles(int currentPage) {
        ArticleMeta articleMeta = mSectionDetailDataProvider.getArticleResponse().getMeta();
        if (null != articleMeta) {
            if (currentPage <= articleMeta.getPages() && ((currentPage * Constants.ARTICLES_PER_PAGE) <= Constants.MAX_ARTICLES)) {
                fetchArticles(getSectionId(), currentPage, Constants.ARTICLES_PER_PAGE);
            }

        }
    }


    /**
     * Start of Button Click related methods
     */
    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            showProgress();
            mListener.onRetryButtonClick();
        }
        mSectionDetailList.clear();
        fetchArticles(getSectionId(), mCurrentPage, Constants.ARTICLES_PER_PAGE);
    }

    /**
     * End of Button Click related methods
     */

    /**
     * Start of Member Variable related methods
     */

    public void setRetryMessage(String message) {
        this.mRetryMessage.setText(message);
    }

    /**
     * End of Member Variable related methods
     */


    /**
     * Start of Screen Display related methods
     */

    private void showProgress() {
        mSwipeContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.VISIBLE);
    }

    private void showRetryFrame(String message) {
        mSwipeContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        setRetryMessage(message);
    }

    private void showSectionData() {
        mRetryFrameContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mSwipeContainer.setVisibility(View.VISIBLE);
    }

    private void showEmptyContainer(String message) {
        mSwipeContainer.setVisibility(View.GONE);
        mProgressBarContainer.setVisibility(View.GONE);
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        mRetryButton.setVisibility(View.GONE);
        setRetryMessage(message);
    }

    /**
     * End of Screen Display related methods
     */

    @Override
    public void onArticleSelection(Article article) {
        mListener.onArticleSelection(article, mSubMenu.getTitle());
    }

    public interface OnSectionDetailFragmentInteractionListener {
        void onArticleSelection(Article article, String articleTitle);

        void onRetryButtonClick();
    }

}
