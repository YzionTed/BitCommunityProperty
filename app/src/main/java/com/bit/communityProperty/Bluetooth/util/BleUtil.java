package com.bit.communityProperty.Bluetooth.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.utils.HexUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleUtil {

    private static final String TAG = "BleUtil";
    private static final long SCAN_PERIOD = 3000;

    public static String characterUUID1 = "00001001-0000-1000-8000-00805f9b34fb";//APP发送命令
    public static String characterUUID2 = "00001002-0000-1000-8000-00805f9b34fb";//BLE用于回复命令
    private static String descriptorUUID = "00001000-0000-1000-8000-00805f9b34fb";//BLE设备特性的UUID

    private Context mContext;
    private static BleUtil mInstance;

    //作为中央来使用和处理数据；  
    private BluetoothGatt mGatt;

    private BluetoothManager manager;
    private BTUtilListener mListener;
    private BluetoothDevice mCurDevice;
    private BluetoothAdapter mBtAdapter;

    private List<BluetoothGattService> serviceList;//服务
    private List<BluetoothGattCharacteristic> characterList;//特征

    private BluetoothGattService service;
    private BluetoothGattCharacteristic character1;
    private BluetoothGattCharacteristic character2;

    private ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList = new ArrayList<>();
    private String keyNo;


    public static synchronized BleUtil getInstance() {
        if (mInstance == null) {
            mInstance = new BleUtil();
        }
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
        init();
    }

    public void init() {

        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("BLE不支持此设备!");
            ((Activity) mContext).finish();
        }
        manager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        //注：这里通过getSystemService获取BluetoothManager，  
        //再通过BluetoothManager获取BluetoothAdapter。BluetoothManager在Android4.3以上支持(API level 18)。  
        if (manager != null) {
            mBtAdapter = manager.getAdapter();
        }
        if (mBtAdapter == null || !mBtAdapter.isEnabled()) {
            mBtAdapter.enable();  
            /*Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); 
            mContext.startActivity(enableBtIntent);*/
        }
    }

    //扫描设备的回调  
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addScanBloothDevice(device, rssi);
                }
            });
        }
    };

    //增加扫描的蓝牙结果
    private void addScanBloothDevice(BluetoothDevice device, int rssi) {
        boolean isExsit = false;
        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {//内存是否有这个设备 有就更新Rssi 没有就增加设备
            if (searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress().equals(device.getAddress())) {
                searchBlueDeviceBeanList.get(i).setRssi(rssi);
                isExsit = true;
                break;
            }
        }
        if (!isExsit) {
            SearchBlueDeviceBean searchBlueDeviceBean = new SearchBlueDeviceBean();
            searchBlueDeviceBean.setBluetoothDevice(device);
            searchBlueDeviceBean.setRssi(rssi);

            searchBlueDeviceBeanList.add(searchBlueDeviceBean);
            Collections.sort(searchBlueDeviceBeanList);

            if (mListener != null) {
                mListener.onLeScanDevices(searchBlueDeviceBeanList);
            }
        }


        Log.d(TAG, "searchBlueDeviceBeanList.size()==" + searchBlueDeviceBeanList.size());
    }

    Timer timer;

    //扫描设备  
    public void scanLeDevice(final boolean enable, int stopTime, final OnSearchCallBack callBack) {

        if (enable) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopScan();
                    callBack.onCall(getSearchBlueDeviceBeanList());
                    if (timer != null) {
                        timer.cancel();
                        // 一定设置为null，否则定时器不会被回收
                        timer = null;
                    }
                    Log.e(TAG, "run: stop");
                }
            }, stopTime);
            startScan();
            Log.e(TAG, "start");
        } else {
            stopScan();
            Log.e(TAG, "stop");
        }
    }

    //开始扫描BLE设备  
    private void startScan() {
        mBtAdapter.startLeScan(mLeScanCallback);
        if (mListener != null) {
            mListener.onLeScanStart();
        }
    }

    //停止扫描BLE设备  
    private void stopScan() {
        mBtAdapter.stopLeScan(mLeScanCallback);
        if (mListener != null) {
            mListener.onLeScanStop();
        }
    }

    //返回中央的状态和周边提供的数据
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            Log.e(TAG, "onConnectionStateChange");
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.e(TAG, "STATE_CONNECTED");

                    if (mListener != null) {
                        mListener.onConnected(mCurDevice);
                    }
                    try {
                        Thread.sleep(300);
                        gatt.discoverServices(); //搜索连接设备所支持的service
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    if (mListener != null) {
                        mListener.onDisConnected(mCurDevice);
                    }
                    disConnGatt();
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    if (mListener != null) {
                        mListener.onConnecting(mCurDevice);
                    }
                    Log.e(TAG, "STATE_CONNECTING");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
                    if (mListener != null) {
                        mListener.onDisConnecting(mCurDevice);
                    }
                    Log.e(TAG, "STATE_DISCONNECTING");
                    break;
            }
            super.onConnectionStateChange(gatt, status, newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Log.d(TAG, "onServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                serviceList = gatt.getServices();
                for (int i = 0; i < serviceList.size(); i++) {
                    BluetoothGattService theService = serviceList.get(i);

                    Log.e(TAG, "ServiceName:" + theService.getUuid());
                    characterList = theService.getCharacteristics();
                    for (int j = 0; j < characterList.size(); j++) {
                        String uuid = characterList.get(j).getUuid().toString();
                        Log.e(TAG, "---CharacterName:" + uuid);
                        if (uuid.equals(characterUUID1)) {
                            character1 = characterList.get(j);
                        } else if (uuid.equals(characterUUID2)) {
                            character2 = characterList.get(j);
                            setNotification();
                        }
                    }
                }
            }
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicRead");
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite");
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (mListener != null) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    Log.e(TAG, "写入数据成功");
                    mListener.onIsSuccess(true);
                } else {
                    Log.e(TAG, "写入数据失败");
                    mListener.onIsSuccess(false);
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic ch) {
            Log.e(TAG, "onCharacteristicChanged");
//            这里是可以监听到设备自身或者手机改变设备的一些数据修改h通知
            receiveData(ch);

            if (ch.getValue() != null) {
                String uuid = ch.getUuid().toString();
                String hexData = HexUtil.byteArrayToHex(ch.getValue()).trim().replace(" ", "");
                Log.e(TAG, "onCharacteristicChanged() - " + ", " + uuid + ", " + hexData);
                Log.e(TAG, "接收---" + hexData);
                if (hexData.equals("4B540AFE")) {
                    if (mListener != null) {
                        mListener.onIsSuccess(false);
                    }
                    Log.e(TAG, "开梯成功");
                } else {
                    if (mListener != null) {
                        mListener.onIsSuccess(true);
                    }
                    Log.e(TAG, "不等于4B540AFE");
                }
            }
            super.onCharacteristicChanged(gatt, ch);
        }
    };

    //获取设备指定的特征中的特性,其中对其进行监听, setCharacteristicNotification与上面的回调onCharacteristicChanged进行一一搭配
    private void setNotification() {
        Log.e(TAG, "setNotification");

        mGatt.setCharacteristicNotification(character2, true);
        BluetoothGattDescriptor descriptor = character2.getDescriptor(UUID.fromString(descriptorUUID));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
        }
        sendWorkModel(keyNo);
    }

    //接收数据,对其进行处理  
    private void receiveData(BluetoothGattCharacteristic ch) {
        byte[] bytes = ch.getValue();
        int cmd = bytes[0];
        int agree = bytes[1];
        switch (cmd) {
            case 1:
//                if (mListener != null) {
//                    mListener.onStrength(agree);
//                }
                Log.e(TAG, "手机通知BLE设备强度:" + agree);
                break;
            case 2:
//                if (mListener != null) {
//                    mListener.onModel(agree);
//                }
                Log.e(TAG, "工作模式:" + agree);
                break;
            case 3:
//                if (mListener != null) {
//                    mListener.onStrength(agree);
//                }
                Log.e(TAG, "设备自身通知改变强度:" + agree);
                break;
        }
    }

    //连接设备  
    public void connectLeDevice(String mac, String keyNo) {
        Log.e(TAG, "connectLeDevice的设备 mac=" + mac + "  keyNo=" + keyNo);
        this.keyNo = keyNo;
        mBtAdapter.stopLeScan(mLeScanCallback);
        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
            if (searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress().equals(mac)) {
                mCurDevice = searchBlueDeviceBeanList.get(i).getBluetoothDevice();
                break;
            }
        }
        Log.e(TAG, "listDevice.size()=" + searchBlueDeviceBeanList.size());
        //   mCurDevice = listDevice.get(devicePos);
        if (mCurDevice == null) {
            Log.e(TAG, "mCurDevice==null");
            return;
        }
        Log.e(TAG, "mCurDevice==" + mCurDevice.getAddress());
        checkConnGatt();
    }

    //发送进入工作模式请求  
    public void sendWorkModel(String macAddress) {
        Log.e(TAG, "sendWorkModel   macAddress ==" + macAddress);
        if (character1 != null) {
            StringBuffer sbf;
            if (macAddress.contains(":")) {
                String[] str = macAddress.split(":");
                sbf = new StringBuffer();
                for (int i = 0; i < str.length; i++) {
                    sbf.append(str[i]);
                }
            } else {
                sbf = new StringBuffer(macAddress);
            }
            Log.e(TAG, "doorJinBoBean.getKeyNo()==" + sbf.toString());
            String sum = HexUtil.getCheckSUMByte("0A" + sbf.toString());

            byte[] head = HexUtil.getMergeBytes("K".getBytes(), "T".getBytes());
            byte[] content = HexUtil.strTobyte("0A" + sbf.toString());
            head = HexUtil.getMergeBytes(head, content);
            byte[] end = HexUtil.getMergeBytes(HexUtil.strTobyte(sum), new byte[]{(byte) 0xFE});
            byte[] bvalue = HexUtil.getMergeBytes(head, end);

            try {
                Log.e(TAG, "电梯指令：" + new String(bvalue, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e(TAG, "写入指令  " + new String(bvalue));

            character1.setValue(bvalue);
            mGatt.writeCharacteristic(character1);
        }
    }

    //发送强度  
    public void sendStrength(int strength) {
        byte[] strengthModel = {0x01, (byte) strength};
        if (character1 != null) {
            character1.setValue(strengthModel);
            mGatt.writeCharacteristic(character1);
        }
    }

    //检查设备是否连接了  
    private void checkConnGatt() {
        if (mGatt == null) {
            mGatt = mCurDevice.connectGatt(mContext, true, mGattCallback);
            if (mListener != null) {
                mListener.onConnecting(mCurDevice);
            }
        } else {
            mGatt.connect();
            mGatt.discoverServices();
        }
    }

    //  断开设备连接  
    private void disConnGatt() {
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt.close();
            mGatt = null;
            searchBlueDeviceBeanList.clear();
            if (mListener != null) {
                mListener.onLeScanDevices(searchBlueDeviceBeanList);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void setBTUtilListener(BTUtilListener listener) {
        mListener = listener;
    }

    public interface BTUtilListener {
        void onLeScanStart(); // 扫描开始  

        void onLeScanStop();  // 扫描停止  

        void onLeScanDevices(ArrayList<SearchBlueDeviceBean> listDevice); //扫描得到的设备

        void onConnected(BluetoothDevice mCurDevice); //设备的连接  

        void onDisConnected(BluetoothDevice mCurDevice); //设备断开连接  

        void onConnecting(BluetoothDevice mCurDevice); //设备连接中

        void onDisConnecting(BluetoothDevice mCurDevice); //设备连接失败

        //        void onStrength(int strength); //给设备设置强度
//
//        void onModel(int model); //设备模式
        void onIsSuccess(boolean b); //电梯开启成功
    }

    public ArrayList<SearchBlueDeviceBean> getSearchBlueDeviceBeanList() {
        StringBuffer sb = new StringBuffer();
        sb.append("蓝牙地址：");
        if (searchBlueDeviceBeanList.size() > 0) {
            for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
                sb.append(searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress() + "   ");
            }
        }
        Log.e(TAG, "" + sb.toString());
        return searchBlueDeviceBeanList;
    }

    public interface OnSearchCallBack {
        void onCall(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList);
    }
}  