package com.bit.communityProperty.Bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.interfcace.OnSearchBlueDeviceListener;
import com.bit.communityProperty.MyApplication;
import com.inuker.bluetooth.library.BluetoothClientManger;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dell on 2018/2/10.
 */

/**
 * 蓝牙Application
 */
public class BluetoothApplication {

    private MyApplication myApp;
    private BluetoothService mBluetoothService;
    private BluetoothDevice mConnectedDevice;         // 链接的蓝牙
    private Intent intentBluetooth;
    private BluetoothAdapter mBluetoothAdapter;

    private ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList = new ArrayList<>();

    private static final String TAG = "BluetoothApplication";
    private BluetoothClientManger mBluetoothClientManger;
    private ScanCallback scanCallback;
    private BluetoothAdapter.LeScanCallback leScanCallback;
    private BluetoothLeScanner mBluetoothLeScanner;
    private OnSearchBlueDeviceListener searchBlueDeviceListener;

    public BluetoothApplication(MyApplication myApp) {
        this.myApp = myApp;
        init();
    }

    //初始化
    public void init() {
        mBluetoothClientManger = new BluetoothClientManger(myApp);
        openBluetooth();
        bindBleService();
        getBluetoothAdapter();

    }

    /**
     * 搜索蓝牙设备
     *
     * @stopMinite 搜索多少秒后停止
     */
    public void scanBluetoothDevice(int stopMinite) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: stopLeScan...");
                stopScanning();
            }
        }, stopMinite);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanCallback = initGetCallback();
            if(mBluetoothLeScanner!=null){
                mBluetoothLeScanner.startScan(scanCallback);
            }
        } else {
            leScanCallback = leScangetCallback();
            mBluetoothAdapter.startLeScan(leScanCallback);
        }

    }

    /**
     * 搜索蓝牙设备
     *
     * @stopMinite 搜索多少秒后停止
     */
    // 初始化定时器
    Timer timer;

    public void scanBluetoothDevice(int stopMinite, final CallBack callBack) {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                callBack.onCall(getSearchBlueDeviceBeanList());
                stopScanning();
                stopTimer();
                Log.e("lzp", "timer excute");
            }
        }, stopMinite);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "run: stopLeScan...");
//
//            }
//        }, stopMinite);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            scanCallback = initGetCallback();
            mBluetoothLeScanner.startScan(scanCallback);
        } else {
            leScanCallback = leScangetCallback();
            mBluetoothAdapter.startLeScan(leScanCallback);
        }

    }

    public interface CallBack {
        void onCall(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList);
    }

    // 停止定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    @NonNull
    private BluetoothAdapter.LeScanCallback leScangetCallback() {
        return new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                Log.d(TAG, "  run: startLeScan  sdk<21  ...");
                Log.d(TAG, "run: Scan... device=" + device.getName() + " Address=" + device.getAddress());
                addScanBloothDevice(device, rssi);
            }
        };
    }

    @NonNull
    private ScanCallback initGetCallback() {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Log.d(TAG, "  run: startLeScan  sdk>21  ..." + "UUID==" + result.getDevice().getUuids());

                addScanBloothDevice(result.getDevice(), result.getRssi());
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.d(TAG, "  errorCode=" + errorCode);
            }
        };
    }

    /**
     * 停止扫描
     */
    private void stopScanning() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mBluetoothLeScanner != null && scanCallback != null) {
                mBluetoothLeScanner.stopScan(scanCallback);
            }
        } else {
            if (mBluetoothAdapter != null && leScanCallback != null) {
                mBluetoothAdapter.stopLeScan(leScanCallback);
            }
        }
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();  // 如果是2.0也要暂停扫描
        }
    }

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

            if (searchBlueDeviceListener != null) {
                searchBlueDeviceListener.OnSearchAllDeviceCallBack(searchBlueDeviceBeanList);
                searchBlueDeviceListener.OnSearchBludeDeviceCallBack(searchBlueDeviceBean);
            }
        }
        Log.d(TAG, "searchBlueDeviceBeanList.size()==" + searchBlueDeviceBeanList.size());
    }

    /**
     * 打开蓝牙设备 如果检测到蓝牙没有开启，尝试开启蓝牙
     */
    public void openBluetooth() {
        if (mBluetoothClientManger.isBleSupported()) {
            if (!mBluetoothClientManger.isBluetoothOpened()) {
                Log.d(TAG, "打开蓝牙设备");
                mBluetoothClientManger.openBluetooth();
            }
        }
    }

    /**
     * 获取蓝牙适配器
     *
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter() {
        if (mBluetoothAdapter == null) {
            final BluetoothManager bluetoothManager = (BluetoothManager) myApp.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        return mBluetoothAdapter;
    }

    /**
     * 连接蓝牙设备
     */
    public void connetBlueDevice(SearchBlueDeviceBean searchBlueDeviceBean) {
        mBluetoothService.connectDevice(searchBlueDeviceBean);
    }

    // 蓝牙是否启用
    public boolean blueIsEnable() {
        if (getBluetoothAdapter() != null && mBluetoothAdapter.isEnabled()) {
            return true;
        }
        return false;
    }


    /**
     * 绑定蓝牙服务
     */
    public void bindBleService() {
        Log.i("ConnectService", "Service bindService ");
        intentBluetooth = new Intent(myApp, BluetoothService.class);
        myApp.bindService(intentBluetooth, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i("ConnectService", "Service Connected ");
            mBluetoothService = ((BluetoothService.LocalBinder) iBinder).getService();
            if (mConnectedDevice != null && !TextUtils.isEmpty(mConnectedDevice.getAddress())) {
                Log.i("ConnectService", "ble mDeviceAddress " + mConnectedDevice.getAddress());
                // connetBle(mConnectedDevice);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 断掉蓝牙
            Log.i("ConnectService", "Service DisConnected ");
            //  mBluetoothService.disconnect();
            mBluetoothService = null;
        }
    };


    public boolean checkLocationEnable(final Activity context) {
        if (isLocationEnalbe()) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
        return true;
    }

    /**
     * 关门
     */
    public void closeBluetooth() {
        mBluetoothAdapter.disable();
    }

    /**
     * 是否可以定位
     *
     * @return
     */
    public boolean isLocationEnalbe() {
        if (ContextCompat.checkSelfPermission(myApp, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(myApp, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return true;
        }
        return false;
    }

    /**
     * 去除服務
     */
    public void onDestroy() {
        mBluetoothService.onUnbind(intentBluetooth);
        mBluetoothService.onDestroy();
    }

    public ArrayList<SearchBlueDeviceBean> getSearchBlueDeviceBeanList() {
        return searchBlueDeviceBeanList;
    }

    public void setOnSearchBlueDeviceListener(OnSearchBlueDeviceListener searchBlueDeviceListener) {
        this.searchBlueDeviceListener = searchBlueDeviceListener;
    }

}
