package com.bit.communityProperty.utils;

import android.util.Log;

import com.bit.communityProperty.BuildConfig;
import com.bit.communityProperty.config.Config;
import com.or.log.EventLog;
import com.or.log.EventLogType;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class LogManager {
    private static final boolean isDebug = BuildConfig.DEBUG;

    static {
        EventLog.getInstance();
        EventLog.setRootPath(Config.getLogPath());
        EventLog.getInstance().setLogFileMaxLength(1024 * 1024 / 2);
    }

    /**
     * 打印操作Log
     *
     * @param tag
     * @param msg
     */
    public static synchronized void printInfoLog(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    /**
     * 打印操作Log
     *
     * @param msg
     */
    public static synchronized void i(String msg) {
        if (isDebug) {
            Log.i("LogUtil", msg);
        }
    }

    /**
     * 打印调试Log
     *
     * @param tag
     * @param msg
     */
    public static synchronized void printDebugLog(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    /**
     * 打印错误Log
     *
     * @param tag
     * @param msg
     */
    public static synchronized void printErrorLog(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    /**
     * 操作的Log
     *
     * @param msg
     */
    public static synchronized void writeOptLog(String msg) {
        EventLog.getInstance().writeEntry("Operation", "opt", msg + "\r\n", EventLogType.Info);
    }

    /**
     * 调试信息的Log
     *
     * @param msg
     */
    public static synchronized void writeDebugLog(String msg) {
        writeDebugLog(msg, EventLogType.Info);
    }

    public static synchronized void writeDebugLog(String msg, EventLogType logType) {
        EventLog.getInstance().writeEntry("Debug", "DebugData", msg + "\r\n", logType);
    }

    /**
     * 传输数据的Log
     *
     * @param msg
     */
    public static synchronized void writeXmlData(String msg) {
        EventLog.getInstance().writeEntry("Data", "XmlData", msg + "\r\n", EventLogType.Info);
    }

    /**
     * 传输数据的Log
     *
     * @param msg
     */
    public static synchronized void writeJsonData(String msg) {
        EventLog.getInstance().writeEntry("Data", "JsonData", msg + "\r\n", EventLogType.Info);
    }

    /**
     * 错误信息的Log
     *
     * @param msg
     */
    public static synchronized void writeErrorLog(String msg) {
//        EventLog.getInstance().writeEntry("Error", "ErrorData", msg + "\r\n", EventLogType.Error);
    }

    /**
     * 错误接口500的Log
     *
     * @param msg
     */
    public static synchronized void writeInterfaceErrorLog(String msg) {
        if (isDebug) {
            EventLog.getInstance().writeEntry("Error", "InterfaceError", msg + "\r\n", EventLogType.Error);
        }
    }

    /*
     * 打印和输出同时的Log方法s
     */
    public static synchronized void printAndWriteErrorLog(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
        EventLog.getInstance().writeEntry("Error", "ErrorData", msg + "\r\n", EventLogType.Error);
    }

    /**
     * 打印网络请求POST的参数
     */
    public static synchronized void writeParameterLog(String url, Map<String, String> values) {
        if (isDebug) {
            StringBuilder strings = new StringBuilder();
            try {
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    strings.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "utf-8"))
                            .append(",");
                }
                strings.deleteCharAt(strings.length() - 1); // 删除最后的一个","
                EventLog.getInstance().writeEntry("Data", "Parameters",
                        "The url is " + url + "\r\n" + "The Parameters are " + strings.toString() + "\r\n",
                        EventLogType.Info);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 写信息入logcat测试查错
     *
     * @param tag
     * @param msg log的类型 eg:v d
     */
    public static synchronized void writeLogCat(String tag, String msg) {
        Log.v(tag, msg);
    }

    /**
     * 保存唯一的机器码
     *
     * @param key
     */
    public static synchronized void writeDeviceInfo(String key) {
        com.or.log.EventLog.getInstance().writeEntry("Data", "DeviceInfo", key + "\r\n", com.or.log.EventLogType.Info);
    }

    /**
     * 删除最后修改时间为Before Second秒之前的所有log。
     *
     * @param secondBefore
     */
    public static void delOldLog(int secondBefore) {
        com.or.log.EventLog.deleteLog(secondBefore);
    }
}
