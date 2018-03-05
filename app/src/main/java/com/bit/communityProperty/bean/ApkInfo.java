package com.bit.communityProperty.bean;

/**
 * APK信息
 * Created by kezhangzhao on 2018/1/13.
 */

public class ApkInfo {
    private String version;
    private String mobileClientUpdateLog;
    private int isShow;
    private int isPublished;
    private int isAutoDownload;
    private String downLoadUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMobileClientUpdateLog() {
        return mobileClientUpdateLog;
    }

    public void setMobileClientUpdateLog(String mobileClientUpdateLog) {
        this.mobileClientUpdateLog = mobileClientUpdateLog;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(int isPublished) {
        this.isPublished = isPublished;
    }

    public int getIsAutoDownload() {
        return isAutoDownload;
    }

    public void setIsAutoDownload(int isAutoDownload) {
        this.isAutoDownload = isAutoDownload;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
