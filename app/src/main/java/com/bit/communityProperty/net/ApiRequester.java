package com.bit.communityProperty.net;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bit.communityProperty.BuildConfig;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.utils.AppUtil;
import com.bit.communityProperty.utils.LogUtil;
import com.bit.communityProperty.utils.SPUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import cn.jpush.android.api.JPushInterface;


/**
 * 服务端请求类
 * Created by zhangjiajie on 2015/11/19.
 */

public class ApiRequester {

    private static final String TAG = ApiRequester.class.getSimpleName();


    /**
     * @param url       请求地址
     * @param paramsMap 请求参数
     * @param callBack  回调
     */
    public static <T> void get(@NonNull String url, Map<String, String> paramsMap, @NonNull final ResponseCallBack<T> callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!url.toLowerCase().startsWith("http")) {
            if (BuildConfig.BUILD_TYPE.equals("release")) {
                url = RetrofitManage.BASE_URL + url;
            }
        }

        RequestParams requestParams = createRequestParams(url);
//        requestParams.setSslSocketFactory(getSocketFactory());

        if (paramsMap != null) {
            Iterator it = paramsMap.keySet().iterator();
            //拼接参数
            while (it.hasNext()) {
                String key = it.next().toString();
                String value = null != paramsMap.get(key) ? paramsMap.get(key) : "";
                requestParams.addBodyParameter(key, value);
            }
        }

        LogUtil.d(TAG, "request:" + requestParams.toString() + requestParams.getBodyContent());
        x.http().request(HttpMethod.GET, requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                LogUtil.d(TAG, "response:" + result);
                try {
                    T data = callBack.parse(result);
                    callBack.onSuccess(data);
                } catch (ServiceException e) {
                    callBack.onFailure(e);
                } catch (Throwable e) {
                    //这里先捕获了异常，防止被xutils捕获然后走onError回调
//                    Toast.makeText(App.sCurrActivity, "系统繁忙", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.d(TAG, "response:" + ex.getMessage());
                if (ex instanceof ServiceException) {
                    //checknetwork();
                    callBack.onFailure((ServiceException) ex);
                } else if (ex instanceof ConnectException || ex instanceof SocketException) {
                    callBack.onFailure(new ServiceException("连接异常，请检查网络"));
                } else if (ex instanceof UnknownHostException || ex instanceof SocketTimeoutException
                        || ex instanceof IOException) {
                    callBack.onFailure(new ServiceException("网络连接失败，请重试"));
                } else {
                    callBack.onFailure(new ServiceException(ex.getMessage()));
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException(cex.getMessage()));
            }

            @Override
            public void onFinished() {

            }
        });
    }

//    private static SSLSocketFactory getSocketFactory() {
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TSL");
//            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//
//                }
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[]{};
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                        throws CertificateException {
//
//                }
//
//            }}, new SecureRandom());
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//            return sslSocketFactory;
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }

    public static String createUrl(@NonNull String url, Object... o) {
        String realUrl = String.format(url, o);
        return realUrl;
    }

    public static RequestParams createRequestParams(@NonNull String url) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setConnectTimeout(3 * 1000);
//        requestParams.setCacheMaxAge(1000);
//        requestParams.setCacheSize(1024 * 1024);
        requestParams.setReadTimeout(3 * 1000);
        requestParams.setMaxRetryCount(2);
        requestParams.setCharset("utf-8");

        requestParams.addHeader(AppConfig.OS, "2");
        requestParams.addHeader("OS-VERSION", AppUtil.getSystemVersion());
        requestParams.addHeader("CLIENT", "1001");
        requestParams.addHeader("APP-VERSION", AppUtil.getVersionName(MyApplication.getInstance()));
        requestParams.addHeader("DEVICE-TYPE", AppUtil.getSystemModel());
        requestParams.addHeader("DEVICE-ID", AppUtil.getImei());
        requestParams.addHeader(AppConfig.BIT_UID, (String) SPUtil.get(MyApplication.getInstance(), AppConfig.id, ""));
        requestParams.addHeader(AppConfig.BIT_TOKEN, (String) SPUtil.get(MyApplication.getInstance(), AppConfig.token, ""));
        requestParams.addHeader("PUSH-ID", JPushInterface.getRegistrationID(MyApplication.getInstance()));
        requestParams.addHeader("Content-Type", "application/json");

        return requestParams;
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 回调
     */
    public static <T, K> void post(@NonNull String url, T params, @NonNull ResponseCallBack<K> callBack) {
        post(HttpMethod.POST, url, params, callBack);
    }

    private static <T, K> void post(@NonNull HttpMethod method, @NonNull String url, T paramsBean,
                                    @NonNull final ResponseCallBack<K> callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!url.toLowerCase().startsWith("http")) {
            if (BuildConfig.BUILD_TYPE.equals("release")) {
                url = RetrofitManage.BASE_URL + url;
            }
        }

        RequestParams requestParams = createRequestParams(url);
