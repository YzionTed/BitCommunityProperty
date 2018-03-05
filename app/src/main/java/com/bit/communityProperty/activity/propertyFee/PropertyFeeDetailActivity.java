package com.bit.communityProperty.activity.propertyFee;

import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 物业费管理详情页面
 * Created by kezhangzhao on 2018/2/13.
 */

public class PropertyFeeDetailActivity extends BaseActivity{

    private TitleBarView mTitleBarView;//标题栏
    @Override
    public int getLayoutId() {
        return R.layout.activity_property_fee_detail;
    }

    @Override
    public void initViewData() {
        initView();
        initDate();
    }
    /**
     * 初始化View
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("物业费详情");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化Date
     */
    private void initDate() {
    }
}
