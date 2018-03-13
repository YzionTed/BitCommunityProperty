package com.bit.communityProperty.activity.deviceManagement;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeDetailBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlDetailBean;
import com.bit.communityProperty.activity.deviceManagement.bean.ElevatorDetailBean;
import com.bit.communityProperty.activity.deviceManagement.bean.ElevatorListBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceInfoAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceInfoBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.view.TitleBarView;
import com.classic.common.MultipleStatusView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 设备详情页面
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceInfoActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private DeviceInfoAdapter adapter;
    private MultipleStatusView multipleStatusView;

    private int page = 1;
    private boolean isRefresh = true;

    private DoorControlBean.RecordsBean doorBean;//门禁
    private CarBrakeBean.RecordsBean carBrakeBean;//车闸
    private ElevatorListBean.RecordsBean elevatorBean;//电梯
    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.activity_device_info;
    }

    @Override
    public void initViewData() {
        initView();
        initData();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("设备信息");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.recyclerview);
        multipleStatusView = findViewById(R.id.multiple_status_view);
        multipleStatusView.showLoading();
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleStatusView.showLoading();
                isRefresh = true;
                getData(type);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        adapter = new DeviceInfoAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getData(type);
            }
        });
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                isRefresh = false;
                getData(type);
            }
        });
        if (getIntent().getSerializableExtra("bean") != null) {
            if (getIntent().getSerializableExtra("bean") instanceof CarBrakeBean.RecordsBean) {
                carBrakeBean = (CarBrakeBean.RecordsBean) getIntent().getSerializableExtra("bean");
                mTitleBarView.setTvTitleText(carBrakeBean.getGateName());
                type = 1;
                getCarDataInfo();
            } else if (getIntent().getSerializableExtra("bean") instanceof ElevatorListBean.RecordsBean) {
                elevatorBean = (ElevatorListBean.RecordsBean) getIntent().getSerializableExtra("bean");
                mTitleBarView.setTvTitleText(elevatorBean.getName());
                type = 2;
                getElevatorInfo();
            }else if (getIntent().getSerializableExtra("bean") instanceof DoorControlBean.RecordsBean){
                doorBean = (DoorControlBean.RecordsBean) getIntent().getSerializableExtra("bean");
                mTitleBarView.setTvTitleText(doorBean.getName());
                type = 3;
                getDoorControlInfo();
            }
        }
    }

    private void getData(int type){
        switch (type){
            case 1:
                getCarDataInfo();
                break;
            case 2:
                getElevatorInfo();
                break;
            case 3:
                getDoorControlInfo();
                break;
        }
    }

    private void getDoorControlInfo(){
        Map<String, Object> map = new HashMap<>();
        map.put("communityId", AppConfig.COMMUNITYID);
        map.put("deviceId", doorBean.getDeviceId());
        if (isRefresh){
            page=1;
        }else{
            page++;
        }
        map.put("page", page);
        map.put("size", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorUseList(map), new Observer<BaseEntity<DoorControlDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<DoorControlDetailBean> objectBaseEntity) {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
                if (objectBaseEntity.isSuccess()) {
                    if (isRefresh){
                        if (objectBaseEntity.getData().getRecords()==null||objectBaseEntity.getData().getRecords().size()==0){
                            multipleStatusView.showEmpty();
                        }else{
                            multipleStatusView.showContent();
                        }
                        adapter.setDataList(objectBaseEntity.getData().getRecords());
                    }else{
                        adapter.addAll(objectBaseEntity.getData().getRecords());
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

    //车闸记录
    private void getCarDataInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("gateNO", carBrakeBean.getGateNO());
        map.put("inOutTag", carBrakeBean.getInOutTag());
        if (isRefresh){
            page=1;
        }else{
            page++;
        }
        map.put("page", page);
        map.put("size", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getCarBrakeDetail(map), new Observer<BaseEntity<CarBrakeDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<CarBrakeDetailBean> carBrakeDetailBeanBaseEntity) {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
                if (carBrakeDetailBeanBaseEntity.isSuccess()) {
                    if (isRefresh){
                        if (carBrakeDetailBeanBaseEntity.getData().getRecords()==null||carBrakeDetailBeanBaseEntity.getData().getRecords().size()==0){
                            multipleStatusView.showEmpty();
                        }else{
                            multipleStatusView.showContent();
                        }
                        adapter.setDataList(carBrakeDetailBeanBaseEntity.getData().getRecords());
                    }else{
                        adapter.addAll(carBrakeDetailBeanBaseEntity.getData().getRecords());
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

    private void getElevatorInfo() {
        Map<String, Object> map = new HashMap<>();
        if (isRefresh){
            page=1;
        }else{
            page++;
        }
        map.put("page", page);
        map.put("size", AppConfig.pageSize);
        map.put("communityId", AppConfig.COMMUNITYID);
        map.put("deviceId", elevatorBean.getId());
        RetrofitManage.getInstance().subscribe(Api.getInstance().getElevatorUseList(map), new Observer<BaseEntity<ElevatorDetailBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<ElevatorDetailBean> objectBaseEntity) {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
                if (objectBaseEntity.isSuccess()) {
                    if (isRefresh){
                        if (objectBaseEntity.getData().getRecords()==null||objectBaseEntity.getData().getRecords().size()==0){
                            multipleStatusView.showEmpty();
                        }else{
                            multipleStatusView.showContent();
                        }
                        adapter.setDataList(objectBaseEntity.getData().getRecords());
                    }else{
                        adapter.addAll(objectBaseEntity.getData().getRecords());
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
}
