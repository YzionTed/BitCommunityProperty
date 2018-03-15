package com.bit.communityProperty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.example.library.FlowAdapter;

import java.util.ArrayList;

/**
 *
 * 九宫格的adapter
 * Created by kezhangzhao on 2018/1/11.
 */

public class HomeMenuAdapter extends FlowAdapter<HomeMenuBean>{

    private Context mContext;
    private ArrayList<HomeMenuBean> mHomeMenuBeanList;

    public HomeMenuAdapter(Context context,ArrayList<HomeMenuBean> datas) {
        super(datas);
        this.mContext = context;
        this.mHomeMenuBeanList = datas;
    }

    @Override
    public View getView(int position) {
        View item = LayoutInflater.from(mContext).inflate(R.layout.picture_comment_item,null);
        ImageView imageView = item.findViewById(R.id.iv_icon);
//        TextView  textView = item.findViewById(R.id.tv_text);
        imageView.setImageResource(mHomeMenuBeanList.get(position).getmImageID());
//        textView.setText(mHomeMenuBeanList.get(position).getmText());
        return item;
    }


}
