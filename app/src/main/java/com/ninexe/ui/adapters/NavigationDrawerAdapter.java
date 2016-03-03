/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ninexe.ui.R;
import com.ninexe.ui.models.Menu;
import com.ninexe.ui.models.SubMenu;
import com.ninexe.ui.utils.DrawableUtils;
import com.ninexe.ui.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/9/15.
 */
public class NavigationDrawerAdapter extends BaseExpandableListAdapter {
    private ArrayList<Menu> menuArrayList;
    private final Context mContext;
    private int mCurrentSelectedPosition = 0;

    public NavigationDrawerAdapter(Context context, ArrayList<Menu> menus) {
        mContext = context;
        menuArrayList = menus;
    }

    @Override
    public int getGroupCount() {
        return menuArrayList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = 0;
        if (null != menuArrayList.get(groupPosition).getSubMenu()) {
            size = menuArrayList.get(groupPosition).getSubMenu().size();
        }
        return size;
    }

    @Override
    public Menu getGroup(int groupPosition) {
        return menuArrayList.get(groupPosition);
    }

    @Override
    public SubMenu getChild(int groupPosition, int childPosition) {
        SubMenu subMenu = null;
        if (null != menuArrayList.get(groupPosition).getSubMenu()) {
            subMenu = menuArrayList.get(groupPosition).getSubMenu().get(childPosition);
            ;
        }
        return subMenu;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MenuViewHolder menuViewHolder;
        Menu menu = getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_menu, null);
            menuViewHolder = new MenuViewHolder();
            menuViewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            menuViewHolder.groupText = (TextView) convertView.findViewById(R.id.title);
            menuViewHolder.groupIndicator = (ImageView) convertView.findViewById(R.id.group_indicator);
            convertView.setTag(menuViewHolder);
        } else {
            menuViewHolder = (MenuViewHolder) convertView.getTag();
        }

        if (null != menu.getSubMenu()) {
            if (isExpanded) {
                menuViewHolder.groupIndicator.setImageResource(R.drawable.uparrow_icn);
                //menuViewHolder.thumbnail.setColorFilter(Color.parseColor("#154DEA"));
                //menuViewHolder.groupText.setSelected(true);
            } else {
                menuViewHolder.groupIndicator.setImageResource(R.drawable.downarrow_icn);
                //menuViewHolder.thumbnail.setColorFilter(Color.parseColor("#242A36"));
                //menuViewHolder.groupText.setSelected(false);
            }
            menuViewHolder.thumbnail.setColorFilter(Color.parseColor("#242A36"));
            menuViewHolder.groupText.setSelected(false);

        } else {
            menuViewHolder.groupIndicator.setImageDrawable(null);
            if (mCurrentSelectedPosition == groupPosition) {
                menuViewHolder.groupText.setSelected(true);
                menuViewHolder.thumbnail.setColorFilter(Color.parseColor("#154DEA"));
            } else {
                menuViewHolder.groupText.setSelected(false);
                menuViewHolder.thumbnail.setColorFilter(Color.parseColor("#242A36"));
            }
        }
        setMenuViewHolderData(menu, menuViewHolder);
        return convertView;
    }

    private void setMenuViewHolderData(Menu menu, MenuViewHolder menuViewHolder) {
        menuViewHolder.groupText.setText(menu.getTitle());
        if (-1 != DrawableUtils.getMenuDrawableResource(menu.getTitle())) {
            menuViewHolder.thumbnail.setImageResource(DrawableUtils.getMenuDrawableResource(menu.getTitle()));
        } else {
            String thumbnailUrl = menu.getIcon();
            thumbnailUrl = thumbnailUrl.replace(".png", "_normal.png");
            LogUtils.LOGD("thumbnail", thumbnailUrl);
            Glide.with(mContext)
                    .load(thumbnailUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .crossFade()
                    .into(menuViewHolder.thumbnail);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        SubMenuViewHolder subMenuViewHolder;
        SubMenu subMenu = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_sub_menu, null);
            subMenuViewHolder = new SubMenuViewHolder();
            subMenuViewHolder.subGroupText = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(subMenuViewHolder);
        } else {
            subMenuViewHolder = (SubMenuViewHolder) convertView.getTag();
        }
        subMenuViewHolder.subGroupText.setText(subMenu.getTitle());
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setCurrentSelectedPosition(int position) {
        mCurrentSelectedPosition = position;
        notifyDataSetChanged();
    }

    class MenuViewHolder {
        ImageView thumbnail;
        TextView groupText;
        ImageView groupIndicator;
    }

    class SubMenuViewHolder {
        TextView subGroupText;
    }
}
