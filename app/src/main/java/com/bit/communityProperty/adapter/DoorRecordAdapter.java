package com.bit.communityProperty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.DoorRecordBean;

/**
 * 门禁记录的adapter
 * Created by kezhangzhao on 2018/1/21.
 */

public class DoorRecordAdapter extends ListBaseAdapter<DoorRecordBean>{
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private DoorRecordBean bean;

    public DoorRecordAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.door_record_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvTime.setText(bean.getTime());
        viewHolder.tvAddress.setText(bean.getAddress());
        if (bean.getType()==1){
            viewHolder.ivIcon.setImageResource(R.mipmap.bluetooth);
        }else {
            viewHolder.ivIcon.setImageResource(R.mipmap.long_range_copy);
        }
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;//地址
        ImageView ivIcon;//图片
        TextView tvTime;//时间

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
