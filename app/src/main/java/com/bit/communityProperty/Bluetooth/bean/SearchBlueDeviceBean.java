package com.bit.communityProperty.Bluetooth.bean;

/**
 * Created by Dell on 2018/2/11.
 */

import android.bluetooth.BluetoothDevice;

/**
 * 搜索到的蓝牙设备对象
 */
public class SearchBlueDeviceBean {

    private BluetoothDevice bluetoothDevice;
    private int rssi;//信号强度
    private float distace;//距离

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public float getDistace() {
       // 10^((Math.abs(rssi) - A) / (10 * n))  距离
        return distace;
    }

    public void setDistace(float distace) {
        this.distace = distace;
    }
}
