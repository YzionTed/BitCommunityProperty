package com.bit.communityProperty.Bluetooth.jinbo;

/**
 * Created by xiewensheng on 2018/2/9.
 */

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.inuker.bluetooth.library.BluetoothClientManger;
import com.inuker.bluetooth.library.base.CardLog;
import com.inuker.bluetooth.library.base.Register;
import com.inuker.bluetooth.library.search.SearchResult;

import net.vidageek.mirror.dsl.Mirror;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 金博工具类
 */
public class JiBoUtils {

    private static JiBoUtils instance;
    private static Context mContext;
    private BluetoothClientManger mBluetoothClientManger;
    private JiBoBean jiBoBean;
    private String Tag = "JiBoUtils";

    private JiBoUtils(Context context) {
        init(context);
    }

    public static synchronized JiBoUtils getInstance(Context context) {
        if (instance == null) {
            instance = new JiBoUtils(context);
        }
        return instance;
    }

    //获取JiBo的管理者对象
    public BluetoothClientManger getmBluetoothClientManger() {
        return mBluetoothClientManger;
    }

    public JiBoBean getJiBoBean() {
        return jiBoBean;
    }

    //初始化数据
    private void init(Context context) {
        mContext = context;
        mBluetoothClientManger = new BluetoothClientManger(context);
        jiBoBean = new JiBoBean();
        jiBoBean.setBluetoothSupporse(mBluetoothClientManger.isBleSupported());

        if (jiBoBean.isBluetoothSupporse()) {
            jiBoBean.setBluetoothOpen(mBluetoothClientManger.isBluetoothOpened());
            //蓝牙是否打开
            if (!mBluetoothClientManger.isBluetoothOpened()) {
                jiBoBean.setBluetoothOpen(mBluetoothClientManger.openBluetooth());
            }
        } else {
            Toast.makeText(mContext, "您的设备不支持蓝牙设备", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 蓝牙开梯
     */
    public void openDevice(final String macAddress, final List<Register> registerList, final OnOpenLiftCallBackListenter onOpenLiftCallBackListenter) {

        Log.e(Tag, "macAddress==" + macAddress + " registerList=" + registerList.get(0).getPhoneMac());
        if (jiBoBean.isBluetoothSupporse()) {
            jiBoBean.setBluetoothOpen(mBluetoothClientManger.isBluetoothOpened());
            //蓝牙是否打开
            if (!mBluetoothClientManger.isBluetoothOpened()) {
                jiBoBean.setBluetoothOpen(mBluetoothClientManger.openBluetooth());
            }
        } else {
            Toast.makeText(mContext, "您的设备不支持蓝牙设备", Toast.LENGTH_LONG).show();
            return;
        }

        mBluetoothClientManger.setBleConnectLister(new BluetoothClientManger.BleConnectLister() {
            @Override
            public void connectedResult(Boolean isConnected, String mac) {
                if (isConnected) {

                    jiBoBean.setRegistMac(mac);
                    mBluetoothClientManger.openNotify(mac);
                    //Toast.makeText(mContext, "连接成功", Toast.LENGTH_LONG).show();
                    Log.e("=====", " connectedResult==连接成功");
                    if (onOpenLiftCallBackListenter != null) {
                        onOpenLiftCallBackListenter.OpenLiftCallBackListenter(2, "连接成功");
                    }
                } else {
                    if (onOpenLiftCallBackListenter != null) {
                        onOpenLiftCallBackListenter.OpenLiftCallBackListenter(2, "连接失败");
                    }
                    // Toast.makeText(mContext, "连接失败", Toast.LENGTH_LONG).show();
                    Log.e("=====", " connectedResult==连接失败");
                }
            }
        });

        mBluetoothClientManger.setBleNotifyLister(new BluetoothClientManger.BleNotifyLister() {
            @Override
            public void onNotifyResult(Register register) {
                if (onOpenLiftCallBackListenter != null) {
                    onOpenLiftCallBackListenter.OpenLiftCallBackListenter(1, "开梯成功");
                }
                // Toast.makeText(mContext, "开梯成功", Toast.LENGTH_LONG).show();
                Log.e("=====", "开梯成功： register.type=" + register.type + ",register.type=" + register.PhoneMac + ",register.register=" + register.register);
            }

            @Override
            public void noNewNotifyResutlt(String s) {

            }

            @Override
            public void dealOver(boolean b) {

            }

            @Override
            public void onResponse(boolean isNoticeOk) {
                Log.e("====", "isNoticeOk=通知 =" + isNoticeOk);
                if (isNoticeOk) {
                    //  Toast.makeText(mContext, "通知成功", Toast.LENGTH_LONG).show();
                    if (macAddress.contains("#")) {

                        String[] split = macAddress.split("#");
                        Log.e(Tag, "split[1]=" + split[1]);
                        mBluetoothClientManger.connect(split[1]);
                        mBluetoothClientManger.write(split[1], registerList);
                    } else {
                        mBluetoothClientManger.write(macAddress, registerList);
                    }
                    if (onOpenLiftCallBackListenter != null) {
                        onOpenLiftCallBackListenter.OpenLiftCallBackListenter(2, "连接设备成功");
                    }
                } else {
                    if (onOpenLiftCallBackListenter != null) {
                        onOpenLiftCallBackListenter.OpenLiftCallBackListenter(2, "连接设备失败");
                    }
                    //  Toast.makeText(mContext, "通知失败", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onLadderNotifyResult(CardLog cardLog) {

            }

            @Override
            public void onLadderOverNotifyResult(boolean b) {

            }
        });
        if (macAddress.contains("#")) {
            String[] split = macAddress.split("#");
            Log.e(Tag, "split[1]=" + split[1]);
            mBluetoothClientManger.connect(split[1]);
        } else {
            mBluetoothClientManger.connect(macAddress);
        }

    }

    //获取本机Mac地址
    public static String getBtAddressViaReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Object bluetoothManagerService = new Mirror().on(bluetoothAdapter).get().field("mService");
        if (bluetoothManagerService == null) {
            Log.e("====", "couldn't find bluetoothManagerService");
            return null;
        }
        Object address = new Mirror().on(bluetoothManagerService).invoke().method("getAddress").withoutArgs();
        if (address != null && address instanceof String) {
            Log.e("====", "using reflection to get the BT MAC address: " + address);
            return (String) address;
        } else {
            return null;
        }
    }

    /**
     * 当前时间（yyyyMMddHHmmssEE）年月日时分秒星期
     *
     * @return
     */
    public static String getFormatUTCTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar c = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        String formattime = sdf.format(date);
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "07";
        } else if ("2".equals(mWay)) {
            mWay = "01";
        } else if ("3".equals(mWay)) {
            mWay = "02";
        } else if ("4".equals(mWay)) {
            mWay = "03";
        } else if ("5".equals(mWay)) {
            mWay = "04";
        } else if ("6".equals(mWay)) {
            mWay = "05";
        } else if ("7".equals(mWay)) {
            mWay = "06";
        }
        return formattime + mWay;
    }

    public interface OnSearchDevicesListener {
        public void OnSearchDeviceCallBack(ArrayList<JiBoBean.DeviceDateBean> bluetoothDevices);
    }

    public interface OnOpenLiftCallBackListenter {
        //1:成功 2：失败
        void OpenLiftCallBackListenter(int backState, String msg);
    }
}
