package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class CarBrakeBean implements Serializable{
    private String id;
    private String gateNO;
    private String gateName;
    private int inOutTag;
    private int gateStatus;
    private String computerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGateNO() {
        return gateNO;
    }

    public void setGateNO(String gateNO) {
        this.gateNO = gateNO;
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public int getInOutTag() {
        return inOutTag;
    }

    public void setInOutTag(int inOutTag) {
        this.inOutTag = inOutTag;
    }

    public int getGateStatus() {
        return gateStatus;
    }

    public void setGateStatus(int gateStatus) {
        this.gateStatus = gateStatus;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }
}
