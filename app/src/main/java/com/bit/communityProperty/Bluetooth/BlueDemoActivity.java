package com.bit.communityProperty.Bluetooth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.interfcace.OnSearchBlueDeviceListener;
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
