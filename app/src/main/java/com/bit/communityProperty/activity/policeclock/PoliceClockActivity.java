package com.bit.communityProperty.activity.policeclock;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;

public class PoliceClockActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvRight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_police_clock;
    }

    @Override
    public void initViewData() {
        initView();
        initListener();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        ivBack = findViewById(R.id.btn_back);
        tvRight = findViewById(R.id.btn_right_action_bar);
        tvTitle.setText("保安打卡");
        tvRight.setText("打卡记录");
        tvRight.setVisibility(View.VISIBLE);
    }

    private void initListener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PoliceClockActivity.this,ClockLogActivity.class));
            }
        });
    }
}
