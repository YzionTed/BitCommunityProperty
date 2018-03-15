package com.bit.communityProperty.Bluetooth.interfcace;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;

import java.util.ArrayList;

/**
 * 搜索到的蓝牙
 */
public interface OnSearchBlueDeviceListener {
    void OnSearchBludeDeviceCallBack(SearchBlueDeviceBean searchBlueDeviceBean);//对每个搜索到蓝牙设备返回监听

    void OnSearchAllDeviceCallBack(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList);//对收索到的所有蓝牙设备监听
}