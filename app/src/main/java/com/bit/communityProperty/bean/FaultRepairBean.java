package com.bit.communityProperty.bean;

import java.io.Serializable;

/**
 * 故障维修的bean类
 * Created by kezhangzhao on 2018/1/27.
 */

public class FaultRepairBean implements Serializable{

    private int type;//类型
    private String  repairInfo;//维修信息
    private String address;//故障地址
    private String time;//时间
    private int status;//状态
    private String money;//维修金额

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRepairInfo() {
        return repairInfo;
    }

    public void setRepairInfo(String repairInfo) {
        this.repairInfo = repairInfo;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
