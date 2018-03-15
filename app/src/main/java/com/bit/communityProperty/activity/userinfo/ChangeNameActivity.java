package com.bit.communityProperty.activity.userinfo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;

public class ChangeNameActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView btnBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_name;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("修改姓名");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
