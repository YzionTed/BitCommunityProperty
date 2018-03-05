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

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.mili.MiLiState;
import com.bit.communityProperty.Bluetooth.util.BluetoothUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.ChangeAccessActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CustomDialog;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.CircleProgressBar;
import com.smarthome.bleself.sdk.BluetoothApiAction;
import com.smarthome.bleself.sdk.IBluetoothApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
                if (!isNeedClickAble) {
                    return;
                }
                if (loadingView != null) {
                    loadingView.start();
                }
                isNeedClickAble = false;
                if (doorMILiBean == null || doorMILiBean.isFirst()) {
                    MyApplication.getInstance().getBlueToothApp().scanBluetoothDevice(2000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList = MyApplication.getInstance().getBlueToothApp().getSearchBlueDeviceBeanList();
                            getMenJinMiLi(searchBlueDeviceBeanList);
                        }
                    }, 2000);
                } else {
                    clickBlueMiLi(doorMILiBean.getMac(), doorMILiBean.getPin());
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
     */
    private void getMenJinMiLi(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
        //  BluetoothUtils.getMiliBluetooth(searchBlueDeviceBeanList);

        if (MyApplication.getInstance().getBlueToothApp().isLocationEnalbe()) {
            ToastUtil.showShort("请打开您的定位权限！");
            loadingView.stop();
            isNeedClickAble = true;
            return;
        }
        if (searchBlueDeviceBeanList.size() < 1) {
            ToastUtil.showShort("没有找到蓝牙设备");
            loadingView.stop();
            isNeedClickAble = true;
            return;
        }

        Map<String, Object> getDoorAuth = new HashMap<>();
        String[] doorMacArr = new String[searchBlueDeviceBeanList.size()];
        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
            String address = searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < address.length(); j++) {
                char c = address.charAt(j);
                if (!(c + "").equals(":")) {
                    sb.append(c);
                }
            }
            doorMacArr[i] = sb.toString();
            Log.e(Tag, " doorMacArr[i]=" + doorMacArr[i]);
        }
        getDoorAuth.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        getDoorAuth.put("mac", doorMacArr);

        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorAuthList(getDoorAuth), new Observer<BaseEntity<List<DoorMILiBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<DoorMILiBean>> logindata) {
                if (logindata.getErrorCode().equals("0")) {
                    DoorMILiBean doorMILiBean = BluetoothUtils.getMaxRsic(searchBlueDeviceBeanList, logindata.getData());
                    if (doorMILiBean != null) {
                        clickBlueMiLi(doorMILiBean.getMac(), doorMILiBean.getPin());
                    } else {
                        if (logindata.getData().size() > 0) {
                            Log.e(Tag, "搜索到的蓝牙您没有权限");
                            ToastUtil.showShort("搜索到的蓝牙您没有权限！");
                            loadingView.stop();
                            isNeedClickAble = true;
                        } else {
                            loadingView.stop();
                            isNeedClickAble = true;
                            ToastUtil.showShort("没有搜索到门的设备");
                            Log.e(Tag, "没有搜索到门的设备！");
                        }
                    }
                } else {
                    loadingView.stop();
                    isNeedClickAble = true;
//                    if (logindata.getErrorCode().equals("9050001")) {
//                        ToastUtil.showShort(logindata.getErrorMsg());
//                    }
                    ToastUtil.showShort(logindata.getErrorMsg());
                    Log.e(Tag, "logindata.getErrorCode()" + logindata.getErrorMsg());
                }
            }

            @Override
            public void onError(Throwable e) {
                loadingView.stop();
                isNeedClickAble = true;
                LogManager.printErrorLog("backinfo", "失败返回的数据：" + e.getMessage());
            }

            @Override
            public void onComplete() {
                if (loadingView != null) {
                    return;
                }
                loadingView.stop();
                isNeedClickAble = true;
            }
        });

    }


    /**
     * 米粒的开门
     */
    private void clickBlueMiLi(String destMac, String destPin) {
        Log.e(Tag, "打开米粒门 mac=" + destMac + " destPin=" + destPin);

        //F0:C7:7F:9D:66:37
        BluetoothApiAction.bluetoothActionUnlock(destMac, destPin, getActivity(), new IBluetoothApiInterface.IBluetoothApiCallback<Object>() {

            @Override
            public void onFailure(final String arg0) {
                if (loadingView != null) {
                    loadingView.stop();
                    isNeedClickAble = true;
                    Log.e(Tag, "" + MiLiState.getCodeDesc(arg0));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "开门失败" + arg0 + MiLiState.getCodeDesc(arg0), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            @Override
            public void onSuccess(Object arg0) {
                if (loadingView != null) {
                    return;
                }

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
        });
    }


}
