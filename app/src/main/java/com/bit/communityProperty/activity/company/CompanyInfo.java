package com.bit.communityProperty.activity.company;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyInfo extends BaseActivity {


    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_company_info;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("公司");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({ R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_back:
                finish();
                break;
        }
    }
}
