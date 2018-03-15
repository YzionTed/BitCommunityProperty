package com.bit.communityProperty.activity.propertyFee;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.propertyFee.fragment.PropertyFeeFragment;
import com.bit.communityProperty.view.TitleBarView;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 物业费主页
 * Created by kezhangzhao on 2018/2/10.
 */

public class PropertyFeeActivity extends BaseActivity{
    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;
    private TimePickerDialog mDialogYearMonth;//时间选择器

    @Override
    public int getLayoutId() {
        return R.layout.activity_property_fee;
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
        mTitleBarView.setTvTitleText("本期物业费");
        mTitleBarView.setIvRightImage(R.drawable.icon_date_change);
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext,"弹出日历选择器",Toast.LENGTH_LONG).show();
//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
//                bottomSheetDialog.setContentView(R.layout.time_select);
//                bottomSheetDialog.show();
                    mDialogYearMonth = new TimePickerDialog.Builder()
                            .setType(Type.YEAR_MONTH)
                            .setCancelStringId("取消")
                            .setSureStringId("完成")
                            .setTitleStringId(null)//设置中间标题文字
                            .setWheelItemTextSize(14)//设置年月的字体大小，默认是12
                            .setThemeColor(getResources().getColor(R.color.title_background))//设置标题颜色
//                        .setToolBarTextColor(getResources().getColor(R.color.red_20))
//                        .setWheelItemTextNormalColor(getResources().getColor(R.color.yellow))
                            .setWheelItemTextSelectorColor(getResources().getColor(R.color.tv_black_33))
                            .setCallBack(new MyDateSetListener())
                            .build();
                mDialogYearMonth.show(getSupportFragmentManager(),"year_month");
            }
        });
        mViewPager = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
    }

    /**
     * 时间选择器的回调方法
     */
    public class MyDateSetListener implements OnDateSetListener{

        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
            String text = getDateToString(millseconds);
            Toast.makeText(mContext,text,Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 转化时间格式：年月
     * @param time
     * @return
     */
    public String getDateToString(long time) {
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        Date d = new Date(time);
        return sf.format(d);
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
                return PropertyFeeFragment.newInstance(position + 1, mContext);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
