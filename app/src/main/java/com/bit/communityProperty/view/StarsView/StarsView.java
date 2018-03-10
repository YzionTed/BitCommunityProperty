package com.bit.communityProperty.view.StarsView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bit.communityProperty.R;

/**
 * 五星评价布局
 * Created by kezhangzhao on 2018/2/12.
 */

public class StarsView extends LinearLayout {

    private Context mContext;
    private RecyclerView mStarsList;//星星列表
    private StarsAdapter mStrasAdapter;//星星的adapter

    public StarsView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public StarsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_stars, this);
        mStarsList = findViewById(R.id.list_stars);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStarsList.setLayoutManager(linearLayoutManager);
        mStrasAdapter = new StarsAdapter(mContext);
        mStarsList.setAdapter(mStrasAdapter);
        mStarsList.setItemAnimator(new DefaultItemAnimator());
//        mStrasAdapter
//        mStrasAdapter.setOnMyItemClickListener(new StarsAdapter.OnMyItemClickListener() {
//            @Override
//            public void myClick(View v, int pos) {
//                Toast.makeText(mContext,pos+"",Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    /**
     * 设置两种状态的图片
     * @param imageYellow 成功点评的黄色星星图片
     * @param imageGray 灰色星星图片
     */
    public  void setImage(int imageYellow,int imageGray){
        mStrasAdapter.setImage(imageYellow,imageGray);
        mStrasAdapter.notifyDataSetChanged();
    }

    /**
     * 设置星星数量
     *
     * @param yellowNum 黄色数量
     * @param grayNum   灰色数量
     */
    public void setmStarsNum(int yellowNum, int grayNum) {
        mStrasAdapter.setStarsNum(yellowNum, grayNum);
        mStrasAdapter.notifyDataSetChanged();
    }

    /**
     * 设置单个星星的宽度和高度
     *
     * @param width  宽
     * @param height 高
     */
    public void setStarsWidthAndHeight(int width, int height) {
        mStrasAdapter.setStarsWidthAndHeight(width, height);
        mStrasAdapter.notifyDataSetChanged();
    }

    /**
     * 设置单个星星的padding
     *
     * @param paddingLeft   左
     * @param paddingTop    上
     * @param paddingRight  右
     * @param paddingBottom 下
     */
    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        mStrasAdapter.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        mStrasAdapter.notifyDataSetChanged();
    }
}
