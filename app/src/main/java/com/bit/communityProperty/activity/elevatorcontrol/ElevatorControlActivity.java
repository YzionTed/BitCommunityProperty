package com.bit.communityProperty.activity.elevatorcontrol;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.Bluetooth.bean.SearchBlueDeviceBean;
import com.bit.communityProperty.Bluetooth.jinbo.JiBoBean;
import com.bit.communityProperty.Bluetooth.jinbo.JiBoUtils;
import com.bit.communityProperty.Bluetooth.util.BluetoothUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.CustomDialog;
import com.bit.communityProperty.utils.DialogUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.view.CircleProgressBar;
import com.inuker.bluetooth.library.base.Conf;
import com.inuker.bluetooth.library.base.Register;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.leefeng.promptlibrary.PromptDialog;

public class ElevatorControlActivity extends BaseActivity {

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

    private PromptDialog promptDialog;
    private String Tag = "ElevatorControlActivity";
    ElevatorListBean doorJinBoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_elevator_control;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("蓝牙梯禁");

        promptDialog = new PromptDialog(this);
        doorJinBoBean = null;
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof ElevatorListBean) {
                    doorJinBoBean = ((ElevatorListBean) o);
                    tv_name.setText(doorJinBoBean.getElevatorNum() + doorJinBoBean.getName());
                }
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

                Intent intent = new Intent(ElevatorControlActivity.this, ChangeElevatorActivity.class);
                if (doorJinBoBean != null) {
                    intent.putExtra("doorJinBoBean", doorJinBoBean);
                }
                startActivity(intent);

                break;
            case R.id.iv_open:
                loadingView.start();
                if (doorJinBoBean == null || doorJinBoBean.isFirst()) {
                    MyApplication.getInstance().getBlueToothApp().scanBluetoothDevice(2000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList = MyApplication.getInstance().getBlueToothApp().getSearchBlueDeviceBeanList();
                            getJinBo(searchBlueDeviceBeanList);
                        }
                    }, 2000);
                } else {

                    JiBoUtils.getInstance(ElevatorControlActivity.this).openDevice(doorJinBoBean, openLift(), new JiBoUtils.OnOpenLiftCallBackListenter() {
                        @Override
                        public void OpenLiftCallBackListenter(int backState, String msg) {
                            tv_name.setText(doorJinBoBean.getElevatorNum() + doorJinBoBean.getName() + msg);
                            if (backState == 1) {//成功
                                loadingView.stop();
                                succssAnimation();
                            } else {//失败
                                loadingView.stop();
                            }
                        }
                    });
                }
                break;
        }

    }


    // 6、组织开梯数据
    public List<Register> openLift() {
        Register reg = new Register();
        reg.PhoneMac = JiBoUtils.getBtAddressViaReflection();
        reg.type = Conf.STATE_DATA_LADDER;
        List<Register> arry = new ArrayList<Register>();
        arry.add(reg);
        return arry;
    }

    /**
     * 判断获取的设备是否是米粒蓝牙
     *
     * @param searchBlueDeviceBeanList
     */
    private void getJinBo(final ArrayList<SearchBlueDeviceBean> searchBlueDeviceBeanList) {
        //  BluetoothUtils.getMiliBluetooth(searchBlueDeviceBeanList);

        if (MyApplication.getInstance().getBlueToothApp().isLocationEnalbe()) {
            ToastUtil.showShort("请打开您的定位权限！");
            loadingView.stop();
            return;
        }
        if (searchBlueDeviceBeanList.size() < 1) {
            ToastUtil.showShort("没有找到蓝牙设备");
            loadingView.stop();
            return;
        }
        String[] doorMacArr = new String[searchBlueDeviceBeanList.size()];

        for (int i = 0; i < searchBlueDeviceBeanList.size(); i++) {
            doorMacArr[i] = searchBlueDeviceBeanList.get(i).getBluetoothDevice().getAddress();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("macAddress", doorMacArr);
        map.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        RetrofitManage.getInstance().subscribe(Api.getInstance().elevatorList(map), new Observer<BaseEntity<List<ElevatorListBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BaseEntity<List<ElevatorListBean>> listBaseEntity) {
                if (listBaseEntity.isSuccess()) {
                    final ElevatorListBean doorJinBoBean = BluetoothUtils.getMaxElevatorRsic(searchBlueDeviceBeanList, listBaseEntity.getData());
                    if (doorJinBoBean != null) {
                        JiBoUtils.getInstance(ElevatorControlActivity.this).openDevice(doorJinBoBean, openLift(), new JiBoUtils.OnOpenLiftCallBackListenter() {
                            @Override
                            public void OpenLiftCallBackListenter(int backState, String msg) {
                                tv_name.setText(doorJinBoBean.getElevatorNum() + doorJinBoBean.getName() + msg);
                                if (backState == 1) {//成功
                                    succssAnimation();
                                    loadingView.stop();
                                } else {//失败
                                    loadingView.stop();
                                }
                            }
                        });
                    } else {

                        loadingView.stop();
                        ToastUtil.showShort("没有搜索到门的设备");
                        Log.e(Tag, "没有搜索到门的设备！");
                    }
                } else {
                    loadingView.stop();
                    if (listBaseEntity.getErrorCode().equals("9050001")) {
                        ToastUtil.showShort(listBaseEntity.getErrorMsg());
                    }
                    Log.e(Tag, "logindata.getErrorCode()" + listBaseEntity.getErrorMsg());
                }

            }

            @Override
            public void onError(Throwable e) {
                loadingView.stop();
            }

            @Override
            public void onComplete() {
                loadingView.stop();
            }
        });
    }

    public void succssAnimation() {
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

}
