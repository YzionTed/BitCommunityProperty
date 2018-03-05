package com.bit.communityProperty.activity.deviceManagement;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.fragment.DoorControlFragment;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.deviceManagement.fragment.CameraFragment;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.view.TitleBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备管理的主页面
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceManagementActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//            return PlaceholderFragment.newInstance(position + 1,mContext);
            if (position == 0) {
                return CameraFragment.newInstance(position + 1, mContext);
            } else {
                return DoorControlFragment.newInstance(position + 1, mContext);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
