package com.bit.communityProperty.activity.household.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bit.communityProperty.R;
import com.bit.communityProperty.activity.household.HouseholdInfoActivity;
import com.bit.communityProperty.activity.household.adapter.AuditingAdapter;
import com.bit.communityProperty.activity.household.bean.AuditingBean;
import com.bit.communityProperty.activity.releasePass.bean.ReleasePassDetailsBean;
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

/**
 * 待审核（正在审核）的fragment，
 * Created by kezhangzhao on 2018/2/8.
 */

public class AuditingFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int pageIndex;//当前列表数据的页数
    private AuditingBean.RecordsBean mRecordsBean;
    private List<AuditingBean.RecordsBean> mRecordsBeanBeanList = new ArrayList<>();//数据列表
    private Context mContext;
    private LRecyclerView mRecyclerView;
    private AuditingAdapter adapter;//待审核的adapter
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

    public AuditingFragment() {
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
    }


    public static AuditingFragment newInstance(int sectionNumber, Context context) {
        AuditingFragment fragment = new AuditingFragment();
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
        View rootView = inflater.inflate(R.layout.layout_recyclerview_refresh, container, false);
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        adapter = new AuditingAdapter(mContext);
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
                mRecordsBeanBeanList.clear();
                //网络请求获取列表数据
                getAuditingList("5a82adf3b06c97e0cd6c0f3d",1,0,pageIndex,REQUEST_COUNT);
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
                    getAuditingList("5a82adf3b06c97e0cd6c0f3d",1,0,pageIndex,REQUEST_COUNT);
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
            public void onItemClick(View view, int position) {//跳转到住户信息页面
                AuditingBean.RecordsBean bean = mRecordsBeanBeanList.get(position);
                Intent intent = new Intent(mContext, HouseholdInfoActivity.class);
                intent.putExtra("RecordsBean", bean);
                startActivity(intent);
            }
        });
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

    /**
     * 按社区获取用户列表
     * @param communityId 社区ID
     * @param relationship 1：业主；2：家属；3：租客
     * @param auditStatus 0：未审核；1：审核通过；-1：驳回；-2：违规; 2: 已注销; 3: 已解绑
     * @param page 当前第几页
     * @param size 一页的数据数量
     */
    private void getAuditingList(String communityId,int relationship,int auditStatus,int page,int size){
        String url = "v1/user/" + communityId + "/getByCommunityId";
        Map<String ,Object> map = new HashMap<>();
        map.put("relationship",relationship);
        map.put("auditStatus",auditStatus);
        map.put("page",page);
        map.put("size",size);
        RetrofitManage.getInstance().subscribe(Api.getInstance().getAuditingList(url,map), new Observer<BaseEntity<AuditingBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BaseEntity<AuditingBean> BaseEntity) {
                LogManager.printErrorLog("backinfo", GsonUtils.getInstance().toJson(BaseEntity));
                if (BaseEntity.isSuccess()&&BaseEntity.getData()!=null) {
                    TOTAL_COUNTER = BaseEntity.getData().getTotal();//服务器上的总数据条数
                    mRecordsBeanBeanList = BaseEntity.getData().getRecords();
                    mRecyclerView.refreshComplete(REQUEST_COUNT);
                    addItems(BaseEntity.getData().getRecords());
                    notifyDataSetChanged();
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

    /**
     * 添加数据到列表
     *
     * @param list
     */
    private void addItems(List<AuditingBean.RecordsBean> list) {
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
