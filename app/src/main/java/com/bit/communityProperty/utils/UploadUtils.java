package com.bit.communityProperty.utils;

/**
 * Created by hjw on 17/3/22.
 */


import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.config.AppConfig;

/**
 * 阿里云上传
 */
public class UploadUtils {


    /**
     * 文件上传阿里云不带进度回调
     *
     * @param data
     * @param filePath
     * @return
     */
    public static String uploadFileToAliYun(UploadInfo data, String filePath) {
        try {
            String endpoint = data.getEndPoint();
            data.setName("ap2"+SPUtil.get(MyApplication.getInstance(), AppConfig.id,"")+"_"+TimeUtils.getCurrentTime()+".jpg");
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(data
                    .getAccessKeyId(), data.getAccessKeySecret(), data.getSecurityToken());
            OSS oss = new OSSClient(MyApplication.getInstance(), endpoint, credentialProvider);
            PutObjectRequest put = new PutObjectRequest(data.getBucket() /*+ "-trans"*/, data.getName
                    (), filePath);
            PutObjectResult putResult = oss.putObject(put);
//            Log.d("okhttp", "http://bit-app.oss-cn-beijing.aliyuncs.com/"+data.getName());
            String url = oss.presignConstrainedObjectURL(data.getBucket(), data.getName(),30 * 60);
            Log.d("okhttp", url);
            data.setPath(url);
            return data.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件上传阿里云带进度回调
     *
     * @param data
     * @param filePath
     * @param progressCallback
     * @return
     */
    public static String uploadFileToAliYun(UploadInfo data, String filePath, OSSProgressCallback<PutObjectRequest> progressCallback) {
        try {
            String endpoint = data.getEndPoint();
            data.setName("ap2"+SPUtil.get(MyApplication.getInstance(), AppConfig.id,"")+"_"+TimeUtils.getCurrentTime()+".jpg");
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(data
                    .getAccessKeyId(), data.getAccessKeySecret(), data.getSecurityToken());
            OSS oss = new OSSClient(MyApplication.getInstance(), endpoint, credentialProvider);
            PutObjectRequest put = new PutObjectRequest(data.getBucket() /*+ "-trans"*/, data.getName
                    (), filePath);
            put.setProgressCallback(progressCallback);
            PutObjectResult putResult = oss.putObject(put);
//            Log.d("okhttp", "http://bit-app.oss-cn-beijing.aliyuncs.com/"+data.getName());
            String url = oss.presignConstrainedObjectURL(data.getBucket(), data.getName(),30 * 60);
            Log.d("okhttp", url);
            data.setPath(url);
            return data.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 文件异步上传阿里云
     *
     * @param data
     * @param filePath
     * @param callback
     * @return
     */
    public static String uploadFileToAliYun(final UploadInfo data, String filePath, OSSCompletedCallback callback) {
        try {
            String endpoint = data.getEndPoint();
            data.setName("ap2"+SPUtil.get(MyApplication.getInstance(), AppConfig.id,"")+"_"+TimeUtils.getCurrentTime()+".jpg");
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(data
                    .getAccessKeyId(), data.getAccessKeySecret(), data.getSecurityToken());
            final OSS oss = new OSSClient(MyApplication.getInstance(), endpoint, credentialProvider);
            PutObjectRequest put = new PutObjectRequest(data.getBucket() /*+ "-trans"*/, data.getName
                    (), filePath);
            OSSAsyncTask task = oss.asyncPutObject(put, callback);
            String url = oss.presignConstrainedObjectURL(data.getBucket(), data.getName(),30 * 60);
            Log.d("okhttp", url);
            data.setPath(url);
            return data.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
