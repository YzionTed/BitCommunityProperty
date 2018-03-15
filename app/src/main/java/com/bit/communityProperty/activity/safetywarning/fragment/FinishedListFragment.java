package com.bit.communityProperty.activity.safetywarning.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.safetywarning.SafeWarningDetailActivity;
import com.bit.communityProperty.activity.safetywarning.adapter.SafeWarningAdapter;
import com.bit.communityProperty.activity.safetywarning.bean.AlarmListBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.RxBus;
import com.classic.common.MultipleStatusView;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by DELL60 on 2018/2/9.
 */

public class FinishedListFragment extends BaseFragment implements SafeWarningAdapter.btnClickListener {
    @BindView(R.id.rlv)
    LRecyclerView rlv;
    Unbinder unbinder;
    @BindView(R.id.multiple_status_view)
    MultipleStatusView multipleStatusView;

    private SafeWarningAdapter adapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private Map<String, Object> map = new HashMap<>();
    private int page = 1;
    private AlarmListBean alarmListBean;
    private boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_safe_warning_list;
    }

    @Override
    protected void initViewAndData() {
        multipleStatusView.showLoading();
        initDate();
        getList();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        adapter = new SafeWarningAdapter(mContext, this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        rlv.setAdapter(mLRecyclerViewAdapter);
        rlv.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerDecoration divider = new DividerDecoration.Builder(mContext)
                .setHeight(R.dimen.common_dp_10)
                .setColorResource(R.color.appbg)
                .build();
        rlv.addItemDecoration(divider);
        rlv.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                getList();
            }
        });

        rlv.setLoadMoreEnabled(false);

        rlv.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (alarmListBean.getCurrentPage() < alarmListBean.getTotalPage()) {
                    isRefresh = false;
                    getList();
                } else {
                    rlv.setNoMore(true);
                }
            }
        });

        rlv.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
            @Override
            public void reload() {
                getList();
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mActivity, SafeWarningDetailActivity.class)
                        .putExtra("bean", alarmListBean.getRecords().get(position)));
            }
        });
        RxBus.get().toObservable().subscribe(new Consumer<Object>() {

            @Override
            public void accept(Object s) throws Exception {
                if (s instanceof String) {
                    if (s.equals("update")) {
                        isRefresh = true;
                        getList();
                    }
                }
            }
        });
    }

    private void getList() {
        map.clear();
//        map.put("buildingId", "");
        map.put("receiveStatus", "3");
        if (isRefresh)
            page = 1;
        else
            page++;
        map.put("page", page);
        map.put("pageSize", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getAlarmList(map), new Observer<BaseEntity<AlarmListBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                rlv.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onNext(BaseEntity<AlarmListBean> alarmListBeanBaseEntity) {
                rlv.refreshComplete(AppConfig.pageSize);
                alarmListBean = alarmListBeanBaseEntity.getData();
                if (alarmListBean != null) {
                    if (isRefresh){
                        if (alarmListBean.getRecords().size()==0){
                            multipleStatusView.showEmpty();
                        }else{
                            multipleStatusView.showContent();
                            adapter.setDataList(alarmListBean.getRecords());
                        }
                    } else
                        adapter.addAll(alarmListBean.getRecords());
                }else{
                    multipleStatusView.showEmpty();
                }
            }

            @Override
            public void onError(Throwable e) {
                rlv.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onComplete() {
                rlv.refreshComplete(AppConfig.pageSize);
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

    @Override
    public void receiveAlarm(int type, String id) {

    }
}
