package com.bit.communityProperty.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bit.communityProperty.MyApplication;

/**
 * 网络工具类
 * Created by kezhangzhao on 2018/1/9.
 */

public class NetWorkUtils {
    /**
     * 检查手机数据网络状态
     * @param context
     * @return
     */
    public static boolean checkMobileNet(Context context) {
        boolean isConnet=false;
        NetworkInfo netInfo = getManager(context).getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE);
        if(netInfo!=null){
            NetworkInfo.State state=netInfo.getState();
            if(state== NetworkInfo.State.CONNECTED || state== NetworkInfo.State.CONNECTING){
                isConnet=true;
            }
        }
        return isConnet;
    }

    /**
     * 检查手机WIFI状态
     * @param context
     * @return
     */
    public static boolean checkMobileWifi(Context context){
        boolean isConnet=false;
        NetworkInfo netInfo=getManager(context).getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(netInfo!=null){
            NetworkInfo.State state=netInfo.getState();
            if(state== NetworkInfo.State.CONNECTED || state== NetworkInfo.State.CONNECTING){
                isConnet=true;
            }
        }
        return isConnet;
    }

    private static ConnectivityManager getManager(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager;
    }

    /**
     * 检查是否有可用网络
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
