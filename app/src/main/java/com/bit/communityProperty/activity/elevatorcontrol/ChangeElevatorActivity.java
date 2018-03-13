package com.bit.communityProperty.activity.elevatorcontrol;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.Bluetooth.util.BluetoothNetUtils;
import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.adapter.ChangeAccessAdapter;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.StoreElevatorListBeans;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.PreferenceUtils;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.CustomExpandListview;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ChangeElevatorActivity extends BaseActivity {

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
    @BindView(R.id.listView)
    CustomExpandListview listview;
    @BindView(R.id.lv_elevator)
    ListView lvElevator;

    private ChangeAccessAdapter myAdapter;

    private String[] parentSource = {"分类1", "分类2"};
    private ArrayList<String> parent = new ArrayList<>();
    private Map<String, ArrayList<String>> datas = new HashMap<>();

    private String[] blueAddressIds;
    private CommonAdapter commonAdapter;
    private ElevatorListBean doorJinBoBean;
    private BluetoothNetUtils bluetoothNetUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_elevator;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("切换电梯");
        doorJinBoBean = (ElevatorListBean) getIntent().getSerializableExtra("doorJinBoBean");

        blueAddressIds = getIntent().getStringArrayExtra("ids");
        bluetoothNetUtils = new BluetoothNetUtils();
        initListView();
        getCashDate();
        getData();
    }

    /**
     * 展示缓存数据
     */
    private void getCashDate() {
        StoreElevatorListBeans bletoothElevateDate = bluetoothNetUtils.getBletoothElevateDate();
        if (bletoothElevateDate != null) {

            ElevatorListBean elevatorListBean = new ElevatorListBean();
            elevatorListBean.setFirst(true);
            elevatorListBean.setName("一键开梯");
            if (bletoothElevateDate.getElevatorListBeans() != null) {
                if (bletoothElevateDate.getElevatorListBeans().size() > 0) {
                    if (!bletoothElevateDate.getElevatorListBeans().get(0).isFirst()) {
                        bletoothElevateDate.getElevatorListBeans().add(0, elevatorListBean);
                    }
                } else {
                    bletoothElevateDate.getElevatorListBeans().add(0, elevatorListBean);
                }
            } else {
                List<ElevatorListBean> doorMILiBeans1 = new ArrayList<>();
                doorMILiBeans1.add(elevatorListBean);
                bletoothElevateDate.setElevatorListBeans(doorMILiBeans1);
            }
        } else {
            bletoothElevateDate = new StoreElevatorListBeans();
            List<ElevatorListBean> doorMILiBeans1 = new ArrayList<>();
            ElevatorListBean elevatorListBean = new ElevatorListBean();
            elevatorListBean.setFirst(true);
            elevatorListBean.setName("一键开梯");
            doorMILiBeans1.add(elevatorListBean);
            bletoothElevateDate.setElevatorListBeans(doorMILiBeans1);
        }

        commonAdapter.setDatas(bletoothElevateDate.getElevatorListBeans());
    }


    private void initListView() {
        commonAdapter = new CommonAdapter<ElevatorListBean>(this, R.layout.item_access_child) {
            @Override
            public void convert(ViewHolder holder, final ElevatorListBean elevatorListBean, final int position, View convertView) {

                if (ChangeElevatorActivity.this.doorJinBoBean != null) {
                    if (ChangeElevatorActivity.this.doorJinBoBean.isFirstChecked()) {
                        if (position == 0) {
                            ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                        }else {
                            ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
                        }
                    } else {
                        if (ChangeElevatorActivity.this.doorJinBoBean.getMacAddress() != null) {
                            if (ChangeElevatorActivity.this.doorJinBoBean.getMacAddress().equals(elevatorListBean.getMacAddress())) {
                                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                            }else {
                                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
                            }
                        }else {
                            ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);

                        }
                    }
                } else {
                    if (position == 0) {
                        ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                    }
                }

                if (elevatorListBean.getName() != null) {
                    holder.setText(R.id.tv_item, elevatorListBean.getName());
                }else {
                    holder.setText(R.id.tv_item, "");
                }
                holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == 0) {
                            elevatorListBean.setFirstChecked(true);
                        } else {
                            elevatorListBean.setFirstChecked(false);
                        }
                        Intent intent = new Intent();
                        intent.putExtra("elevatorListBean", elevatorListBean);
                        setResult(10, intent);
                        finish();
                    }
                });
            }
        };
    }

    private void getData() {

        bluetoothNetUtils.getBluetoothElevatorDate(1, new BluetoothNetUtils.OnBlutoothElevatorCallBackListener() {
            @Override
            public void OnCallBack(int state, StoreElevatorListBeans storeElevatorListBeans) {

                if (state == 1) {

                    if (storeElevatorListBeans != null) {
                        if (storeElevatorListBeans.getElevatorListBeans().size() > 0) {
                            ElevatorListBean elevatorListBean = new ElevatorListBean();
                            elevatorListBean.setFirst(true);
                            elevatorListBean.setName("一键开梯");
                            storeElevatorListBeans.getElevatorListBeans().add(0, elevatorListBean);
                            PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, bluetoothNetUtils.user_id +bluetoothNetUtils.communityId + PreferenceConst.BLUETOOTHELEVATOR, new Gson().toJson(storeElevatorListBeans));
                            commonAdapter.setDatas(storeElevatorListBeans.getElevatorListBeans());
                        } else {
                            ToastUtil.showShort("没有找到您可以开的电梯");
                        }
                    } else {
                        ToastUtil.showShort("没有找到您可以开的电梯");
                    }
                } else if (state == 2) {

                }
            }
        });

    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

}
