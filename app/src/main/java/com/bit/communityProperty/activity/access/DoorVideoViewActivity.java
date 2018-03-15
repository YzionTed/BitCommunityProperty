package com.bit.communityProperty.activity.access;

import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.jnisdk.InfoUser;
import com.gViewerX.util.LogUtils;
import com.smarthome.yunintercom.sdk.AbstractIntercomCallbackProxy;
import com.smarthome.yunintercom.sdk.IntercomSDKProxy;

/**
 * 门禁开门
 */
public class DoorVideoViewActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTySpk;
    private TextView mTvVideo;
    private TextView mTvStop;
    private TextView mTvAudio;
    private TextView tv_open;
    private TextView tv_open_view;

    private SurfaceView mSurfaceView;
    private boolean isVideoOn;
    private boolean isSoundOn;
    private boolean isMicroOn;

    // 正在播放的设备
    private DeviceInfo mDeviceInfo;
    private VideoViewActivityIntercomAccountCallbackImp mIntercomAccountCallBackImpl = new VideoViewActivityIntercomAccountCallbackImp();
    private VideoViewActivityIntercomDeviceCallBackImpl mIntercomDeviceCallBackImpl = new VideoViewActivityIntercomDeviceCallBackImpl();
    private VideoViewActivityIntercomDeviceSettingImpl mIntercomDeviceSettingCallBackImpl = new VideoViewActivityIntercomDeviceSettingImpl();

    @Override
    public int getLayoutId() {
        return R.layout.activity_door_video_view;
    }

    @Override
    public void initViewData() {
        mTySpk = (TextView) findViewById(R.id.tv_spk);
        tv_open = (TextView) findViewById(R.id.tv_open);
        tv_open_view = (TextView) findViewById(R.id.tv_open_view);
        mTvAudio = (TextView) findViewById(R.id.tv_audio);
        mTvVideo = (TextView) findViewById(R.id.tv_video);
        mTvStop = (TextView) findViewById(R.id.tv_stop);
        mSurfaceView = (SurfaceView) findViewById(R.id.sfv_play);
        mTySpk.setOnClickListener(this);
        mTvAudio.setOnClickListener(this);
        mTvVideo.setOnClickListener(this);
        mTvStop.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDeviceInfo = DongConfiguration.mDeviceInfo;
        LogUtils.i("VideoViewActivity.clazz--->>>onResume.... mDeviceInfo:" + mDeviceInfo);
        boolean initIntercomAccountLan = IntercomSDKProxy.initCompleteIntercomAccountLan();
        if (!initIntercomAccountLan) {
            IntercomSDKProxy.registerIntercomAccountLanCallback(mIntercomAccountCallBackImpl);
        }
        IntercomSDKProxy.registerIntercomDeviceCallback(mIntercomDeviceCallBackImpl);
        IntercomSDKProxy.registerIntercomDeviceSettingCallback(mIntercomDeviceSettingCallBackImpl);
        videoPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();

        boolean initIntercomAccountLan = IntercomSDKProxy.initCompleteIntercomAccountLan();
        if (initIntercomAccountLan) {
            IntercomSDKProxy.unRegisterIntercomAccountLanCallback(mIntercomAccountCallBackImpl);
        } else {
            IntercomSDKProxy.unRegisterIntercomAccountCallback(mIntercomAccountCallBackImpl);
        }
        IntercomSDKProxy.unRegisterIntercomDeviceCallback(mIntercomDeviceCallBackImpl);
        IntercomSDKProxy.unRegisterIntercomDeviceSettingCallback(mIntercomDeviceSettingCallBackImpl);
        LogUtils.i("VideoViewActivity.clazz--->>>onPause .... mDeviceInfo:" + mDeviceInfo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_spk://对讲
                if (!isSoundOn) {
                    Toast.makeText(DoorVideoViewActivity.this, "请先打开语音按钮！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isMicroOn) {
                    IntercomSDKProxy.requestOpenPhoneMic();// 打开手机麦克风
                    IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_AUDIO);// 打开设备音响
                    mTySpk.setBackgroundResource(R.color.blue);
                    isMicroOn = true;
                } else {
                    IntercomSDKProxy.requestStop(IntercomSDKProxy.PLAY_TYPE_AUDIO_USER);// 关闭设备音响
                    IntercomSDKProxy.requestClosePhoneMic();// 关闭 手机麦克风
                    isMicroOn = false;
                    mTySpk.setBackgroundResource(R.color.bar_grey);
                }
                break;
            case R.id.tv_audio://语音
                if (!isSoundOn) {
                    IntercomSDKProxy.requestOpenPhoneSound();// 打开手机音响
                    IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_AUDIO);// 打开设备麦克风
                    mTvAudio.setBackgroundResource(R.color.blue);
                    isSoundOn = true;
                } else {
                    if (isMicroOn) {
                        mTySpk.performClick();
                    }
                    IntercomSDKProxy.requestClosePhoneSound();// 关闭手机音响
                    IntercomSDKProxy.requestStop(IntercomSDKProxy.PLAY_TYPE_VIDEO);// 关闭设备麦克风
                    mTvAudio.setBackgroundResource(R.color.bar_grey);
                    isSoundOn = false;
                }
                break;
            case R.id.tv_video:
                if (!isVideoOn) {
                    IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_VIDEO);// 打开设备摄像头
                    mTvVideo.setBackgroundResource(R.color.blue);
                    isVideoOn = true;
                } else {
                    IntercomSDKProxy.requestStop(IntercomSDKProxy.PLAY_TYPE_VIDEO);// 关闭设备摄像头
                    mTvVideo.setBackgroundResource(R.color.bar_grey);
                    isVideoOn = false;
                }
                break;
            case R.id.tv_stop:
                IntercomSDKProxy.requestTakePicture("Viewer", mDeviceInfo);// 拍照截图
                // PS:Viewer手机根目录下的Viewer文件夹
                stopVideo();
                DoorVideoViewActivity.this.finish();
