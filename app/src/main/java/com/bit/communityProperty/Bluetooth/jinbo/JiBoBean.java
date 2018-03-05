package com.bit.communityProperty.Bluetooth.jinbo;

import com.inuker.bluetooth.library.search.SearchResult;

import java.util.ArrayList;

/**
 * Created by Dell on 2018/2/9.
 * JiBo的对象类
 */

public class JiBoBean {

    //是否支持蓝牙
    private boolean isBluetoothSupporse = false;
    //蓝牙是否打开 默认是不打开
    private boolean isBluetoothOpen = false;

    private String registMac;//注冊的Mac
    private String isRegist;//是否已经注册
    //搜索到的所有蓝牙设备  3秒内一直在搜索 数据在变动
    ArrayList<DeviceDateBean> bluetoothDevices;

    public String getRegistMac() {
        return registMac;
    }

    public void setRegistMac(String registMac) {
        this.registMac = registMac;
    }

    public boolean isBluetoothSupporse() {
        return isBluetoothSupporse;
    }

    public void setBluetoothSupporse(boolean bluetoothSupporse) {
        isBluetoothSupporse = bluetoothSupporse;
    }

    public boolean isBluetoothOpen() {
        return isBluetoothOpen;
    }

    public void setBluetoothOpen(boolean bluetoothOpen) {
        isBluetoothOpen = bluetoothOpen;
    }

    public ArrayList<DeviceDateBean> getBluetoothDevices() {
        return bluetoothDevices;
    }

    public void setBluetoothDevices(ArrayList<DeviceDateBean> bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
    }

    public static class DeviceDateBean {
        private SearchResult searchResult;//设备数据
        private boolean isRegist;// 是否这个设备注册过
        private int state;//注册： 1 连接 2 通知 3注冊    开梯： 4 连接 5 通知 6：开梯


        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public SearchResult getSearchResult() {
            return searchResult;
        }

        public void setSearchResult(SearchResult searchResult) {
            this.searchResult = searchResult;
        }

        public boolean isRegist() {
            return isRegist;
        }

        public void setRegist(boolean regist) {
            isRegist = regist;
        }
    }

}
