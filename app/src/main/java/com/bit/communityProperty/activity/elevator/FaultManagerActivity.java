package com.bit.communityProperty.activity.elevator;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.elevator.adapter.FragmentAdapter;
import com.bit.communityProperty.activity.elevator.fragment.FaultLogFragment;
import com.bit.communityProperty.activity.elevator.fragment.FixDeclareFragment;
import com.bit.communityProperty.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class FaultManagerActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView btnBack;
    private TabLayout slidingTabs;
    private ViewPager viewPager;
    private String[] tabTitles = new String[]{"故障记录", "维修申报"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_manager;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("故障管理");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        slidingTabs = findViewById(R.id.sliding_tabs);
        viewPager = findViewById(R.id.viewpager);
        final List<Fragment> list = new ArrayList();
        list.add(new FaultLogFragment());
        list.add(new FixDeclareFragment());
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager(), list, tabTitles);
        viewPager.setAdapter(adapter);
        slidingTabs.setupWithViewPager(viewPager);
        slidingTabs.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(slidingTabs, 60, 60);
            }
        });
    }
}
