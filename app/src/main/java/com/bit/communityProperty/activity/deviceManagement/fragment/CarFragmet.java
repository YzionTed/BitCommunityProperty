package com.bit.communityProperty.activity.deviceManagement.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.DeviceInfoActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.CarBrakeBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.classic.common.MultipleStatusView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 设备管理中的车闸grament
 * Created by kezhangzhao on 2018/3/1.
 */

public class CarFragmet extends BaseFragment {

    @BindView(R.id.recyclerview)
    LRecyclerView mRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private DeviceAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private List<CarBrakeBean.RecordsBean> carBrakeBeanList;
    private CarBrakeBean carBrakeBean;
    private int page = 1;
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recyclerview_refresh;
    }

    @Override
    protected void initViewAndData() {
        multipleStatusView.showLoading();
        adapter = new DeviceAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 刷新数据
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getData();
            }
        });

        mRecyclerView.setPullRefreshEnabled(true);
        mRecyclerView.setLoadMoreEnabled(true);

        // 加载更多
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (carBrakeBean != null && carBrakeBean.getCurrentPage() < carBrakeBean.getTotalPage()) {
                    isRefresh = false;
                    getData();
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {//跳转到设备详情信息页面
                CarBrakeBean.RecordsBean bean = carBrakeBeanList.get(position);
                Intent intent = new Intent(mContext, DeviceInfoActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });
        getData();

        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleStatusView.showLoading();
                isRefresh = true;
                getData();
            }
        });
    }

    private void getData() {
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        RetrofitManage.getInstance().subscribe(Api.getInstance().getCarGateList(page, AppConfig.pageSize), new Observer<BaseEntity<CarBrakeBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<CarBrakeBean> listBaseEntity) {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
                if (listBaseEntity.isSuccess()) {
                    carBrakeBean = listBaseEntity.getData();
                    carBrakeBeanList = listBaseEntity.getData().getRecords();
                    if (isRefresh) {
                        if (carBrakeBeanList==null||carBrakeBeanList.size()==0){
                            multipleStatusView.showEmpty();
                        }else{
                            multipleStatusView.showContent();
                        }
                        adapter.setDataList(carBrakeBeanList);
                    } else {
                        adapter.addAll(carBrakeBeanList);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}
