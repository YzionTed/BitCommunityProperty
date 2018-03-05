package com.bit.communityProperty.activity.household.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.utils.TimeUtils;

/**
 * 待审核的adapter
 * Created by kezhangzhao on 2018/2/8.
 */

public class AuditingAdapter extends ListBaseAdapter<AuditingBean.RecordsBean> {

    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private AuditingBean.RecordsBean bean;

    public AuditingAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.fragment_auditing_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        if (bean.getAuditStatus()==0){
            viewHolder.tvStatus.setText("待审核");
        }else if (bean.getAuditStatus()==1){
            viewHolder.tvStatus.setText("审核通过");
        }else if (bean.getAuditStatus()==-1){
            viewHolder.tvStatus.setText("已驳回");
        }else if (bean.getAuditStatus()==-2){
            viewHolder.tvStatus.setText("违规");
        }
        viewHolder.tvName.setText(bean.getName());
        viewHolder.tvPhone.setText(bean.getPhone());
        viewHolder.tvAddress.setText(bean.getCurrentAddress());
        viewHolder.tvTime.setText(TimeUtils.stampToDateWithHm(bean.getCreateAt()));
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivIcon;//图标
        TextView tvName;//名字
        TextView tvPhone;//电话
        TextView tvAddress;//地址
        TextView tvTime;//时间
        TextView tvStatus;//状态

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvPhone = itemView.findViewById(R.id.tv_phone);
        }
    }
}
