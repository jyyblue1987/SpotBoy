/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.QuizOptionsAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.Option;
import com.ninexe.ui.models.PollResponse;
import com.ninexe.ui.models.Question;
import com.ninexe.ui.models.QuizAnswer;
import com.ninexe.ui.models.QuizAnswerModel;
import com.ninexe.ui.models.QuizArticle;
import com.ninexe.ui.models.QuizResponse;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.Theme;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends BaseFragment implements QuizOptionsAdapter.OnOptionSelectListener {

    public static final String QUIZ_FRAGMENT = "quiz_fragment";
    private static String ARG_ARTICLE_ID = "arg_article_id";

    @Bind(R.id.options_recycler_view)
    RecyclerView mOptionsView;

    @Bind(R.id.progress_layout)
    LinearLayout mProgressLayout;

    @Bind(R.id.progressBar)
    LinearLayout mProgressBar;

    @Bind(R.id.answered_count)
    TextView mAnsweredQuestionsView;

    @Bind(R.id.questionText)
    TextView mQuestionText;

    @Bind(R.id.totalCount)
    TextView mTotalCountView;

    @Bind(R.id.quizTitle)
    TextView mQuizTitle;

    @Bind(R.id.thumbnail)
    ImageView mThumbnail;

    @Bind(R.id.layoutContainer)
    LinearLayout mLayoutContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.attemptedText)
    TextView mAttemptedText;

    @Bind(R.id.questionLayout)
    LinearLayout mQuestionLayout;

    @Bind(R.id.q)
    TextView mQ;

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        getResponse();
    }


    private int mTotalQuestions, mAnsweredQuestions = 0;
    private QuizOptionsAdapter mQuizOptionsAdapter;
    private String mArticleId;
    private ArrayList<Option> mOptions;
    private ArrayList<Question> mQuestions;
    private ArrayList<QuizAnswer> mQuizAnswers = new ArrayList<>();
    private String mThumbnailUrl;
    private QuizInteractionListener mListener;
    private String mType;
    private String mArticleQuestion;
    private boolean mAlreadyAttempted = false;
    private String mShareURL;

    public QuizFragment() {
    }

    public boolean isAlreadyAttempted() {
        return mAlreadyAttempted;
    }

    public static QuizFragment newInstance(String articleId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ARTICLE_ID, articleId);
        QuizFragment fragment = new QuizFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mArticleId = (getArguments().getString(ARG_ARTICLE_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getResponse();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Theme currentTheme = PreferenceManager.getCurrentTheme(getActivity());
        switch (currentTheme) {
            case Blue:
                mQ.setBackgroundResource(R.drawable.left_curved_rectangle);
                break;
            case Red:
                mQ.setBackgroundResource(R.drawable.left_curved_rectangle_red);
                break;

        }
    }

    private void initQuiz(QuizArticle quizArticle) {
        mLayoutContainer.setVisibility(View.VISIBLE);
        checkLoginStatus(quizArticle.isRequiresLogin(), quizArticle.isAttempted(), quizArticle.getType());
        mTotalQuestions = quizArticle.getQuestions().size();
        mProgressLayout.setWeightSum(mTotalQuestions);
        mTotalCountView.setText("" + mTotalQuestions);
        mType = quizArticle.getType();
        mArticleQuestion = quizArticle.getTitle();
        if (TextUtils.equals(quizArticle.getType(), Constants.PERSONALITY_TEST)) {
            mQuizTitle.setVisibility(View.VISIBLE);
            mQuizTitle.setText(quizArticle.getTitle());
        }
        mShareURL = quizArticle.getShareURL();
        updateProgressBar();
        mThumbnailUrl = quizArticle.getThumbnail();
        Glide.with(getActivity())
                .load(ViewUtils.getThumbnail(mThumbnailUrl, Constants.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.placeholder_featured_images)
                .centerCrop()
                .crossFade()
                .into(mThumbnail);
        mQuestions = quizArticle.getQuestions();
        mQuestionText.setText(quizArticle.getQuestions().get(mAnsweredQuestions).getQuestionText());
        mOptions = quizArticle.getQuestions().get(mAnsweredQuestions).getOptions();
        mQuizOptionsAdapter = new
                QuizOptionsAdapter(mOptions);
        mQuizOptionsAdapter.setListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mOptionsView.setLayoutManager(linearLayoutManager);
        mOptionsView.setAdapter(mQuizOptionsAdapter);
    }

    private void checkLoginStatus(boolean requiresLogin, boolean isAttempted, String type) {
        if (TextUtils.equals(type, Constants.PERSONALITY_TEST)) {
            type = getString(R.string.personality_test);
        }
        if (requiresLogin && !UserDataProvider.getInstance().isLoggedIn(getActivity())) {
            DialogUtils.showAlertDialogWithCallBack(getActivity(), "", getString(R.string.login_required) + " " + type, getString(R.string.login),
                    getString(R.string.cancel), true, new DialogUtils.DialogInterfaceCallBack() {
                        @Override
                        public void positiveButtonClick(DialogInterface dialog) {
                            dialog.dismiss();
                            mListener.login();
                        }

                        @Override
                        public void negativeButtonClick(DialogInterface dialog) {
                            dialog.dismiss();
                            mListener.onQuit();
                        }
                    }, false);
        } else if (requiresLogin && isAttempted && UserDataProvider.getInstance().isLoggedIn(getActivity())) {
            mAlreadyAttempted = true;
            mProgressLayout.setVisibility(View.GONE);
            mQuestionLayout.setVisibility(View.GONE);
            mOptionsView.setVisibility(View.GONE);
            mAttemptedText.setVisibility(View.VISIBLE);
            mAttemptedText.setText(getString(R.string.quiz_attempted) + " " + type);
        }
    }

    public void getResponse() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            mRetryFrameContainer.setVisibility(View.GONE);
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            ResponseCallback<GenericResponse<QuizArticle>> responseCallback = new ResponseCallback<GenericResponse<QuizArticle>>() {
                @Override
                public void success(GenericResponse<QuizArticle> quizArticleGenericResponse) {
                    DialogUtils.cancelProgressDialog();
                    initQuiz(quizArticleGenericResponse.getData());
                    initToolbar();
                }

                @Override
                public void failure(RestError error) {
                    if (getActivity() != null && isAdded()) {
                        DialogUtils.cancelProgressDialog();
                        showRetryFrame(getString(R.string.server_error));
                    }
                }
            };
            boolean isLoggedIn = UserDataProvider.getInstance().isLoggedIn(getActivity());
            NetworkAdapter.get(getContext()).getQuizArticleDetail(getActivity(), getArticleId(), responseCallback, isLoggedIn);
        } else {
            showRetryFrame(getString(R.string.no_network));
        }
    }

    private void showRetryFrame(String message) {
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        setRetryMessage(message);
    }

    public void setRetryMessage(String message) {
        this.mRetryMessage.setText(message);
    }


    private String getArticleId() {
        return mArticleId;
    }

    private void updateProgressBar() {
        mAnsweredQuestionsView.setText(Integer.toString(mAnsweredQuestions + 1));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = mAnsweredQuestions + 1;
        mProgressBar.setLayoutParams(params);
        mProgressBar.requestLayout();
    }

    @Override
    public void onOptionSelect(Option option) {

        final QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setQuestionId(mQuestions.get(mAnsweredQuestions).getQuestionId());
        quizAnswer.setOptionId(option.getOptionId());
        mQuizAnswers.add(quizAnswer);

        mAnsweredQuestions++;
        if (mAnsweredQuestions == mTotalQuestions) {
            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                postAnswers();
            } else {
                showNoNetworkDialog();
            }
            return;
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOptionsView.scrollToPosition(0);
                mQuestionText.setText(mQuestions.get(mAnsweredQuestions).getQuestionText());
                mOptions.clear();
                mOptions.addAll(mQuestions.get(mAnsweredQuestions).getOptions());
                mQuizOptionsAdapter.notifyDataSetChanged();
                updateProgressBar();
            }
        }, 500);

    }

    private void showNoNetworkDialog() {
        DialogUtils.showAlertDialogWithCallBack(getActivity(), "", getString(R.string.no_network), getString(R.string.ok), "", false, new DialogUtils.DialogInterfaceCallBack() {
            @Override
            public void positiveButtonClick(DialogInterface dialog) {
                mListener.onQuit();
            }

            @Override
            public void negativeButtonClick(DialogInterface dialog) {

            }
        }, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (QuizInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    public interface QuizInteractionListener {
        void onQuit();

        void onResult(QuizResponse quizResponse);

        void login();
    }

    private void postAnswers() {
        DialogUtils.showCustomProgressDialog(getActivity(), true);
        ResponseCallback<GenericResponse<QuizResponse>> responseCallback = new ResponseCallback<GenericResponse<QuizResponse>>() {
            @Override
            public void success(GenericResponse<QuizResponse> quizResponse) {
                quizResponse.getData().setThumbnail(mThumbnailUrl);
                quizResponse.getData().setQuestion(mArticleQuestion);
                quizResponse.getData().setType(mType);
                quizResponse.getData().setShareURL(mShareURL);
                mListener.onResult(quizResponse.getData());
                DialogUtils.cancelProgressDialog();
            }

            @Override
            public void failure(RestError error) {
                DialogUtils.cancelProgressDialog();
                DialogUtils.showSingleButtonAlertDialog(getActivity(), "", getString(R.string.server_error), getString(R.string.ok));
            }
        };
        QuizAnswerModel quizAnswerModel = new QuizAnswerModel();
        quizAnswerModel.setAnswers(mQuizAnswers);
        NetworkAdapter.get(getContext()).getQuizResult(getArticleId(), quizAnswerModel, responseCallback);
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        if (TextUtils.equals(mType, Constants.PERSONALITY_TEST))
            setToolbarTitle(getString(R.string.personality_test));
        else
            setToolbarTitle(mType);
        enableBackButton();
        displaySearch(false);
    }
}
