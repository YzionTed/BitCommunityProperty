package com.bit.communityProperty.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.utils.AppUtil;
import com.bit.communityProperty.utils.PreferenceUtils;
import com.bit.communityProperty.utils.SPUtil;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by kezhangzhao on 2018/1/10.
 */

public class LauncherActivity extends BaseActivity {

    private static final int MSG_SET_ALIAS = 1001;// 极光注册
    private static int mJPushTimes = 5;// 极光注册次数
    private String versionCode;
    private String versionName;


    @Override
    public int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    public void initViewData() {
        registerJPush();
        initScreenParam();
        initData();
    }

    private void initData(){
        versionCode = String.valueOf(AppUtil.getVersionCode(MyApplication.getInstance().getContext()));
        versionName = AppUtil.getVersionName(MyApplication.getInstance().getContext());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (!TextUtils.isEmpty((String) SPUtil.get(mContext,AppConfig.id,""))&&!TextUtils.isEmpty((String)SPUtil.get(mContext,AppConfig.token,""))){
                    intent = new Intent(mContext,MainActivity.class);
                }else{
                    intent = new Intent(mContext, LogonActivity.class);
                }
                if(getIntent().getBundleExtra(AppConfig.EXTRA_BUNDLE) != null){
                    intent.putExtra(AppConfig.EXTRA_BUNDLE, getIntent().getBundleExtra(AppConfig.EXTRA_BUNDLE));
                }
                startActivity(intent);
                finish();
            }
        },3000);
    }

    /**
     * 根据是否是第一次打开或是否已登录，获取传递过来的值来确定打开哪个Activity
     *
     * @param cls
     */
    private void startActivity(final Class<?> cls) {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(mContext, cls);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                LauncherActivity.this.finish();
            }
        }, 2 * 1000);
    }

    /**
     * 注册极光推送
     */
    private void registerJPush() {
        // 调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS));
    }

    private static TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set set) {
            //            LogManager.printInfoLog("TAG", TAG + "与极光服务器连接情况" + code);
            switch (code) {
                case 0:
                    PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, PreferenceConst.ISPUSH, "1");
                    break;
                case 6002:
                    if (mJPushTimes > 1) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS), 1000 * 60);
                        mJPushTimes -= 1;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAlias(MyApplication.getInstance().getApplicationContext(),
                            PreferenceUtils.createAlias(MyApplication.getInstance().getApplicationContext()), mAliasCallback);
                    break;
                default:
                    break;
            }
        }
    };


    /**
     * 获取屏幕宽高
     */
    private void initScreenParam() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        MyApplication.getInstance().setdWidth(width);
        MyApplication.getInstance().setdHeight(height);
    }

    //启动中不给退出-.-
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
