package com.bit.communityProperty.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.elevatorcontrol.ChangeElevatorActivity;
import com.bit.communityProperty.activity.household.HouseholdManagementActivity;
import com.bit.communityProperty.activity.newsdetail.NewsDetail;
import com.bit.communityProperty.activity.safetywarning.SafeWarningListActivity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;

import java.util.Random;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

/**
 * Created by kezhangzhao on 2018/1/8.
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "JIGUANG-Example";
    private Random random;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            random = new Random();
//            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//                processCustomMessage(context, bundle);
                String extras = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                LogManager.i(extras + "");
                final JPushBean jPushBean = GsonUtils.getInstance().fromJson(extras, JPushBean.class);
                if (jPushBean.getAction().equals("100301")){//一键报警推送
                    if (MyApplication.getActivitySize() != 0) {
                        DialogUtil.dissmiss();
                        DialogUtil.showTipDialog(MyApplication.getInstance().getCurrentActivity(), R.mipmap.ic_jb_yuan, jPushBean.getData().getTitle(), "取消", "确定", true, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (MyApplication.getInstance().getCurrentActivity() instanceof SafeWarningListActivity){
                                    RxBus.get().post(jPushBean);
                                }else{
                                    Intent i = new Intent(MyApplication.getInstance().getCurrentActivity(), SafeWarningListActivity.class);
                                    i.putExtra("jpushbean", jPushBean);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    MyApplication.getInstance().getCurrentActivity().startActivity(i);
                                }
                                DialogUtil.dissmiss();
                            }
                        });
                    } else {
                        JPushLocalNotification ln = new JPushLocalNotification();
                        ln.setBuilderId(0);
                        ln.setContent(jPushBean.getData().getAddress());
                        ln.setTitle(jPushBean.getData().getTitle());
                        ln.setNotificationId(random.nextInt());
                        ln.setExtras(extras);
                        JPushInterface.addLocalNotification(MyApplication.getInstance(), ln);
                    }
                }
//                JSONObject jsonObject = new JSONObject(extras);
//                JSONObject jsonData = jsonObject.getJSONObject("data");
//                String title = jsonData.getString("title");
//                String content = jsonData.getString("work_time");
//                sendBradcast(context,title,content);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //接收到推送下来的通知
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogManager.i(extras + "通知");
                final JPushBean jPushBean = GsonUtils.getInstance().fromJson(extras, JPushBean.class);
                switch (jPushBean.getAction()){
                    case "100101"://社区公告
                        break;
                    case "100301"://一键报警
                        if (MyApplication.getActivitySize() != 0) {
                            DialogUtil.dissmiss();
                            DialogUtil.showTipDialog(MyApplication.getInstance().getCurrentActivity(), R.mipmap.ic_jb_yuan, jPushBean.getData().getTitle(), "取消", "确定", true, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (MyApplication.getInstance().getCurrentActivity() instanceof SafeWarningListActivity){
                                        RxBus.get().post(jPushBean);
                                    }else{
                                        Intent i = new Intent(MyApplication.getInstance().getCurrentActivity(), SafeWarningListActivity.class);
                                        i.putExtra("jpushbean", jPushBean);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        MyApplication.getInstance().getCurrentActivity().startActivity(i);
                                    }
                                    DialogUtil.dissmiss();
                                }
                            });
                        }
                        break;
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                LogManager.i(extras + "点击");
                JPushBean jPushBean = GsonUtils.getInstance().fromJson(extras, JPushBean.class);
                switch (jPushBean.getAction()){
                    case "100301": //一键报警
                        if (MyApplication.getActivitySize() != 0){
                            Intent i = new Intent(context, SafeWarningListActivity.class);
//                            i.putExtra("jpushbean", jPushBean);  //从通知栏点击不弹窗提示
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        }else{
                            Intent launchIntent = context.getPackageManager().
                                    getLaunchIntentForPackage("com.bit.communityProperty");
                            launchIntent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            Bundle args = new Bundle();
                            args.putSerializable("jpushbean", jPushBean);
                            launchIntent.putExtra(AppConfig.EXTRA_BUNDLE, args);
                            context.startActivity(launchIntent);
                        }
                        break;
                    case "100101": //社区公告
                        if (MyApplication.getActivitySize() != 0){
                            Intent i = new Intent(context, NewsDetail.class);
                            i.putExtra("id",jPushBean.getData().getNotice_id());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        }else{
                            Intent launchIntent = context.getPackageManager().
                                    getLaunchIntentForPackage("com.bit.communityProperty");
                            launchIntent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            Bundle args = new Bundle();
                            args.putSerializable("jpushbean", jPushBean);
                            launchIntent.putExtra(AppConfig.EXTRA_BUNDLE, args);
                            context.startActivity(launchIntent);
                        }
                        break;
                    case "100401": //房屋认证
                        if (MyApplication.getActivitySize() != 0){
                            Intent i = new Intent(context, HouseholdManagementActivity.class);
                            i.putExtra("id",jPushBean.getData().getCommunityId());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                        }else{
                            Intent launchIntent = context.getPackageManager().
                                    getLaunchIntentForPackage("com.bit.communityProperty");
                            launchIntent.setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            Bundle args = new Bundle();
                            args.putSerializable("jpushbean", jPushBean);
                            launchIntent.putExtra(AppConfig.EXTRA_BUNDLE, args);
                            context.startActivity(launchIntent);
                        }
                        break;
                }
                //打开自定义的Activity
//                Intent i = new Intent(context, ChangeElevatorActivity.class);
//                i.putExtras(bundle);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
//                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//                Logger.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
//                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }
    // 打印所有的 intent extra 数据
//    private static String printBundle(Bundle bundle) {
//        StringBuilder sb = new StringBuilder();
//        for (String key : bundle.keySet()) {
//            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
//                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
//            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
//                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
//            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
//                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
//                    Logger.i(TAG, "This message has no Extra data");
//                    continue;
//                }
//
//                try {
//                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
//                    Iterator<String> it =  json.keys();
//
//                    while (it.hasNext()) {
//                        String myKey = it.next();
//                        sb.append("\nkey:" + key + ", value: [" +
//                                myKey + " - " +json.optString(myKey) + "]");
//                    }
//                } catch (JSONException e) {
//                    Logger.e(TAG, "Get message extra JSON error!");
//                }
//
//            } else {
//                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
//            }
//        }
//        return sb.toString();
//    }

    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (!ExampleUtil.isEmpty(extras)) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
//        }
//    }

    private void sendBradcast(Context context, String title, String content) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        //设置小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        //设置标题
        mBuilder.setContentTitle(title);
        //设置通知正文
        mBuilder.setContentText(content);
//        //设置摘要
//        mBuilder.setSubText("这是摘要");
        //设置是否点击消息后自动clean
        mBuilder.setAutoCancel(true);
        //显示指定文本
//        mBuilder.setContentInfo("Info");
        //与setContentInfo类似，但如果设置了setContentInfo则无效果
        //用于当显示了多个相同ID的Notification时，显示消息总数
        mBuilder.setNumber(2);
        //通知在状态栏显示时的文本
        mBuilder.setTicker("在状态栏上显示的文本");
        //设置优先级
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        //自定义消息时间，以毫秒为单位，当前设置为比系统时间少一小时
//        mBuilder.setWhen(System.currentTimeMillis() - 3600000);
        //设置为一个正在进行的通知，此时用户无法清除通知
//        mBuilder.setOngoing(true);
        //设置消息的提醒方式，震动提醒：DEFAULT_VIBRATE     声音提醒：NotificationCompat.DEFAULT_SOUND
        //三色灯提醒NotificationCompat.DEFAULT_LIGHTS     以上三种方式一起：DEFAULT_ALL
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        //设置震动方式，延迟零秒，震动一秒，延迟一秒、震动一秒
//        mBuilder.setVibrate(new long[]{0, 1000, 1000, 1000});
        Intent intent = new Intent(context, ChangeElevatorActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setContentIntent(pIntent);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        mNotificationManager.notify(random.nextInt(), mBuilder.build());
    }
}
