/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.QuizOptionsAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.Option;
import com.ninexe.ui.models.PollDataHandler;
import com.ninexe.ui.models.PollResponse;
import com.ninexe.ui.models.PollResultModel;
import com.ninexe.ui.models.PollResults;
import com.ninexe.ui.models.Question;
import com.ninexe.ui.models.QuizAnswer;
import com.ninexe.ui.models.QuizAnswerModel;
import com.ninexe.ui.models.QuizArticle;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollsFragment extends BaseFragment implements QuizOptionsAdapter.OnOptionSelectListener {

    public static final String POLLS_FRAGMENT = "polls_fragment";
    private ArrayList<QuizAnswer> mQuizAnswers = new ArrayList<>();
    @Bind(R.id.options_recycler_view)
    RecyclerView mOptionsView;

    @Bind(R.id.thumbnail)
    ImageView mThumbnail;

    @Bind(R.id.questionText)
    TextView mQuestionText;

    @Bind(R.id.layoutContainer)
    LinearLayout mLayoutContainer;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        getResponse();
    }

    private PollInteractionListener mListener;
    private PollResults mPollResults = new PollResults();
    private String mShareURL;

    @OnClick(R.id.goBtn)
    void onGo() {
        if (isSelected) {
            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                postAnswer();
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    private void postAnswer() {
        QuizAnswerModel quizAnswerModel = new QuizAnswerModel();
        quizAnswerModel.setAnswers(mQuizAnswers);
        ResponseCallback<GenericResponse<PollResponse>> responseCallback = new ResponseCallback<GenericResponse<PollResponse>>() {
            @Override
            public void success(GenericResponse<PollResponse> pollsResponse) {
                DialogUtils.cancelProgressDialog();
                PollDataHandler.addPollData(getActivity(), getArticleId());
                ArrayList<PollResultModel> pollResultModels = new ArrayList<>();
                for (int i = 0; i < mOptions.size(); i++) {
                    PollResultModel pollResultModel = new PollResultModel();
                    pollResultModel.setOption(mOptions.get(i).getOptionText());
                    pollResultModel.setPercentage(pollsResponse.getData().getAnswerPercent().get(Integer.toString(mOptions.get(i).getOptionId())));
                    pollResultModels.add(pollResultModel);
                }
                mPollResults.setPollResultModels(pollResultModels);
                mPollResults.setShareURL(mShareURL);
                mListener.onPollResult(mPollResults);
            }

            @Override
            public void failure(RestError error) {
                DialogUtils.cancelProgressDialog();
                DialogUtils.showSingleButtonAlertDialog(getActivity(), "", getString(R.string.server_error), getString(R.string.ok));
            }
        };
        NetworkAdapter.get(getContext()).getPollResult(getArticleId(), quizAnswerModel, responseCallback);
    }


    private String mArticleId;
    private QuizOptionsAdapter mQuizOptionsAdapter;
    private ArrayList<Option> mOptions;
    private ArrayList<Question> mQuestions;
    private boolean isSelected = false;


    public PollsFragment() {
    }

    public static PollsFragment newInstance(String articleId) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_ARTICLE_ID, articleId);
        PollsFragment fragment = new PollsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_polls, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        getResponse();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mArticleId = (getArguments().getString(Constants.ARG_ARTICLE_ID));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PollInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    private String getArticleId() {
        return mArticleId;
    }

    private void getResponse() {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            mRetryFrameContainer.setVisibility(View.GONE);
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            ResponseCallback<GenericResponse<QuizArticle>> responseCallback = new ResponseCallback<GenericResponse<QuizArticle>>() {
                @Override
                public void success(GenericResponse<QuizArticle> quizArticleGenericResponse) {
                    DialogUtils.cancelProgressDialog();
                    initQuiz(quizArticleGenericResponse.getData());
                    mShareURL = quizArticleGenericResponse.getData().getShareURL();
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
            NetworkAdapter.get(getContext()).getQuizArticleDetail(getActivity(),getArticleId(), responseCallback, isLoggedIn);
        } else {
            showRetryFrame(getString(R.string.no_network));
        }
    }

    private void initQuiz(QuizArticle quizArticle) {
        mLayoutContainer.setVisibility(View.VISIBLE);
        Glide.with(getActivity())
                .load(ViewUtils.getThumbnail(quizArticle.getThumbnail(), Constants.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.placeholder_featured_images)
                .centerCrop()
                .crossFade()
                .into(mThumbnail);

        mPollResults.setThumbnail(quizArticle.getThumbnail());
        mQuestionText.setText(quizArticle.getQuestions().get(0).getQuestionText());
        mOptions = quizArticle.getQuestions().get(0).getOptions();
        mQuestions = quizArticle.getQuestions();
        mQuizOptionsAdapter = new
                QuizOptionsAdapter(mOptions);
        mQuizOptionsAdapter.setListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mOptionsView.setLayoutManager(linearLayoutManager);
        mQuizOptionsAdapter.setIsPoll();
        mOptionsView.setAdapter(mQuizOptionsAdapter);
    }

    private void showRetryFrame(String message) {
        mRetryFrameContainer.setVisibility(View.VISIBLE);
        setRetryMessage(message);
    }

    public void setRetryMessage(String message) {
        this.mRetryMessage.setText(message);
    }


    @Override
    public void onOptionSelect(Option option) {
        isSelected = true;
        final QuizAnswer quizAnswer = new QuizAnswer();
        quizAnswer.setQuestionId(mQuestions.get(0).getQuestionId());
        quizAnswer.setOptionId(option.getOptionId());
        mQuizAnswers.add(quizAnswer);
        if (isSelected)
            mQuizOptionsAdapter.notifyDataSetChanged();
    }

    public interface PollInteractionListener {
        void onPollResult(PollResults pollResults);
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.polls));
        enableBackButton();
        displaySearch(false);
    }

}
