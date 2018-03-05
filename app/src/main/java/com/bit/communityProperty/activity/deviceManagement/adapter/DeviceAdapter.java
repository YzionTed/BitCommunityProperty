package com.bit.communityProperty.activity.deviceManagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.bean.CameraBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.view.CircleImageView;

/**
 * 设备管理的adapter
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceAdapter<T> extends ListBaseAdapter<T> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private CameraBean.RecordsBean camerBean;//摄像头bean类
    private DoorControlBean.RecordsBean doorBean;//门禁bean类

    public DeviceAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_device_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        if (mDataList.get(position)instanceof CameraBean.RecordsBean){//摄像头
            camerBean = (CameraBean.RecordsBean) mDataList.get(position);
            if (camerBean.getCameraStatus()==1){
                viewHolder.tvStatus.setText("运行正常");
            }else {
                viewHolder.tvStatus.setText("运行故障");
            }
            viewHolder.tvName.setText(camerBean.getName());
            viewHolder.tvId.setText(camerBean.getCameraCode());
        }
        if (mDataList.get(position)instanceof DoorControlBean.RecordsBean){//门禁
            doorBean = (DoorControlBean.RecordsBean) mDataList.get(position);
            if (doorBean.getDoorStatus()==1){
                viewHolder.tvStatus.setText("运行正常");
            }else {
                viewHolder.tvStatus.setText("运行故障");
            }
            viewHolder.tvName.setText(doorBean.getName());
            viewHolder.tvId.setText(doorBean.getDeviceCode());
        }
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView ivIcon;//图标
        TextView tvName;//名字
        TextView tvId;//时间
        TextView tvStatus;//状态

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvId = itemView.findViewById(R.id.tv_id);
        }
    }
}
