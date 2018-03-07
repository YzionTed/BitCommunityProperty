package com.bit.communityProperty.utils;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by DELL60 on 2018/3/5.
 */

public class OssManager {
    private UploadInfo uploadInfo;
    private OSS oss;

    public static OssManager getInstance() {
        return OssInstance.instance;
    }

    private static class OssInstance {
        private static final OssManager instance = new OssManager();
    }

    private OssManager() {
    }

    /**
     * 初始化
     **/
    public OssManager init(Context context, UploadInfo uploadInfo) {
        if (oss == null) {
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(uploadInfo.getAccessKeyId(), uploadInfo.getAccessKeySecret(), uploadInfo.getSecurityToken());
            oss = new OSSClient(context, uploadInfo.getEndPoint(), credentialProvider);
        }
        this.uploadInfo = uploadInfo;
        return OssInstance.instance;
    }

    public String getUrl(String url) {
        if (uploadInfo != null && TimeUtils.isExpiration(uploadInfo.getExpiration())) {
            initOssToken();
        }
        if (oss != null) {
            try {
                return oss.presignConstrainedObjectURL("bit-test", url, 30 * 60);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

//    public String getUrl(String url,String bucket){
//        if (oss!=null){
//            try {
//                return oss.presignConstrainedObjectURL(bucket, url,30 * 60);
//            } catch (ClientException e) {
//                e.printStackTrace();
//            }
//        }
//        return url;
//    }

    /**
     * 文件异步上传阿里云
     *
     * @param data
     * @param filePath
     * @param callback
     * @return
     */
    public String uploadFileToAliYun(final UploadInfo data, String filePath, OSSCompletedCallback callback) {
        try {
            if (oss != null) {
                data.setName("ap1" + SPUtil.get(MyApplication.getInstance(), AppConfig.id, "") + "_" + data.getBucket() + "_" + TimeUtils.getCurrentTime() + ".jpg");
                PutObjectRequest put = new PutObjectRequest(data.getBucket() /*+ "-trans"*/, data.getName
                        (), filePath);
                OSSAsyncTask task = oss.asyncPutObject(put, callback);
//                String url = oss.presignConstrainedObjectURL(data.getBucket(), data.getName(),30 * 60);
                Log.d("okhttp", getUrl(filePath));
                data.setPath(getUrl(filePath));
            }
            return data.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void initOssToken() {
        RetrofitManage.getInstance().subscribe(Api.getInstance().ossToken(), new Observer<BaseEntity<UploadInfo>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<UploadInfo> uploadInfoBaseEntity) {
                if (uploadInfoBaseEntity.isSuccess()) {
                    UploadInfo uploadInfo = uploadInfoBaseEntity.getData();
                    SPUtil.saveObject(MyApplication.getInstance(), AppConfig.UPLOAD_INFO, uploadInfo);
                    if (uploadInfo != null) {
                        oss = null;
                        init(MyApplication.getInstance(), uploadInfo);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });
    }
}
