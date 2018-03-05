package com.bit.communityProperty.activity.faultManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.bean.FaultManagerCommonBean;

/**
 * 故障管理的adapter
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultManagerCommonAdapter extends ListBaseAdapter<FaultManagerCommonBean> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private FaultManagerCommonBean bean;

    public FaultManagerCommonAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_fault_manager_common_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (bean.getReason()==1){//电梯故障
            viewHolder.ivIcon.setImageResource(R.mipmap.elevator_failure2);
            viewHolder.tvReason.setText("电梯故障");
        }else {
            viewHolder.ivIcon.setImageResource(R.mipmap.water_eletricity_failure);
            viewHolder.tvReason.setText("水电煤气故障");
        }
        if (bean.getType()==1){//公共物业
            viewHolder.tvType.setText("公共物业");
        }else {
            viewHolder.tvType.setText("住户");
        }
        if (bean.getStatus()==0){
            viewHolder.tvStatus.setText("待确认");
        }else {
            viewHolder.tvStatus.setText("待接单");
        }
        viewHolder.tvAddress.setText(bean.getAddress());
        viewHolder.tvTime.setText(bean.getTime());
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;//图标
        TextView tvType;//类型
        TextView tvReason;//故障原因
        TextView tvAddress;//故障地址
        TextView tvTime;//时间
        TextView tvStatus;//状态

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvType = itemView.findViewById(R.id.tv_type);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
