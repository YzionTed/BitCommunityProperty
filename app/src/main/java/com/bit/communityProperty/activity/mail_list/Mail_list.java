package com.bit.communityProperty.activity.mail_list;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.activity.mail_list.adapter.Mail_ListAdapter;
import com.bit.communityProperty.utils.FloatingBarItemDecoration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 23 on 2018/2/7.
 */

public class Mail_list extends BaseActivity {
    @BindView(R.id.action_bar_title)
    TextView actionBarTitle;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_right_action_bar)
    TextView btnRightActionBar;
    @BindView(R.id.iv_right_action_bar)
    ImageView ivRightActionBar;
    @BindView(R.id.pb_loaing_action_bar)
    ProgressBar pbLoaingActionBar;
    @BindView(R.id.action_bar)
    RelativeLayout actionBar;
    @BindView(R.id.mail_list_ml)
    RecyclerView mailListMl;
    private LinkedHashMap<Integer, String> mHeaderList;
    private List<String> mContentList;
    Mail_ListAdapter adapter;
    @Override
    public int getLayoutId() {
        return R.layout.mail_list_activity;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("通讯录");
        mContentList = new ArrayList<>();
        mHeaderList = new LinkedHashMap<>();
        mHeaderList = new LinkedHashMap<>();
        for (int i = 0; i < 20; i++) {
            if (i==0){
                mHeaderList.put(i,"管理处");
            }else if (i==5){
                mHeaderList.put(i, "保安部");
            }
            mContentList.add("数据" + i);
        }
        mailListMl.setLayoutManager(new LinearLayoutManager(this));
        mailListMl.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mailListMl.addItemDecoration(new FloatingBarItemDecoration(this,mHeaderList));
        adapter = new Mail_ListAdapter(this, mContentList);
        mailListMl.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
        }
    }
}
