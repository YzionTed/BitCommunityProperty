package com.bit.communityProperty.bean;

import com.ddclient.dongsdk.DeviceInfo;

import java.util.ArrayList;

/**
 * Created by Dell on 2018/2/28.
 */

public class DoorMiLiDevicesBean {

  private   ArrayList<DeviceInfo> deviceInfoList;

    public ArrayList<DeviceInfo> getDeviceInfoList() {
        return deviceInfoList;
    }

    public void setDeviceInfoList(ArrayList<DeviceInfo> deviceInfoList) {
        this.deviceInfoList = deviceInfoList;
    }
}
