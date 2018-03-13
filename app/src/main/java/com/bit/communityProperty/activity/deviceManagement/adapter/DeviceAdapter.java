package com.bit.communityProperty.activity.deviceManagement.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.bean.CameraBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.activity.deviceManagement.bean.ElevatorListBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.view.CircleImageView;

/**
 * 设备管理的adapter
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceAdapter<T> extends ListBaseAdapter<T> {
    private Context mContext;
    private ViewHolder viewHolder = null;
    private CameraBean.RecordsBean camerBean;//摄像头bean类
    private DoorControlBean.RecordsBean doorBean;//门禁bean类

    public DeviceAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.fragment_device_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        if (mDataList.get(position) instanceof CameraBean.RecordsBean) {//摄像头
            camerBean = (CameraBean.RecordsBean) mDataList.get(position);
            if (camerBean.getCameraStatus() == 1) {
                viewHolder.tvStatus.setText("运行正常");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_blue));
            } else {
                viewHolder.tvStatus.setText("运行故障");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_yellow));
            }
            viewHolder.tvName.setText(camerBean.getName());
            viewHolder.tvId.setText(camerBean.getCameraCode());
            viewHolder.ivIcon.setImageResource(R.mipmap.ic_sbgl_sxt);
        }else if (mDataList.get(position) instanceof DoorControlBean.RecordsBean) {//门禁
            doorBean = (DoorControlBean.RecordsBean) mDataList.get(position);
            if (doorBean.getDoorStatus() == 1) {
                viewHolder.tvStatus.setText("运行正常");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_blue));
            } else {
                viewHolder.tvStatus.setText("运行故障");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_yellow));
            }
            viewHolder.tvName.setText(doorBean.getName());
            viewHolder.tvId.setText(doorBean.getSerialNo());
            viewHolder.ivIcon.setImageResource(R.mipmap.ic_sbgl_mj);
        }else if (mDataList.get(position) instanceof CarBrakeBean.RecordsBean){//车闸
            CarBrakeBean.RecordsBean carBrakeBean = (CarBrakeBean.RecordsBean) mDataList.get(position);
            if (carBrakeBean.getGateStatus() == 1) {
                viewHolder.tvStatus.setText("运行正常");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_blue));
            } else {
                viewHolder.tvStatus.setText("运行故障");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_yellow));
            }
            viewHolder.tvName.setText(carBrakeBean.getGateName());
            viewHolder.tvId.setText(carBrakeBean.getGateNO());
            viewHolder.ivIcon.setImageResource(R.mipmap.ic_sbgl_cz);
        }else if (mDataList.get(position) instanceof ElevatorListBean.RecordsBean){//电梯
            ElevatorListBean.RecordsBean carBrakeBean = (ElevatorListBean.RecordsBean) mDataList.get(position);
            if (carBrakeBean.getElevatorStatus() == 0) {
                viewHolder.tvStatus.setText("运行正常");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_blue));
            } else {
                viewHolder.tvStatus.setText("运行故障");
                viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(mContext,R.drawable.shape_rad22_yellow));
            }
            viewHolder.tvName.setText(carBrakeBean.getName());
            viewHolder.tvId.setText(carBrakeBean.getElevatorTypeName());
            viewHolder.ivIcon.setImageResource(R.mipmap.ic_sbgl_dt);
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
