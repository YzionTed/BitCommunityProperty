package com.bit.communityProperty.activity.elevatorcontrol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.Bluetooth.BluetoothApplication;
import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.jinbo.JiBoUtils;
import com.bit.communityProperty.Bluetooth.util.BluetoothNetUtils;
import com.bit.communityProperty.Bluetooth.util.BluetoothUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.CardListBean;
import com.bit.communityProperty.bean.StoreElevatorListBeans;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.BlueToothUtil;
import com.bit.communityProperty.utils.CustomDialog;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.LiteOrmUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.CircleProgressBar;
import com.inuker.bluetooth.library.base.Conf;
import com.inuker.bluetooth.library.base.Register;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import me.leefeng.promptlibrary.PromptDialog;

public class ElevatorControlActivity extends BaseActivity implements BlueToothUtil.OnCharacteristicListener, BlueToothUtil.BTUtilListener {

    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_right_action_bar)
    TextView btnRightActionBar;
    @BindView(R.id.iv_right_action_bar)
    ImageView ivRightActionBar;
    @BindView(R.id.pb_loaing_action_bar)
    ProgressBar pbLoaingActionBar;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_open)
    ImageView ivOpen;
    @BindView(R.id.loading_view)
    CircleProgressBar loadingView;

    private String[] blueAddressIds;
    private String haiKangOpenKeyNo;//自己公司电梯的开梯mac

    private PromptDialog promptDialog;
    private String Tag = "ElevatorControlActivity";
    ElevatorListBean doorJinBoBean;
    private BluetoothNetUtils bluetoothNetUtils;
    ElevatorListBean openDoorBean;

    boolean isYaoyiYao = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_elevator_control;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("蓝牙梯禁");

        BlueToothUtil.getInstance().setContext(this);
        BlueToothUtil.getInstance().setBTUtilListener(this);
        BlueToothUtil.getInstance().setOnCharacteristicListener(this);


        promptDialog = new PromptDialog(this);
        doorJinBoBean = null;
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof ElevatorListBean) {
                    doorJinBoBean = ((ElevatorListBean) o);
                    tv_name.setText(doorJinBoBean.getName());
                }
            }
        });

        if (bluetoothNetUtils == null) {
            bluetoothNetUtils = new BluetoothNetUtils(this);
        }

        initListener();
    }

    private void initListener() {

        BlueToothUtil.getInstance().setOnBluetoothStateCallBack(new BlueToothUtil.OnBluetoothStateCallBack() {
            @Override
            public void OnBluetoothState(final String state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_name.setText(state);
                    }
                });
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.tv_change, R.id.iv_open})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_change:
                Intent intent = new Intent(this, ChangeElevatorActivity.class);
                if (doorJinBoBean != null) {
                    intent.putExtra("doorJinBoBean", doorJinBoBean);
                }
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_open:
                tv_name.setText("开始开梯");
                loadingView.start();

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        if (doorJinBoBean == null || doorJinBoBean.isFirst()) {
                            MyApplication.getInstance().getBlueToothApp().scanBluetoothDevice(2000, new BluetoothApplication.CallBack() {
                                @Override
                                public void onCall(ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
                                    getDevice(searchBlueDeviceBeanList, 1);
                                }
                            });
                        } else if (doorJinBoBean.getMacType() == 1 || doorJinBoBean.getMacType() == 2) {
                            openDoorBean = doorJinBoBean;
                            openElevator(doorJinBoBean.getMacAddress(), 1);
                        }
                    }
                }.start();
                break;
        }

    }

    /**
     * 判断获取的设备是否是米粒蓝牙
     *
     * @param searchBlueDeviceBeanList fromType 1:点击 2：摇一摇
     */
    private void getDevice(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, final int fromType) {

        if (MyApplication.getInstance().getBlueToothApp().isLocationEnalbe()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("请打开您的定位权限！");
                    if (fromType == 1) {
                        isYaoyiYao = true;
                    }
                    loadingView.stop();
                }
            });
            return;
        }
        if (searchBlueDeviceBeanList.size() < 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShort("没有找到蓝牙设备");
                    loadingView.stop();
                    if (fromType == 1) {
                        isYaoyiYao = true;
                    }
                }
            });

            return;
        }
        StoreElevatorListBeans bletoothElevateDate = bluetoothNetUtils.getBletoothElevateDate();
        if (bletoothElevateDate != null) {
            if (bletoothElevateDate.getElevatorListBeans() != null) {
                if (bletoothElevateDate.getElevatorListBeans().size() > 0) {//缓存不为空时
                    final ElevatorListBean doorJinBoBean = BluetoothUtils.getMaxElevatorRsic(searchBlueDeviceBeanList, bletoothElevateDate.getElevatorListBeans());
                    if (doorJinBoBean != null) {//匹配最强信号
                        openDoorBean = doorJinBoBean;
                        openElevator(doorJinBoBean.getMacAddress(), fromType);
                    } else {//如果搜索设备的数组个数为0,则选择蓝牙信号最强的1个蓝牙设备Mac地址和虚
                        if (searchBlueDeviceBeanList.size() > 0) {
                            CardListBean.RecordsBean cardListBean = LiteOrmUtil.getInstance().queryById(bluetoothNetUtils.user_id, bluetoothNetUtils.communityId);
                            if (cardListBean == null) {
                                Log.e(Tag, "获取极光推动的缓存为空");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showShort("没找到您可以开的电梯");
                                        loadingView.stop();
                                    }
                                });
                                return;
                            }
                            isSuccess = false;
                            openElevatorByAuto(searchBlueDeviceBeanList, 0, fromType);
                        }
                    }
                } else {
                    getElevateDate(searchBlueDeviceBeanList, fromType);
                }
            } else {
                getElevateDate(searchBlueDeviceBeanList, fromType);
            }
        } else {
            getElevateDate(searchBlueDeviceBeanList, fromType);
        }

    }


    /**
     * 获取网络的数据
     *
     * @param searchBlueDeviceBeanList fromType1.点击 2，摇一摇
     */
    private void getElevateDate(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, final int fromType) {
        bluetoothNetUtils.getBluetoothElevatorDate(1, new BluetoothNetUtils.OnBlutoothElevatorCallBackListener() {
            @Override
            public void OnCallBack(int state, StoreElevatorListBeans storeDoorMILiBeanList) {
                if (state == 1) {
                    final ElevatorListBean doorJinBoBean = BluetoothUtils.getMaxElevatorRsic(searchBlueDeviceBeanList, storeDoorMILiBeanList.getElevatorListBeans());
                    if (doorJinBoBean != null) {
                        openDoorBean = doorJinBoBean;
                        openElevator(doorJinBoBean.getMacAddress(), fromType);
                    } else {
                        CardListBean.RecordsBean cardListBean = LiteOrmUtil.getInstance().queryById(bluetoothNetUtils.user_id, bluetoothNetUtils.communityId);
                        if (cardListBean != null) {
                            Log.e(Tag, "获取极光推动的缓存为空");
                            ToastUtil.showShort("没找到您可以开的电梯");
                            loadingView.stop();
                            return;
                        }
                        isSuccess = false;
                        openElevatorByAuto(searchBlueDeviceBeanList, 0, fromType);
                    }
                } else if (state == 2) {
                    tv_name.setText("开梯失败,重新开梯");
                    loadingView.stop();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 10) {
                doorJinBoBean = (ElevatorListBean) data.getSerializableExtra("elevatorListBean");
                tv_name.setText(doorJinBoBean.getName());
            }
        }

    }

    /**
     * 打开电梯
     */
    private void openElevator(final String macAddress, final int fromType) {

        if (openDoorBean != null) {
            if (openDoorBean.getMacType() == 1) {
                Log.e(Tag, "匹配金箔：" + openDoorBean.getMacType() + "  openDoorBean.getMacAddress() =" + openDoorBean.getMacAddress());
                JiBoUtils.getInstance(this).openDevice(macAddress, openLift(openDoorBean), new JiBoUtils.OnOpenLiftCallBackListenter() {
                    @Override
                    public void OpenLiftCallBackListenter(int backState, String msg) {
                        tv_name.setText(openDoorBean.getName() + msg);
                        if (backState == 1) {//成功
                            succssAnimation();
                            if (fromType == 1) {
                                isYaoyiYao = true;
                            }
                            loadingView.stop();
                        } else {//失败
                            if (fromType == 1) {
                                isYaoyiYao = true;
                            }
                            loadingView.stop();

                        }
                    }
                });
            } else if (openDoorBean.getMacType() == 2) {
                Log.e(Tag, "匹配海康：" + openDoorBean.getMacType() + "  doorJinBoBean.getMacAddress() =" + openDoorBean.getMacAddress());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        haiKangOpenKeyNo = openDoorBean.getKeyNo();
                        BlueToothUtil.getInstance().connectLeDevice(openDoorBean.getMacAddress());
                    }
                });
            } else {
                Log.e(Tag, "openDoorBean.getMacType()==" + openDoorBean.getMacType() + "  openDoorBean.getMacAddress() =" + openDoorBean.getMacAddress());
            }
        }
    }

    /**
     * 打开电梯
     * 5秒自动连接开梯
     */
    boolean isSuccess = false;//最近的两个蓝牙 进行匹配
    // 初始化定时器
    Timer timer;

    private void openElevatorByAuto(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList, int postion, final int fromType) {
        if (searchBlueDeviceBeanList.size() == 0) {
            Log.e(Tag, "自动匹配蓝牙 searchBlueDeviceBeanList.size");
            return;
        }
        final String macAddress = searchBlueDeviceBeanList.get(postion).getBluetoothDevice().getAddress();

        Log.e(Tag, "自动匹配蓝牙：" + macAddress);
        if (openLift().size() == 0) {
            ToastUtil.showShort("没有找到您可以开的电梯");
            loadingView.stop();
            return;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isSuccess) {
                    if (searchBlueDeviceBeanList.size() > 1) {
                        isSuccess = false;
                        openElevatorByAuto(searchBlueDeviceBeanList, 1, fromType);
                    }
                }
                if (timer != null) {
                    timer.cancel();
                    // 一定设置为null，否则定时器不会被回收
                    timer = null;
                }
                Log.e("lzp", "timer excute");
            }
        }, 3000);

        JiBoUtils.getInstance(this).openDevice(macAddress, openLift(), new JiBoUtils.OnOpenLiftCallBackListenter() {
            @Override
            public void OpenLiftCallBackListenter(int backState, String msg) {
                tv_name.setText(msg);
                if (backState == 1) {//成功
                    isSuccess = true;
                    succssAnimation();
                    if (fromType == 1) {
                        isYaoyiYao = true;
                    }
                    loadingView.stop();
                } else {//失败
                    if (fromType == 1) {
                        isYaoyiYao = true;
                    }
                    loadingView.stop();
                }
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bluetoothNetUtils.communityId != null && bluetoothNetUtils.user_id != null)
                    ;
                CardListBean.RecordsBean cardListBean = LiteOrmUtil.getInstance().queryById(bluetoothNetUtils.user_id, bluetoothNetUtils.communityId);
                if (cardListBean != null) {
                    haiKangOpenKeyNo = cardListBean.getKeyNo();
                    BlueToothUtil.getInstance().connectLeDevice(macAddress);
                } else {
                    loadingView.stop();
                    tv_name.setText("没找您可以开的电梯");
                }

            }
        });
    }


    // 组织开梯数据
    public List<Register> openLift(ElevatorListBean doorJinBoBean) {
        Register reg = new Register();
        if (doorJinBoBean.getKeyNo().length() == 12) {
            String keyNo = doorJinBoBean.getKeyNo();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < keyNo.length() / 2; i++) {
                String a = keyNo.substring(2 * i, 2 * i + 2);
                stringBuffer.append(a + ":");
            }
            String stringBuffer1 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
            Log.e(" reg.PhoneMac", " reg.PhoneMac==" + stringBuffer1);
            // reg.PhoneMac = "12:34:56:12:34:57";
            reg.PhoneMac = stringBuffer1;
        } else {
            reg.PhoneMac = doorJinBoBean.getKeyNo();
        }

        reg.type = Conf.STATE_DATA_LADDER;
        List<Register> arry = new ArrayList<Register>();
        arry.add(reg);
        return arry;
    }

    // 组织开梯数据
    public List<Register> openLift() {
        Register reg = new Register();
        StoreElevatorListBeans bletoothElevateDate = bluetoothNetUtils.getBletoothElevateDate();
        String keyNo = "";
        if (bletoothElevateDate != null) {
            if (bletoothElevateDate.getElevatorListBeans() != null) {
                if (bletoothElevateDate.getElevatorListBeans().get(0).isFirst()) {
                    if (bletoothElevateDate.getElevatorListBeans().get(1).getKeyNo() != null) {
                        keyNo = bletoothElevateDate.getElevatorListBeans().get(1).getKeyNo();
                    }
                } else {
                    if (bletoothElevateDate.getElevatorListBeans().get(0).getKeyNo() != null) {
                        keyNo = bletoothElevateDate.getElevatorListBeans().get(0).getKeyNo();
                    }
                }

            } else {
                CardListBean.RecordsBean cardListBean = LiteOrmUtil.getInstance().queryById(bluetoothNetUtils.user_id, bluetoothNetUtils.communityId);
                if (cardListBean.getKeyNo() != null) {
                    keyNo = cardListBean.getKeyNo();
                }
            }
        } else {
            CardListBean.RecordsBean cardListBean = LiteOrmUtil.getInstance().queryById(bluetoothNetUtils.user_id, bluetoothNetUtils.communityId);
            if (cardListBean.getKeyNo() != null) {
                keyNo = cardListBean.getKeyNo();
            }
        }
        if (keyNo != null && keyNo.trim().length() > 0) {
            if (keyNo.trim().length() == 12) {

                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < keyNo.length() / 2; i++) {
                    String a = keyNo.substring(2 * i, 2 * i + 2);
                    stringBuffer.append(a + ":");
                }
                String stringBuffer1 = stringBuffer.toString().substring(0, stringBuffer.length() - 1);
                Log.e(" reg.PhoneMac", " reg.PhoneMac==" + stringBuffer1);
                // reg.PhoneMac = "12:34:56:12:34:57";
                reg.PhoneMac = stringBuffer1;
            } else {
                reg.PhoneMac = keyNo;
            }
        }
        reg.type = Conf.STATE_DATA_LADDER;
        List<Register> arry = new ArrayList<Register>();
        arry.add(reg);
        return arry;
    }

    public void succssAnimation() {
        Log.e(Tag, "开梯成功");
        tv_name.setText("开梯成功！");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CustomDialog customDialog = DialogUtil.showFrameAnimDialog(mContext, R.drawable.anim_open_door);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (customDialog != null && customDialog.isShowing()) {
                            customDialog.dismiss();
                            loadingView.stop();
                        }
                    }
                }, 3000);

            }
        });
    }

    @Override
    public void onLeScanStart() {

    }

    @Override
    public void onLeScanStop() {

    }

    @Override
    public void onLeScanDevice(BluetoothDevice device) {

    }

    private boolean connected = false;

    @Override
    public void onLeScanDevices(List<BluetoothDevice> listDevice) {

    }

    @Override
    public void onConnected(BluetoothDevice mCurDevice) {
        connected = true;
    }

    @Override
    public void onDisConnected(BluetoothDevice mCurDevice) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                isYaoyiYao = true;
                loadingView.stop();
                connected = false;
            }
        });

    }

    @Override
    public void onServicesDiscovered() {
        isYaoyiYao = true;
        loadingView.stop();
    }


    @Override
    public void onNotificationSetted() {
        BlueToothUtil.getInstance().sendOpen(haiKangOpenKeyNo);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        BlueToothUtil.getInstance().handleResultCallBack(characteristic, new BlueToothUtil.OnCallBackListener() {
            @Override
            public void OnCallBack(final int state) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingView.stop();
                        isYaoyiYao = true;
                        if (state == 1) {
                            isSuccess = true;
                            tv_name.setText("开梯成功");
                            succssAnimation();
                        } else {
                            ToastUtil.showShort("开梯失败");
                        }
                    }
                });

            }
        });
    }


    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    succssAnimation();
                }
            });
            //  Log.e(Tag,"写入数据成功");
        }
    }

}
