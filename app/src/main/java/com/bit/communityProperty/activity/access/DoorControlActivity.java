package com.bit.communityProperty.activity.access;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.access.fragment.BluetoothDoorFragment;
import com.bit.communityProperty.activity.access.fragment.RemoteDoorFragment;
import com.bit.communityProperty.utils.ViewUtils;
import com.bit.communityProperty.view.TitleBarView;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.dongsdk.PushInfo;
import com.ddclient.jnisdk.InfoUser;
import com.ddclient.push.DongPushMsgManager;
import com.gViewerX.util.LogUtils;
import com.smarthome.yunintercom.sdk.AbstractIntercomCallbackProxy;
import com.smarthome.yunintercom.sdk.IntercomSDK;
import com.smarthome.yunintercom.sdk.IntercomSDKProxy;

import java.util.ArrayList;

/**
 * 门禁主页
 * Created by kezhangzhao on 2018/1/25.
 */

public class DoorControlActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tabLayout;//顶部菜单
    private ViewPager mViewPager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_door_control;
    }

    @Override
    public void initViewData() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("智能门禁");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                ViewUtils.setIndicator(tabLayout, 60, 60);
            }
        });

        boolean initIntercomAccount = IntercomSDKProxy.initCompleteIntercomAccount();
        if (!initIntercomAccount) {
            IntercomSDKProxy.initIntercomAccount(mIntercomAccountProxy);
        }

    }

    private ListActivityIntercomAccountProxy mIntercomAccountProxy = new ListActivityIntercomAccountProxy();

    private class ListActivityIntercomAccountProxy extends AbstractIntercomCallbackProxy.IntercomAccountCallbackImp {

        @Override
        public int onAuthenticate(InfoUser tInfo) {
            LogUtils.i("ListActivity.clazz--->>>onAuthenticate........tInfo:"
                    + tInfo);
            return 0;
        }

        @Override
        public int onUserError(int nErrNo) {
            LogUtils.e("ListActivity.clazz--->>>onUserError........nErrNo:"
                    + nErrNo);
            return 0;
        }

        @Override
        public int onNewListInfo() {
            ArrayList<DeviceInfo> deviceInfoList = IntercomSDKProxy.requestGetDeviceListFromCache(DoorControlActivity.this);

            LogUtils.e("ListActivity.clazz--->>>onNewListInfo........deviceInfoList.size:"
                    + deviceInfoList.size());
            return 0;
        }

        /**
         * 平台在线推送时回调该方法
         */
        @Override
        public int onCall(ArrayList<DeviceInfo> list) {
            LogUtils.e("ListActivity.clazz--->>>onCall........list.size():" + list.size());
            Toast.makeText(DoorControlActivity.this, "平台推送到达!!!", Toast.LENGTH_SHORT).show();
            int size = list.size();
            if (size > 0) {
                DeviceInfo deviceInfo = list.get(0);
                String message = deviceInfo.deviceName + deviceInfo.dwDeviceID
                        + deviceInfo.msg;
                DongPushMsgManager.pushMessageChange(DoorControlActivity.this, message);
            }
            return 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntercomSDKProxy.registerIntercomAccountCallback(mIntercomAccountProxy);
        //ArrayList<DeviceInfo> deviceList = IntercomSDKProxy.requestGetDeviceListFromCache(this);
       // commonAdapter.setDatas(dataList);
       // LogUtils.e("ListActivity.clazz--->>>onResume.....deviceList:" + deviceList);
    }



    @Override
    public void onPause() {
        super.onPause();
        IntercomSDKProxy.unRegisterIntercomAccountCallback(mIntercomAccountProxy);
    }




    /**
     * 继承ViewPagerOnTabSelectedListener，
     * 因为要实现标题栏的右边文字来回切换：（蓝牙记录——远程记录）
     * 如果没有这个需求，可以直接用父类。
     */
//    class MyViewPagerOnTabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener {
//
//        public MyViewPagerOnTabSelectedListener(ViewPager viewPager) {
//            super(viewPager);
//        }
//
//        @Override
//        public void onTabSelected(TabLayout.Tab tab) {
//            mViewPager.setCurrentItem(tab.getPosition());
//            switch (tab.getPosition()) {
//                case 0:
//                    mTitleBarView.setRightText("蓝牙记录");
//                    break;
//                case 1:
//                    mTitleBarView.setRightText("远程记录");
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

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
            if (position == 0) {//蓝牙门禁
                return RemoteDoorFragment.newInstance(position + 1);
            } else {//远程门禁
                return BluetoothDoorFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        IntercomSDKProxy.requestSetPushInfo(0);
//        IntercomSDK.finishIntercomSDK();
//        System.exit(0);
    }


}
