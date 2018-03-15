package com.bit.communityProperty.Bluetooth.bean;

/**
 * Created by Dell on 2018/2/11.
 */

import android.bluetooth.BluetoothDevice;
import android.util.Log;

/**
 * 搜索到的蓝牙设备对象
 */
public class SearchBlueDeviceBean implements Comparable<SearchBlueDeviceBean>{

    private BluetoothDevice bluetoothDevice;
    private int rssi;//信号强度
    private double distace;//距离

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
        distace = getDistance();
        if (bluetoothDevice != null) {
            Log.e("distace", "bluetoothDevice=" + bluetoothDevice.getAddress() + "distace==" + distace);
        }
    }

    public double getDistace() {
        // 10^((Math.abs(rssi) - A) / (10 * n))  距离
        return distace;
    }

    /**
     * 根据Rssi获得返回的距离,返回数据单位为m
     *
     * @return
     */

    //A和n的值，需要根据实际环境进行检测得出
    private static final double A_Value = 50;
    /**
     * A - 发射端和接收端相隔1米时的信号强度
     */
    private static final double n_Value = 2.5;

    /**
     * n - 环境衰减因子
     */
    public double getDistance() {
        int iRssi = Math.abs(getRssi());
        double power = (iRssi - A_Value) / (10 * n_Value);
        return Math.pow(10, power);
    }


    public void setDistace(float distace) {
        this.distace = distace;
    }

    @Override
    public int compareTo(SearchBlueDeviceBean o) {
        int i = o.getRssi() - this.getRssi();//先按照年龄排序
        return i;
    }
}
