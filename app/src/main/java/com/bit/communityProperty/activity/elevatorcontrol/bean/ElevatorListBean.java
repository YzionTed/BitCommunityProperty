package com.bit.communityProperty.activity.elevatorcontrol.bean;

import java.io.Serializable;

/**
 * Created by DELL60 on 2018/2/23.
 */

public class ElevatorListBean implements Serializable{


    /**
     * elevatorNum : 办公楼
     * keyNo : BC:A9:20:9F:FA:4D
     * keyType : 1
     * macAddress : E6:48:3C:5A:D4:1D
     * macType : 1
     * name : 一号梯
     * temporary : true
     */

    private String elevatorNum;
    private String keyNo;
    private int keyType;
    private String macAddress;
    private int macType;
    private String name;
    private boolean temporary;
    private boolean isFirst;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getElevatorNum() {
        return elevatorNum;
    }

    public void setElevatorNum(String elevatorNum) {
        this.elevatorNum = elevatorNum;
    }

    public String getKeyNo() {
        return keyNo;
    }

    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getMacType() {
        return macType;
    }

    public void setMacType(int macType) {
        this.macType = macType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }
}
