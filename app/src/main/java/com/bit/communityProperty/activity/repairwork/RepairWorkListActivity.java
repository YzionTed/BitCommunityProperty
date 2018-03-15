package com.bit.communityProperty.activity.repairwork;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.elevator.adapter.FragmentAdapter;
import com.bit.communityProperty.activity.repairwork.fragment.AllWorkOrderFragment;
import com.bit.communityProperty.activity.repairwork.fragment.FinishOrderFragment;
import com.bit.communityProperty.activity.repairwork.fragment.WaitRepairOrderFragment;
import com.bit.communityProperty.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RepairWorkListActivity extends BaseActivity {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_right_action_bar)
    TextView btnRightActionBar;
    @BindView(R.id.iv_right_action_bar)
    ImageView ivRightActionBar;
    @BindView(R.id.pb_loaing_action_bar)
    ProgressBar pbLoaingActionBar;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private String[] tabTitles = new String[]{"全部", "待检修","已完成"};
    @Override
    public int getLayoutId() {
        return R.layout.activity_repair_work;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("维修工单");
        final List<Fragment> list = new ArrayList();
        list.add(new AllWorkOrderFragment());
        list.add(new WaitRepairOrderFragment());
        list.add(new FinishOrderFragment());
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager(), list, tabTitles);
        viewpager.setAdapter(adapter);
        slidingTabs.setupWithViewPager(viewpager);
        slidingTabs.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(slidingTabs, 20, 20);
            }
        });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
