package com.bit.communityProperty.activity.access.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.Bluetooth.BluetoothApplication;
import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.mili.MiLiState;
import com.bit.communityProperty.Bluetooth.util.BluetoothNetUtils;
import com.bit.communityProperty.Bluetooth.util.BlueUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.ChangeAccessActivity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.bean.StoreDoorMILiBeanList;
import com.bit.communityProperty.utils.CustomDialog;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.CircleProgressBar;
import com.smarthome.bleself.sdk.BluetoothApiAction;
import com.smarthome.bleself.sdk.IBluetoothApiInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 蓝牙门禁fragment
 * Created by kezhangzhao on 2018/1/25.
 */

public class BluetoothDoorFragment extends BaseFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_name)
    TextView tv_name;
    Unbinder unbinder;
    @BindView(R.id.iv_open)
    ImageView ivOpen;
    @BindView(R.id.loading_view)
    CircleProgressBar loadingView;

    private String Tag = "BluetoothDoorFragment";
    private PromptDialog sinInLogin;
    private DoorMILiBean doorMILiBean;
    private BluetoothNetUtils bluetoothNetUtils;
    private boolean isNeedShake = true;

    public static BluetoothDoorFragment newInstance(int sectionNumber) {
        BluetoothDoorFragment fragment = new BluetoothDoorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bluetooth_door;
    }

    @Override
    protected void initViewAndData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        MyApplication.getInstance().getBlueToothApp().checkLocationEnable(getActivity());
        MyApplication.getInstance().getBlueToothApp().checkLocationEnable(getActivity());
        MyApplication.getInstance().getBlueToothApp().openBluetooth();
        bluetoothNetUtils = new BluetoothNetUtils(getActivity());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private boolean isNeedClickAble = true;

    @OnClick({R.id.iv_open, R.id.tv_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_open:
                if (isNeedClickAble) {
                    loadingView.start();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (doorMILiBean == null || doorMILiBean.isFirst()) {
                                MyApplication.getInstance().getBlueToothApp().scanBluetoothDevice(2000, new BluetoothApplication.CallBack() {
                                    @Override
                                    public void onCall(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
                                        getMenJinMiLi(searchBlueDeviceBeanList, 1);
                                    }
                                });
                            } else {
                                clickBlueMiLi(doorMILiBean.getMac(), doorMILiBean.getPin(), 1);
                            }
                        }
                    }.start();
                }
                break;
            case R.id.tv_change:
                Intent intent = new Intent(mActivity, ChangeAccessActivity.class);
                if (doorMILiBean != null) {
                    intent.putExtra("doorMILiBean", doorMILiBean);
                }
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 10) {
                doorMILiBean = (DoorMILiBean) data.getSerializableExtra("doorMILiBean");
                tv_name.setText(doorMILiBean.getName());
            }
        }
    }

    /**
     * 判断获取的设备是否是米粒蓝牙
     *
     * @param searchBlueDeviceBeanList
     * @param type                     1:点击 2：摇一摇
     */
    private void getMenJinMiLi(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, final int type) {

        if (MyApplication.getInstance().getBlueToothApp().isLocationEnalbe()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("请打开您的定位权限！");
                    loadingView.stop();
                    isNeedClickAble = true;
                    if (type == 2) {
                        isNeedShake = true;
                    }
                }
            });
            return;
        }
        if (searchBlueDeviceBeanList.size() < 1) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("没有找到蓝牙设备");
                    loadingView.stop();
                    isNeedClickAble = true;
                    if (type == 2) {
                        isNeedShake = true;
                    }
                }
            });
            return;
        }

        String[] doorMacArr = new String[searchBlueDeviceBeanList.size()];
