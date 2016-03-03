/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.ArticleMedia;
import com.ninexe.ui.utils.ViewUtils;

import java.util.ArrayList;

public class CoverFlowAdapter extends BaseAdapter {

    private ArrayList<ArticleMedia> mData;
    private Context mContext;

    public CoverFlowAdapter(Context context, ArrayList<ArticleMedia> medias) {
        mContext = context;
        mData = medias;
        Glide.get(mContext).clearMemory();
    }

    public void setData(ArrayList<ArticleMedia> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_coverflow, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) rowView.findViewById(R.id.label);
            viewHolder.image = (ImageView) rowView
                    .findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        Glide.with(mContext)
                .load(ViewUtils.getThumbnail(mData.get(position).getFile(), Constants.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.placeholder_featured_images)
                .override(mContext.getResources().getDimensionPixelSize(R.dimen.cover_width), mContext.getResources().getDimensionPixelSize(R.dimen.cover_height))
                .dontAnimate()
                .into(holder.image);
        holder.text.setText(mData.get(position).getDescription());

        return rowView;
    }


    static class ViewHolder {
        public TextView text;
        public ImageView image;
    }
}
