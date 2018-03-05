package com.bit.communityProperty.bean;

import java.io.Serializable;

/**
 * 故障管理的bean类（普通物业人员）
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultManagerCommonBean implements Serializable{

    private int type;//类型
    private int  reason;//故障原因
    private String address;//故障地址
    private String time;//时间
    private int status;//状态

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
