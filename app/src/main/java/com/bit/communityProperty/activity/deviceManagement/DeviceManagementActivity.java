package com.bit.communityProperty.activity.deviceManagement;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.fragment.CarFragmet;
import com.bit.communityProperty.activity.deviceManagement.fragment.DoorControlFragment;
import com.bit.communityProperty.activity.deviceManagement.fragment.ElevatorFragment;
import com.bit.communityProperty.activity.elevator.adapter.FragmentAdapter;
import com.bit.communityProperty.activity.safetywarning.fragment.FinishedListFragment;
import com.bit.communityProperty.activity.safetywarning.fragment.WaitDealListFragment;
import com.bit.communityProperty.activity.safetywarning.fragment.WaitSolveListFragment;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.deviceManagement.fragment.CameraFragment;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备管理的主页面
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceManagementActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

    private String[] tabTitles = new String[]{"摄像头", "门禁","电梯","车闸"};
    @Override
    public int getLayoutId() {
        return R.layout.activity_device_management;
    }

    @Override
    public void initViewData() {
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("设备管理");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        final List<Fragment> list = new ArrayList();
        list.add(new CameraFragment());
        list.add(new DoorControlFragment());
        list.add(new ElevatorFragment());
        list.add(new CarFragmet());
        FragmentAdapter adapter = new FragmentAdapter(this.getSupportFragmentManager(), list, tabTitles);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }
}
