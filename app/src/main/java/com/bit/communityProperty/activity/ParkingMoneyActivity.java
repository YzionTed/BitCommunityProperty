package com.bit.communityProperty.activity;

import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 停车收费页面
 * Created by kezhangzhao on 2018/1/19.
 */

public class ParkingMoneyActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏

    @Override
    public int getLayoutId() {
        return R.layout.activity_parking_money;
    }

    @Override
    public void initViewData() {
        initView();
        initData();
    }

    private void initView(){
        mTitleBarView = findViewById(R.id.titlebarview);
    }

    private void initData(){
        mTitleBarView.setTvTitleText("停车收费");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
