package com.bit.communityProperty.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.adapter.ParkingManagementAdapter;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.ParkingManagementBean;
import com.bit.communityProperty.view.TitleBarView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

/**
 * 停车管理页面
 * Created by kezhangzhao on 2018/1/18.
 */

public class ParkingManagementActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private LRecyclerView mRecyclerView;//上下拉刷新列表
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private ParkingManagementAdapter adapter;//停车管理列表的adapter
    private int pageIndex;//当前列表数据的页数
    private ArrayList<ParkingManagementBean> parkingManagementBeanList = new ArrayList<>();//数据列表

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
        return R.layout.activity_parking_management;
    }

    @Override
    public void initViewData() {
        initView();
        initDate();
    }

    /**
     * 初始化view
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("停车管理");
        mTitleBarView.setRightText("收费");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"收费",Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView = findViewById(R.id.lv_parking_message);
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        adapter = new ParkingManagementAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                parkingManagementBeanList.clear();
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
            public void onItemClick(View view, int position) {
               ParkingManagementBean bean =  parkingManagementBeanList.get(position);
                Intent intent = new Intent(mContext,ParkingMoneyActivity.class);
                intent.putExtra("ParkingManagementBean",bean);
               startActivity(intent);
            }
        });
    }

    private ArrayList<ParkingManagementBean> doSomeDate(){
        for (int i=0;i<4;i++) {
            ParkingManagementBean bean = new ParkingManagementBean();
            bean.setTitle("A区车库入闸A101");
            bean.setType("设备类型001");
            bean.setStatus(1);
            ParkingManagementBean bean2 = new ParkingManagementBean();
            bean2.setTitle("A区车库入闸A102");
            bean2.setType("设备类型002");
            bean2.setStatus(1);
            ParkingManagementBean bean3 = new ParkingManagementBean();
            bean3.setTitle("A区车库入闸A103");
            bean3.setType("设备类型003");
            bean3.setStatus(0);
            parkingManagementBeanList.add(bean);
            parkingManagementBeanList.add(bean2);
            parkingManagementBeanList.add(bean3);
        }
        return parkingManagementBeanList;
    }

    private void setDate(ArrayList<ParkingManagementBean> list){
        mRecyclerView.refreshComplete(REQUEST_COUNT);
//        TOTAL_COUNTER = messageCenterInfo.getPageInfo().getTotalCount();//服务器上的总数据条数
        TOTAL_COUNTER=30;
        addItems(list);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到列表
     * @param list
     */
    private void addItems(ArrayList<ParkingManagementBean> list) {
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
