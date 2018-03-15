package com.bit.communityProperty.activity.mail_list.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.videomonitor.RealMonitorActivity;

import java.util.List;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class Mail_ListAdapter extends RecyclerView.Adapter {
    private List<String> mList;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public Mail_ListAdapter(Context context, List<String> list) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_mail_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
//            ((ViewHolder) holder).tvXinghao.setText(mList.get(position));
            ((ViewHolder) holder).tvXinghao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,position+"click",Toast.LENGTH_SHORT).show();
                }
            });

            ((ViewHolder) holder).llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, RealMonitorActivity.class));
                }
            });
        } else {
            throw new IllegalStateException("Illegal state Exception onBindviewHolder");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvXinghao;
        TextView tvAddress;
        TextView tvStatus;
        LinearLayout llItem;
        ImageView ivStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvXinghao = (TextView) itemView.findViewById(R.id.tv_xinghao);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvStatus = itemView.findViewById(R.id.tv_status);
            ivStatus = itemView.findViewById(R.id.iv_status);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
