package com.bit.communityProperty.activity.elevator;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class IntelligentElevatorActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvRight;
    private ImageView btnBack;
    private ListView lvElevator;
    private CommonAdapter commonAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_intelligent_elevator;
    }

    @Override
    public void initViewData() {
        initView();
    }

    private void initView(){
        tvTitle = findViewById(R.id.action_bar_title);
        btnBack = findViewById(R.id.btn_back);
        tvRight = findViewById(R.id.btn_right_action_bar);
        tvTitle.setText("智能电梯");
        tvRight.setText("故障管理");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntelligentElevatorActivity.this,FaultManagerActivity.class));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lvElevator = findViewById(R.id.lv_elevator);
        commonAdapter = new CommonAdapter<String>(this,R.layout.item_intelligent_elevator) {
            @Override
            public void convert(ViewHolder holder, String o, int position, View convertView) {
                if (position==1){
                    holder.setText(R.id.tv_status, "正在维修");
                    holder.setTextDrawableLeftAndRight(R.id.tv_status, R.mipmap.icon_fix);
                }else if (position==2){
                    holder.setText(R.id.tv_status, "电梯故障");
                    holder.setTextDrawableLeftAndRight(R.id.tv_status, R.mipmap.icon_fault);
                }else{
                    holder.setText(R.id.tv_status, "正常运行");
                    holder.setTextDrawableLeftAndRight(R.id.tv_status, 0);
                }
                holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(IntelligentElevatorActivity.this,ElevatorDetailActivity.class));
                    }
                });
            }
        };
        lvElevator.setAdapter(commonAdapter);
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            s.add("");
        }
        commonAdapter.setDatas(s);
    }
}
