package com.bit.communityProperty.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.adapter.MeterReadAdapter;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.MeterReadBean;
import com.bit.communityProperty.view.TitleBarView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

/**
 * 抄表管理主页面
 * Created by kezhangzhao on 2018/1/21.
 */

public class MeterReadActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private MeterReadAdapter adapter;//抄表管理的adapter
    private LRecyclerView mRecyclerView;//上下拉刷新列表
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
    private ArrayList<MeterReadBean> meterReadBeanList = new ArrayList<>();//数据列表

    /**
     * 当前列表数据的页数
     */
    private int PAGE_INDEX;
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
        return R.layout.activity_meter_read;
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
        mTitleBarView.setTvTitleText("抄表管理");
        mTitleBarView.setRightText("新增");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitleBarView.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, MeterReadAddActivity.class));
            }
        });
        mRecyclerView = findViewById(R.id.lv_meter_read);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        adapter = new MeterReadAdapter(mContext);
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
                PAGE_INDEX = 1;
                mLRecyclerViewAdapter.removeFooterView();
                adapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                //网络请求获取列表数据
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                meterReadBeanList.clear();
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
                    PAGE_INDEX++;
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
//                MeterReadBean bean =  meterReadBeanList.get(position);
//                Intent intent = new Intent(mContext,ParkingMoneyActivity.class);
//                intent.putExtra("MeterReadBean",bean);
//                startActivity(intent);
            }
        });
    }
    private ArrayList<MeterReadBean> doSomeDate(){
        for (int i=0;i<4;i++) {
            MeterReadBean bean = new MeterReadBean();
            bean.setMonth("九月份水表记录");
            bean.setTime("09-27 15:30");
            bean.setCurrentNum("1000");
            bean.setAllNum("2000");
            bean.setStatus(0);
            MeterReadBean bean2 = new MeterReadBean();
            bean2.setMonth("八月份水表记录");
            bean2.setTime("05-27 12:10");
            bean2.setCurrentNum("1500");
            bean2.setAllNum("2000");
            bean2.setStatus(1);
            MeterReadBean bean3 = new MeterReadBean();
            bean3.setMonth("七月份水表记录");
            bean3.setTime("01-12 09:45");
            bean3.setCurrentNum("890");
            bean3.setAllNum("1500");
            bean3.setStatus(1);
            meterReadBeanList.add(bean);
            meterReadBeanList.add(bean2);
            meterReadBeanList.add(bean3);
        }
        return meterReadBeanList;
    }

    private void setDate(ArrayList<MeterReadBean> list){
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
    private void addItems(ArrayList<MeterReadBean> list) {
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
