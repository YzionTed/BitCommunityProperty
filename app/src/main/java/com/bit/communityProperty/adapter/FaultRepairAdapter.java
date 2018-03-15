package com.bit.communityProperty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.FaultRepairBean;

/**
 * 故障维修的adapter
 * Created by kezhangzhao on 2018/1/27.
 */

public class FaultRepairAdapter extends ListBaseAdapter<FaultRepairBean> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private FaultRepairBean bean;
    private int fragmentNum;//第几个fragment。

    public FaultRepairAdapter(Context context, int fragmentNum) {
        this.mContext = context;
        this.fragmentNum = fragmentNum;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_fault_repair_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (bean.getType()==1){//公共物业
            viewHolder.tvFaultType.setText("公共物业");
        }else {
            viewHolder.tvFaultType.setText("住户");
        }
//        if (bean.getStatus()==0){
//            viewHolder.tvStatus.setText("待确认");
//        }else {
//            viewHolder.tvStatus.setText("待接单");
//        }
        if (fragmentNum==1){//待接单
            viewHolder.tvStatus.setText("待接单");
            viewHolder.layoutRepair_info.setVisibility(View.GONE);
            viewHolder.layoutMoney.setVisibility(View.GONE);
            viewHolder.btTodo.setVisibility(View.VISIBLE);
            viewHolder.btTodo.setText("快速接单");
        }else if (fragmentNum==2){//待检修
            viewHolder.tvStatus.setText("待检修");
            viewHolder.layoutRepair_info.setVisibility(View.GONE);
            viewHolder.layoutMoney.setVisibility(View.GONE);
            viewHolder.btTodo.setVisibility(View.VISIBLE);
            viewHolder.btTodo.setText("检修定价");
        }else if (fragmentNum==3){//待收费
            viewHolder.tvStatus.setText("待收费");
            viewHolder.layoutMoney.setVisibility(View.GONE);
            viewHolder.btTodo.setVisibility(View.VISIBLE);
            viewHolder.btTodo.setText("确认收费");
            viewHolder.tvRepairInfo.setText(bean.getRepairInfo());
        }else {//已完成
            viewHolder.tvStatus.setText("已完成");
            viewHolder.btTodo.setVisibility(View.GONE);
            viewHolder.tvMoney.setText(bean.getMoney());
            viewHolder.tvRepairInfo.setText(bean.getRepairInfo());
        }
        viewHolder.tvAddress.setText(bean.getAddress());
        viewHolder.tvTime.setText(bean.getTime());
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        Button btTodo;//按钮
        TextView tvMoney;//维修金额
        TextView tvFaultType;//类型
        TextView tvRepairInfo;//维修信息
        TextView tvAddress;//故障地址
        TextView tvTime;//时间
        TextView tvStatus;//状态
        LinearLayout layoutAddress;//地址布局
        LinearLayout layoutTime;//时间布局
        LinearLayout layoutRepair_info;//维修信息布局
        LinearLayout layoutMoney;//维修金额布局

        public ViewHolder(View itemView) {
            super(itemView);
            tvMoney = itemView.findViewById(R.id.tv_money);
            tvFaultType = itemView.findViewById(R.id.tv_fault_type);
            tvRepairInfo = itemView.findViewById(R.id.tv_repair_info);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btTodo = itemView.findViewById(R.id.bt_todo);
            layoutAddress = itemView.findViewById(R.id.layout_address);
            layoutTime = itemView.findViewById(R.id.layout_time);
            layoutRepair_info = itemView.findViewById(R.id.layout_repair_info);
            layoutMoney = itemView.findViewById(R.id.layout_money);
        }
    }
}
