package com.bit.communityProperty.activity.policeclock;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.policeclock.adapter.ClockLogListAdapter;
import com.bit.communityProperty.utils.FloatingBarItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ClockLogActivity extends BaseActivity {

    private TextView tvTitle;
    private ImageView ivBack;
    private RecyclerView rlvClock;
    private ClockLogListAdapter mAdapter;
    private LinkedHashMap<Integer, String> mHeaderList;
    private List<String> mContentList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_clock_log;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        ivBack = findViewById(R.id.btn_back);
        tvTitle.setText("打卡记录");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rlvClock = findViewById(R.id.rlv);
        mContentList = new ArrayList<>();
        mHeaderList = new LinkedHashMap<>();
        for (int i = 0; i < 9; i++) {
            if (i==0){
                mHeaderList.put(i, "今天");
            }else if (i==5){
                mHeaderList.put(i, "0201");
            }else if (i==7){
                mHeaderList.put(i, "0103");
            }
            mContentList.add("小区大门" + i);
        }
        rlvClock.setLayoutManager(new LinearLayoutManager(this));
        rlvClock.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        rlvClock.addItemDecoration(new FloatingBarItemDecoration(this,mHeaderList));
        mAdapter = new ClockLogListAdapter(this, mContentList);
        rlvClock.setAdapter(mAdapter);
    }
}
