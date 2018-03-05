package com.bit.communityProperty.activity.faultManager;

import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 新增故障的页面
 * Created by kezhangzhao on 2018/1/27.
 */

public class FaultAddActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_add;
    }

    @Override
    public void initViewData() {
        initView();
    }

    /**
     * 初始化view
     */
    private void initView(){
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("新增故障");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(){

    }
}
