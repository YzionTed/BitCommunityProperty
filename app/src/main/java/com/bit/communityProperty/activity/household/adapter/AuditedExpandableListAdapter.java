package com.bit.communityProperty.activity.household.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.bean.AuditedBean;
import com.bit.communityProperty.activity.household.bean.AuditedUserBean;
import com.bit.communityProperty.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 可展开的listview 的adapter
 * Created by kezhangzhao on 2018/2/8.
 */

public class AuditedExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    //    public String[] groupDatas;
//    public String[][] childDatas;
    public List<AuditedBean> groupList = new ArrayList();

    public List<List<AuditedUserBean.RecordsBean>> childList = new ArrayList();

    public void addChildData(int position ,List<AuditedUserBean.RecordsBean> childData){
        childList.remove(position);
        childList.add(position,childData);
    }

    public AuditedExpandableListAdapter(Context context, List<AuditedBean> groupList,
                                        List<List<AuditedUserBean.RecordsBean>> childList) {
        this.mContext = context;
        this.groupList = groupList;
        this.childList = childList;
        //创建childList里面对象，为后面添加数据做准备。
        for (int i=0;i<groupList.size();i++){
            List<AuditedUserBean.RecordsBean> list = new ArrayList<>();
            this.childList.add(list);
        }
    }

    // 获取分组的个数
    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    //获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
//        childDatas[groupPosition].length;
    }

    //获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
//        return childDatas[groupPosition];
        return groupList.get(groupPosition);
    }

    //获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
//        return childDatas[groupPosition][childPosition];
        return childList.get(groupPosition).get(childPosition);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.audited_expandable_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvGroupName = convertView.findViewById(R.id.group_name);
            groupViewHolder.tvChildNum = convertView.findViewById(R.id.child_num);
            groupViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {
            groupViewHolder.ivIcon.setImageResource(R.drawable.icon_down);
        } else {
            groupViewHolder.ivIcon.setImageResource(R.drawable.icon_right);
        }
//        groupViewHolder.tvTitle.setText(groupDatas[groupPosition]);
        groupViewHolder.tvGroupName.setText(groupList.get(groupPosition).getBuildingEntity().get(0).getName());
        groupViewHolder.tvChildNum.setText("（"+groupList.get(groupPosition).getTotal()+"）");
        return convertView;
    }

    //获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.audited_expandable_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.ivIcon = convertView.findViewById(R.id.iv_icon);
            childViewHolder.tvName = convertView.findViewById(R.id.tv_name);
            childViewHolder.tvAddress = convertView.findViewById(R.id.tv_address);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tvName.setText(childList.get(groupPosition).get(childPosition).getName());
        childViewHolder.tvAddress.setText(childList.get(groupPosition).get(childPosition).getRoomName());
        return convertView;
    }

    //定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    static class GroupViewHolder {
        TextView tvGroupName;
        TextView tvChildNum;
        ImageView ivIcon;
    }

    static class ChildViewHolder {
        TextView tvName;
        TextView tvAddress;
        CircleImageView ivIcon;
    }
}