//                byte[] a = new byte[1];
//                a[0] = 0x01;
//                int sRet = IntercomSDKProxy.requestSdkTunnel(mDeviceInfo.dwDeviceID, a);
//                LogUtils.i("VideoViewActivity.java--->>>requestSdkTunnel = " + sRet);
                break;
            case R.id.tv_open:
                IntercomSDKProxy.requestUnlock(mDeviceInfo.dwDeviceID);
                break;
            case R.id.tv_open_view:
                IntercomSDKProxy.requestDOControl();
                break;
            default:
                break;
        }
    }


    private void videoPlay() {
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay.... mDeviceInfo:" + mDeviceInfo);
        if (mDeviceInfo == null) {
            Toast.makeText(DoorVideoViewActivity.this, "请先在列表中选择一个设备",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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
        mTvVideo.setBackgroundResource(R.color.blue);
        IntercomSDKProxy.requestRealtimePlay(IntercomSDKProxy.PLAY_TYPE_VIDEO);
        LogUtils.i("VideoViewActivity.clazz--->>>videoPlay ..... initCompleteIntercomDevice:"
                + initCompleteIntercomDevice + ",completeIntercomDeviceSetting:" + completeIntercomDeviceSetting);
    }

    private void stopVideo() {
        mTySpk.setBackgroundResource(R.color.bar_grey);
        mTvVideo.setBackgroundResource(R.color.bar_grey);
        mTvAudio.setBackgroundResource(R.color.bar_grey);

        IntercomSDKProxy.requestClosePhoneSound();// 关闭手机音响
        IntercomSDKProxy.requestClosePhoneMic();// 关闭 手机麦克风

        if (mIntercomDeviceCallBackImpl != null) {
            isSoundOn = false;
            isMicroOn = false;
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


}
