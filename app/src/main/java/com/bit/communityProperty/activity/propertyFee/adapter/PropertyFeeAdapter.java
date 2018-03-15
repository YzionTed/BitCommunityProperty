package com.bit.communityProperty.activity.propertyFee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;

/**
 * 物业费管理的adapter
 * Created by kezhangzhao on 2018/2/13.
 */

public class PropertyFeeAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    public String[] groupDatas;
    public String[][] childDatas;


    public PropertyFeeAdapter(Context context, String[] groupDatas, String[][] childDatas) {
        this.mContext = context;
        this.groupDatas = groupDatas;
        this.childDatas = childDatas;
    }

    // 获取分组的个数
    @Override
    public int getGroupCount() {
        return groupDatas.length;
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childDatas[groupPosition].length;
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return childDatas[groupPosition];
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childDatas[groupPosition][childPosition];
    }

    //获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_fee_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = convertView.findViewById(R.id.group_name);
            groupViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupDatas[groupPosition]);
        if (isExpanded){
            groupViewHolder.ivIcon.setImageResource(R.drawable.icon_down);
        }else {
            groupViewHolder.ivIcon.setImageResource(R.drawable.icon_right);
        }
        return convertView;
    }

    //获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.property_fee_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = convertView.findViewById(R.id.tv_name);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvTitle.setText(childDatas[groupPosition][childPosition]);
        return convertView;
    }

    //定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    static class GroupViewHolder {
        TextView tvTitle;
        ImageView ivIcon;//图标
    }

    static class ChildViewHolder {
        TextView tvTitle;
    }

}
