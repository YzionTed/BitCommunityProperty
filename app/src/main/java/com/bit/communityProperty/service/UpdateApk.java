package com.bit.communityProperty.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bit.communityProperty.BuildConfig;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.bean.ApkInfo;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.config.Config;
import com.bit.communityProperty.data.download.DownloadTask;
import com.bit.communityProperty.net.ThrowableUtils;
import com.bit.communityProperty.utils.DialogUtils;
import com.bit.communityProperty.utils.LogManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * APP升级服务
 * Created by kezhangzhao on 2018/1/13.
 */
@SuppressLint("HandlerLeak")
public class UpdateApk {


    private Activity mActivity;
    private ApkInfo mApkInfo;
    private DialogUtils mDialogUtils;
    private ProgressDialog mProgressDialog;
    private MyApplication myApplication = MyApplication.getInstance();

    private static final int UPDATEAPP = 2;
    public static final int PROGRESSING = 1;
    public static final int UPDATERR = 0;// 旧版更新错误
    public static final int COMPLETE = 5;
    public static final int ERROR = 3;// 新版更新错误
    public static final int UPDATEAPP_NO_DIALOG = 6;

    private String filepath;// apk路径
    private DownloadTask dt;// 下载线程
    private boolean isdownFile;// 是否下载过
    private UpdateApk mUpdateApk;
    private String dowloadDir;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public UpdateApk(Activity activity) {
        mActivity = activity;
        mApkInfo = myApplication.getApkInfo();
        mProgressDialog = new ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_LIGHT);
        handler.sendEmptyMessage(UPDATEAPP);
        // 获取SD卡目录
        dowloadDir = Config.getAppDownLoad();
        File file = new File(dowloadDir);
        // 创建下载目录
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public UpdateApk(Activity activity, int message) {
        mActivity = activity;
        mApkInfo = myApplication.getApkInfo();
        handler.sendEmptyMessage(message);
        // 获取SD卡目录
        dowloadDir = Config.getAppDownLoad();
        File file = new File(dowloadDir);
        // 创建下载目录
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                // 显示下载百分比
                case PROGRESSING:
                    try {
                        calculatePercent();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                // 显示更新版本对话框
                case UPDATEAPP:
                    showUpdateDialog();
                    break;

                // 显示连接地址错误对话框
                case UPDATERR:
                    connetErrDialog();
                    break;
                // 新版本更新错误提示,自定义错误提示
                case ERROR:
                    Bundle bundle = msg.getData();
                    customErrDialog(bundle.getString("error_msg", ""));
                    break;
                case UPDATEAPP_NO_DIALOG:
                    String path = mApkInfo.getDownLoadUrl();
                    if (checkPackageStatus(dowloadDir + path.substring(path.lastIndexOf('/') + 1))) {
                        openFile(new File(dowloadDir + path.substring(path.lastIndexOf('/') + 1)));
                    } else {
                        commonUpdate();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示升级对话框
     */
    protected void showUpdateDialog() {
        mDialogUtils = DialogUtils.getInstance();
        mDialogUtils.initSubmitDialog(mActivity);
        mDialogUtils.setSubmitTitle("升级提示");
//delete start ke 2018.1.13 暂时删除判断
//        if (!TextUtils.isEmpty(mApkInfo.getMobileClientUpdateLog())) {
//            mDialogUtils.setSubmitContent(mApkInfo.getMobileClientUpdateLog());
//        } else {
            mDialogUtils.setSubmitContent("有新版本更新 , 是否立即更新？");
//        }
//delete end ke 2018.1.13
        mDialogUtils.setSubBtnConfirmText("立即更新");
        mDialogUtils.setSubBtnCancelText("暂不更新");
        mDialogUtils.setBtnTextColor(mActivity.getResources().getColor(R.color.dialog_submit_btn_text_color_bule));
        mDialogUtils.setSubCancel(false);
        mDialogUtils.setSubConfirmClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtils.disMissDialog();
                String path = mApkInfo.getDownLoadUrl();
                if (checkPackageStatus(dowloadDir + path.substring(path.lastIndexOf('/') + 1))) {
                    openFile(new File(dowloadDir + path.substring(path.lastIndexOf('/') + 1)));
                } else {
                    if (mApkInfo.getIsAutoDownload() == 1) {// 强制更新样式
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条风格
                        mProgressDialog.setTitle("提示");// 设置标题
                        mProgressDialog.setMessage("正在加载资源 , 请勿退出!");// 设置提示信息
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.show();
                        download();
                    } else {// 非强制更新样式
                        commonUpdate();
                    }
                }
            }
        });
        mDialogUtils.setSubCancelClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mApkInfo != null && mApkInfo.getIsAutoDownload() == 1) { // 是否强制更新
                    mDialogUtils.disMissDialog();
//                    JniCrypt4A.getInstance().exitEncrypt();
//                    GlideUtils.guideClearMemory(GwtKeyApp.getApplication());
//                    GwtKeyApp.getInstance().finishActivitys();
                    mActivity.finish();
                } else {
                    mDialogUtils.disMissDialog();
                }
            }
        });
        mDialogUtils.showDialog();
    }

    private void commonUpdate() {
        Intent intent = new Intent(mActivity, DownAPKService.class);
        intent.putExtra("apk_url", mApkInfo.getDownLoadUrl());
        mActivity.startService(intent);
        Toast.makeText(mActivity, "正在后台进行下载，稍后会自动安装", Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * 开启下载方法
     */
    private void download() {
        // 读取下载线程数，如果为空，则单线程下载
        int downloadTN = 3;
        // 如果下载文件名为空则获取Url尾为文件名
//        int fileNameStart = mApkInfo.getDownLoadUrl().toString().lastIndexOf(".");
//        String fileName = mApkInfo.getDownLoadUrl().toString().substring(fileNameStart);
        filepath = dowloadDir + "APK";
        // 启动文件下载线程
        //下载的地址URL
        RequestParams mRequestParams = new RequestParams(AppConfig.IMG+mApkInfo.getDownLoadUrl().toString());
        // 设置断点续传
        mRequestParams.setAutoResume(true);
        mRequestParams.setSaveFilePath(filepath);
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
                Message message = handler.obtainMessage();
                message.what = ERROR;
                Bundle bundle = new Bundle();
                bundle.putString("error_msg", ThrowableUtils.getInstance(mActivity).downloadException(ex));
                message.setData(bundle);
                message.sendToTarget();
            }

            @Override
            public void onFinished() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuccess(File result) {
                // TODO Auto-generated method stub
                mProgressDialog.cancel();// 取消进度条
                isdownFile = true;
                myApplication.setApkInfo(null);
                openFile(result);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // TODO Auto-generated method stub
                int progress = (Double.valueOf((current * 1.0 / total * 100))).intValue();
                mProgressDialog.setProgress(progress);
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 显示计算下载的百分比
     */
    protected void calculatePercent() throws Exception {
        // 当收到更新视图消息时，计算已完成下载百分比，同时更新进度条信息
        int progress = (Double.valueOf((dt.downloadedSize * 1.0 / dt.fileSize * 100))).intValue();
        if (progress == 100) {
            mProgressDialog.cancel();// 取消进度条
            isdownFile = true;
            myApplication.setApkInfo(null);
            // 打开apk操作
            openFile(new File(filepath));
        } else {
            mProgressDialog.setProgress(progress);
        }
    }

    // 打开本地apk文件
    private void openFile(File file) {
        // TODO Auto-generated method stub

        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        // 判断是否时Android N以及更高版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(myApplication.getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(file);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mActivity.startActivity(installIntent);
    }

    /**
     * 显示访问下载地址错误对话框
     */
    protected void connetErrDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            final DialogUtils dialogUtils = DialogUtils.getInstance();
            dialogUtils.initSubmitDialog(mActivity);
            dialogUtils.setSubCancel(false);
            dialogUtils.setTitleVisibility(View.GONE);
            dialogUtils.setBtnDiderVisi(View.GONE, View.GONE);
            dialogUtils.setSubmitContent("下载地址无法访问或已失效");
            dialogUtils.setSubTvCenter(mActivity);
            dialogUtils.setSubBtnConfirmText("确 认");
            dialogUtils.setBtnTextColor(mActivity.getResources().getColor(R.color.dialog_submit_btn_text_color_bule));
            dialogUtils.setSubConfirmClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUtils.disMissDialog();
                    // mActivity.finish();//测试部说不要finish
                }
            });
            dialogUtils.showDialog();
        }
    }

    /**
     * 更新错误提示,自定义错误提示
     */
    private void customErrDialog(String txt) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            final DialogUtils dialogUtils = DialogUtils.getInstance();
            dialogUtils.initSubmitDialog(mActivity);
            dialogUtils.setSubCancel(false);
            dialogUtils.setTitleVisibility(View.GONE);
            dialogUtils.setBtnDiderVisi(View.GONE, View.GONE);
            dialogUtils.setSubmitContent(txt);
            dialogUtils.setSubTvCenter(mActivity);
            dialogUtils.setSubBtnConfirmText("确 认");
            dialogUtils.setBtnTextColor(mActivity.getResources().getColor(R.color.dialog_submit_btn_text_color_bule));
            dialogUtils.setSubConfirmClick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogUtils.disMissDialog();
                }
            });
            dialogUtils.showDialog();
        }
    }

    /**
     * 检测安装包能否正常安装
     *
     * @param archiveFilePath
     * @return
     */
    private boolean checkPackageStatus(String archiveFilePath) {
        boolean result = false;
        try {
            PackageManager pm = mActivity.getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }
}
