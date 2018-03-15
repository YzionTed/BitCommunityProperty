package com.bit.communityProperty.net;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.utils.AppUtil;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.SPUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by 23 on 2018/2/10.
 */

public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        //  将token统一放到请求头
        String token= (String) SPUtil.get(MyApplication.getInstance(),AppConfig.token,"");
        //  也可以统一配置用户名
        String user_id=(String)SPUtil.get(MyApplication.getInstance(),AppConfig.id,"");
        LogManager.printErrorLog("backinfo","token:"+token);
        LogManager.printErrorLog("backinfo","user_id:"+user_id);
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .addHeader("BIT_TOKEN", token)
                .addHeader(AppConfig.BIT_UID, user_id)

                .addHeader(AppConfig.OS, "2")
                .addHeader("CLIENT","1001")
                .addHeader("APP_VERSION", AppUtil.getVersionName(MyApplication.getInstance()))
                .addHeader("DEVICE_TYPE", "Android")
                .addHeader(AppConfig.BIT_UID, (String)SPUtil.get(MyApplication.getInstance(),AppConfig.id,""))
                .addHeader("BIT_TOKEN", (String) SPUtil.get(MyApplication.getInstance(),AppConfig.token,""))
                .build();
    }
}
