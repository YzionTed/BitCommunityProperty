package com.bit.communityProperty.utils;

import java.io.Serializable;

/**
 * Created by hjw on 17/3/22.
 */

public class UploadInfo implements Serializable {

//    private String accessid;
//    private String bucket;
//    private String endPoint;
//    private String expire;
//    private String name;
//    private String policy;
//    private String signature;
//    private String path;

    private String accessKeyId;
    private String securityToken;
    private String accessKeySecret;
    private String expiration;
    private String endPoint = "oss-cn-beijing.aliyuncs.com";
    private String bucket = "bit-smcm-img";//bit-app
    private String name;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
