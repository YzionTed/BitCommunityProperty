package com.bit.communityProperty.activity.elevatorcontrol;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.access.adapter.ChangeAccessAdapter;
import com.bit.communityProperty.activity.elevatorcontrol.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.CustomExpandListview;

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

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_elevator;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("切换电梯");
        doorJinBoBean = (ElevatorListBean) getIntent().getSerializableExtra("doorJinBoBean");

        blueAddressIds = getIntent().getStringArrayExtra("ids");
        initListView();
        getData();
    }

    private void initListView() {
        commonAdapter = new CommonAdapter<ElevatorListBean>(this, R.layout.item_access_child) {
            @Override
            public void convert(ViewHolder holder, final ElevatorListBean elevatorListBean, int position, View convertView) {

                if (doorJinBoBean != null) {
                    if (ChangeElevatorActivity.this.doorJinBoBean.getMacAddress() != null) {
                        if (ChangeElevatorActivity.this.doorJinBoBean.getMacAddress().equals(elevatorListBean.getMacAddress())) {
                            ((CheckBox) holder.getView(R.id.tv_item)).setChecked(true);
                        }
                    }
                }

                holder.setText(R.id.tv_item, elevatorListBean.getElevatorNum() + elevatorListBean.getName());
                holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RxBus.get().post(elevatorListBean);
                        finish();
                    }
                });
            }
        };
        lvElevator.setAdapter(commonAdapter);
    }

    private void getData() {
        Map<String, Object> map = new HashMap<>();

        map.put("communityId", "5a82adf3b06c97e0cd6c0f3d");
        map.put("userId", SPUtil.get(this, AppConfig.id, ""));

        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorGetAuthsList(map), new Observer<BaseEntity<List<ElevatorListBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(BaseEntity<List<ElevatorListBean>> listBaseEntity) {
                if (listBaseEntity.isSuccess()) {
                    if (listBaseEntity.getData() != null) {
                        ElevatorListBean elevatorListBean = new ElevatorListBean();
                        elevatorListBean.setName("自动解锁");
                       elevatorListBean.setElevatorNum("");
                        elevatorListBean.setFirst(true);
                        listBaseEntity.getData().add(0, elevatorListBean);
                        commonAdapter.setDatas(listBaseEntity.getData());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }

}
