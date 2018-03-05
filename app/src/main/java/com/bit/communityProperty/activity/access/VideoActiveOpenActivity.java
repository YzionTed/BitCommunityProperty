package com.bit.communityProperty.activity.access;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.utils.ToastUtil;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.dongsdk.DongSDKProxy;
import com.ddclient.jnisdk.InfoUser;
import com.gViewerX.util.LogUtils;
import com.smarthome.yunintercom.sdk.AbstractIntercomCallbackProxy;
import com.smarthome.yunintercom.sdk.IntercomSDKProxy;

/**
 * 主动打开门
 */
public class VideoActiveOpenActivity extends BaseActivity implements View.OnClickListener {

    private SurfaceView mSurfaceView;

    private boolean isVideoOn;

    // 正在播放的设备
    private DeviceInfo mDeviceInfo;
    private VideoViewActivityIntercomAccountCallbackImp mIntercomAccountCallBackImpl = new VideoViewActivityIntercomAccountCallbackImp();
    private VideoViewActivityIntercomDeviceCallBackImpl mIntercomDeviceCallBackImpl = new VideoViewActivityIntercomDeviceCallBackImpl();
    private VideoViewActivityIntercomDeviceSettingImpl mIntercomDeviceSettingCallBackImpl = new VideoViewActivityIntercomDeviceSettingImpl();
    private Button bnt_close;
    private Button bnt_open;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_active_open;
    }

    @Override
    public void initViewData() {
        fullScreen(this);
        initView();

    }

    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.sfv_play);
        bnt_close = (Button) findViewById(R.id.bnt_close);
        bnt_open = (Button) findViewById(R.id.bnt_open);

        bnt_close.setOnClickListener(this);
        bnt_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnt_open:
                int result = IntercomSDKProxy.requestDOControl();
                if (result == 0) {
                    ToastUtil.showShort("开门成功");
                } else {
                    ToastUtil.showShort("开门失败");
                }
                break;
            case R.id.bnt_close:
                stopVideo();
                finish();
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDeviceInfo = DongConfiguration.mDeviceInfo;
        LogUtils.i("VideoViewActivity.clazz--->>>onResume.... mDeviceInfo:" + mDeviceInfo);
        boolean initIntercomAccountLan = IntercomSDKProxy.initCompleteIntercomAccountLan();
        if (!initIntercomAccountLan) {
            //帐号注册监听
            IntercomSDKProxy.registerIntercomAccountLanCallback(mIntercomAccountCallBackImpl);
        }
        IntercomSDKProxy.registerIntercomDeviceCallback(mIntercomDeviceCallBackImpl);
        IntercomSDKProxy.registerIntercomDeviceSettingCallback(mIntercomDeviceSettingCallBackImpl);
        videoPlay();

        openDevice();
    }

    /**
     * 打开手机和设备语音
     */
    private void openDevice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


