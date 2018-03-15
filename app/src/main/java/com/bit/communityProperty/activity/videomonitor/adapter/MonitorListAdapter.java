package com.bit.communityProperty.activity.videomonitor.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.adapter.ListBaseAdapter;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class MonitorListAdapter extends ListBaseAdapter {
    private Context context;

    public MonitorListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_monitor_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (position==2){
            viewHolder.tvStatus.setText("运行故障");
            viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_shade_red_bg));
        }else{
            viewHolder.tvStatus.setText("运行正常");
            viewHolder.tvStatus.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_weather_bg));
        }
    }

    @Override
    public int getItemCount() {
        return 18;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvXinghao;
        TextView tvAddress;
        TextView tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvXinghao = itemView.findViewById(R.id.tv_xinghao);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
