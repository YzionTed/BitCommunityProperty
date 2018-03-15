package com.bit.communityProperty.utils;

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
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import net.vidageek.mirror.dsl.Mirror;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Dell on 2017/11/6.
 * Created time:2017/11/6 9:36
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BlueToothUtil {

    private static final String TAG = "BlueToothUtil";

    private static final long SCAN_PERIOD = 8 * 1000;

    private final UUID KT_UUID = UUID
            .fromString("00001000-0000-1000-8000-00805f9b34fb");
    private final UUID KT_NOTIFY = UUID
            .fromString("00001002-0000-1000-8000-00805f9b34fb");
    private final UUID KT_WRITE = UUID
            .fromString("00001001-0000-1000-8000-00805f9b34fb");
    private static final String UUID_DESCRIPTOR = "00002902-0000-1000-8000-00805f9b34fb";

    public static byte[] workModel = {0x02, 0x01};

    private Context mContext;
    private static BlueToothUtil mInstance;

    //作为中央来使用和处理数据；
    private BluetoothGatt mGatt;

    private BluetoothManager manager;
    private BTUtilListener mListener;
    private OnCharacteristicListener mCharacteristicListener = null;
    private BluetoothDevice mCurDevice;
    private BluetoothAdapter mBtAdapter;
    private List<BluetoothDevice> listDevice;
    private List<BluetoothGattService> serviceList;//服务
    private List<BluetoothGattCharacteristic> characterList;//特征
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;//广播

    private BluetoothGattService service;
    private BluetoothGattCharacteristic wCharacter;
    private BluetoothGattCharacteristic nCharacter;

    private Handler handler = new Handler();
    private Runnable runnable;
    int count = 0;
    private boolean isConnected = false;
    private boolean broadcastState = false;
    private OnBluetoothStateCallBack onBluetoothStateCallBack;

    private boolean isOpenSuccess = false;

    public static synchronized BlueToothUtil getInstance() {
        if (mInstance == null) {
            mInstance = new BlueToothUtil();
        }
        return mInstance;
    }

    public void setContext(Context context) {
        mContext = context;
        init();
    }

    public void init() {

        listDevice = new ArrayList<>();
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
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!listDevice.contains(device)) {
                        //不重复添加
                        listDevice.add(device);
                        mListener.onLeScanDevice(device);
                        mListener.onLeScanDevices(listDevice);
                        Log.e(TAG, "device:" + device.toString());
                    }
                }
            });
        }
    };

    //扫描设备
    public void scanLeDevice(final boolean enable) {
        if (enable) {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan();
                    Log.e(TAG, "run: stop");
                }
            }, SCAN_PERIOD);
            startScan();
            Log.e(TAG, "start");
        } else {
            stopScan();
            Log.e(TAG, "stop");
        }
    }

    public boolean isBroadcastOpen() {
        return broadcastState;
    }

    //开始扫描BLE设备
    private void startScan() {
        mBtAdapter.startLeScan(mLeScanCallback);
        mListener.onLeScanStart();
    }

    //停止扫描BLE设备
    private void stopScan() {
        mBtAdapter.stopLeScan(mLeScanCallback);
        mListener.onLeScanStop();
    }


    //开启广播
    public void openBroadcast(String keyNo) {

        if (keyNo == null) {
            Log.e(TAG, "广播发送的keyNo=null");
            return;
        }
        if ("".equals(keyNo)) {
            Log.e(TAG, "广播发送的keyNo=null");
            return;
        }

        if (mBluetoothLeAdvertiser == null) {
            mBluetoothLeAdvertiser = mBtAdapter.getBluetoothLeAdvertiser();
        }
        AdvertiseSettings settings = createAdvertiseSettings(0);
        AdvertiseData data = createAdvertiseData(keyNo);
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.startAdvertising(settings, data, mAdvCallback);
        }
    }

    //关闭广播
    public void closeBroadcast() {
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvCallback);
            mBluetoothLeAdvertiser = null;
            broadcastState = false;
        }
    }

    //广播设置
    private AdvertiseSettings createAdvertiseSettings(int timeoutMillis) {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);
        builder.setTimeout(timeoutMillis);

        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);
        return builder.build();
    }

    //