//        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
//            String address = searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress();
//            StringBuffer sb = new StringBuffer();
//            for (int j = 0; j < address.length(); j++) {
//                char c = address.charAt(j);
//                if (!(c + "").equals(":")) {
//                    sb.append(c);
//                }
//            }
//            doorMacArr[i] = sb.toString();
//            Log.e(Tag, " doorMacArr[i]=" + doorMacArr[i]);
//        }

        StoreDoorMILiBeanList bletoothDoorDate = bluetoothNetUtils.getBletoothDoorDate();
        if (bletoothDoorDate != null) {
            if (bletoothDoorDate.getDoorMILiBeans() != null) {
                if (bletoothDoorDate.getDoorMILiBeans().size() > 0) {
                    DoorMILiBean doorMILiBean = BlueUtils.getMaxRsic(searchBlueDeviceBeanList, bletoothDoorDate.getDoorMILiBeans());
                    if (doorMILiBean != null) {
                        clickBlueMiLi(doorMILiBean.getMac(), doorMILiBean.getPin(), type);
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (type == 2) {
                                    isNeedShake = true;
                                }
                                loadingView.stop();
                                isNeedClickAble = true;
                                ToastUtil.showShort("没有搜索到门的设备");
                                Log.e(Tag, "没有搜索到门的设备！");
                            }
                        });
                        // getMiLiNetDate(searchBlueDeviceBeanList, type, doorMacArr);
                    }
                } else {
                    getMiLiNetDate(searchBlueDeviceBeanList, type, doorMacArr);
                }
            } else {
                getMiLiNetDate(searchBlueDeviceBeanList, type, doorMacArr);
            }

        } else {
            getMiLiNetDate(searchBlueDeviceBeanList, type, doorMacArr);
        }
    }

    /**
     * 没有缓存时从缓存获取数据
     *
     * @param searchBlueDeviceBeanList
     * @param type
     * @param doorMacArr
     */
    private void getMiLiNetDate(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, final int type, String[] doorMacArr) {
        bluetoothNetUtils.getMiLiNetDate(doorMacArr, 1, new BluetoothNetUtils.OnBlutoothDoorCallBackListener() {
            @Override
            public void OnCallBack(int state, StoreDoorMILiBeanList storeDoorMILiBeanList) {
                if (state == 1) {
                    if (storeDoorMILiBeanList != null) {
                        if (storeDoorMILiBeanList.getDoorMILiBeans().size() > 0) {
                            DoorMILiBean doorMILiBean = BlueUtils.getMaxRsic(searchBlueDeviceBeanList, storeDoorMILiBeanList.getDoorMILiBeans());
                            if (doorMILiBean != null) {
                                clickBlueMiLi(doorMILiBean.getMac(), doorMILiBean.getPin(), type);
                            } else {
                                if (type == 2) {
                                    isNeedShake = true;
                                }
                                loadingView.stop();
                                isNeedClickAble = true;
                                ToastUtil.showShort("没有搜索到门的设备");
                                Log.e(Tag, "没有搜索到门的设备！");
                            }
                        } else {
                            if (type == 2) {
                                isNeedShake = true;
                            }
                            loadingView.stop();
                            isNeedClickAble = true;
                            ToastUtil.showShort("没有搜索到门的设备");
                            Log.e(Tag, "没有有用的蓝牙设备");
                        }
                    } else {
                        if (type == 2) {
                            isNeedShake = true;
                        }
                        ToastUtil.showShort("没有搜索到门的设备");
                        loadingView.stop();
                        isNeedClickAble = true;
                        Log.e(Tag, "没有有用的蓝牙设备");
                    }
                } else if (state == 2) {
                    loadingView.stop();
                    isNeedClickAble = true;
                    if (type == 2) {
                        isNeedShake = true;
                    }
                }
            }
        });
    }

    /**
     * 米粒的开门
     */
    private void clickBlueMiLi(final String destMac, final String destPin, final int type) {
        Log.e(Tag, "打开米粒门 mac=" + destMac + " destPin=" + destPin);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //F0:C7:7F:9D:66:37
                BluetoothApiAction.bluetoothActionUnlock(destMac, destPin, getActivity(), new IBluetoothApiInterface.IBluetoothApiCallback<Object>() {

                    @Override
                    public void onFailure(final String arg0) {
                        Log.e(Tag, "" + MiLiState.getCodeDesc(arg0));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingView.stop();
                                isNeedClickAble = true;
                                if (type == 2) {
                                    isNeedShake = true;
                                }
                                Toast.makeText(getActivity(), "开门失败" + MiLiState.getCodeDesc(arg0), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Object arg0) {
                        if (type == 2) {
                            isNeedShake = true;
                        }
                        onsuccessAnimation();
                    }
                });
            }
        });

    }



    private void onsuccessAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CustomDialog customDialog = DialogUtil.showFrameAnimDialog(mContext, R.drawable.anim_open_door);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                            if (loadingView != null) {
                                loadingView.stop();
                                isNeedClickAble = true;
                            }
                        }
                    }
                }, 3000);

            }
        });
    }


}