//        requestParams.setSslSocketFactory(getSocketFactory());

        requestParams.setAsJsonContent(true);
        if (paramsBean != null) {
            requestParams.setBodyContent(new Gson().toJson(paramsBean));
        }

        LogUtil.i(TAG, "request:" + requestParams.toString() + requestParams.getBodyContent());
        x.http().request(method, requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                LogUtil.i(TAG, "response:" + result);
                try {
                    K data = callBack.parse(result);
                    callBack.onSuccess(data);
                } catch (ServiceException e) {
                    callBack.onFailure(e);
                } catch (Throwable e) {
                    //这里先捕获了异常，防止被xutils捕获然后走onError回调
//                    Toast.makeText(App.sCurrActivity, "系统繁忙", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ServiceException) {
                    callBack.onFailure((ServiceException) ex);
                } else if (ex instanceof ConnectException || ex instanceof SocketException) {
                    callBack.onFailure(new ServiceException("连接异常，请检查网络"));
                } else if (ex instanceof UnknownHostException || ex instanceof SocketTimeoutException
                        || ex instanceof IOException) {
                    callBack.onFailure(new ServiceException("网络连接失败，请重试"));
                } else {
                    callBack.onFailure(new ServiceException(ex.getMessage()));
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException(cex.getMessage()));
            }

            @Override
            public void onFinished() {

            }
        });
    }

    //test
    public static <T> void sendRequest(String url, final RequestParams requestParams,
                                       @NonNull final ResponseCallBack<T> callBack) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

//        url = "https://api.netease.im/nimserver" + url;     //加上域名部分
//        url = "https://api.tudouni.doubozhibo.com" + url;     //加上域名部分

        requestParams.setUri(url);
        requestParams.setConnectTimeout(3 * 1000);
//        requestParams.setCacheMaxAge(1000);
//        requestParams.setCacheSize(1024 * 1024);
        requestParams.setReadTimeout(3 * 1000);
        requestParams.setMaxRetryCount(2);
        requestParams.setCharset("utf-8");

        LogUtil.d("RequestUtils", "request : " + requestParams.toString());
        x.http().request(HttpMethod.POST, requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                LogUtil.d("RequestUtils", "response: " + result);
                try {
                    T data = callBack.parse(result);
                    callBack.onSuccess(data);
                } catch (ServiceException e) {
                    callBack.onFailure(e);
                } catch (Throwable e) {
                    //这里先捕获了异常，防止被xutils捕获然后走onError回调
//                    Toast.makeText(App.sCurrActivity, "系统繁忙", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    callBack.onFailure(new ServiceException("解析失败"));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof ServiceException) {
                    //checknetwork();
                    callBack.onFailure((ServiceException) ex);
                } else if (ex instanceof ConnectException || ex instanceof SocketException) {
                    callBack.onFailure(new ServiceException("连接异常，请检查网络"));
                } else if (ex instanceof UnknownHostException || ex instanceof SocketTimeoutException
                        || ex instanceof IOException) {
                    callBack.onFailure(new ServiceException("网络连接失败，请重试"));
                } else {
                    callBack.onFailure(new ServiceException(ex.getMessage()));
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                callBack.onFailure(new ServiceException(cex.getMessage()));
            }

            @Override
            public void onFinished() {
            }
        });


    }

    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
}
