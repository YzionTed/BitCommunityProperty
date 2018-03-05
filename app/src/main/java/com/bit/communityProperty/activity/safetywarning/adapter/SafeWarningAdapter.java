package com.bit.communityProperty.activity.safetywarning.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.safetywarning.bean.AlarmListBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.utils.TimeUtils;

/**
 * Created by DELL60 on 2018/2/9.
 */

public class SafeWarningAdapter extends ListBaseAdapter{
    private Context context;
    private btnClickListener listener;

    public SafeWarningAdapter(Context context,btnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_safety_warning,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final AlarmListBean.RecordsBean recordsBean = (AlarmListBean.RecordsBean) mDataList.get(position);
        viewHolder.tvTitle.setText(recordsBean.getCommunityName()+recordsBean.getBuildingName()+recordsBean.getRoomName());
        viewHolder.tvTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getCallTime()));
        switch (recordsBean.getReceiveStatus()){
            case 1:
                viewHolder.llBaoan.setVisibility(View.GONE);
                viewHolder.llTime.setVisibility(View.GONE);
                viewHolder.llJiechu.setVisibility(View.GONE);
                viewHolder.llBaogao.setVisibility(View.GONE);
                viewHolder.btnGo.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("待受理");
                viewHolder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.text_status_red));
                break;
            case 2:
                viewHolder.llBaoan.setVisibility(View.VISIBLE);
                viewHolder.llTime.setVisibility(View.VISIBLE);
                viewHolder.llJiechu.setVisibility(View.GONE);
                viewHolder.llBaogao.setVisibility(View.GONE);
                viewHolder.btnGo.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("待排查");
                viewHolder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.text_status_red));
                viewHolder.tvBaoan.setText(recordsBean.getReceiverName());
                viewHolder.tvAcceptTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getReceiveTime()));
                break;
            case 3:
                viewHolder.llBaoan.setVisibility(View.VISIBLE);
                viewHolder.llTime.setVisibility(View.VISIBLE);
                viewHolder.llJiechu.setVisibility(View.VISIBLE);
                viewHolder.llBaogao.setVisibility(View.VISIBLE);
                viewHolder.btnGo.setVisibility(View.GONE);
                viewHolder.tvStatus.setText("已解决");
                viewHolder.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.tv_black_33));
                viewHolder.tvBaoan.setText(recordsBean.getReceiverName());
                viewHolder.tvAcceptTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getReceiveTime()));
                viewHolder.tvRemoveTime.setText(TimeUtils.stampToDateWithHm(recordsBean.getTroubleShootingTime()));
                viewHolder.tvReport.setText(recordsBean.getTroubleShootingReport());
                break;
        }
        viewHolder.tvBjName.setText(recordsBean.getCallerName());
        viewHolder.tvContact.setText(recordsBean.getCallerPhoneNum());
        viewHolder.btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.receiveAlarm(recordsBean.getReceiveStatus(),recordsBean.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llBaoan;
        LinearLayout llTime;
        LinearLayout llJiechu;
        LinearLayout llBaogao;
        Button btnGo;
        TextView tvTitle;
        TextView tvTime;
        TextView tvStatus;
        TextView tvBjName;
        TextView tvContact;
        TextView tvBaoan;
        TextView tvAcceptTime;
        TextView tvRemoveTime;
        TextView tvReport;

        public ViewHolder(View itemView) {
            super(itemView);
            llBaoan = itemView.findViewById(R.id.ll_baoan);
            llTime = itemView.findViewById(R.id.ll_time);
            llJiechu = itemView.findViewById(R.id.ll_jiechu);
            llBaogao = itemView.findViewById(R.id.ll_baogao);
            btnGo = itemView.findViewById(R.id.btn_go);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvBjName = itemView.findViewById(R.id.tv_bj_name);
            tvContact = itemView.findViewById(R.id.tv_contact);
            tvBaoan = itemView.findViewById(R.id.tv_baoan);
            tvAcceptTime = itemView.findViewById(R.id.tv_accept_time);
            tvRemoveTime = itemView.findViewById(R.id.tv_remove_time);
            tvReport = itemView.findViewById(R.id.tv_report);
        }
    }

    public interface btnClickListener{
        void receiveAlarm(int type,String id);
    }
}
