package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 设备的bean类
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceBean implements Serializable{

    private String name;
    private String doorId;
    private int dataStatus;


    private int currentPage;
    private ArrayList<String> records;
    private String communityId;
    private String buildingId;
    private String mac;
    private String rank;
    private String terminalCode;
    private String terminalPort;
    private String guardSwitch;
    private String doorType;
    private String creatorId;
    private String createAt;
    private String updateAt;
    private String total;
    private String totalPage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoorId() {
        return doorId;
    }

    public void setDoorId(String doorId) {
        this.doorId = doorId;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<String> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<String> records) {
        this.records = records;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getTerminalPort() {
        return terminalPort;
    }

    public void setTerminalPort(String terminalPort) {
        this.terminalPort = terminalPort;
    }

    public String getGuardSwitch() {
        return guardSwitch;
    }

    public void setGuardSwitch(String guardSwitch) {
        this.guardSwitch = guardSwitch;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }
}
