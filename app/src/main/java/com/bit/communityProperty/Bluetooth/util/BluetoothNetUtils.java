package com.bit.communityProperty.Bluetooth.util;

/**
 * Created by Dell on 2018/3/8.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.bean.StoreDoorMILiBeanList;
import com.bit.communityProperty.bean.StoreElevatorListBeans;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.PreferenceUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 米粒蓝牙保存网络数据
 */
public class BluetoothNetUtils {


    private static final String TAG = "BluetoothNetUtils";
    public final String user_id;
    public String communityId;

    public BluetoothNetUtils() {
        user_id = (String) SPUtil.get(MyApplication.getInstance(), AppConfig.id, "");
        communityId = "5a82adf3b06c97e0cd6c0f3d";
    }

    /**
     * type==1 代表的是需要谈提示 type==2不需要提示
     *
     * @param doorMacArr
     * @param type
     * @param onBlutoothDoorCallBackListener
     */
    public void getMiLiNetDate(final String[] doorMacArr, final int type, final OnBlutoothDoorCallBackListener onBlutoothDoorCallBackListener) {

        if (!isNetworkAvailable(MyApplication.getInstance())) {
            if (type == 1) {
                ToastUtil.showShort("连接异常，请检查网络");
            }
            return;
        }
        Map<String, Object> getDoorAuth = new HashMap<>();
        getDoorAuth.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorAuthList(getDoorAuth), new Observer<BaseEntity<List<DoorMILiBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<DoorMILiBean>> logindata) {
                if (logindata.getErrorCode().equals("0")) {

                    if (logindata != null) {

                        if (logindata.getData().size() > 0) {
                            StoreDoorMILiBeanList storeDoorMILiBeanList = new StoreDoorMILiBeanList();
                            storeDoorMILiBeanList.setDoorMILiBeans(logindata.getData());
                            storeDoorMILiBeanList.setStoreTime(new Date().getTime());
                            if (doorMacArr == null) {
                                PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, user_id + communityId + PreferenceConst.MILIDOORMAC, new Gson().toJson(storeDoorMILiBeanList));
                            }
                            if (onBlutoothDoorCallBackListener != null) {
                                onBlutoothDoorCallBackListener.OnCallBack(1, storeDoorMILiBeanList);
                            }
                        } else {
                            if (type == 1) {
                                if (onBlutoothDoorCallBackListener != null) {
                                    onBlutoothDoorCallBackListener.OnCallBack(2, null);
                                }
                                ToastUtil.showShort("您还没有可以开锁的设备");
                            }
                        }
                    } else {
                        if (type == 1) {
                            if (onBlutoothDoorCallBackListener != null) {
                                onBlutoothDoorCallBackListener.OnCallBack(2, null);
                            }
                            ToastUtil.showShort("您还没有可以开锁的设备");
                        }
                    }
                } else {
                    if (type == 1) {
                        if (onBlutoothDoorCallBackListener != null) {
                            onBlutoothDoorCallBackListener.OnCallBack(2, null);
                        }
                        ToastUtil.showShort(logindata.getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (onBlutoothDoorCallBackListener != null) {
                    if (type == 1) {
                        ToastUtil.showShort(e.getMessage());
                    }
                    onBlutoothDoorCallBackListener.OnCallBack(2, null);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 获取电梯的蓝牙数据
     * <p>
     * type==1 代表的是需要谈提示 type==2不需要提示
     */
    public void getBluetoothElevatorDate(final int type, final OnBlutoothElevatorCallBackListener onBlutoothDoorCallBackListener) {

        Map<String, Object> map = new HashMap<>();

        map.put("communityId", communityId);
        map.put("userId", user_id);

        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorGetAuthsList(map), new Observer<BaseEntity<List<ElevatorListBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BaseEntity<List<ElevatorListBean>> listBaseEntity) {
                if (listBaseEntity.isSuccess()) {
                    if (listBaseEntity.getData() != null) {
                        if (listBaseEntity.getData().size() > 0) {
                            StoreElevatorListBeans storeElevatorListBeans = new StoreElevatorListBeans();
                            storeElevatorListBeans.setElevatorListBeans(listBaseEntity.getData());
                            storeElevatorListBeans.setStoreTime(new Date().getTime());

                            PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, user_id + communityId + PreferenceConst.BLUETOOTHELEVATOR, new Gson().toJson(storeElevatorListBeans));

                            if (onBlutoothDoorCallBackListener != null) {
                                onBlutoothDoorCallBackListener.OnCallBack(1, storeElevatorListBeans);
                            }
                        } else {
                            if (type == 1) {
                                if (onBlutoothDoorCallBackListener != null) {
                                    onBlutoothDoorCallBackListener.OnCallBack(2, null);
                                }
                                ToastUtil.showShort("没有找到您可以开的电梯");
                            }
                        }
                    }
                    if (type == 1) {
                        if (onBlutoothDoorCallBackListener != null) {
                            onBlutoothDoorCallBackListener.OnCallBack(2, null);
                        }
                        ToastUtil.showShort("没有找到您可以开的电梯");
                    }
                } else {
                    if (type == 1) {
                        if (onBlutoothDoorCallBackListener != null) {
                            onBlutoothDoorCallBackListener.OnCallBack(2, null);
                        }
                        ToastUtil.showShort(listBaseEntity.getErrorMsg());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (onBlutoothDoorCallBackListener != null) {
                    ToastUtil.showShort(e.getMessage());
                    onBlutoothDoorCallBackListener.OnCallBack(2, null);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }


    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }


    /**
     * 获取米粒门蓝牙的数据
     */
    public StoreDoorMILiBeanList getBletoothDoorDate() {
        String prefString = PreferenceUtils.getPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, user_id + communityId + PreferenceConst.MILIDOORMAC, "");
        StoreDoorMILiBeanList storeDoorMILiBeanList = null;
        try {
            if (prefString != null && prefString.trim().length() > 0) {
                storeDoorMILiBeanList = new Gson().fromJson(prefString, StoreDoorMILiBeanList.class);
            }
        } catch (Exception e) {
            Log.e(TAG, "StoreElevatorListBeans的Gson转换对象出问题");
        }
        return storeDoorMILiBeanList;
    }

    /**
     * 获取电梯蓝牙的数据
     */
    public StoreElevatorListBeans getBletoothElevateDate() {
        String prefString = PreferenceUtils.getPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, user_id + communityId + PreferenceConst.BLUETOOTHELEVATOR, "");
        StoreElevatorListBeans storeElevatorListBeans = null;
        try {
            if (prefString != null && prefString.trim().length() > 0) {
                storeElevatorListBeans = new Gson().fromJson(prefString, StoreElevatorListBeans.class);
            }
        } catch (Exception e) {
            Log.e(TAG, "StoreElevatorListBeans的Gson转换对象出问题");
        }
        return storeElevatorListBeans;
    }


    public interface OnBlutoothDoorCallBackListener {
        //state 1:表示成功 2：表示失败
        void OnCallBack(int state, StoreDoorMILiBeanList storeDoorMILiBeanList);
    }

    public interface OnBlutoothElevatorCallBackListener {
        //state 1:表示成功 2：表示失败
        void OnCallBack(int state, StoreElevatorListBeans storeDoorMILiBeanList);
    }


}
