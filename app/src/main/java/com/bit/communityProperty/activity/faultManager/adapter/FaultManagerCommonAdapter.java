package com.bit.communityProperty.activity.faultManager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.bean.FaultManagementBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.bean.FaultManagerCommonBean;
import com.bit.communityProperty.utils.TimeUtils;

/**
 * 故障管理的adapter
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultManagerCommonAdapter extends ListBaseAdapter<FaultManagementBean.RecordsBean> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private FaultManagementBean.RecordsBean bean;

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
        if (bean.getFaultItem()==1){//故障种类 1：水电煤气；2：房屋结构；3：消防安防；9：其它；10：电梯；11：门禁；99：其它；
            viewHolder.ivIcon.setImageResource(R.mipmap.water_eletricity_failure);
            viewHolder.tvReason.setText("水电煤气故障");
        }else {
            viewHolder.ivIcon.setImageResource(R.mipmap.elevator_failure2);
            viewHolder.tvReason.setText("电梯故障");
        }
        if (bean.getFaultType()==1){//故障类型 1：住户；2：公共；
            viewHolder.tvType.setText("住户");
        }else {
            viewHolder.tvType.setText("公共物业");
        }
        if (bean.getFaultStatus()==0){//故障状态 0：已取消；1：待接受；2：待分派；3：待检修；4：已完成；-1：已驳回；
            viewHolder.tvStatus.setText("已取消");
        }else if (bean.getFaultStatus()==1){
            viewHolder.tvStatus.setText("待接受");
        }else if (bean.getFaultStatus()==2){
            viewHolder.tvStatus.setText("待分派");
        }else if (bean.getFaultStatus()==3){
            viewHolder.tvStatus.setText("待检修");
        }else if (bean.getFaultStatus()==4){
            viewHolder.tvStatus.setText("已完成");
        }else if (bean.getFaultStatus()==-1){
            viewHolder.tvStatus.setText("已驳回");
        }
        viewHolder.tvAddress.setText(bean.getFaultAddress());
        if (bean.getAcceptTime()!=null)
        viewHolder.tvTime.setText(TimeUtils.stampToDateWithHms(bean.getAcceptTime().toString()));
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
