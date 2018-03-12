package com.bit.communityProperty.activity.faultManager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.bean.AssignPersonBean;
import com.bit.communityProperty.view.CircleImageView;

import java.util.ArrayList;

/**
 * 维修人员列表的adapter
 * Created by kezhangzhao on 2018/3/12.
 */

public class AssignSelectAdapter extends BaseAdapter{

    private LayoutInflater inflater = null;
    private ArrayList<AssignPersonBean> listDate = new ArrayList<>();
    private ArrayList<Boolean> isSelectList = new ArrayList<>();
    private Context mContext;
    public AssignSelectAdapter (Context context,ArrayList<AssignPersonBean> list){
        this.listDate = list;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        for (int i=0;i<list.size();i++){
            isSelectList.add(false);
        }
    }
    @Override
    public int getCount() {
        return listDate.size();
    }

    @Override
    public Object getItem(int position) {
        return listDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_assign_select_item,null);
            viewHolder.ivHead = convertView.findViewById(R.id.iv_head);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.ivPosition = convertView.findViewById(R.id.iv_position);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(listDate.get(position).getUserName());
        if (isSelectList.get(position)){
            viewHolder.ivPosition.setImageResource(R.mipmap.position_blue);
        }else {
            viewHolder.ivPosition.setImageResource(R.mipmap.position_gray);
        }

        return convertView;
    }

    class ViewHolder{
        CircleImageView ivHead;//头像
        TextView tvName;//姓名
        ImageView ivPosition;//选择点
    }

    public void setPositionGray( int position,boolean isSelect){
        isSelectList.set(position,isSelect);
        notifyDataSetChanged();
    }
}
