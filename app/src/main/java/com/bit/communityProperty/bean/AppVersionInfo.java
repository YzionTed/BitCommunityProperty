package com.bit.communityProperty.bean;

/**
 * Created by DELL60 on 2018/2/28.
 */

public class AppVersionInfo {

    /**
     * appId : 5a961e7c0cf2c1914073dece
     * appName : null
     * appSize : 12.88
     * createAt : 1519719597679
     * creatorId : 5a82a37d9ce93e30677c3f9c
     * dataStatus : 1
     * details : 安卓系统的安装软件
     * forceUpgrade : true
     * hasError : false
     * id : 5a9514ad67fa7618b6787639
     * publishAt : 1519719610700
     * published : true
     * sequence : V3.0.0
     * updateAt : 1519719610700
     * url : xxx
     */

    private String appId;
    private String appName;
    private double appSize;
    private long createAt;
    private String creatorId;
    private int dataStatus;
    private String details;
    private boolean forceUpgrade;
    private boolean hasError;
    private String id;
    private long publishAt;
    private boolean published;
    private String sequence;
    private long updateAt;
    private String url;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public double getAppSize() {
        return appSize;
    }

    public void setAppSize(double appSize) {
        this.appSize = appSize;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isForceUpgrade() {
        return forceUpgrade;
    }

    public void setForceUpgrade(boolean forceUpgrade) {
        this.forceUpgrade = forceUpgrade;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(long publishAt) {
        this.publishAt = publishAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
