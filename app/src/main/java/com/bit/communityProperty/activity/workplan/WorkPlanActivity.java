package com.bit.communityProperty.activity.workplan;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.workplan.adapter.WorkPlanListAdapter;
import com.bit.communityProperty.utils.FloatingBarItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WorkPlanActivity extends BaseActivity {

    private RecyclerView rlvPlan;
    private LinkedHashMap<Integer, String> mHeaderList;
    private List<String> mContentList;
    private WorkPlanListAdapter mAdapter;
    private TextView tvTitle;
    private ImageView btnBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_work_plan;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvTitle.setText("工作排班");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlvPlan = findViewById(R.id.rlv);
        mContentList = new ArrayList<>();
        mHeaderList = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            if (i==0){
                mHeaderList.put(i, 1 + "月份排班");
            }else if (i==5){
                mHeaderList.put(i, i + "月份排班");
            }else if (i==9){
                mHeaderList.put(i, i + "月份排班");
            }
            mContentList.add("数据" + i);
        }
        rlvPlan.setLayoutManager(new LinearLayoutManager(this));
        rlvPlan.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rlvPlan.addItemDecoration(new FloatingBarItemDecoration(this,mHeaderList));
        mAdapter = new WorkPlanListAdapter(this, mContentList);
        rlvPlan.setAdapter(mAdapter);
    }
}
