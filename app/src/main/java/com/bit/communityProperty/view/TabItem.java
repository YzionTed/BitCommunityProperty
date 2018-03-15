package com.bit.communityProperty.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;


/**
 * Created by Dell on 2017/8/5.
 * Created time:2017/8/5 8:43
 */

public class TabItem {

    private ImageView ivViewTabIndicator;
    private TextView txtViewTabIndicator;

    //正常情况下显示的图片
    private int imageNormal;
    //选中情况下显示的图片
    private int imagePress;
    //tab的名字
    private String titleString;

    private View view;
    private Context context;

    //tab对应的fragment
    public Class<? extends Fragment> fragmentClass;

    /**
     * tabitem 初始化
     *
     * @param context       上下文
     * @param imageNormal   正常状态下的tabimage
     * @param imagePress    按下的tabimage
     * @param title         tab 的text
     * @param fragmentClass tab content fragment
     */
    public TabItem(Context context, int imageNormal, int imagePress, String title, Class<? extends Fragment> fragmentClass) {
        this.context = context;
        this.imageNormal = imageNormal;
        this.imagePress = imagePress;
        this.titleString = title;
        this.fragmentClass = fragmentClass;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public int getImageNormal() {
        return imageNormal;
    }

    public int getImagePress() {
        return imagePress;
    }

    public String getTitleString() {
        return titleString;
    }

    public View getView(int pos) {
        if (this.view == null) {
            this.view = LayoutInflater.from(context).inflate(R.layout.view_tab_indicator, null);
            ivViewTabIndicator = (ImageView)view.findViewById(R.id.iv_view_tab_indicator);
            txtViewTabIndicator = (TextView)view.findViewById(R.id.txt_view_tab_indicator);

            if (TextUtils.isEmpty(titleString)) {
                this.txtViewTabIndicator.setVisibility(View.GONE);
            } else {
                this.txtViewTabIndicator.setVisibility(View.VISIBLE);
                this.txtViewTabIndicator.setText(getTitleString());
            }
            this.ivViewTabIndicator.setImageResource(imageNormal);
        }
        return this.view;
    }

    //切换tab的方法
    public void setChecked(boolean isChecked) {
        if (ivViewTabIndicator != null) {
            if (isChecked) {
                ivViewTabIndicator.setImageResource(imagePress);
            } else {
                ivViewTabIndicator.setImageResource(imageNormal);
            }
        }
        if (txtViewTabIndicator != null && !TextUtils.isEmpty(titleString)) {
            if (isChecked) {
                txtViewTabIndicator.setTextColor(context.getResources().getColor(R.color.blue1));
            } else {
                txtViewTabIndicator.setTextColor(context.getResources().getColor(R.color.bs_grary3));
            }
        }
    }
}
