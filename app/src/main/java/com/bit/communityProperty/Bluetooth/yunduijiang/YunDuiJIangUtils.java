package com.bit.communityProperty.Bluetooth.yunduijiang;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.bean.DoorMiLiDevicesBean;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.utils.PreferenceUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.dongsdk.PushInfo;
import com.ddclient.jnisdk.InfoUser;
import com.ddclient.push.DongPushMsgManager;
import com.gViewerX.util.LogUtils;
import com.google.gson.Gson;
import com.smarthome.yunintercom.sdk.AbstractIntercomCallbackProxy;
import com.smarthome.yunintercom.sdk.IntercomSDK;
import com.smarthome.yunintercom.sdk.IntercomSDKProxy;

import java.util.ArrayList;

/**
 * Created by Dell on 2018/2/26.
 */

public class YunDuiJIangUtils {

    private Context context;
    private OnYunDuiJIangListener onYunDuiJIangListener;

    public YunDuiJIangUtils(Context context) {
        this.context = MyApplication.getInstance();
        init();
    }

    /**
     * 初始化
     */
    private void init() {

        int result = IntercomSDK.initIntercomSDK(context);//米粒
        Log.e("===", "IntercomSDK  result==" + result);

        //初始化推送设置
        boolean initIntercomAccount = IntercomSDKProxy.initCompleteIntercomAccount();
        if (!initIntercomAccount) {
            IntercomSDKProxy.initIntercomAccount(mIntercomAccountProxy);
        }
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    public void login(String name, String password) {
        IntercomSDKProxy.initIntercomAccount(mIntercomAccountProxy);
        IntercomSDKProxy.login((String) SPUtil.get(context, AppConfig.phone,""), "123456");
    }

    public void onResume() {
        IntercomSDKProxy.registerIntercomAccountCallback(mIntercomAccountProxy);
    }


    public void onPause() {
        IntercomSDKProxy.unRegisterIntercomAccountCallback(mIntercomAccountProxy);
    }

    private LoginActivityIntercomAccountProxy mIntercomAccountProxy = new LoginActivityIntercomAccountProxy();

    private class LoginActivityIntercomAccountProxy extends AbstractIntercomCallbackProxy.IntercomAccountCallbackImp {

        @Override
        public int onAuthenticate(InfoUser tInfo) {

            DongConfiguration.mUserInfo = tInfo;
            LogUtils.e("认证成功........tInfo:"
                    + tInfo);
            IntercomSDKProxy.requestSetPushInfo(PushInfo.PUSHTYPE_FORCE_ADD);
            IntercomSDKProxy.requestGetDeviceListFromPlatform();
            return 0;
        }

        @Override
        public int onUserError(int nErrNo) {

            LogUtils.e("LoginActivity.clazz--->>>onUserError........nErrNo:"
                    + nErrNo);
//            IntercomSDK.reInitIntercomSDK();
            return 0;
        }

        @Override
        public int onNewListInfo() {
            ArrayList<DeviceInfo> deviceInfoList = IntercomSDKProxy.requestGetDeviceListFromCache(context);
            if (onYunDuiJIangListener != null) {
                onYunDuiJIangListener.onNewListInfo(deviceInfoList);
            }
            DoorMiLiDevicesBean doorMiLiDevicesBean = new DoorMiLiDevicesBean();
            doorMiLiDevicesBean.setDeviceInfoList(deviceInfoList);
            PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, PreferenceConst.MILIDOORDEVICE, new Gson().toJson(doorMiLiDevicesBean));

            LogUtils.e("ListActivity.clazz--->>>onNewListInfo........deviceInfoList.size:"
                    + deviceInfoList.size());
            return 0;
        }

        @Override
        public int onCall(ArrayList<DeviceInfo> list) {

            Log.e("dsfd", "onCall........list.size():" + list.size());
            Toast.makeText(context, "平台推送到达!!!", Toast.LENGTH_SHORT).show();
            int size = list.size();
            if (size > 0) {
                DeviceInfo deviceInfo = list.get(0);
                String message = deviceInfo.deviceName + deviceInfo.dwDeviceID
                        + deviceInfo.msg;
                DongPushMsgManager.pushMessageChange(context, message);
            }
            return super.onCall(list);
        }

    }

    public void onDestry() {
        IntercomSDKProxy.requestSetPushInfo(0);
        IntercomSDK.finishIntercomSDK();
    }

    public interface OnYunDuiJIangListener {
        void onNewListInfo(ArrayList<DeviceInfo> deviceInfoList);
    }

    public void setOnYunDuiJIangListener(OnYunDuiJIangListener onYunDuiJIangListener) {
        this.onYunDuiJIangListener = onYunDuiJIangListener;
    }

}
