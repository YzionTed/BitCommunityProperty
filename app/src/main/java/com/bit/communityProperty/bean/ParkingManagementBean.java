package com.bit.communityProperty.bean;

import java.io.Serializable;

/**
 * 停车管理的列表中item的bean类
 * Created by kezhangzhao on 2018/1/18.
 */

public class ParkingManagementBean implements Serializable{
    private String title;
    private String type;
    private int status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
