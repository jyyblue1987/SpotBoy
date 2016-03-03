/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.models.ReactionResponseModel;
import com.ninexe.ui.models.ReactometerSection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 2/9/15.
 */
public class ReactometerViewHolder extends RecyclerView.ViewHolder {

    ReactometerSection reactometerSection;

    @Bind(R.id.btn_reactometer_wow)
    ImageView buttonWow;

    @Bind(R.id.btn_reactometer_lol)
    View buttonLol;

    @Bind(R.id.btn_reactometer_omg)
    View buttonOmg;

    @Bind(R.id.btn_reactometer_wtf)
    View buttonWtf;

    @Bind(R.id.wowValue)
    TextView wowValue;

    @Bind(R.id.lolValue)
    TextView lolValue;

    @Bind(R.id.wtfValue)
    TextView wtfValue;

    @Bind(R.id.omgValue)
    TextView omgValue;

    @Bind(R.id.layoutContainer)
    LinearLayout containerLayout;


    private Context context;
    private IReactometerViewHolderClicks listener;
    private boolean isReacted = false;

    public ReactometerViewHolder(View itemView, IReactometerViewHolderClicks reactometerViewHolderClicks) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        listener = reactometerViewHolderClicks;
        if (null == context) {
            context = itemView.getContext();
        }
    }

    public void bindReactometerSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem) {
        if (articleDetailRecyclerViewItem instanceof ReactometerSection) {
            reactometerSection = (ReactometerSection) articleDetailRecyclerViewItem;
            if (null != reactometerSection.getReaction()) {
                setSelection(reactometerSection.getReaction());
                setReactionValues();
            } else {
                initReactometer();
            }
        }
    }

    public void bindReactometerSectionData(IArticleDetailRecyclerViewItem articleDetailRecyclerViewItem, ReactionResponseModel reactionResponseModel) {
        if (articleDetailRecyclerViewItem instanceof ReactometerSection) {
            reactometerSection = (ReactometerSection) articleDetailRecyclerViewItem;
            if (null != reactionResponseModel.getReaction()) {
                setSelection(reactionResponseModel.getReaction());
                setReactionValues(reactionResponseModel);
            } else
                initReactometer();
        }
    }

    private void initReactometer() {
        enableReactometerButtons();
    }

    private void enableReactometerButtons() {
        buttonWow.setSelected(true);
        buttonLol.setSelected(true);
        buttonWtf.setSelected(true);
        buttonOmg.setSelected(true);
    }

    private void disableReactometerButtons() {
        buttonLol.setClickable(false);
        buttonWow.setClickable(false);
        buttonWtf.setClickable(false);
        buttonOmg.setClickable(false);
    }

    private void setButtonSelectionState(View view, boolean isSet) {
        view.setSelected(isSet);
    }

    private void setWowButtonSelectedState(boolean isSet) {
        setButtonSelectionState(buttonWow, isSet);
    }

    private void setLolButtonSelectedState(boolean isSet) {
        setButtonSelectionState(buttonLol, isSet);
    }

    private void setWtfButtonSelectedState(boolean isSet) {
        setButtonSelectionState(buttonWtf, isSet);
    }

    private void setOmgButtonSelectedState(boolean isSet) {
        setButtonSelectionState(buttonOmg, isSet);
    }

    @OnClick(R.id.btn_reactometer_wow)
    void onReacometerWowButtonClick() {
        if (!isReacted) {
            listener.onReacted(Constants.WOW);
        }
    }

    @OnClick(R.id.btn_reactometer_lol)
    void onReacometerLolButtonClick() {
        if (!isReacted) {
            listener.onReacted(Constants.LOL);
        }
    }

    @OnClick(R.id.btn_reactometer_wtf)
    void onReacometerWtfButtonClick() {
        if (!isReacted) {
            listener.onReacted(Constants.WTF);
        }
    }

    @OnClick(R.id.btn_reactometer_omg)
    void onReacometerOmgButtonClick() {
        if (!isReacted) {
            listener.onReacted(Constants.OMG);
        }
    }

    @OnClick(R.id.btn_share)
    public void onShareButtonClick() {
        listener.onShareButtonClick();
    }

    public interface IReactometerViewHolderClicks {
        void onShareButtonClick();

        void onReacted(String reaction);
    }

    private void setSelection(String reaction) {
        isReacted = true;
        switch (reaction) {
            case Constants.WOW:
                setWowButtonSelectedState(true);
                setLolButtonSelectedState(false);
                setWtfButtonSelectedState(false);
                setOmgButtonSelectedState(false);
                break;
            case Constants.WTF:
                setWtfButtonSelectedState(true);
                setLolButtonSelectedState(false);
                setOmgButtonSelectedState(false);
                setWowButtonSelectedState(false);
                break;
            case Constants.LOL:
                setLolButtonSelectedState(true);
                setWtfButtonSelectedState(false);
                setOmgButtonSelectedState(false);
                setWowButtonSelectedState(false);
                break;
            case Constants.OMG:
                setOmgButtonSelectedState(true);
                setLolButtonSelectedState(false);
                setWtfButtonSelectedState(false);
                setWowButtonSelectedState(false);
                break;
        }
    }

    private void setReactionValues(ReactionResponseModel reactionResponseModel) {
        wowValue.setText(reactionResponseModel.getWOW() + "%");
        lolValue.setText(reactionResponseModel.getLOL() + "%");
        omgValue.setText(reactionResponseModel.getOMG() + "%");
        wtfValue.setText(reactionResponseModel.getWTF() + "%");
        wtfValue.setVisibility(View.VISIBLE);
        lolValue.setVisibility(View.VISIBLE);
        wowValue.setVisibility(View.VISIBLE);
        omgValue.setVisibility(View.VISIBLE);
    }

    private void setReactionValues() {
        //wowValue.setText(String.format(context.getString(R.string.reactometer_response), reactometerSection.getWowValue()));
        wowValue.setText(reactometerSection.getWowValue() + "%");
        lolValue.setText(reactometerSection.getLolValue() + "%");
        omgValue.setText(reactometerSection.getOmgValue() + "%");
        wtfValue.setText(reactometerSection.getWtfValue() + "%");
        wtfValue.setVisibility(View.VISIBLE);
        lolValue.setVisibility(View.VISIBLE);
        wowValue.setVisibility(View.VISIBLE);
        omgValue.setVisibility(View.VISIBLE);
    }
}
