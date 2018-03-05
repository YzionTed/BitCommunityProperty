package com.bit.communityProperty.activity.policeclock.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.workplan.PoliceWorkActivity;

import java.util.List;

/**
 * Created by DELL60 on 2018/1/30.
 */

public class ClockLogListAdapter extends RecyclerView.Adapter {
    private List<String> mList;
    private LayoutInflater mLayoutInflater;
    private Context context;

    public ClockLogListAdapter(Context context, List<String> list) {
        this.mList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_clock_log, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).nameTx.setText(mList.get(position));
            ((ViewHolder) holder).nameTx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PoliceWorkActivity.class));
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
        private TextView nameTx;

        ViewHolder(View itemView) {
            super(itemView);
            nameTx = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
