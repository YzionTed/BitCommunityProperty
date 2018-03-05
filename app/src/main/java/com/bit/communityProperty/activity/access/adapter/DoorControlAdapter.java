package com.bit.communityProperty.activity.access.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.example.library.FlowAdapter;

import java.util.ArrayList;

/**
 * 门禁中的网格布局adapter
 * Created by kezhangzhao on 2018/1/25.
 */

public class DoorControlAdapter extends FlowAdapter<String> {

    private Context mContext;
    private ArrayList<String> dataList;

    public DoorControlAdapter(Context context,ArrayList<String> datas) {
        super(datas);
        this.mContext = context;
        this.dataList = datas;
    }

    @Override
    public View getView(int position) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.door_control_adapter_item,null);
        TextView textView = item.findViewById(R.id.tv_text);
        textView.setText(dataList.get(position));
        return item;
    }
}
