package com.bit.communityProperty.activity.household;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.household.fragment.AuditedFragment;
import com.bit.communityProperty.activity.household.fragment.AuditingFragment;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 住户管理主页面
 * Created by kezhangzhao on 2018/2/8.
 */

public class HouseholdManagementActivity extends BaseActivity{

    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_houseld_management;
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
        mTitleBarView.setTvTitleText("住户管理");
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
    private void initData(){
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
            if (position==0) {
                return AuditingFragment.newInstance(position + 1, mContext);
            }else {
                return AuditedFragment.newInstance(position + 1, mContext);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
