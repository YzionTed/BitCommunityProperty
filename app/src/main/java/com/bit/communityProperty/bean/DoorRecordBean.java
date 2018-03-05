package com.bit.communityProperty.bean;

import java.io.Serializable;

/**
 * Created by kezhangzhao on 2018/1/21.
 */

public class DoorRecordBean implements Serializable{

    private String address;
    private String time;
    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
