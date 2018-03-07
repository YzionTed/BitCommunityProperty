package com.bit.communityProperty;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.bit.communityProperty.Bluetooth.BluetoothApplication;
import com.bit.communityProperty.bean.ApkInfo;
import com.bit.communityProperty.utils.GlideUtils;
import com.ddclient.push.DongPushMsgManager;
import com.inuker.bluetooth.library.BluetoothClientManger;
import com.netease.nim.uikit.api.NimUIKit;
import com.smarthome.yunintercom.sdk.IntercomSDK;

import java.util.Stack;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by kezhangzhao on 2018/1/8.
 */

public class MyApplication extends Application {

    private static Stack<Activity> activityStack;
    private static MyApplication mInstance;
    private int dWidth; // 设备屏幕的宽
    private int dHeight; // 设备屏幕的高
    private ApkInfo apkInfo;//APK信息类

    private BluetoothApplication blueToothApp;

    private static BluetoothClientManger bluetoothClientManger;//蓝牙梯禁

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        bluetoothClientManger = new BluetoothClientManger(this);
        //初始化极光推送
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);

        blueToothApp = new BluetoothApplication(this);
        NimUIKit.init(mInstance);
//        int result = IntercomSDK.initIntercomSDK(this);//米粒
//        Log.e("===","IntercomSDK  result=="+result);
        //初始化推送设置
        IntercomSDK.initializePush(this, DongPushMsgManager.PUSH_TYPE_GETUI);
        IntercomSDK.initializePush(this, DongPushMsgManager.PUSH_TYPE_JG);

    }


    public ApkInfo getApkInfo() {
        return apkInfo;
    }

    public void setApkInfo(ApkInfo apkInfo) {
        this.apkInfo = apkInfo;
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

//    public static BluetoothClientManger getBluetoothInstance(){
//        return bluetoothClientManger;
//    }

    public Context getContext() {
        return this.getApplicationContext();
    }

    public int getdWidth() {
        return dWidth;
    }

    public void setdWidth(int dWidth) {
        this.dWidth = dWidth;
    }

    public int getdHeight() {
        return dHeight;
    }

    public void setdHeight(int dHeight) {
        this.dHeight = dHeight;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取界面数量
     *
     * @return activity size
     */
    public static int getActivitySize() {
        if (activityStack != null) {
            return activityStack.size();
        }
        return 0;
    }

    // 容器中删除Activity
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    //结束所有activity
    public void finishAllActivity() {
        if (activityStack == null) {
            return;
        }
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
        GlideUtils.guideClearMemory(mInstance);//清理内存缓存
    }

    //获取当前activity
    public Activity getCurrentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    //清除所有activity,除MainActivity之外
    public void retainActivity(Activity a) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(a.getClass())) {
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        }
    }

    /**
     * 退出app
     */
    public void exitApp() {
        if (activityStack != null) {
            synchronized (activityStack) {
             //   IntercomSDK.finishIntercomSDK();
                for (Activity act : activityStack) {
                    if (!act.isFinishing()) {
                        act.finish();
                    }
                }
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack != null && activityStack.size() > 0) {
                activityStack.remove(activity);
                activity.finish();
            }
        }
    }

    public BluetoothApplication getBlueToothApp() {
        return blueToothApp;
    }



}
