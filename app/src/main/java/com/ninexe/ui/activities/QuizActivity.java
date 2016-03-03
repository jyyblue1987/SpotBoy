/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.PollsFragment;
import com.ninexe.ui.fragments.PollsResultFragment;
import com.ninexe.ui.fragments.QuizFragment;
import com.ninexe.ui.fragments.QuizResultFragment;
import com.ninexe.ui.models.PollResults;
import com.ninexe.ui.models.QuizResponse;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.RateAppUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuizActivity extends BaseActivity implements QuizFragment.QuizInteractionListener, PollsFragment.PollInteractionListener {

    private static final int LOGIN_ACTIVITY = 1;
    private QuizFragment mQuizFragment;
    private static final String QUIZ_RESPONSE = "quiz_response";

    @Bind(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        ButterKnife.bind(this);

        switch (getArticleType()) {
            case Constants.QUIZ:
                loadQuizFragment();
                break;
            case Constants.PERSONALITY_TEST:
                loadQuizFragment();
                break;
            case Constants.POLL:
                loadPollsFragment();
                break;
            case Constants.CONTEST:
                loadQuizFragment();
                break;
        }
        loadAd(mAdView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RateAppUtil.showRateDialog(this);
    }

    private void loadQuizFragment() {
        mQuizFragment = QuizFragment.newInstance(getArticleId());
        loadFragment(R.id.quizFrameContainer, mQuizFragment, QuizFragment.QUIZ_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }

    private void loadPollsFragment() {
        PollsFragment pollsFragment = PollsFragment.newInstance(getArticleId());
        loadFragment(R.id.quizFrameContainer, pollsFragment, PollsFragment.POLLS_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }

    private void loadPollResultFragment(PollResults pollResults) {
        PollsResultFragment pollsResultFragment = PollsResultFragment.newInstance(pollResults);
        loadFragment(R.id.quizFrameContainer, pollsResultFragment, PollsResultFragment.class.getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }

    private void loadQuizResultFragment(QuizResponse quizResponse) {
        mQuizFragment = null;
        QuizResultFragment quizResultFragment = QuizResultFragment.newInstance(quizResponse);
        loadFragment(R.id.quizFrameContainer, quizResultFragment, QuizResultFragment.class.getName(), 0, 0,
                BaseFragment.FragmentTransactionType.ADD);
    }

    @Override
    public void onQuit() {
        finish();
    }

    @Override
    public void onResult(QuizResponse quizResponse) {
        loadQuizResultFragment(quizResponse);
    }

    @Override
    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY);
        // NavigationUtils.startLoginActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (null != mQuizFragment && !mQuizFragment.isAlreadyAttempted()) {
            showConfirmationDialog();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmationDialog() {
        String type = getArticleType();
        if (TextUtils.equals(getArticleType(), Constants.PERSONALITY_TEST)) {
            type = getString(R.string.personality_test);
        }
        DialogUtils.showAlertDialogWithCallBack(QuizActivity.this, "", getString(R.string.quiz_cuit) + " " + type + "?", getString(R.string.yes),
                getString(R.string.no), true, new DialogUtils.DialogInterfaceCallBack() {
                    @Override
                    public void positiveButtonClick(DialogInterface dialog) {
                        finish();
                    }

                    @Override
                    public void negativeButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    private String getArticleId() {
        String articleId = null;
        if (null != getIntent().getExtras()) {
            articleId = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_ID);
        }
        return articleId;
    }

    private String getArticleType() {
        String articleType = null;
        if (null != getIntent().getExtras()) {
            articleType = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_TYPE);
        }
        return articleType;
    }

    @Override
    public void onPollResult(PollResults pollResults) {
        loadPollResultFragment(pollResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QuizFragment quizFragment = (QuizFragment) getSupportFragmentManager().findFragmentByTag(QuizFragment.QUIZ_FRAGMENT);
        quizFragment.getResponse();
    }
}
