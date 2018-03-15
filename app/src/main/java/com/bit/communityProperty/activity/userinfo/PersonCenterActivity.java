package com.bit.communityProperty.activity.userinfo;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.utils.DialogUtil;


public class PersonCenterActivity extends BaseActivity {

    private TextView tvDetail;
    private TextView tvTitle;
    private ImageView btnBack;
    private LinearLayout llUpdate;
    private LinearLayout llFeedback;
    private LinearLayout llAbout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_center;
    }

    @Override
    public void initViewData() {
        initView();
        initListener();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("个人中心");
        tvDetail = findViewById(R.id.tv_detail);
        llUpdate = findViewById(R.id.ll_update);
        llFeedback = findViewById(R.id.ll_feedback);
        llAbout = findViewById(R.id.ll_about);
    }
    private void initListener(){
        tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonCenterActivity.this,PersonInfoActivity.class));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showConfirmDialog(PersonCenterActivity.this, "检测更新", "检测到最新版本V2.0.0，是否下载更新？", true, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.dissmiss();
                    }
                });
            }
        });
        llFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonCenterActivity.this,FeedbackActivity.class));
            }
        });
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonCenterActivity.this,AboutUsActivity.class));
            }
        });
    }
}
