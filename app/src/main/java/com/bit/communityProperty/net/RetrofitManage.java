package com.bit.communityProperty.net;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.MessageEvent;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.data.Constant;
import com.bit.communityProperty.utils.AppUtil;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.NetWorkUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public enum RetrofitManage {
    INSTANCE;

//    public static String BASE_URL = "http://39.106.249.8:9000"; // 测试环境API环境
    public static String BASE_URL = "https://smcm.bitiot.com.cn"; // 生产环境API环境
    private static Retrofit mRetrofit;
    private static HttpService mHttpService;


//    public static String SKU_URL = getSkuUrl(); // 自动获取API环境
//    public static String SKU_URL = "https://api.e-gatenet.cn/";

    public static void setSkuUrl(String baseUrl) {
        RetrofitManage.BASE_URL = baseUrl;
        mHttpService = null;
        mRetrofit = null;
    }



    RetrofitManage() {
    }

    public static RetrofitManage getInstance() {
        return INSTANCE;
    }

    /**
     * 获取retrofit对象
     *
     * @return
     */
    public Retrofit getRetrofit() {
        if (null == mRetrofit) {
            OkHttpClient client = new OkHttpClient();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            // if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    LogManager.printInfoLog("OKHTTP", message);
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(logging);
            // }
            //SKU接口要在Header添加下面的三个参数
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader(AppConfig.OS, "2")
                            .addHeader("OS-VERSION",AppUtil.getSystemVersion())
                            .addHeader("CLIENT", "1001")
                            .addHeader("APP-VERSION", AppUtil.getVersionName(MyApplication.getInstance()))
                            .addHeader("DEVICE-TYPE", AppUtil.getSystemModel())
                            .addHeader("DEVICE-ID",AppUtil.getImei())
                            .addHeader(AppConfig.BIT_UID, (String) SPUtil.get(MyApplication.getInstance(), AppConfig.id, ""))
                            .addHeader(AppConfig.BIT_TOKEN, (String) SPUtil.get(MyApplication.getInstance(), AppConfig.token, ""))
                            .addHeader("PUSH-ID", JPushInterface.getRegistrationID(MyApplication.getInstance()))
                            .addHeader("Content-Type","application/json")
                            .build();
                    Log.e("header===","header=="+request.headers().toString());
                    return chain.proceed(request);
                }
            });


            File cacheFile = new File(Constant.PATH_CACHE);
            Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
            Interceptor cacheInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetWorkUtils.isNetworkConnected()) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build();
                    }
                    Response response = chain.proceed(request);
                    if (NetWorkUtils.isNetworkConnected()) {
                        int maxAge = 0;
                        // 有网络时, 不缓存, 最大保存时长为0
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")
                                .build();
                    } else {
                        // 无网络时，设置超时为4周
                        int maxStale = 60 * 60 * 24 * 28;
                        response.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .removeHeader("Pragma")
                                .build();
                    }
                    return response;
                }
            };
            //设置缓存
            builder.addNetworkInterceptor(cacheInterceptor);
            builder.addInterceptor(cacheInterceptor);
            builder.cache(cache);
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);

            OkHttpClient okHttpClient = builder.build();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public HttpService getHttpServiceConnection() {
        if (mHttpService == null) {
            mHttpService = getRetrofit().create(HttpService.class);
        }
        return mHttpService;
    }

    /**
     * 设置观察者
     *
     * @param o   被观察者
     * @param s   观察者
     * @param <T> 数据类型
     */
    public <T> void toSubscribe(Observable<T> o, Observer<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) //在io线程中处理网络请求
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);//在主线程中处理数据
    }


    public static <T> void sendRequest(final Activity activity, Call<BaseResponse<T>> call, final RequestCallback<T> requestCallback) {
        call.enqueue(new Callback<BaseResponse<T>>() {
            @Override
            public void onResponse(Call<BaseResponse<T>> call, retrofit2.Response<BaseResponse<T>> response) {
//                if (DialogUtils.getInstance() != null) {
//                    DialogUtils.disMissRemind();
//                }

                if (response.isSuccessful()) {
                    switch (response.body().errorCode) {

                        case "1000"://请求成功
                            Log.e("responce","responce=="+response.body().toString());
                            break;

                        case "9050001"://token失效
                            MessageEvent event = new MessageEvent();
                            event.setLoginSuccess(false);
                            EventBus.getDefault().post(event);
                            return;

                        case "500"://token失效
                            return;

                        case "3001"://当前帐号已经被管理员禁用

                            break;

                        case "3002"://当前帐号被强制下线

                            break;

                    }
                } else {
                    if (response.raw().code() == 500) {
                        Toast.makeText(activity, "服务器异常500", Toast.LENGTH_LONG).show();
                    } else if (response.raw().code() == 401 && response.raw().message().equals("Unauthorized")) {
//                        GwtKeyApp.getInstance().doReLogin(activity);
                    }
                }
                requestCallback.onResponse(call, response);


            }

            @Override
            public void onFailure(Call<BaseResponse<T>> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
//                    Toast.makeText(GwtKeyApp.getInstance(), "网络请求超时", Toast.LENGTH_SHORT).show();
                }
                requestCallback.onFailure(call, t);
            }
        });
    }

    public <T> void subscribe(Observable<BaseEntity<T>> baseEntityObservable, final Observer<BaseEntity<T>> observer) {
        baseEntityObservable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) //在io线程中处理网络请求
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BaseEntity<T>>() {


            @Override
            public void onSubscribe(Disposable d) {
                observer.onSubscribe(d);
            }

            @Override
            public void onNext(BaseEntity<T> baseEntity) {

                if (baseEntity.isSuccess()) {
                    switch (baseEntity.getErrorCode()) {

                        case "1000"://请求成功

                            break;

                        case "500"://token失效
                            return;

                        case "3001"://当前帐号已经被管理员禁用

                            break;

                        case "3002"://当前帐号被强制下线

                            break;

                    }
                } else if (baseEntity.isSuccess() == false && baseEntity.getErrorCode().equals("9050001")) {
                    MessageEvent event = new MessageEvent();
                    event.setLoginSuccess(false);
                    EventBus.getDefault().post(event);
                } else {
                }
                observer.onNext(baseEntity);

            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showSingletonText(MyApplication.getInstance(),ThrowableUtils.getInstance(MyApplication.getInstance()).catchHttpConnectException(e),3000);
                observer.onError(e);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }
        });
    }

    public interface RequestCallback<T> {
        void onResponse(Call<BaseResponse<T>> call, retrofit2.Response<BaseResponse<T>> response);

        void onFailure(Call<BaseResponse<T>> call, Throwable t);

    }
}
