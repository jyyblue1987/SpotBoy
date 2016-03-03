/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.models.PollResultModel;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.Theme;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PollResultAdapter extends RecyclerView.Adapter<PollResultAdapter.PollResultHolder> {

    private ArrayList<PollResultModel> mPollResults;
    private Context mContext;

    public PollResultAdapter(ArrayList<PollResultModel> pollResultModels) {
        mPollResults = pollResultModels;
    }

    @Override
    public PollResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View pollResultHolder = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poll_result, parent, false);
        mContext = parent.getContext();
        return new PollResultHolder(pollResultHolder);
    }

    @Override
    public void onBindViewHolder(PollResultHolder holder, int position) {
        holder.mPollText.setText(mPollResults.get(position).getOption());
        holder.mPollResult.setText(mPollResults.get(position).getPercentage() + "%");
        holder.mSiNo.setText(position + 1 + ". ");
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        p1.weight = Float.parseFloat(mPollResults.get(position).getPercentage());
        holder.mPollBar.setLayoutParams(p1);


        final Theme currentTheme = PreferenceManager.getCurrentTheme(mContext);
        switch (currentTheme) {
            case Blue:
                holder.mPollBar.setBackgroundResource(R.drawable.poll_item_progress_background);
                break;
            case Red:
                holder.mPollBar.setBackgroundResource(R.drawable.poll_item_progress_background_red);
                break;


        }
    }

    @Override
    public int getItemCount() {
        return mPollResults.size();
    }

    public class PollResultHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pollText)
        TextView mPollText;

        @Bind(R.id.pollBar)
        LinearLayout mPollBar;

        @Bind(R.id.pollResult)
        TextView mPollResult;

        @Bind(R.id.siNo)
        TextView mSiNo;

        public PollResultHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
