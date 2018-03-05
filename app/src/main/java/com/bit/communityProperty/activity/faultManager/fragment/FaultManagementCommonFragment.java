package com.bit.communityProperty.activity.faultManager.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.faultManager.FaultDetailsActivity;
import com.bit.communityProperty.activity.faultManager.adapter.FaultManagerCommonAdapter;
import com.bit.communityProperty.bean.FaultManagerCommonBean;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;

import java.util.ArrayList;

/**
 * 故障管理页面
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultManagementCommonFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int pageIndex;//当前列表数据的页数
    private ArrayList<FaultManagerCommonBean> FaultManagerCommonBeanList = new ArrayList<>();//数据列表
    private Context mContext;
    private LRecyclerView mRecyclerView;
    private FaultManagerCommonAdapter adapter;//普通物业人员的adapter
    private LRecyclerViewAdapter mLRecyclerViewAdapter;//上下拉的recyclerView的adapter
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

    public FaultManagementCommonFragment() {
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
//        adapter = new FaultManagerCommonAdapter(context);
//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
    }


    public static FaultManagementCommonFragment newInstance(int sectionNumber, Context context) {
        FaultManagementCommonFragment fragment = new FaultManagementCommonFragment();
//        fragment.setContext(context);
        fragment.initView(context);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        //add
        //add
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fault_management, container, false);
        mRecyclerView = rootView.findViewById(R.id.lv_fault_management);
        adapter = new FaultManagerCommonAdapter(mContext);
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
//                getMessageInfoList(pageIndex,REQUEST_COUNT,category);
                FaultManagerCommonBeanList.clear();
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
            public void onItemClick(View view, int position) {//跳转到故障的详情页面
                FaultManagerCommonBean bean = FaultManagerCommonBeanList.get(position);
                Intent intent = new Intent(mContext, FaultDetailsActivity.class);
                intent.putExtra("FaultManagerCommonBean", bean);
                startActivity(intent);
            }
        });
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    private ArrayList<FaultManagerCommonBean> doSomeDate() {
        for (int i = 0; i < 4; i++) {
            FaultManagerCommonBean bean = new FaultManagerCommonBean();
            bean.setAddress("A区车库入闸A101");
            bean.setType(1);
            bean.setStatus(1);
            bean.setTime("0907 11:30");
            bean.setReason(1);
            FaultManagerCommonBean bean2 = new FaultManagerCommonBean();
            bean2.setAddress("A区车库入闸A101");
            bean2.setType(0);
            bean2.setStatus(0);
            bean2.setTime("0907 11:30");
            bean2.setReason(0);
            FaultManagerCommonBean bean3 = new FaultManagerCommonBean();
            bean3.setAddress("A区车库入闸A101");
            bean3.setType(1);
            bean3.setStatus(0);
            bean3.setTime("0907 11:30");
            bean3.setReason(0);
            FaultManagerCommonBeanList.add(bean);
            FaultManagerCommonBeanList.add(bean2);
            FaultManagerCommonBeanList.add(bean3);
        }
        return FaultManagerCommonBeanList;
    }

    private void setDate(ArrayList<FaultManagerCommonBean> list) {
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
    private void addItems(ArrayList<FaultManagerCommonBean> list) {
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