//    //设置广播数据
    public AdvertiseData createAdvertiseData(String keyNo) {

        AdvertiseData.Builder mDataBuilder = new AdvertiseData.Builder();
        //    String[] str = getBtAddressViaReflection().split(":");
        StringBuffer sbf = new StringBuffer(keyNo);
//        for (int i = 0; i < str.length; i++) {
//            sbf.append(str[i]);
//        }
        Log.e(TAG, "打开梯禁==keyNo" + keyNo);
        String sum = HexUtil.getCheckSUMByte("0B" + sbf.toString());
        String msg = HexUtil.str2HexStr("K") + HexUtil.str2HexStr("T") + "0B" + sbf.toString() + sum.toUpperCase() + "FE";
        Log.e("msg", msg);
        mBtAdapter.setName(msg);
        mDataBuilder.setIncludeDeviceName(true);
        mDataBuilder.setIncludeTxPowerLevel(false);

        AdvertiseData mAdvertiseData = mDataBuilder.build();
        if (mAdvertiseData == null) {
            Toast.makeText(mContext, "mAdvertiseData == null", Toast.LENGTH_LONG).show();
            Log.e(TAG, "mAdvertiseData == null");
        } else {
            Log.e("eeeeeee", mAdvertiseData.toString());
        }
        return mAdvertiseData;

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
                    isConnected = true;
                    mCurDevice = gatt.getDevice();
                    mListener.onConnected(mCurDevice);
                    gatt.discoverServices(); //搜索连接设备所支持的service
                    if (onBluetoothStateCallBack != null) {
                        onBluetoothStateCallBack.OnBluetoothState("连接成功");
                    }
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    isConnected = false;
                    mListener.onDisConnected(mCurDevice);
                    disConnGatt();
                    if (onBluetoothStateCallBack != null) {
                        if (!isOpenSuccess) {
                            isOpenSuccess = false;
                            onBluetoothStateCallBack.OnBluetoothState("开梯失败，重新开梯");
                        }
                    }
                    Log.e(TAG, "STATE_DISCONNECTED");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.e(TAG, "STATE_CONNECTING");
                    break;
                case BluetoothProfile.STATE_DISCONNECTING:
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
                    if (theService.getUuid().equals(KT_UUID)) {
                        characterList = theService.getCharacteristics();
                        for (int j = 0; j < characterList.size(); j++) {
                            UUID uuid = characterList.get(j).getUuid();
//                            Log.e(TAG, "---CharacterName:" + uuid);
                            if (uuid.equals(KT_WRITE)) {
                                wCharacter = characterList.get(j);
                            } else if (uuid.equals(KT_NOTIFY)) {
                                nCharacter = characterList.get(j);
                                setNotification();
                            }
                        }
                    }

                }
            }
            super.onServicesDiscovered(gatt, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicRead");
            if (mCharacteristicListener != null) {
                mCharacteristicListener.onCharacteristicRead(gatt, characteristic, status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e(TAG, "onCharacteristicWrite");
            if (mCharacteristicListener != null) {
                mCharacteristicListener.onCharacteristicWrite(gatt, characteristic, status);
            }
            if (onBluetoothStateCallBack != null) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    isOpenSuccess = true;
                    onBluetoothStateCallBack.OnBluetoothState("开梯成功");
                    //  Log.e(Tag,"写入数据成功");
                } else {
                    isOpenSuccess = false;
                    onBluetoothStateCallBack.OnBluetoothState("开梯失败");
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            Log.e(TAG, "onCharacteristicChanged");
            if (mCharacteristicListener != null) {
                mCharacteristicListener.onCharacteristicChanged(gatt, characteristic);
            }
        }
    };

    private AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            if (settingsInEffect != null) {
                Log.e("debug", "onStartSuccess TxPowerLv="
                        + settingsInEffect.getTxPowerLevel()
                        + " mode=" + settingsInEffect.getMode()
                        + " timeout=" + settingsInEffect.getTimeout());
            } else {
                Log.e("debug", "onStartSuccess, settingInEffect is null");
            }
            broadcastState = true;
        }

        public void onStartFailure(int errorCode) {
            Log.e("debug", "onStartFailure errorCode=" + errorCode);
            broadcastState = false;
            Toast.makeText(mContext, "开启失败", Toast.LENGTH_SHORT).show();
        }
    };

    //获取设备指定的特征中的特性,其中对其进行监听, setCharacteristicNotification与上面的回调onCharacteristicChanged进行一一搭配
    private void setNotification() {
        mGatt.setCharacteristicNotification(nCharacter, true);
        BluetoothGattDescriptor descriptor = nCharacter.getDescriptor(UUID.fromString(UUID_DESCRIPTOR));
        if (descriptor != null) {
            System.out.println("write descriptor");
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
        }

        if (mCharacteristicListener != null) {
            mCharacteristicListener.onNotificationSetted();
        }
    }

    //接收数据,对其进行处理
    public void handleResult(BluetoothGattCharacteristic ch) {
        if (ch.getValue() != null) {
            String uuid = ch.getUuid().toString();
            String hexData = HexUtil.byteArrayToHex(ch.getValue()).trim().replace(" ", "");
            Log.e(TAG, "onCharacteristicChanged() - " + ", " + uuid + ", " + hexData);
            Log.e(TAG, "接收---" + hexData);
            if (hexData.equals("4B540AFE")) {
                count = 0;
                handler.removeCallbacks(runnable);
                mGatt.disconnect();
            }
        }
    }

    //接收数据,对其进行处理
    public void handleResultCallBack(BluetoothGattCharacteristic ch, OnCallBackListener onCallBackListener) {
        if (ch.getValue() != null) {
            String uuid = ch.getUuid().toString();
            String hexData = HexUtil.byteArrayToHex(ch.getValue()).trim().replace(" ", "");
            Log.e(TAG, "onCharacteristicChanged() - " + ", " + uuid + ", " + hexData);
            Log.e(TAG, "接收---" + hexData);
            if (hexData.equals("4B540AFE")) {
                count = 0;
                handler.removeCallbacks(runnable);
                mGatt.disconnect();
                if (onCallBackListener != null) {
                    onCallBackListener.OnCallBack(1);
                }
            } else {
                onCallBackListener.OnCallBack(2);

            }
        }
    }


    // 临时加的代码
    public interface OnCallBackListener {
        void OnCallBack(int state);//1:代表成功 2：代表失败
    }

    //连接设备
    public void connectLeDevice(String mac) {
//        stopScan();
//        mCurDevice = listDevice.get(devicePos);
        count = 0;
        isOpenSuccess = false;

        if (mac == null) {
            return;
        }
        if ("".equals(mac)) {
            return;
        }
       // checkConnGatt("44:A6:E5:45:12:49");
         checkConnGatt(mac);
    }

    //发送开梯指令
    public void sendOpen() {
        String[] str = "29:44:25:DD:31:E5".split(":");
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            sbf.append(str[i]);
        }
        String sum = HexUtil.getCheckSUMByte("0A" + sbf.toString());

        byte[] head = HexUtil.getMergeBytes("K".getBytes(), "T".getBytes());
        byte[] content = HexUtil.strTobyte("0A" + sbf.toString());
        head = HexUtil.getMergeBytes(head, content);
        byte[] end = HexUtil.getMergeBytes(HexUtil.strTobyte(sum), new byte[]{(byte) 0xFE});
        byte[] bvalue = HexUtil.getMergeBytes(head, end);

        Log.e(TAG, "电梯指令：" + new String(bvalue));
        if (wCharacter != null) {
            wCharacter.setValue(bvalue);
        }
        // wCharacter.setValue("A1B2C3D4F5D6");
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mGatt != null) {
                    mGatt.writeCharacteristic(wCharacter);
                    count++;
                    if (count < 6) {
                        handler.postDelayed(this, 200);
                    } else {
                        handler.removeCallbacks(this);
                        count = 0;
                        mGatt.disconnect();
                    }
                } else {
                    handler.removeCallbacks(this);
                    count = 0;
                    if (mGatt != null) {
                        mGatt.disconnect();
                    }

                }
            }
        };
        handler.postDelayed(runnable, 0);

    }

    //发送开梯指令
    public void sendOpen(String macAddress) {

        if (onBluetoothStateCallBack != null) {
            onBluetoothStateCallBack.OnBluetoothState("发送开梯指令");
        }
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
        if (wCharacter != null) {
            wCharacter.setValue(bvalue);
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mGatt != null) {
                    mGatt.writeCharacteristic(wCharacter);
                    count++;
                    if (count < 6) {
                        handler.postDelayed(this, 200);
                    } else {
                        handler.removeCallbacks(this);
                        count = 0;
                        mGatt.disconnect();
                    }
                } else {
                    handler.removeCallbacks(this);
                    count = 0;
                    if (mGatt != null) {
                        mGatt.disconnect();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);

    }

    Timer timer;


    //检查设备是否连接了
    private boolean checkConnGatt(String mac) {

        if (mBtAdapter == null || mac == null) {
            Log.e("BluetoothLeService", "蓝牙适配器没有初始化获取mac地址未指明。");
            return false;
        }
        mCurDevice = mBtAdapter.getRemoteDevice(mac);
        if (mCurDevice == null) {
            Log.e("BluetoothLeService", "蓝牙设备没有发现，无法连接。");
            return false;
        }
        close();
        Log.e(TAG, "连接的设备mac==" + mac);
        mGatt = mCurDevice.connectGatt(mContext, false, mGattCallback);
        if (mGatt.connect()) {

            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (!isConnected) {
                        try {
                            if (null != mGatt) {
                                mGatt.disconnect();
                                mGatt.close();
                                mGatt = null;
                                mListener.onDisConnected(mCurDevice);
                                if (onBluetoothStateCallBack != null) {
                                    if (!isOpenSuccess) {
                                        isOpenSuccess = false;
                                        onBluetoothStateCallBack.OnBluetoothState("开梯失败，重新开梯");
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mListener.onDisConnected(mCurDevice);
                        }
                    }
                    timer.cancel();

                    Log.e("lzp", "timer excute");
                }
            }, 3000);
        } else {
        }

        return true;
    }


    /**
     * 断开设备连接
     */
    public void disConnGatt() {
        if (mGatt != null) {
            mGatt.close();
            mGatt = null;
            listDevice = new ArrayList<>();
        }
    }

    private void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void setBTUtilListener(BTUtilListener listener) {
        mListener = listener;
    }

    public void setOnCharacteristicListener(OnCharacteristicListener listener) {
        this.mCharacteristicListener = listener;
    }

    /**
     * 释放资源
     */
    public void close() {
        if (mGatt != null) {
            mGatt.close();
            mGatt = null;
        }
    }

    /**
     * 蓝牙扫描连接操作接口
     */
    public interface BTUtilListener {
        void onLeScanStart(); // 扫描开始

        void onLeScanStop();  // 扫描停止

        void onLeScanDevice(BluetoothDevice device); //扫描得到的设备

        void onLeScanDevices(List<BluetoothDevice> listDevice); //扫描得到的设备

        void onConnected(BluetoothDevice mCurDevice); //设备的连接

        void onDisConnected(BluetoothDevice mCurDevice); //设备断开连接

    }

    /**
     * 发现服务、数据读写操作接口
     */
    public interface OnCharacteristicListener {
        void onServicesDiscovered();

        void onNotificationSetted();

        void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

        void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic);

        void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status);

    }

    /**
     * 获取手机真实蓝牙地址
     */
    public static String getBtAddressViaReflection() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Object bluetoothManagerService = new Mirror().on(bluetoothAdapter).get().field("mService");
        if (bluetoothManagerService == null) {
            return null;
        }
        Object address = new Mirror().on(bluetoothManagerService).invoke().method("getAddress").withoutArgs();
        if (address != null && address instanceof String) {
            // return (String) address;
            return (String) "74:23:44:6E:67:7D";
        } else {
            return null;
        }
    }

    /**
     * 电梯状态的返回监听
     */
    public interface OnBluetoothStateCallBack {
        void OnBluetoothState(String state);
    }

    public void setOnBluetoothStateCallBack(OnBluetoothStateCallBack onBluetoothStateCallBack) {
        this.onBluetoothStateCallBack = onBluetoothStateCallBack;
    }


}
