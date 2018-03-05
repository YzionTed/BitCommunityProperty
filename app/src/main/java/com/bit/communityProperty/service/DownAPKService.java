package com.bit.communityProperty.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.bit.communityProperty.BuildConfig;
import com.bit.communityProperty.R;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.config.Config;
import com.bit.communityProperty.net.ThrowableUtils;
import com.bit.communityProperty.utils.LogManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by kezhangzhao on 2018/1/13.
 */

public class DownAPKService extends Service{
    private final int NotificationID = 0x10000;
    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder builder;

    // private HttpHandler<File> mDownLoadHelper;
    private RequestParams mRequestParams;

    // 文件下载路径
    private String APK_url = "";
    // 文件保存路径
    private String APK_dir = Config.getAppDownLoad();

    /**
     * Title: onBind
     *
     * @Description:
     * @param intent
     * @return
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Title: onCreate
     *
     * @Description:
     * @see android.app.Service#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        initAPKDir();// 创建保存路径
    }

    /**
     * Title: onStartCommand
     *
     * @Description:
     * @param intent
     * @param flags
     * @param startId
     * @return
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 接收Intent传来的参数:
        APK_url = intent.getStringExtra("apk_url");
        // 如果下载文件名为空则获取Url尾为文件名
        DownFile(AppConfig.IMG+APK_url, APK_dir + "/APK");

        return super.onStartCommand(intent, flags, startId);
    }

    private void initAPKDir() {
        File file = new File(APK_dir);
        // 创建下载目录
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private void DownFile(String file_url, String target_name) {
        mRequestParams = new RequestParams(file_url);
        // 设置断点续传
        mRequestParams.setAutoResume(true);
        mRequestParams.setSaveFilePath(target_name);
        x.http().get(mRequestParams, new Callback.ProgressCallback<File>() {

            @Override
            public void onCancelled(CancelledException cex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // TODO Auto-generated method stub
                LogManager.writeErrorLog("The exception is " + ex.getClass());
                Log.e("TAG", "下载时候的异常" + ex.getClass());
                mNotificationManager.cancel(NotificationID);
                Toast.makeText(getApplicationContext(),
                        ThrowableUtils.getInstance(getApplicationContext()).downloadException(ex), Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(File result) {
                // TODO Auto-generated method stub
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                // 判断是否时Android N以及更高版本
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", result);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                } else {
                    Uri uri = Uri.fromFile(result);
                    installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                PendingIntent mPendingIntent = PendingIntent.getActivity(DownAPKService.this, 0, installIntent, 0);
                builder.setContentText("下载完成");
                builder.setContentIntent(mPendingIntent);
                mNotificationManager.notify(NotificationID, builder.build());
                stopSelf();
                startActivity(installIntent);// 下载完成之后自动弹出安装界面
                mNotificationManager.cancel(NotificationID);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // TODO Auto-generated method stub
                int x = Long.valueOf(current).intValue();
                int totalS = Long.valueOf(total).intValue();
                builder.setProgress(totalS, x, false);
                builder.setContentInfo(getPercent(x, totalS));
                mNotificationManager.notify(NotificationID, builder.build());
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setSmallIcon(R.mipmap.camera);//APP图标设置显示
                builder.setTicker("正在下载新版本");
                // builder.setContentTitle(getApplicationName());
                builder.setContentTitle("福星物业");
                builder.setContentText("正在下载,请稍后……");
                builder.setNumber(0);
                builder.setAutoCancel(true);
                mNotificationManager.notify(NotificationID, builder.build());
            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     *
     * @param x
     *            当前值
     * @param total
     *            总值
     * @return 当前百分比
     * @Description:返回百分之值
     */
    private String getPercent(int x, int total) {
        String result = "";// 接受百分比的值
        double x_double = x * 1.0;
        double tempresult = x_double / total;
        // 百分比格式，后面不足2位的用0补齐 ##.00%
        DecimalFormat df1 = new DecimalFormat("0.00%");
        result = df1.format(tempresult);
        return result;
    }

    /**
     * Title: onDestroy
     *
     * @Description:
     * @see android.app.Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
