package com.bit.communityProperty.activity.deviceManagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceInfoBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.view.CircleImageView;

/**
 * Created by kezhangzhao on 2018/2/20.
 */

public class DeviceInfoAdapter extends ListBaseAdapter<DeviceInfoBean> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private DeviceAdapter.ViewHolder viewHolder = null;
    private DeviceInfoBean bean;

    public DeviceInfoAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.activity_device_info_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
//        if (bean.getDataStatus()==0){
//            viewHolder.tvStatus.setText("运行正常");
//        }else {
//            viewHolder.tvStatus.setText("运行故障");
//        }
//        viewHolder.tvName.setText(bean.getName());
//        viewHolder.tvId.setText(bean.getDoorId());
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
