package com.bit.communityProperty.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.fragment.FaultRepairFragment;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 故障维修主页面
 * Created by kezhangzhao on 2018/1/27.
 */

public class FaultRepairActivity extends BaseActivity {
    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_repair;
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
        mTitleBarView.setTvTitleText("故障维修");
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
        mViewPager.setOffscreenPageLimit(3);//ViewPager设置左右各缓存个数
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
            return FaultRepairFragment.newInstance(position + 1,mContext);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
