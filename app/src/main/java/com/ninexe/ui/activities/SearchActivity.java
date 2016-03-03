/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.TabSectionRecyclerViewAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.custom.DividerItemDecoration;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleMeta;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.EndlessRecyclerOnScrollListener;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.PollDataHandler;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.AdsUtil;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.dataproviders.SearchResultsDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 13/10/15.
 */
public class SearchActivity extends BaseActivity
        implements TabSectionRecyclerViewAdapter.OnTabSectionRecyclerAdapterInteractionListener {


    @Bind(R.id.search_result_recycler_view)
    RecyclerView mSearchResultsRecyclerView;

    @Bind(R.id.searchTextView)
    EditText mSearchSrcTextView;

    @Bind(R.id.action_empty_btn)
    ImageButton mEmptyBtn;

    @Bind(R.id.empty_view)
    TextView mEmptyView;

    @Bind(R.id.adView)
    AdView mAdView;

    private int mCurrentPage = 1;
    private TabSectionRecyclerViewAdapter mTabSectionRecyclerViewAdapter;
    private ArrayList<ITabSectionRecyclerViewItem> mSearchResultsList = new ArrayList<>();
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        loadAd();
        initSearchView();
        showSearch();
        initSectionDetailRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mEndlessRecyclerOnScrollListener) {
            mEndlessRecyclerOnScrollListener.reset(0, true);
        }
    }

    private void loadAd() {
        AdsUtil.loadBannerAd(this, mAdView);
    }

    private void initSectionDetailRecyclerView() {
        mSearchResultsRecyclerView.setHasFixedSize(true);
        setSearchResultsLayoutManager();
        mTabSectionRecyclerViewAdapter =
                new TabSectionRecyclerViewAdapter(this, mSearchResultsList, this);
        mSearchResultsRecyclerView.setAdapter(mTabSectionRecyclerViewAdapter);
    }

    private void setSearchResultsLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mSearchResultsRecyclerView.setLayoutManager(linearLayoutManager);
        mSearchResultsRecyclerView.setHasFixedSize(true);
        mSearchResultsRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                if (NetworkCheckUtility.isNetworkAvailable(SearchActivity.this)) {
                    loadMoreArticles(currentPage);
                }
            }
        };
        mSearchResultsRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);
    }

    private void loadMoreArticles(int currentPage) {
        ArticleMeta articleMeta = SearchResultsDataProvider.getInstance().getArticleResponse().getMeta();
        if (null != articleMeta) {
            if (currentPage <= articleMeta.getPages() && ((currentPage * Constants.ARTICLES_PER_PAGE) <= Constants.MAX_ARTICLES))
                getSearchResults(getQueryText(), currentPage);

        }
    }

    private void showSearch() {
        //Request Focus
        mSearchSrcTextView.setText(null);
        mSearchSrcTextView.requestFocus();
        showKeyboard(mSearchSrcTextView);
    }

    private void initSearchView() {

        mSearchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onSubmitQuery();
                return true;
            }
        });

        mSearchSrcTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchActivity.this.onTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*mSearchSrcTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showKeyboard(mSearchSrcTextView);
                }
            }
        });*/
    }

    private void onSubmitQuery() {
        hideKeyboard(mSearchSrcTextView);
        if (null != getQueryText()) {
            mSearchResultsList.clear();
            getSearchResults(getQueryText(), mCurrentPage);
        }
    }

    private String getQueryText() {
        String queryText = null;
        CharSequence query = mSearchSrcTextView.getText();
        if (query != null && TextUtils.getTrimmedLength(query) > 0)
            queryText = query.toString().trim();
        return queryText;
    }

    private void getSearchResults(String query, int page) {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            DialogUtils.showCustomProgressDialog(this, true);
            ResponseCallback<ArticleResponse> responseCallback = new ResponseCallback<ArticleResponse>() {
                @Override
                public void success(ArticleResponse articleResponse) {
                    if (SearchActivity.this != null) {
                        DialogUtils.cancelProgressDialog();
                        SearchResultsDataProvider.getInstance().setArticleResponse(articleResponse);
                        setRecyclerData(SearchResultsDataProvider.getInstance().getSearchResultsArticles());
                    }
                }

                @Override
                public void failure(RestError error) {
                    if (SearchActivity.this != null) {
                        DialogUtils.cancelProgressDialog();
                    }
                }
            };
            NetworkAdapter.get(this).getArticlesForSearch(query, page, Constants.ARTICLES_PER_PAGE, responseCallback);
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

    private void setRecyclerData(ArrayList<ITabSectionRecyclerViewItem> searchResultArticles) {
        if (!searchResultArticles.isEmpty()) {
            hideEmptyView();
            mSearchResultsList.addAll(searchResultArticles);
            mTabSectionRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            showEmptyView();
        }

    }

    private void onTextChanged(CharSequence newText) {
        CharSequence text = mSearchSrcTextView.getText();
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            mEmptyBtn.setVisibility(View.VISIBLE);
        } else {
            mEmptyBtn.setVisibility(View.GONE);
        }
    }

    private void closeSearch() {
        mSearchSrcTextView.setText(null);
        dismissSuggestions();
        clearFocus();
        finish();
    }

    public void clearFocus() {
        hideKeyboard(mSearchSrcTextView);
        mSearchSrcTextView.clearFocus();
    }

    /**
     * Dismiss the suggestions list.
     */
    public void dismissSuggestions() {
        if (mSearchResultsRecyclerView.getVisibility() == View.VISIBLE) {
            mSearchResultsRecyclerView.setVisibility(View.GONE);
        }
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        view.performClick();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*private void showKeyboard(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1 && view.hasFocus()) {
            view.clearFocus();
        }
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }*/


    @OnClick(R.id.action_up_btn)
    void onBackButtonClick() {
        mAdView.setVisibility(View.GONE);
        closeSearch();
    }

    @OnClick(R.id.action_empty_btn)
    void onClearButtonClick() {
        mSearchSrcTextView.setText(null);
    }


    @Override
    public void onArticleSelection(Article article) {
        //this.finish();
        if (article.isPhoto()) {
            NavigationUtils.startPhotoGalleryActivity(this, article.getId());
        } else if (article.isQuiz()) {
            if (TextUtils.equals(article.getType(), Constants.POLL)) {
                if (PollDataHandler.isPollAttempted(article.getId(), this)) {
                    DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.poll_attemted), getString(R.string.ok));
                    return;
                }
            }
            NavigationUtils.startQuizActivity(this, article.getId(), article.getType());
        } else {
            NavigationUtils.startArticleDetailActivity(this, article.getId(), getQueryText());
        }
    }

    private void showEmptyView() {
        mEmptyView.setText(String.format(getString(R.string.msg_search_no_results), getQueryText()));
        mSearchResultsRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        mEmptyView.setVisibility(View.GONE);
        mSearchResultsRecyclerView.setVisibility(View.VISIBLE);

    }
}
