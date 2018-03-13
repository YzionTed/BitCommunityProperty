package com.bit.communityProperty.activity.deviceManagement.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.DeviceInfoActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBeanPar;
import com.bit.communityProperty.base.BaseEntity;
import com.bit.communityProperty.base.BaseFragment;
import com.bit.communityProperty.config.AppConfig;
import com.bit.communityProperty.net.Api;
import com.bit.communityProperty.net.RetrofitManage;
import com.bit.communityProperty.utils.GsonUtils;
import com.bit.communityProperty.utils.LogManager;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 设备管理中的多媒体grament
 * Created by kezhangzhao on 2018/3/1.
 */

public class MultimediaFragment extends BaseFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.recyclerview)
    LRecyclerView mRecyclerView;
    Unbinder unbinder;
    private int pageIndex;//当前列表数据的页数
    private ArrayList<DeviceBean> mDeviceBeanList = new ArrayList<>();//数据列表
    private DeviceAdapter adapter;//设备管理的adapter
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private PromptDialog sinInLogin;
    /**
     * 服务器端一共多少条数据
     */
    private static int TOTAL_COUNTER = 0;

    /**
     * 每一页展示多少条数据
     */
    private static final int REQUEST_COUNT = 30;

    /**
     * 已经获取到多少条数据了
     */
    private static int mCurrentCounter = 0;

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
        // 设置RecyclerView刷新加载样式
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        // 刷新数据
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentCounter = 0;
                pageIndex = 1;
                mLRecyclerViewAdapter.removeFooterView();
                adapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                //网络请求获取列表数据
                if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
//                    getDataList();
                }
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                mDeviceBeanList.clear();
//                setDate(doSomeDate());//假数据
            }
        });

        //是否禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(true);

        // 加载更多
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                if (mCurrentCounter < TOTAL_COUNTER) {
                    pageIndex++;
                    //网络请求获取列表数据
//                    getMessageInfoList(pageIndex,REQUEST_COUNT,category);
//                    setDate(doSomeDate());//假数据
                } else {
                    mRecyclerView.setNoMore(true);
                }
            }
        });

        // RecyclewView的item点击事件监听
//        mLRecyclerViewAdapter.setOnItemClickListener(new MyOnItemClickListener());

        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.swipe_refresh_color1, R.color.swipe_refresh_color2, R.color.swipe_refresh_color3);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.swipe_refresh_color1, R.color.swipe_refresh_color2, R.color.swipe_refresh_color3);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "已经全部为你呈现了", "网络不给力啊，点击再试一次吧");

        mRecyclerView.refresh();
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {//跳转到设备详情信息页面
                DeviceBean bean = mDeviceBeanList.get(position);
                Intent intent = new Intent(mContext, DeviceInfoActivity.class);
                intent.putExtra("AuditingBean", bean);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取数据列表
     */
    private void getDataList() {
        DeviceBeanPar par = new DeviceBeanPar();
        par.setBuildingId("2");
        par.setDoorId("56");
        par.setMac("蓝牙地址");
        par.setPage(1);
        par.setSize(20);
        ArrayList macList = new ArrayList();
        macList.add("2");
        macList.add("3");
        HashMap map = new HashMap();
        map.put("communityId", AppConfig.COMMUNITYID);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getMonitorList(map), new Observer<BaseEntity<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<String> stringBaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(stringBaseEntity));

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private ArrayList<DeviceBean> doSomeDate() {
        for (int i = 0; i < 4; i++) {
            DeviceBean bean = new DeviceBean();
            bean.setName("习近平");
            bean.setDataStatus(1);
            bean.setDoorId("CZ234355");
            DeviceBean bean2 = new DeviceBean();
            bean2.setName("dddddd");
            bean2.setDataStatus(0);
            bean2.setDoorId("CZ2343334");
            DeviceBean bean3 = new DeviceBean();
            bean3.setName("iuoiwu");
            bean3.setDataStatus(0);
            bean3.setDoorId("0907 11:30");
            mDeviceBeanList.add(bean);
            mDeviceBeanList.add(bean2);
            mDeviceBeanList.add(bean3);
        }
        return mDeviceBeanList;
    }

    private void setDate(ArrayList<DeviceBean> list) {
        mRecyclerView.refreshComplete(REQUEST_COUNT);
//        TOTAL_COUNTER = messageCenterInfo.getPageInfo().getTotalCount();//服务器上的总数据条数
        TOTAL_COUNTER = 30;
        addItems(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到列表
     *
     * @param list
     */
    private void addItems(ArrayList<DeviceBean> list) {
        mCurrentCounter += list.size();
//        adapter.addAll(list);
    }

    /**
     * 更新adapter数据
     */
    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
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
