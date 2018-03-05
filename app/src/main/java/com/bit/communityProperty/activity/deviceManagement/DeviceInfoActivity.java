package com.bit.communityProperty.activity.deviceManagement;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.deviceManagement.adapter.DeviceInfoAdapter;
import com.bit.communityProperty.activity.deviceManagement.bean.DeviceInfoBean;
import com.bit.communityProperty.view.TitleBarView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

/**
 * 设备详情页面
 * Created by kezhangzhao on 2018/2/10.
 */

public class DeviceInfoActivity extends BaseActivity{

    private TitleBarView mTitleBarView;//标题栏
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private ArrayList<DeviceInfoBean> mDeviceInfoBeanList = new ArrayList<>();//数据列表
    private int pageIndex;//当前列表数据的页数
    private DeviceInfoAdapter adapter;
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
//                if (getArguments().getInt(ARG_SECTION_NUMBER)==2){
//                    getDataList();
//                }
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                mDeviceInfoBeanList.clear();
                setDate(doSomeDate());//假数据
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
                    setDate(doSomeDate());//假数据
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
            public void onItemClick(View view, int position) {//跳转到
//                DeviceBean bean = mDeviceBeanList.get(position);
//                Intent intent = new Intent(mContext, DeviceInfoActivity.class);
//                intent.putExtra("AuditingBean", bean);
//                startActivity(intent);
            }
        });
    }
    private ArrayList<DeviceInfoBean> doSomeDate() {
        for (int i = 0; i < 4; i++) {
            DeviceInfoBean bean = new DeviceInfoBean();
//            bean.setName("习近平");
//            bean.setDataStatus(1);
//            bean.setDoorId("CZ234355");
            DeviceInfoBean bean2 = new DeviceInfoBean();
//            bean2.setName("dddddd");
//            bean2.setDataStatus(0);
//            bean2.setDoorId("CZ2343334");
            DeviceInfoBean bean3 = new DeviceInfoBean();
//            bean3.setName("iuoiwu");
//            bean3.setDataStatus(0);
//            bean3.setDoorId("0907 11:30");
            mDeviceInfoBeanList.add(bean);
            mDeviceInfoBeanList.add(bean2);
            mDeviceInfoBeanList.add(bean3);
        }
        return mDeviceInfoBeanList;
    }

    private void setDate(ArrayList<DeviceInfoBean> list) {
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
    private void addItems(ArrayList<DeviceInfoBean> list) {
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
