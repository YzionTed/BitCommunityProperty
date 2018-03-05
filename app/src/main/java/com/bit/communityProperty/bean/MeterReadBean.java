package com.bit.communityProperty.bean;

import java.io.Serializable;

/**
 * 抄表管理的bean类
 * Created by kezhangzhao on 2018/1/21.
 */

public class MeterReadBean implements Serializable{

    private String month;
    private String time;
    private int status;
    private String currentNum;//当前数量
    private String allNum;//总数

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
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

    public String getCurrentNum() {
        return currentNum;
    }

    public void setCurrentNum(String currentNum) {
        this.currentNum = currentNum;
    }

    public String getAllNum() {
        return allNum;
    }

    public void setAllNum(String allNum) {
        this.allNum = allNum;
    }
}
