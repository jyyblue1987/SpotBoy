/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.models.Option;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuizOptionsAdapter extends RecyclerView.Adapter<QuizOptionsAdapter.OptionsHolder> {

    private ArrayList<Option> mOptions;
    private Context mContext;
    private boolean isSelected = false;
    private OnOptionSelectListener mListener;
    private boolean isPoll = false;


    public QuizOptionsAdapter(ArrayList<Option> options) {
        mOptions = options;
    }


    @Override
    public OptionsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View QuizOptionLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz_option, parent, false);
        mContext = parent.getContext();
        return new OptionsHolder(QuizOptionLayout);
    }

    @Override
    public void onBindViewHolder(OptionsHolder holder, int position) {
        if (mOptions.get(position).isSelected()) {
            holder.mOption.setSelected(true);
            holder.mOptionHolder.setSelected(true);
            holder.mOption.setText(mOptions.get(position).getOptionText());
        } else {
            isSelected = false;
            holder.mOption.setSelected(false);
            holder.mOptionHolder.setSelected(false);
            holder.mOption.setText(mOptions.get(position).getOptionText());
        }

    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }

    public class OptionsHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.optionText)
        TextView mOption;

        @Bind(R.id.optionHolder)
        LinearLayout mOptionHolder;

        @OnClick(R.id.optionHolder)
        void onOptionClick() {
            if (isPoll) {
                isSelected = false;
                for (int i = 0; i < mOptions.size(); i++) {
                    mOptions.get(i).setIsSelected(false);
                    notifyItemChanged(i);
                }

            }
            if (!isSelected) {
                mOption.setSelected(true);
                mOptionHolder.setSelected(true);
                mOptions.get(getAdapterPosition()).setIsSelected(true);
                isSelected = true;
                if (null != mListener)
                    mListener.onOptionSelect(mOptions.get(getAdapterPosition()));
            }


        }

        public OptionsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnOptionSelectListener {
        void onOptionSelect(Option option);
    }

    public void setListener(OnOptionSelectListener listener) {
        mListener = listener;
    }

    public void setIsPoll() {
        isPoll = true;
    }
}
