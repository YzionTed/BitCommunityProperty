package com.bit.communityProperty.config;

import android.os.Environment;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class Config {
    /**
     * 程序的文件所放置的目錄
     */
    public static String ROOT_PATH;

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            ROOT_PATH = Environment.getExternalStorageDirectory() + "/FuXingWuYe/";
        } else {
            ROOT_PATH = Environment.getDataDirectory().getAbsolutePath() + "/FuXingWuYe/";
        }
    }

    /**
     * 版本更新文件存放的目录
     *
     * @return
     */
    public static String getAppUpdate() {
        return ROOT_PATH + "Backup/";
    }

    /**
     * 日志文件存放目录
     *
     * @return
     */
    public static String getLogPath() {
        return ROOT_PATH + "Log/";
    }

    public static String getAppDownLoad() {
        return ROOT_PATH + "FuXingDownload/";
    }

    public static String getAvatarPath() {
        return ROOT_PATH + "Avatar/";
    }

    public static String getCachePath() {
        return ROOT_PATH + "Cache/";
    }
}
