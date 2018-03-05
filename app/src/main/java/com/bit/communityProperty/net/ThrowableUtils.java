package com.bit.communityProperty.net;

import android.content.Context;

import com.bit.communityProperty.R;

import org.xutils.ex.FileLockedException;
import org.xutils.ex.HttpException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * 这里针对网络下载的时的异常提示
 * Created by kezhangzhao on 2018/1/13.
 */

public class ThrowableUtils {

    private static ThrowableUtils instance = null;
    private Context mContext;

    public static synchronized ThrowableUtils getInstance(Context context) {
        if (instance == null) {
            instance = new ThrowableUtils(context);
        }
        return instance;
    }

    private ThrowableUtils(Context context) {
        mContext = context;
    }

    /**
     * HttpManager捕获网络异常
     *
     * @param e
     * @return
     */
    public String catchHttpConnectException(Throwable e) {
        Class<?> clazz = e.getClass();
        if (clazz.equals(UnknownHostException.class))
            return "异常请求域名，请稍后再试。 ";
        else if (clazz.equals(SocketTimeoutException.class))
            return "网络连接超时";
        else if (clazz.equals(IOException.class))
            return "网络连接失败，请检查网络设置";
        else if (e instanceof HttpException) {
            HttpException httpEx = (HttpException) e;
            int responseCode = httpEx.getCode();
            if (responseCode >= 400 && responseCode <= 417) {
                return "访问地址异常,请稍后重试";
            } else if (responseCode >= 500 && responseCode <= 505) {
                return "服务器繁忙";
            } else {
                return "网络连接异常";
            }
        } else
            return "网络错误,请稍后重试";

    }

    // 针对软件下载的异常抛出
    public String downloadException(Throwable ex) {
        Class<?> clazz = ex.getClass();
        int txt;

        if (clazz.equals(UnknownHostException.class))
            txt = R.string.network_connection_faile_unknownhost;
        else if (clazz.equals(SocketTimeoutException.class))
            txt = R.string.network_connection_time_out;
        else if (clazz.equals(IOException.class))
            txt = R.string.download_io_exception;
        else if (ex instanceof HttpException) {
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            if (responseCode >= 400 && responseCode <= 417) {
                txt = R.string.common_url_error;
            } else if (responseCode >= 500 && responseCode <= 505) {
                txt = R.string.network_connection_busy;
            } else {
                txt = R.string.network_connection_exception;
            }
        } else if (clazz.equals(FileLockedException.class))
            txt = R.string.network_file_locked_exception;
        else if (clazz.equals(FileNotFoundException.class))
            txt = R.string.file_permission_closed;
        else if (clazz.equals(ProtocolException.class))
            txt = R.string.network_download_exception;
        else
            txt = R.string.common_unknown_error;

        String hint = mContext.getString(txt);
        return hint;
    }
}
