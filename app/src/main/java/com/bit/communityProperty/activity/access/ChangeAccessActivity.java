package com.bit.communityProperty.activity.access;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.bean.DoorMILiBeanList;
import com.bit.communityProperty.bean.StoreDoorMILiBeanList;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.PreferenceUtils;
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

public class ChangeAccessActivity extends BaseActivity {

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


    private ChangeAccessAdapter myAdapter;

    private String[] parentSource = {"分类1", "分类2", "分类3", "分类4"};
    private ArrayList<String> parent = new ArrayList<>();
    private Map<String, ArrayList<String>> datas = new HashMap<>();
    private String Tag = "ChangeAccessActivity";
    private ListView lv_list;
    private CommonAdapter commonAdapter;
    private DoorMILiBean doorMiLiBean;
    private BluetoothNetUtils bluetoothNetUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_access;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("切换门禁");
        bluetoothNetUtils = new BluetoothNetUtils(this);
        doorMiLiBean = (DoorMILiBean) getIntent().getSerializableExtra("doorMILiBean");
        setCustomExpandListview();

        lv_list = (ListView) findViewById(R.id.lv_list);

        commonAdapter = new CommonAdapter<DoorMILiBean>(this, R.layout.item_door_access) {
            @Override
            public void convert(final ViewHolder holder, final DoorMILiBean doorMILiBean, int position, View convertView) {

                itemDo(holder, doorMILiBean, position);
            }
        };
        lv_list.setAdapter(commonAdapter);

        setCashDate();

        getMenJinMiLi();
    }

    //放入缓存数据
    private void setCashDate() {

        StoreDoorMILiBeanList doorMILiBeans = bluetoothNetUtils.getBletoothDoorDate();
        if (doorMILiBeans != null) {
            DoorMILiBean doorMILiBean = new DoorMILiBean();
            doorMILiBean.setFirst(true);
            doorMILiBean.setName("一键开门");
            if (doorMILiBeans.getDoorMILiBeans() != null) {
                if (doorMILiBeans.getDoorMILiBeans().size() > 0) {
                    if (!doorMILiBeans.getDoorMILiBeans().get(0).isFirst()) {
                        doorMILiBeans.getDoorMILiBeans().add(0, doorMILiBean);
                    }
                } else {
                    doorMILiBeans.getDoorMILiBeans().add(0, doorMILiBean);
                }
            } else {
                List<DoorMILiBean> doorMILiBeans1 = new ArrayList<>();
                doorMILiBeans1.add(doorMILiBean);
                doorMILiBeans.setDoorMILiBeans(doorMILiBeans1);
            }
        } else {
            doorMILiBeans = new StoreDoorMILiBeanList();
            List<DoorMILiBean> doorMILiBeans1 = new ArrayList<>();
            DoorMILiBean doorMILiBean = new DoorMILiBean();
            doorMILiBean.setFirst(true);
            doorMILiBean.setName("一键开门");
            doorMILiBeans1.add(doorMILiBean);
            doorMILiBeans.setDoorMILiBeans(doorMILiBeans1);
        }
        commonAdapter.setDatas(doorMILiBeans.getDoorMILiBeans());

    }


    /**
     * 每个Item进行操作
     *
     * @param holder
     * @param doorMILiBean
     */
    private void itemDo(final ViewHolder holder, final DoorMILiBean doorMILiBean, final int position) {

        if (ChangeAccessActivity.this.doorMiLiBean != null) {
            if (ChangeAccessActivity.this.doorMiLiBean.isFirstChecked()) {
                if (position == 0) {
                    ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                } else {
                    ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
                }
            } else {
                if (ChangeAccessActivity.this.doorMiLiBean.getMac() != null) {
                    if (ChangeAccessActivity.this.doorMiLiBean.getMac().equals(doorMILiBean.getMac())) {
                        ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                    } else {
                        ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
                    }
                } else {
                    ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
                }
            }
        } else {
            if (position == 0) {
                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
            } else {
                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(false);
            }
        }

        holder.setText(R.id.tv_item, doorMILiBean.getName());
        holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    doorMILiBean.setFirstChecked(true);
                } else {
                    doorMILiBean.setFirstChecked(false);
                }

                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                Intent intent = new Intent();
                intent.putExtra("doorMILiBean", doorMILiBean);
                setResult(10, intent);
                finish();
            }
        });
    }

    /**
     * 后期可能扩展会用到
     */
    private void setCustomExpandListview() {

        //模拟数据
//        for (int i = 0; i < parentSource.length; i++) {
//            parent.add(parentSource[i]);
//        }
//        for (int i = 0; i < parent.size(); i++) {
//            String str = parent.get(i);
//            ArrayList<String> temp = new ArrayList<>();
//            for (int j = 0; j < 10; j++) {
//                temp.add("" + j);
//            }
//            datas.put(str, temp);
//        }

        //        myAdapter = new ChangeAccessAdapter(this, parent, datas, listview);
//        listview.setAdapter(myAdapter);
//        listview.setHeaderView(getLayoutInflater().inflate(
//                R.layout.layout_indictor_expand, listview, false));
//
//        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
//                return true;
//            }
//        });
        // 默认全部展开
//        for (int i = 0; i < parent.size(); i++) {
//            listview.expandGroup(i);
//        }

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }


    /**
     * 判断获取的设备是否是米粒蓝牙
     */
    private void getMenJinMiLi() {

        bluetoothNetUtils.getMiLiNetDate(null, 1, new BluetoothNetUtils.OnBlutoothDoorCallBackListener() {
            @Override
            public void OnCallBack(int state, StoreDoorMILiBeanList storeDoorMILiBeanList) {
                if (state == 1) {//请求网络成功
                    if (storeDoorMILiBeanList != null) {
                        if (storeDoorMILiBeanList.getDoorMILiBeans().size() > 0) {
                            DoorMILiBean doorMILiBean = new DoorMILiBean();
                            doorMILiBean.setFirst(true);
                            doorMILiBean.setName("一键开门");
                            storeDoorMILiBeanList.getDoorMILiBeans().add(0, doorMILiBean);
                            PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, bluetoothNetUtils.user_id + bluetoothNetUtils.communityId + PreferenceConst.MILIDOORMAC, new Gson().toJson(storeDoorMILiBeanList));
                            commonAdapter.setDatas(storeDoorMILiBeanList.getDoorMILiBeans());
                        } else {
                            ToastUtil.showShort("您还没有可以开锁的设备");
                        }
                    } else {
                        ToastUtil.showShort("您还没有可以开锁的设备");
                    }
                }
            }
        });

    }

}
