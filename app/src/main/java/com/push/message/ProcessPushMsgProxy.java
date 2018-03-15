
package com.push.message;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bit.communityProperty.activity.access.VideoUnActiveOpenActivity;
import com.bit.communityProperty.bean.DoorMiLiDevicesBean;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.utils.PreferenceUtils;
import com.bit.communityProperty.utils.ToastUtil;
import com.ddclient.configuration.DongConfiguration;
import com.ddclient.dongsdk.DeviceInfo;
import com.ddclient.dongsdk.PushMsgBean;
import com.gViewerX.util.LogUtils;
import com.google.gson.Gson;


/**
 * 必须拷贝这个类到应用中去，此类用于离线推送处理的中转站
 */

public class ProcessPushMsgProxy {


    /**
     * 处理离线推送
     *
     * @param context     上下文
     * @param dongMessage 消息载体
     */

    public static void processPushMsg(Context context, PushMsgBean dongMessage) {

        startIntentActivity(context, dongMessage);
        //   ToastUtil.showShort("极光推送" + dongMessage.getDeviceId());
        LogUtils.i("ProcessPushMsgProxy.clazz---------->>>pushMessageReceiver PushMsgBean:"
                + dongMessage);
    }


    /**
     * 自定义的消息格式
     *
     * @param context 上下文对象
     * @param msg     自定义推送信息
     */

    public static void processPushMsg(Context context, String msg) {
        LogUtils.i("ProcessPushMsgProxy.clazz----------->>>message define message:" + msg);
    }


    /**
     * 处理消息通知(目前不支持个推和华为)
     *
     * @param context       上下文
     * @param notifyTitle   消息通知标题
     * @param notifyContent 消息通知内容
     */

    public static void processPushMsg(Context context, String notifyTitle, String notifyContent) {
        LogUtils.i("ProcessPushMsgProxy.clazz---------->>>message default notifyTitle:" +
                notifyTitle + ",notifyContent:" + notifyContent);
    }


    /**
     * 进入可以米粒视频的界面
     *
     * @param context
     * @param dongMessage
     */
    public static void startIntentActivity(Context context, PushMsgBean dongMessage) {
        //  C1<大门测试1> <设备呼叫> <2018-02-28 16:19:25>|8|1365
        // (deviceId:1365,message:C1<大门测试1> <设备呼叫> <2018-02-28 17:07:01>,pushState:8,pushTime:2018-02-28 17:07:01)
        // if ( dongMessage.getMessage().startsWith("C1") || dongMessage.getMessage().startsWith("C3")) {
        ToastUtil.showShort(dongMessage.getMessage() + dongMessage.getDeviceId());
        Log.e("startIntentActivity", "推送过来的C1 或者C3=" + dongMessage.getDeviceId());
        String prefString = PreferenceUtils.getPrefString(context, PreferenceConst.PRE_NAME, PreferenceConst.MILIDOORDEVICE, "");
        if (prefString != null) {
            DoorMiLiDevicesBean doorMiLiDevicesBean = new Gson().fromJson(prefString, DoorMiLiDevicesBean.class);
            if (doorMiLiDevicesBean.getDeviceInfoList().size() > 0) {
                for (int i = 0; i < doorMiLiDevicesBean.getDeviceInfoList().size(); i++) {
                    DeviceInfo deviceInfo = doorMiLiDevicesBean.getDeviceInfoList().get(i);
                    Log.e("startIntentActivity", "手机搜索Id==" + deviceInfo.dwDeviceID + "  推送过来的Id=" + dongMessage.getDeviceId());
                    if (deviceInfo.dwDeviceID == Integer.parseInt(dongMessage.getDeviceId())) {
                        DongConfiguration.mDeviceInfo = deviceInfo;
                        Intent intent = new Intent(context, VideoUnActiveOpenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        break;
                    }
                }
            }

        } else {
            Log.e("startIntentActivity", "prefString==null");
        }

        // }


        // C1<大门测试1> <设备呼叫> <2018-02-28 15:41:17>|8|1365

        //  C0<大门测试1> <设备呼叫> <2018-02-28 14:47:49>|8|1365
//        Intent intent = new Intent(context, VideoUnActiveOpenActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("date", msg);
//        intent.putExtras(bundle);
//        DeviceInfo deviceInfo=new DeviceInfo();
//         DongConfiguration.mDeviceInfo = deviceInfo;
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
    }
}

