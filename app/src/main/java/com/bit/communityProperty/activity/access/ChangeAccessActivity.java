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

import com.bit.communityProperty.MyApplication;
import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.adapter.ChangeAccessAdapter;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.bean.DoorMILiBean;
import com.bit.communityProperty.bean.DoorMILiBeanList;
import com.bit.communityProperty.data.PreferenceConst;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.LogManager;
import com.bit.communityProperty.utils.PreferenceUtils;
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
    private DoorMILiBean doorMILiBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_access;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("切换门禁");

        doorMILiBean = (DoorMILiBean) getIntent().getSerializableExtra("doorMILiBean");
        setCustomExpandListview();

        lv_list = (ListView) findViewById(R.id.lv_list);

        commonAdapter = new CommonAdapter<DoorMILiBean>(this, R.layout.item_door_access) {
            @Override
            public void convert(final ViewHolder holder, final DoorMILiBean doorMILiBean, int position, View convertView) {

                itemDo(holder, doorMILiBean);
            }
        };
        lv_list.setAdapter(commonAdapter);

        setCashDate();

        getMenJinMiLi();
    }

    //放入缓存数据
    private void setCashDate() {

        String prefString = PreferenceUtils.getPrefString(this, PreferenceConst.PRE_NAME, PreferenceConst.MILIDOORMAC, "");
        if (prefString != null) {
            DoorMILiBeanList doorMILiBeans = new Gson().fromJson(prefString, DoorMILiBeanList.class);
            if (doorMILiBeans != null) {
                DoorMILiBean doorMILiBean = new DoorMILiBean();
                doorMILiBean.setFirst(true);
                doorMILiBean.setName("自动开门");
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
                doorMILiBeans = new DoorMILiBeanList();
                List<DoorMILiBean> doorMILiBeans1 = new ArrayList<>();
                DoorMILiBean doorMILiBean = new DoorMILiBean();
                doorMILiBean.setFirst(true);
                doorMILiBean.setName("自动开门");
                doorMILiBeans1.add(doorMILiBean);
                doorMILiBeans.setDoorMILiBeans(doorMILiBeans1);
            }
            commonAdapter.setDatas(doorMILiBeans.getDoorMILiBeans());
        }

    }


    /**
     * 每个Item进行操作
     *
     * @param holder
     * @param doorMILiBean
     */
    private void itemDo(final ViewHolder holder, final DoorMILiBean doorMILiBean) {

        if (ChangeAccessActivity.this.doorMILiBean != null) {
            if (ChangeAccessActivity.this.doorMILiBean.getMac() != null) {
                if (ChangeAccessActivity.this.doorMILiBean.getMac().equals(doorMILiBean.getMac())) {
                    ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                }
            }
        }

        holder.setText(R.id.tv_item, doorMILiBean.getName());
        holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.putExtra("doorMILiBean", doorMILiBean);
                        setResult(10, intent);
                        finish();
                    }
                }, 500);
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
        //  BluetoothUtils.getMiliBluetooth(searchBlueDeviceBeanList);

        Map<String, Object> getDoorAuth = new HashMap<>();
        getDoorAuth.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorAuthList(getDoorAuth), new Observer<BaseEntity<List<DoorMILiBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<DoorMILiBean>> logindata) {
                if (logindata.getErrorCode().equals("0")) {
                    if (logindata.getData().size() > 0) {
                        DoorMILiBean doorMILiBean = new DoorMILiBean();
                        doorMILiBean.setFirst(true);
                        doorMILiBean.setName("自动开门");
                        DoorMILiBeanList doorMILiBeanList = new DoorMILiBeanList();
                        doorMILiBeanList.setDoorMILiBeans(logindata.getData());
                        doorMILiBeanList.getDoorMILiBeans().add(0, doorMILiBean);
                        commonAdapter.setDatas(doorMILiBeanList.getDoorMILiBeans());
                        PreferenceUtils.setPrefString(MyApplication.getInstance().getContext(), PreferenceConst.PRE_NAME, PreferenceConst.MILIDOORMAC, new Gson().toJson(doorMILiBeanList));
                    }
                } else {
                    Log.e(Tag, "logindata.getErrorCode()" + logindata.getErrorCode());
                }
            }

            @Override
            public void onError(Throwable e) {

                LogManager.printErrorLog("backinfo", "失败返回的数据：" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

}
