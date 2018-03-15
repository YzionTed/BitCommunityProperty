package com.bit.communityProperty.Bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;

/**
 * Created by Dell on 2018/2/10.
 * 藍牙服務
 */

public class BluetoothService extends Service {

    private LocalBinder mIBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mIBinder == null) {
            mIBinder = new LocalBinder();
        }
        Log.i("ConnectService", "service onBind: ");
        return mIBinder;
    }

    /**
     * 连接蓝牙
     *
     * @param searchBlueDeviceBean
     */
    public void connectDevice(SearchBlueDeviceBean searchBlueDeviceBean) {


    }




    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }
}
