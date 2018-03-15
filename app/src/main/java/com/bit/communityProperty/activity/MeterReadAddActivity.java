package com.bit.communityProperty.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 新增抄表页面
 * Created by kezhangzhao on 2018/1/22.
 */

public class MeterReadAddActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private Button btNext;//下一页按钮

    @Override
    public int getLayoutId() {
        return R.layout.activity_meter_read_add;
    }

    @Override
    public void initViewData() {
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView(){
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("新增抄表记录");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btNext = findViewById(R.id.bt_next);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,MeterReadInputActivity.class));
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(){
    }
}
