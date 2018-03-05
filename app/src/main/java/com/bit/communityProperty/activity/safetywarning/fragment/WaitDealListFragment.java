package com.bit.communityProperty.activity.safetywarning.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.safetywarning.SafeWarningDetailActivity;
import com.bit.communityProperty.activity.safetywarning.SafeWarningReportActivity;
import com.bit.communityProperty.activity.safetywarning.adapter.SafeWarningAdapter;
import com.bit.communityProperty.activity.safetywarning.bean.AlarmListBean;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.receiver.JPushBean;
import com.bit.communityProperty.receiver.RxBus;
import com.bit.communityProperty.utils.SPUtil;
import com.bit.communityProperty.utils.ToastUtil;
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
import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by DELL60 on 2018/2/9.
 */

public class WaitDealListFragment extends BaseFragment implements SafeWarningAdapter.btnClickListener {
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

    private PromptDialog mLoadingDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_safe_warning_list;
    }

    @Override
    protected void initViewAndData() {
        mLoadingDialog = new PromptDialog(mActivity);
        multipleStatusView.showLoading();
        initDate();
        getList();
    }

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
                }else if (s instanceof JPushBean){
                    JPushBean jPushBean = (JPushBean) s;
                    receiveAlarm(jPushBean.getData().getPolice_id());
//                    isRefresh = true;
//                    getList();
//                    RxBus.get().post("change1");
                }
            }
        });
    }

    private void getList() {
        map.clear();
//        map.put("buildingId", "");
        map.put("receiveStatus", "1");
        if (isRefresh)
            page = 1;
        else
            page++;
        map.put("page", page);
        map.put("pageSize", AppConfig.pageSize);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getAlarmList(map), new Observer<BaseEntity<AlarmListBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
//                rlv.refreshComplete(AppConfig.pageSize);
            }

            @Override
            public void onNext(BaseEntity<AlarmListBean> alarmListBeanBaseEntity) {
                if (alarmListBeanBaseEntity.isSuccess()){
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

    public void receiveAlarm(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("Id", id);
        map.put("receiverId", SPUtil.get(mActivity, AppConfig.id, ""));
        RetrofitManage.getInstance().subscribe(Api.getInstance().receiveAlarm(map), new Observer<BaseEntity<Object>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<Object> baseEntity) {
                mLoadingDialog.dismiss();
                if (baseEntity.isSuccess()) {
                    RxBus.get().post("update");
                    RxBus.get().post("change2");//接警成功，切换到待排查fragment
                    ToastUtil.showShort("接单成功,请前往排查");
                }else{
                    ToastUtil.showShort(baseEntity.getErrorMsg());
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
    public void receiveAlarm(int type, String id) {
        if (type == 1) {
            mLoadingDialog.showLoading("请求中...");
            receiveAlarm(id);
        } else if (type == 2) {
            startActivity(new Intent(mActivity, SafeWarningReportActivity.class).putExtra("id", id));
        }
    }
}
