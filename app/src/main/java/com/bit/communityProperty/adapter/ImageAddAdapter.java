package com.bit.communityProperty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 图片添加的adapter。
 * Created by kezhangzhao on 2018/1/20.
 */

public class ImageAddAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private ArrayList<Integer> imageList = new ArrayList();

    public ImageAddAdapter(){

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
