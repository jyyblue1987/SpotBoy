/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.ArticleDetailArticleContentSection;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.utils.DateTimeUtils;
import com.ninexe.ui.utils.DeviceUtils;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 14/10/15.
 */
public class ArticleDetailArticleContentSectionViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.author)
    TextView author;

    @Bind(R.id.published_at)
    TextView publishedAt;

    @Bind(R.id.views)
    TextView views;

    @Bind(R.id.short_body)
    TextView shortBody;

    @Bind(R.id.article_detail_web_view)
    WebView articleContentWebView;

    @Bind(R.id.btn_read_more)
    Button readMoreButton;

    ArticleDetailArticleContentSection articleDetailArticleContentSection;

    private View mCustomView;
    private int mOriginalSystemUiVisibility;
    private int mOriginalOrientation;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    private IArticleContentSectionViewHolderClicks listener;
    private Context context;

    public ArticleDetailArticleContentSectionViewHolder(View itemView,
                                                        IArticleContentSectionViewHolderClicks articleContentSectionViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        if (null == context) {
            context = itemView.getContext();
        }
        listener = articleContentSectionViewHolderClicks;
        listener.getWebViewInstance(articleContentWebView);
    }


    public void bindArticleContentSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ArticleDetailArticleContentSection) {
            articleDetailArticleContentSection = (ArticleDetailArticleContentSection) articleDetailRecyclerViewItem;
            ViewUtils.setText(title, articleDetailArticleContentSection.getTitle());
            if (TextUtils.isEmpty(articleDetailArticleContentSection.getAuthorName())) {
                ViewUtils.hideView(author);
            }
            ViewUtils.setText(author, articleDetailArticleContentSection.getAuthorName());
            ViewUtils.setText(publishedAt, DateTimeUtils.getDate(articleDetailArticleContentSection.getPublishedAt()));
            ViewUtils.setText(shortBody, articleDetailArticleContentSection.getShortBody());
            // views.setText(String.valueOf(articleDetailArticleContentSection.getViews()));
            views.setText(ViewUtils.getNumberWithSuffix(articleDetailArticleContentSection.getViews()));
            setArticleWebViewConfigurationProperties();
            setArticleWebViewData();
        }
    }

    private void setArticleWebViewConfigurationProperties() {
        articleContentWebView.getSettings().setJavaScriptEnabled(true);
        articleContentWebView.setLongClickable(false);
    }

    private void setArticleWebViewData() {
        ViewUtils.showView(articleContentWebView);
        if (articleDetailArticleContentSection.isBodyTrimmed() &&
                !articleDetailArticleContentSection.isReadMoreClicked()) {
            loadWebViewData(articleDetailArticleContentSection.getTrimmedBody());
        } else {
            ViewUtils.hideView(readMoreButton);
            loadWebViewData(articleDetailArticleContentSection.getBody());
        }
    }

    @OnClick(R.id.btn_read_more)
    void onReadMoreButtonClick() {
//        DialogUtils.showCustomProgressDialog(context, true);
        LogUtils.LOGD(Constants.APP_TAG, "Clicked on Read More Button");
        articleDetailArticleContentSection.setIsReadMoreClicked(true);
        listener.onReadMoreButtonClick();
        ViewUtils.hideView(readMoreButton);
        loadWebViewData(articleDetailArticleContentSection.getBody());
    }


    private void loadWebViewData(String data) {
        articleContentWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
//                DialogUtils.cancelProgressDialog();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (NetworkCheckUtility.isNetworkAvailable(context)) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    DialogUtils.showNoNetworkDialog(context);
                }
                return true;
            }
        });
        if (!TextUtils.isEmpty(data.trim())) {
            articleContentWebView.loadDataWithBaseURL(null, data, Constants.TEXT_HTML_TYPE, Constants.ENCODING_UTF_8, null);
            if (-1 != PreferenceManager.getWebViewTextSize(context)) {
                articleContentWebView.getSettings().setTextZoom(PreferenceManager.getWebViewTextSize(context));
            }

            articleContentWebView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });
        } else {
            articleContentWebView.setVisibility(View.GONE);
        }
    }

    public interface IArticleContentSectionViewHolderClicks {
        void onReadMoreButtonClick();

        void getWebViewInstance(WebView webView);
    }

}
