package com.bit.communityProperty.activity.workplan;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.WorkTime;
import com.bit.communityProperty.widget.locktable.DisplayUtil;
import com.bit.communityProperty.widget.locktable.LockTableView;
import com.bit.communityProperty.widget.locktable.ProgressStyle;

import java.util.ArrayList;

public class PoliceWorkActivity extends BaseActivity {

    private LinearLayout llTable;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView btnBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_police_work;
    }

    @Override
    public void initViewData() {
        initView();
        initDisplayOpinion();
        initData();
    }

    private void initView(){
        llTable = findViewById(R.id.ll_table);
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvRight = findViewById(R.id.btn_right_action_bar);
        tvTitle.setText("保安排班");
        tvRight.setText("个人排班");
        tvRight.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PoliceWorkActivity.this,PersonalWorkActivity.class));
            }
        });
    }
    private void initData(){
        //构造假数据
        ArrayList<ArrayList<WorkTime>> mTableDatas = new ArrayList<ArrayList<WorkTime>>();
        ArrayList<WorkTime> mfristData = new ArrayList<WorkTime>();
        WorkTime firstData = new WorkTime();
        firstData.setWorkContent("时间");
        firstData.setType(1);
        mfristData.add(firstData);
        for (int i = 0; i < 10; i++) {
            WorkTime wt = new WorkTime();
            wt.setWorkContent("标题" + i);
            wt.setType(2);
            mfristData.add(wt);
        }
        mTableDatas.add(mfristData);
        for (int i = 0; i < 20; i++) {
            ArrayList<WorkTime> mRowDatas = new ArrayList<WorkTime>();
            WorkTime rowData = new WorkTime();
            rowData.setType(3);
            rowData.setWorkContent("标题" + i);
            mRowDatas.add(rowData);
            for (int j = 0; j < 10; j++) {
                if (j==0){
                    WorkTime wt = new WorkTime();
                    wt.setWorkContent("早班\n(8:00~14:00)");
                    wt.setType(0);
                    mRowDatas.add(wt);
                }else if (j==1){
                    WorkTime wt = new WorkTime();
                    wt.setWorkContent("中班\n(8:00~14:00)");
                    wt.setType(1);
                    mRowDatas.add(wt);
                }else if (j==2){
                    WorkTime wt = new WorkTime();
                    wt.setWorkContent("晚班\n(8:00~14:00)");
                    wt.setType(2);
                    mRowDatas.add(wt);
                }else{
                    WorkTime wt = new WorkTime();
                    wt.setWorkContent(null);
                    wt.setType(3);
                    mRowDatas.add(wt);
                }
            }
            mTableDatas.add(mRowDatas);
        }
        final LockTableView mLockTableView = new LockTableView(this, llTable, mTableDatas);
        Log.e("表格加载开始", "当前线程：" + Thread.currentThread());
        mLockTableView.setLockFristColumn(true) //是否锁定第一列
                .setLockFristRow(true) //是否锁定第一行
                .setMaxColumnWidth(100) //列最大宽度
                .setMinColumnWidth(100) //列最小宽度
                .setMinRowHeight(20)//行最小高度
                .setMaxRowHeight(40)//行最大高度
                .setTextViewSize(14) //单元格字体大小
                .setFristRowBackGroudColor(R.color.blues)//表头背景色
                .setTableHeadTextColor(R.color.white)//表头字体颜色
                .setTableContentTextColor(R.color.black)//设置单元格字体颜色
                .setNullableString("N/A") //空值替换值
                .setTableViewListener(new LockTableView.OnTableViewListener() {
                    @Override
                    public void onTableViewScrollChange(int x, int y) {
                        Log.e("滚动值","["+x+"]"+"["+y+"]");
                    }
                })//设置横向滚动回调监听
                .setTableViewRangeListener(new LockTableView.OnTableViewRangeListener() {
                    @Override
                    public void onLeft(HorizontalScrollView view) {
                        Log.e("滚动边界","滚动到最左边");
                    }

                    @Override
                    public void onRight(HorizontalScrollView view) {
                        Log.e("滚动边界","滚动到最右边");
                    }
                })//设置横向滚动边界监听
//                .setOnLoadingListener(new LockTableView.OnLoadingListener() {
//                    @Override
//                    public void onRefresh(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<WorkTime>> mTableDatas) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.e("现有表格数据", mTableDatas.toString());
//                                //构造假数据
//                                ArrayList<ArrayList<WorkTime>> mTableDatas = new ArrayList<ArrayList<WorkTime>>();
//                                ArrayList<WorkTime> mfristData = new ArrayList<WorkTime>();
//                                mfristData.add("标题");
//                                for (int i = 0; i < 10; i++) {
//                                    mfristData.add("标题" + i);
//                                }
//                                mTableDatas.add(mfristData);
//                                for (int i = 0; i < 20; i++) {
//                                    ArrayList<String> mRowDatas = new ArrayList<String>();
//                                    mRowDatas.add("标题" + i);
//                                    for (int j = 0; j < 10; j++) {
//                                        mRowDatas.add("数据" + j);
//                                    }
//                                    mTableDatas.add(mRowDatas);
//                                }
//                                mLockTableView.setTableDatas(mTableDatas);
//                                mXRecyclerView.refreshComplete();
//                            }
//                        }, 1000);
//                    }
//
//                    @Override
//                    public void onLoadMore(final XRecyclerView mXRecyclerView, final ArrayList<ArrayList<WorkTime>> mTableDatas) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (mTableDatas.size() <= 60) {
//                                    for (int i = 0; i < 10; i++) {
//                                        ArrayList<WorkTime> mRowDatas = new ArrayList<WorkTime>();
//                                        WorkTime wt = new WorkTime();
//                                        wt.setWorkContent("标题" + (mTableDatas.size() - 1));
//                                        wt.setType(4);
//                                        mRowDatas.add(wt);
//                                        for (int j = 0; j < 10; j++) {
//                                            WorkTime wts = new WorkTime();
//                                            wts.setWorkContent("数据" + j);
//                                            wts.setType(4);
//                                            mRowDatas.add(wts);
//                                        }
//                                        mTableDatas.add(mRowDatas);
//                                    }
//                                    mLockTableView.setTableDatas(mTableDatas);
//                                } else {
//                                    mXRecyclerView.setNoMore(true);
//                                }
//                                mXRecyclerView.loadMoreComplete();
//                            }
//                        }, 1000);
//                    }
//                })
                .show(); //显示表格,此方法必须调用
        mLockTableView.getTableScrollView().setPullRefreshEnabled(true);
        mLockTableView.getTableScrollView().setLoadingMoreEnabled(true);
        mLockTableView.getTableScrollView().setRefreshProgressStyle(ProgressStyle.SquareSpin);
        //属性值获取
        Log.e("每列最大宽度(dp)", mLockTableView.getColumnMaxWidths().toString());
        Log.e("每行最大高度(dp)", mLockTableView.getRowMaxHeights().toString());
        Log.e("表格所有的滚动视图", mLockTableView.getScrollViews().toString());
        Log.e("表格头部固定视图(锁列)", mLockTableView.getLockHeadView().toString());
        Log.e("表格头部固定视图(不锁列)", mLockTableView.getUnLockHeadView().toString());
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }
}
