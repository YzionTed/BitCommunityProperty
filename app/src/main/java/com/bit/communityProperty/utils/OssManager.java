package com.bit.communityProperty.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;

/**
 * Created by DELL60 on 2018/3/5.
 */

public class OssManager {
    private OSS oss;
    public static OssManager getInstance() {
        return OssInstance.instance;
    }
    private static class OssInstance{
        private static final OssManager instance= new OssManager();
    }
    private OssManager(){}

    /**
     * 初始化
     * **/
    public OssManager init(Context context, UploadInfo uploadInfo){
        if(oss==null){
            OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(uploadInfo.getAccessKeyId(), uploadInfo.getAccessKeySecret(),uploadInfo.getSecurityToken());
            oss= new OSSClient(context, uploadInfo.getEndPoint(), credentialProvider);
        }
        return  OssInstance.instance;
    }

    public String getUrl(String url){
        if (oss!=null){
            try {
                return oss.presignConstrainedObjectURL("bit-app", url,30 * 60);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return url;
    }
}