//                DongSDKProxy.requestOpenPhoneSound();//打开手机音响
//                DongSDKProxy.requestOpenPhoneMic();//打开手机麦克
//                DongSDKProxy.requestRealtimePlay(4);//通知设备进入对讲状态


                IntercomSDKProxy.requestOpenPhoneMic();// 打开手机麦克风
                IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_AUDIO);// 打开设备音响
                IntercomSDKProxy.requestOpenPhoneSound();// 打开手机音响
                DongSDKProxy.requestRealtimePlay(4);//通知设备进入对讲状态

            }
        }, 1000);

    }


    /**
     * 打开视频
     */
    private void videoPlay() {
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay.... mDeviceInfo:" + mDeviceInfo);

        boolean initIntercomAccountLan = IntercomSDKProxy.initCompleteIntercomAccountLan();
        if (initIntercomAccountLan) {
            IntercomSDKProxy.registerIntercomAccountLanCallback(mIntercomAccountCallBackImpl);
        } else {
            IntercomSDKProxy.registerIntercomAccountCallback(mIntercomAccountCallBackImpl);
        }
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay ..... registerAccountCallback");
        boolean initCompleteIntercomDevice = IntercomSDKProxy.initCompleteIntercomDevice();
        if (!initCompleteIntercomDevice) {
            IntercomSDKProxy.initIntercomDevice(mIntercomDeviceCallBackImpl);
        } else {
            IntercomSDKProxy.registerIntercomDeviceCallback(mIntercomDeviceCallBackImpl);
        }
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay ..... initIntercomDevice");
        boolean completeIntercomDeviceSetting = IntercomSDKProxy.initCompleteIntercomDeviceSetting();
        if (!completeIntercomDeviceSetting) {
            IntercomSDKProxy.initIntercomDeviceSetting(mIntercomDeviceSettingCallBackImpl);
        } else {
            IntercomSDKProxy.registerIntercomDeviceSettingCallback(mIntercomDeviceSettingCallBackImpl);
        }
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay ..... initIntercomDeviceSetting");
        // ////////////////////////////////////////////
        IntercomSDKProxy.requestStartPlayDevice(this, mSurfaceView, mDeviceInfo, false);

        IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_VIDEO);
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay ..... initCompleteIntercomDevice:"
                + initCompleteIntercomDevice + ",completeIntercomDeviceSetting:" + completeIntercomDeviceSetting);
    }

    /**
     * 关闭
     */
    private void stopVideo() {

        IntercomSDKProxy.requestClosePhoneSound();// 关闭手机音响
        IntercomSDKProxy.requestClosePhoneMic();// 关闭 手机麦克风
        IntercomSDKProxy.requestStopDeice();//

        if (mIntercomDeviceCallBackImpl != null) {
            isVideoOn = false;
            IntercomSDKProxy.requestStopDeice();
        }
    }


    private class VideoViewActivityIntercomAccountCallbackImp extends AbstractIntercomCallbackProxy.IntercomAccountCallbackImp {

        @Override
        public int onAuthenticate(InfoUser tInfo) {
            LogUtils.i("VideoViewActivityIntercomAccountCallbackImp.clazz--->>>onAuthenticate........tInfo:"
                    + tInfo);
            return 0;
        }

        @Override
        public int onUserError(int nErrNo) {
            LogUtils.i("VideoViewActivityIntercomAccountCallbackImp.clazz--->>>onUserError........nErrNo:"
                    + nErrNo);
            return 0;
        }
    }

    private class VideoViewActivityIntercomDeviceSettingImpl extends AbstractIntercomCallbackProxy.IntercomDeviceSettingCallbackImp {

        @Override
        public int onOpenDoor(int result) {
            return 0;
        }
    }

    private class VideoViewActivityIntercomDeviceCallBackImpl extends AbstractIntercomCallbackProxy.IntercomDeviceCallbackImp {

        @Override
        public int onConnect(int nType) {
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onConnect nType:"
                    + nType);
            return 0;
        }

        @Override
        public int onAuthenticate(int nType) {// 认证成功会回调两次-----音频认证成功，视频认证成功
            // 获取音频大小
            int audioSize = IntercomSDKProxy.requestGetAudioQuality();
            // 获取设备亮度
            int bCHS = IntercomSDKProxy.requestGetBCHS();
            // 获取视频品质
            int quality = IntercomSDKProxy.requestGetQuality();
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onAuthenticate nType:"
                    + nType + ",audioSize:" + audioSize + ",bCHS:" + bCHS + ",quality:" + quality);
            return 0;
        }

        @Override
        public int onVideoSucc() {
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onVideoSucc");
            isVideoOn = true;
            return 0;
        }

        @Override
        public int onViewError(int nErrNo) {
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onViewError...nErrNo:"
                    + nErrNo);
            if (isVideoOn) {
                stopVideo();
            }
            //WarnDialog.showDialog(DoorVideoViewActivity.this, null, nErrNo);
            return 0;
        }

        @Override
        public int onTrafficStatistics(float upload, float download) {
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onTrafficStatistics upload:"
                    + upload + ";download:" + download);
            return 0;
        }

        @Override
        public int onPlayError(int nReason, String username) {
            LogUtils.i("VideoViewActivityIntercomDeviceCallBackImpl.clazz--->>>onPlayError nReason:"
                    + nReason + ";username:" + username);
            return 0;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            stopVideo();
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
