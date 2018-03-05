package com.bit.communityProperty.activity.cleanclock.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.cleanclock.bean.CleanClockListBean;
import com.bit.communityProperty.adapter.ListBaseAdapter;
import com.bit.communityProperty.utils.GlideUtils;

/**
 * Created by DELL60 on 2018/2/9.
 */

public class CleanClockListAdapter extends ListBaseAdapter<CleanClockListBean.RecordsBean>{
    private Context context;
    public CleanClockListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_clean_clokc,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvTitle.setText(mDataList.get(position).getCreateAt());
        GlideUtils.loadImageView(context,mDataList.get(position).getUrl(),viewHolder.ivImg);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivImg = itemView.findViewById(R.id.iv_img);
        }
    }
}
