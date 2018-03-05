package com.bit.communityProperty.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bit.communityProperty.R;
import com.bit.communityProperty.adapter.DoorRecordAdapter;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.DoorRecordBean;
import com.bit.communityProperty.view.TitleBarView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

/**
 * 门禁记录页面
 * Created by kezhangzhao on 2018/1/21.
 */

public class DoorRecordActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private LRecyclerView mRecyclerView;//上下拉刷新列表
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉刷新的基础adapter
    private DoorRecordAdapter adapter;//门禁记录数据的adapter
    private ArrayList<DoorRecordBean> mDoorRecordBeanList = new ArrayList<>();//门禁数据列表

    /**
     * 当前列表数据的页数
     */
    private int PAGE_INDEX = 0;

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
        return R.layout.activity_door_record;
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
        mTitleBarView.setTvTitleText("门禁记录");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerView = findViewById(R.id.lv_door_record);

    }

    /**
     * 初始化数据
     */
    private void initDate() {

        adapter = new DoorRecordAdapter(mContext);
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
                mDoorRecordBeanList.clear();
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
//                DoorRecordBean bean = mDoorRecordBeanList.get(position);
//                Intent intent = new Intent(mContext, ParkingMoneyActivity.class);
//                intent.putExtra("DoorRecordBean", bean);
//                startActivity(intent);
            }
        });

    }

    private ArrayList<DoorRecordBean> doSomeDate(){
        for (int i=0;i<4;i++) {
            DoorRecordBean bean = new DoorRecordBean();
            bean.setAddress("A区车库入闸A101");
            bean.setType(0);
            bean.setTime("12:30:45");
            DoorRecordBean bean2 = new DoorRecordBean();
            bean2.setAddress("A区车库入闸A102");
            bean2.setType(1);
            bean2.setTime("12:37:00");
            DoorRecordBean bean3 = new DoorRecordBean();
            bean3.setAddress("A区车库入闸A103");
            bean3.setType(1);
            bean3.setTime("09:10:00");
            mDoorRecordBeanList.add(bean);
            mDoorRecordBeanList.add(bean2);
            mDoorRecordBeanList.add(bean3);
        }
        return mDoorRecordBeanList;
    }

    private void setDate(ArrayList<DoorRecordBean> list){
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
    private void addItems(ArrayList<DoorRecordBean> list) {
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
