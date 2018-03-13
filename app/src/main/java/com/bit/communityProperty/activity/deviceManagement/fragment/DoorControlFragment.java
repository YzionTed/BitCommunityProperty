package com.bit.communityProperty.activity.deviceManagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.DeviceInfoActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.classic.common.MultipleStatusView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 设备管理中的门禁fragment
 * Created by kezhangzhao on 2018/3/1.
 */

public class DoorControlFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    LRecyclerView mRecyclerView;
    Unbinder unbinder1;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;
    private DeviceAdapter adapter;//设备管理的adapter
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private PromptDialog sinInLogin;

    private int page = 1;
    private boolean isRefresh = true;
    private DoorControlBean doorControlBean;
    private List<DoorControlBean.RecordsBean> recordsBeans;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recyclerview_refresh;
    }

    @Override
    protected void initViewAndData() {
        sinInLogin = new PromptDialog((Activity) mContext);
        adapter = new DeviceAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        // 刷新数据
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getDataList(AppConfig.COMMUNITYID, null, null, null);
            }
        });

        //是否禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);

        // 加载更多
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (doorControlBean.getCurrentPage() < doorControlBean.getTotalPage()) {
                    isRefresh = false;
                    //网络请求获取列表数据
                    getDataList(AppConfig.COMMUNITYID, null, null, null);
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });

        mRecyclerView.refresh();
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {//跳转到设备详情信息页面
                DoorControlBean.RecordsBean bean = recordsBeans.get(position);
                Intent intent = new Intent(mContext, DeviceInfoActivity.class);
                intent.putExtra("bean", bean);
                startActivity(intent);
            }
        });
        multipleStatusView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleStatusView.showLoading();
                isRefresh = true;
                getDataList(AppConfig.COMMUNITYID, null, null, null);
            }
        });
    }

    /**
     * 获取门禁列表
     *
     * @param communityId 社区ID 5a82adf3b06c97e0cd6c0f3d （必传参数）
     * @param mac         机器硬件地址
     * @param buildingId  楼栋ID
     * @param doorType    门禁类型 1:社区门，2:楼栋门
     */
    private void getDataList(String communityId, String[] mac, String[] buildingId, String doorType) {
        Map<String, Object> map = new HashMap();
        map.put("communityId", communityId);
        if (mac != null)
            map.put("mac", mac);
        if (buildingId != null)
            map.put("buildingId", buildingId);
        if (doorType != null)
            map.put("doorType", doorType);
        if (isRefresh) {
            page = 1;
        } else {
            page++;
        }
        map.put("page", page);
        map.put("size", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorControlList(map), new Observer<BaseEntity<DoorControlBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<DoorControlBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.getData() != null) {
                    doorControlBean = baseEntity.getData();
                    recordsBeans = doorControlBean.getRecords();
                    if (isRefresh) {
                        if (recordsBeans == null || recordsBeans.size() == 0) {
                            multipleStatusView.showEmpty();
                        } else {
                            multipleStatusView.showContent();
                        }
                        adapter.setDataList(recordsBeans);
                    } else {
                        adapter.addAll(recordsBeans);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onComplete() {
                mRecyclerView.refreshComplete(AppConfig.pageSize);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
