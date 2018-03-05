package com.bit.communityProperty.activity.deviceManagement.bean;

/**
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceBeanPar {

    private String doorId;
    private String mac;
    private String buildingId;
    private int page;
    private int size;

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
