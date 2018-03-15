package com.bit.communityProperty.utils;


import android.util.Log;

/**
 * 日志管理工具
 *
 * @author zhangjiajie
 */
public class LogUtil {

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private LogUtil() {
    }

    private static int LOG_LEVEL = DEBUG;

    /***************** 1、error级别的日志管理 *****************/
    /**
     * error级别的日志
     *
     * @param tag 日志的tag
     * @param msg 日志的输出信息
     */
    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= ERROR)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= ERROR)
            Log.e(tag, msg, tr);
    }

    /***************** 2、warn级别的日志管理 *****************/
    /**
     * warn
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= WARN)
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= WARN)
            Log.w(tag, msg, tr);
    }

    /***************** 3、info级别的日志管理 *****************/
    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= INFO)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= INFO)
            Log.i(tag, msg, tr);
    }

    /***************** 4、debug级别的日志管理 *****************/
    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= DEBUG)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= DEBUG)
            Log.d(tag, msg, tr);
    }

    /***************** 5、verbose级别的日志管理 *****************/
    public static void v(String tag, String msg) {
        if (LOG_LEVEL <= VERBOSE)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= VERBOSE)
            Log.v(tag, msg, tr);
    }
    /***************** 6、更改日志显示等级 *****************/
    /**
     * 更改日志显示等级
     *
     * @param logLevel
     */
    public static void setLogLevel(int logLevel) {
        LOG_LEVEL = logLevel;
    }

}