package com.bit.communityProperty.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.interfcace.OnSearchBlueDeviceListener;
import com.bit.communityProperty.Bluetooth.util.BleUtil;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.utils.ToastUtil;

import java.util.ArrayList;

public class BlueDemoActivity extends AppCompatActivity {

    private LinearLayout ll_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);
        Button bnt_scan = (Button) findViewById(R.id.bnt_scan);
        Button bnt_open = (Button) findViewById(R.id.bnt_open);
        Button bnt_scan1 = (Button) findViewById(R.id.bnt_scan1);
        ll_content = (LinearLayout) findViewById(R.id.ll_content);
        MyApplication.getInstance().getBlueToothApp().checkLocationEnable(this);
        MyApplication.getInstance().getBlueToothApp().openBluetooth();
        bnt_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyApplication.getInstance().getBlueToothApp().isLocationEnalbe()) {
                    ToastUtil.showShort("请打开您的定位权限！");
                    return;
                }
                MyApplication.getInstance().getBlueToothApp().scanBluetoothDevice(5000);
            }
        });

        MyApplication.getInstance().getBlueToothApp().setOnSearchBlueDeviceListener(new OnSearchBlueDeviceListener() {
            @Override
            public void OnSearchBludeDeviceCallBack(SearchBlueDeviceBean searchBlueDeviceBean) {

            }

            @Override
            public void OnSearchAllDeviceCallBack(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
                setDate(searchBlueDeviceBeanList);
            }
        });

        BleUtil.getInstance().setContext(this);
        bnt_scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        BleUtil.getInstance().scanLeDevice(true, 2000, new BleUtil.OnSearchCallBack() {
                            @Override
                            public void onCall(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
                                // BleUtil.getInstance().connectLeDevice(0);
                            }
                        });
                    }
                }.start();

            }
        });

        BleUtil.getInstance().setBTUtilListener(new BleUtil.BTUtilListener() {
            @Override
            public void onLeScanStart() {

            }

            @Override
            public void onLeScanStop() {

            }

            @Override
            public void onLeScanDevices(ArrayList<SearchBlueDeviceBean> listDevice) {

            }

            @Override
            public void onConnected(BluetoothDevice mCurDevice) {

            }

            @Override
            public void onDisConnected(BluetoothDevice mCurDevice) {

            }

            @Override
            public void onConnecting(BluetoothDevice mCurDevice) {

            }

            @Override
            public void onDisConnecting(BluetoothDevice mCurDevice) {

            }

            @Override
            public void onIsSuccess(boolean b) {

            }

        });

        bnt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        // BleUtil.getInstance().connectLeDevice(0);
                    }
                }.start();

                // List<BluetoothDevice> listDevice = BleUtil.getInstance().listDevice;
//                for (int i = 0; i < listDevice.size(); i++) {
//                    if (listDevice.get(i).getAddress().equals("44:A6:E5:45:12:49")) {
//
//                        break;
//                    }
//                }
                //BleUtil.getInstance().sendWorkModel("39EF1436A98E");

            }
        });

    }


    public void setDate(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
        ll_content.removeAllViews();
        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
            Log.e("====", "i--" + i + "  getName==" + searchBlueDeviceBeanList.get(i).getBluetoothDevice().getName() + " Address==" + searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress());
            TextView textView = new TextView(this);
            textView.setText("i--" + i + "  getName==" + searchBlueDeviceBeanList.get(i).getBluetoothDevice().getName() + " Address==" + searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress());
            final SearchBlueDeviceBean deviceDateBean = searchBlueDeviceBeanList.get(i);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if(deviceDateBean.getBluetoothDevice().getAddress().endsWith("4D4C00000067")){
//                        clickSmartBlue();
//                        return;
//                    }
//                    if (deviceDateBean.isRegist()) {
//                        Log.e("====", " 开梯： getName==" + deviceDateBean.getSearchResult().getName() + " Address==" + deviceDateBean.getSearchResult().getAddress());
//                        JiBoUtils.getInstance(JIBoActivity.this).openDevice(deviceDateBean, openLift());
//                    } else {
//                        Log.e("====", " 注冊： getName==" + deviceDateBean.getSearchResult().getName() + " Address==" + deviceDateBean.getSearchResult().getAddress());
//                        JiBoUtils.getInstance(JIBoActivity.this).register(deviceDateBean, getDataArray(), openLift());
//                    }
                }
            });
            ll_content.addView(textView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            layoutParams.setMargins(0, 10, 0, 10);
            textView.setLayoutParams(layoutParams);
            textView.requestLayout();
        }
    }


}
