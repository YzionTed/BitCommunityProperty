package com.bit.communityProperty.Bluetooth.util;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.bean.DoorMILiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2018/2/26.
 */

public class BlueUtils {

    public static String TAG = "BlueUtils";

    /**
     米粒蓝牙 02_BLE_OPEN 4D:4C:00:00:00:67
     金博蓝牙 JB FOR BIT  E6:48:3C:5A:D4:1D
     自己开发的蓝牙 KT05  44:A6:E5:14:CE:43
     加速度蓝牙   KTJSD   44:A6:E5:1B:C2:C1
     调试工具蓝牙 HC-08   50:65:83:9B:69:68
     */

    /**
     * 筛选米粒的设备
     *
     * @param searchBlueDeviceBeanList
     */
    public static void getMiliBluetooth(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {

        if (searchBlueDeviceBeanList.size() == 0) {
            Log.e(TAG, "searchBlueDeviceBeanList.size()=0");
            return;
        }
        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
            SearchBlueDeviceBean searchBlueDeviceBean = searchBlueDeviceBeanList.get(i);
            Log.e(TAG, "searchBlueDeviceBean address=" + searchBlueDeviceBean.getBluetoothDevice().getAddress());
            //  Log.e(TAG,"searchBlueDeviceBean UUID="+searchBlueDeviceBean.getBluetoothDevice().getUuids().length);
            //4D:4C:00:00:00:67

        }
    }


    /**
     * 获取距离最近的蓝牙米粒设备
     *
     * @param searchBlueDeviceBeanList
     * @param logindata
     */
    public static DoorMILiBean getMaxRsic(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, List<DoorMILiBean> logindata) {
        int maxSrc = 0;
        DoorMILiBean doorMILiBeanMax = null;
        for (int i = 0; i < logindata.size(); i++) {
            DoorMILiBean doorMILiBean = logindata.get(i);
            Log.e("BluetoothDoorFragment", "doorMILiBean=" + doorMILiBean.getMac());
            if (doorMILiBean.getDataStatus() == 1) {
                for (int j = 0; j < searchBlueDeviceBeanList.size(); j++) {
                    BluetoothDevice bluetoothDevice = searchBlueDeviceBeanList.get(j).getBluetoothDevice();
                    String address = bluetoothDevice.getAddress();
                    StringBuffer sb = new StringBuffer();
                    for (int k = 0; k < address.length(); k++) {
                        char c = address.charAt(k);
                        if (!(c + "").equals(":")) {
                            sb.append(c);
                        }
                    }
                    if (sb.toString().equals(doorMILiBean.getMac())) {
                        if (maxSrc == 0) {
                            doorMILiBeanMax = doorMILiBean;
                            maxSrc = searchBlueDeviceBeanList.get(j).getRssi();
                        } else {
                            if (maxSrc > searchBlueDeviceBeanList.get(j).getRssi()) {
                                maxSrc = searchBlueDeviceBeanList.get(j).getRssi();
                                doorMILiBeanMax = doorMILiBean;
                            }
                        }
                    }
                }
            }
        }
        return doorMILiBeanMax;
    }

    /**
     * @param searchBlueDeviceBeanList
     * @param elevatorListBeans
     * @return
     */
    public static ElevatorListBean getMaxElevatorRsic(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, List<ElevatorListBean> elevatorListBeans) {
        int maxSrc = 0;
        ElevatorListBean doorBojinBeanMax = null;
        for (int i = 0; i < elevatorListBeans.size(); i++) {
            ElevatorListBean doorMILiBean = elevatorListBeans.get(i);
            for (int j = 0; j < searchBlueDeviceBeanList.size(); j++) {
                BluetoothDevice bluetoothDevice = searchBlueDeviceBeanList.get(j).getBluetoothDevice();
                if (bluetoothDevice.getAddress().equals(doorMILiBean.getMacAddress())) {
                    if (maxSrc == 0) {
                        doorBojinBeanMax = doorMILiBean;
                        maxSrc = searchBlueDeviceBeanList.get(j).getRssi();
                    } else {
                        if (maxSrc > searchBlueDeviceBeanList.get(j).getRssi()) {
                            maxSrc = searchBlueDeviceBeanList.get(j).getRssi();
                            doorBojinBeanMax = doorMILiBean;
                        }
                    }
                }
            }

        }
        return doorBojinBeanMax;
    }
}
