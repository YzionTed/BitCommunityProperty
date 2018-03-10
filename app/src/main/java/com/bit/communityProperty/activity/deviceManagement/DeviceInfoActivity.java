package com.bit.communityProperty.activity.deviceManagement;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeBean;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeDetailBean;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceInfoAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceInfoBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.view.TitleBarView;
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

public class DeviceInfoActivity extends BaseActivity{

    private TitleBarView mTitleBarView;//标题栏
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private ArrayList<DeviceInfoBean> mDeviceInfoBeanList = new ArrayList<>();//数据列表
    private DeviceInfoAdapter adapter;

    private CarBrakeBean carBrakeBean;

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
    private void initView(){
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("设备信息");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.recyclerview);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        adapter = new DeviceInfoAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        mRecyclerView.setLoadMoreEnabled(true);
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
            }
        });
        if (getIntent().getSerializableExtra("bean")!=null){
            if (getIntent().getSerializableExtra("bean") instanceof CarBrakeBean){
                carBrakeBean = (CarBrakeBean) getIntent().getSerializableExtra("bean");
                mTitleBarView.setTvTitleText(carBrakeBean.getGateName());
                getCarDataInfo();
            }
        }
    }

    //车闸记录
    private void getCarDataInfo(){
        Map<String, Object> map = new HashMap<>();
        map.put("gateNO", carBrakeBean.getGateNO());
        map.put("inOutTag", carBrakeBean.getInOutTag());
        RetrofitManage.getInstance().subscribe(Api.getInstance().getCarBrakeDetail(map), new Observer<BaseEntity<List<CarBrakeDetailBean>>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<List<CarBrakeDetailBean>> carBrakeDetailBeanBaseEntity) {
                if (carBrakeDetailBeanBaseEntity.isSuccess()){
                    adapter.setDataList(carBrakeDetailBeanBaseEntity.getData());
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
