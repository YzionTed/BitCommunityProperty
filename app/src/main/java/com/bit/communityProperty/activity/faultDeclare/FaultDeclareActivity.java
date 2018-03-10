package com.bit.communityProperty.activity.faultDeclare;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultDeclare.fragment.EvaluateFaultActivity;
import com.bit.communityProperty.activity.faultManager.fragment.CheckFaultFragment;
import com.bit.communityProperty.activity.faultManager.fragment.WaitFaultFragment;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.faultManager.FaultAddActivity;
import com.bit.communityProperty.activity.faultManager.fragment.AllFaultFragment;
import com.bit.communityProperty.view.TitleBarView;

/**
 * 故障申报主页面
 * 故障申报模块跟故障管理模块大多数相似，有选择性公用view和部分代码
 * Created by kezhangzhao on 2018/2/12.
 */

public class FaultDeclareActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_declare;
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
        mTitleBarView.setTvTitleText("故障申报");
        mTitleBarView.setRightText("添加");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext,FaultAddActivity.class));
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
            if (position==0) {//全部
                return AllFaultFragment.newInstance(position + 1, mContext);
            }else if (position==1){//待受理
                return WaitFaultFragment.newInstance(position + 1, mContext);
            }else if (position==1) {//待检修
                return CheckFaultFragment.newInstance(position + 1, mContext);
            }else {//待评价
                return EvaluateFaultActivity.newInstance(position + 1, mContext);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
