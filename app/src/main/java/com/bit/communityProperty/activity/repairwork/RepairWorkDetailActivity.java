package com.bit.communityProperty.activity.repairwork;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RepairWorkDetailActivity extends BaseActivity {

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
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_reason)
    TextView tvReason;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.fault_describe)
    TextView faultDescribe;
    @BindView(R.id.ns_grid)
    NoScrollGridView nsGrid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_repair_work_detail;
    }

    @Override
    public void initViewData() {
        actionBarTitle.setText("工单详情");
        setPicData();
    }

    private void setPicData(){
        CommonAdapter mAdapter = new CommonAdapter<String>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, String s, final int position, View convertView) {
                holder.setOnClickListener(R.id.grid_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (position) {
                            case 0://第一个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 1://第二个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 2://第三个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                            case 3://第四个
                                Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }
        };
        nsGrid.setAdapter(mAdapter);
        List<String> ss = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ss.add("");
        }
        mAdapter.setDatas(ss);
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        finish();
    }
}
