package com.bit.communityProperty.activity.elevator;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;

public class ElevatorDetailActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView btnBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_elevator_detail;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("电梯详情");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
