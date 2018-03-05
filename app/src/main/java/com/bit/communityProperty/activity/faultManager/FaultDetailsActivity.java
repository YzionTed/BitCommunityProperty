package com.bit.communityProperty.activity.faultManager;

import android.view.View;
import android.widget.Toast;

import com.bit.communityProperty.R;
import com.bit.communityProperty.base.BaseActivity;
import com.bit.communityProperty.bean.FaultManagerCommonBean;
import com.bit.communityProperty.bean.HomeMenuBean;
import com.bit.communityProperty.utils.CommonAdapter;
import com.bit.communityProperty.utils.ViewHolder;
import com.bit.communityProperty.view.StarsView.StarsView;
import com.bit.communityProperty.view.TitleBarView;
import com.bit.communityProperty.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 故障详情页面
 * Created by kezhangzhao on 2018/1/20.
 */

public class FaultDetailsActivity extends BaseActivity {

    private TitleBarView mTitleBarView;//标题栏
    private FaultManagerCommonBean commonBean;
    private ArrayList<HomeMenuBean> mHomeMenuBeanList = new ArrayList<>();
    private NoScrollGridView noScrollGridView;//评论图片布局
    private StarsView mStarsView;//五星评论自定义view
    private CommonAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fault_details;
    }

    @Override
    public void initViewData() {
        this.commonBean = (FaultManagerCommonBean) getIntent().getSerializableExtra("FaultManagerCommonBean");
        initView();
        initDate();
    }

    /**
     * 初始化View
     */
    private void initView() {
        mTitleBarView = findViewById(R.id.titlebarview);
        mTitleBarView.setTvTitleText("故障详情");
        mTitleBarView.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        noScrollGridView = findViewById(R.id.ns_grid);
        mStarsView = findViewById(R.id.view_stars);
    }

    /**
     * 初始化Date
     */
    private void initDate() {
        initGridView();
        mStarsView.setmStarsNum(3,5);//设置黄色星星和灰色星星数量
        mStarsView.setPadding(3,3,3,3);//设置单个星星的padding
    }

    /**
     * 初始化网格布局：图片评论
     */
    private void initGridView() {
        mAdapter = new CommonAdapter<HomeMenuBean>(mContext, R.layout.picture_comment_item) {
            @Override
            public void convert(ViewHolder holder, HomeMenuBean homeMenuBean, final int position, View convertView) {
                holder.setImageResource(R.id.iv_icon, homeMenuBean.getmImageID());
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
        noScrollGridView.setAdapter(mAdapter);
        List<HomeMenuBean> mainWorkBeanList = new ArrayList<>();
        HomeMenuBean bean = new HomeMenuBean();//暂时用这个bean类
        bean.setmImageID(R.mipmap.add);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mainWorkBeanList.add(bean);
        mAdapter.setDatas(mainWorkBeanList);
    }
}
