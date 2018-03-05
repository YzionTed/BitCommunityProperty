package com.bit.communityProperty.activity.videomonitor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.videomonitor.HistoryMonitorActivity;

import java.util.List;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class HistoryVideoAdapter extends RecyclerView.Adapter {
    private List<String> mList;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public HistoryVideoAdapter(Context context, List<String> list) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_video, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.flItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, HistoryMonitorActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        ImageView ivImg;
        FrameLayout flItem;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImg = itemView.findViewById(R.id.iv_img);
            flItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
