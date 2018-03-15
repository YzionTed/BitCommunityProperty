package com.bit.communityProperty.activity;

import android.view.View;
import android.widget.Button;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 抄表输入信息
 * Created by kezhangzhao on 2018/1/22.
 */

public class MeterReadInputActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private Button btUpload;//上传按钮

    @Override
    public int getLayoutId() {
        return R.layout.activity_meter_read_input;
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
        mTitleBarView.setTvTitleText("抄表");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btUpload = findViewById(R.id.bt_next);
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(mContext,MeterReadInputActivity.class));
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData(){
    }
}
