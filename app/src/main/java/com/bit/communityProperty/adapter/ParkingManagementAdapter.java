package com.bit.communityProperty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.ParkingManagementBean;

/**
 * 停车管理列表的adapter
 * Created by kezhangzhao on 2018/1/18.
 */

public class ParkingManagementAdapter extends ListBaseAdapter<ParkingManagementBean>{
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private ParkingManagementBean bean;

    public ParkingManagementAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.parking_management_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvType.setText(bean.getType());
        viewHolder.tvTitle.setText(bean.getTitle());
        if (bean.getStatus()==1){
            viewHolder.tvStatus.setText("运行正常");
            viewHolder.ivFaulting.setVisibility(View.GONE);
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.tv_gray_99));
        }else {
            viewHolder.tvStatus.setText("设备故障");
            viewHolder.ivFaulting.setVisibility(View.VISIBLE);
            viewHolder.tvStatus.setTextColor(mContext.getResources().getColor(R.color.red_fe));
        }
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;//标题
        TextView tvType;//设备类型
        TextView tvStatus;//运行状态
        ImageView ivFaulting;//红色警告图片

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_type);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivFaulting = itemView.findViewById(R.id.iv_faulting);
        }
    }
}
