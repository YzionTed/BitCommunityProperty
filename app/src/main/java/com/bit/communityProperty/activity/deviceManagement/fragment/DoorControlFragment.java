package com.bit.communityProperty.activity.deviceManagement.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.deviceManagement.DeviceInfoActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBean;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceBeanPar;
import com.bit.communityProperty.activity.deviceManagement.bean.DoorControlBean;
import com.bit.communityProperty.base.BaseEntity;
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
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * 设备管理中的门禁fragment
 * Created by kezhangzhao on 2018/3/1.
 */

public class DoorControlFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int pageIndex;//当前列表数据的页数
    private ArrayList<DoorControlBean.RecordsBean> mDeviceBeanList = new ArrayList<>();//数据列表
    private Context mContext;
    private LRecyclerView mRecyclerView;
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

    public DoorControlFragment() {
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void initView(Context context) {
        this.mContext = context;
        sinInLogin = new PromptDialog((Activity) mContext);
    }


    public static DoorControlFragment newInstance(int sectionNumber, Context context) {
        DoorControlFragment fragment = new DoorControlFragment();
//        fragment.setContext(context);
        fragment.initView(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_recyclerview_refresh, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
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
                if (getArguments().getInt(ARG_SECTION_NUMBER)==1){
//                    getDataList();
                }
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                mDeviceBeanList.clear();
//                setDate(doSomeDate());//假数据
                getDataList("5a82adf3b06c97e0cd6c0f3d",null,null,null,pageIndex,REQUEST_COUNT);
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
                    getDataList("5a82adf3b06c97e0cd6c0f3d",null,null,null,pageIndex,REQUEST_COUNT);
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
//                DoorControlBean.RecordsBean bean = mDeviceBeanList.get(position);
//                Intent intent = new Intent(mContext, DeviceInfoActivity.class);
//                intent.putExtra("RecordsBean", bean);
//                startActivity(intent);
            }
        });
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    /**
     * 获取门禁列表
     * @param communityId  社区ID 5a82adf3b06c97e0cd6c0f3d （必传参数）
     * @param mac 机器硬件地址
     * @param buildingId 楼栋ID
     * @param doorType 门禁类型 1:社区门，2:楼栋门
     * @param page 页码
     * @param size 每页条数
     */
    private void getDataList(String communityId,String[] mac,String [] buildingId,String doorType,int page,int size){
        Map<String,Object> map = new HashMap();
        map.put("communityId",communityId);
        if (mac!=null)
        map.put("mac",mac);
        if (buildingId!=null)
        map.put("buildingId",buildingId);
        if (doorType!=null)
        map.put("doorType",doorType);
        map.put("page",page);
        map.put("size",size);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getDoorControlList(map), new Observer<BaseEntity<DoorControlBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<DoorControlBean> baseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(baseEntity));
                if (baseEntity.getData()!=null){
                    TOTAL_COUNTER = baseEntity.getData().getTotal();
                    if (baseEntity.getData().getRecords()!=null){
                        mDeviceBeanList.addAll(baseEntity.getData().getRecords());
                        addItems(baseEntity.getData().getRecords());
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                mRecyclerView.refreshComplete(REQUEST_COUNT);
            }

            @Override
            public void onComplete() {
                mRecyclerView.refreshComplete(REQUEST_COUNT);
            }
        });
    }


    /**
     * 添加数据到列表
     *
     * @param list
     */
    private void addItems(List<DoorControlBean.RecordsBean> list) {
        mCurrentCounter += list.size();
        adapter.addAll(list);
    }

    /**
     * 更新adapter数据
     */
    private void notifyDataSetChanged() {
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }
}
