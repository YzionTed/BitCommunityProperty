package com.bit.communityProperty.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.MeterReadBean;

/**
 * 抄表管理的adapter
 * Created by kezhangzhao on 2018/1/21.
 */

public class MeterReadAdapter extends ListBaseAdapter<MeterReadBean> {
    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private MeterReadBean bean;

    public MeterReadAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.meter_read_item, parent, false));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bean = mDataList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvMonth.setText(bean.getMonth());
        viewHolder.tvTime.setText(bean.getTime());
        viewHolder.tvProportion.setText(bean.getCurrentNum() + "/" + bean.getAllNum());
        if (bean.getStatus() == 0) {//未完成
            viewHolder.tvStatus.setText("未完成");
            viewHolder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red_20));
            viewHolder.tvProportion.setTextColor(ContextCompat.getColor(mContext, R.color.red_20));
        } else {//已经上传
            viewHolder.tvStatus.setText("已上传");
            viewHolder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.title_background));
            viewHolder.tvProportion.setTextColor(ContextCompat.getColor(mContext, R.color.title_background));
        }
        viewHolder.tvDelete.setOnClickListener(new MyOnClickListener());
        viewHolder.tvUpload.setOnClickListener(new MyOnClickListener());

    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_delete://删除本地备份
                    Toast.makeText(mContext,"删除本地备份",Toast.LENGTH_LONG).show();
                    break;
                case R.id.tv_upload://上传水费记录
                    Toast.makeText(mContext,"上传水费记录",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth;//月份
        TextView tvStatus;//状态
        TextView tvTime;//时间
        TextView tvProportion;//完成度
        TextView tvDelete;//删除本地备份（按钮）
        TextView tvUpload;//上传水费记录（按钮）

        public ViewHolder(View itemView) {
            super(itemView);
            tvMonth = itemView.findViewById(R.id.tv_month);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvProportion = itemView.findViewById(R.id.tv_proportion);
            tvDelete = itemView.findViewById(R.id.tv_delete);
            tvUpload = itemView.findViewById(R.id.tv_upload);
        }
    }
}
