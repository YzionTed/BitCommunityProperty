package com.bit.communityProperty.view.StarsView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bit.communityProperty.R;

import java.util.ArrayList;

/**
 * 评价星星的自定义view
 * Created by kezhangzhao on 2018/2/12.
 */

public class StarsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;
    private int yellowNum = 0;
    private int grayNum = 5;
    private ArrayList<Boolean> listData = new ArrayList<>();
    private int mStarsHeight = ViewGroup.LayoutParams.WRAP_CONTENT;//星星的高度
    private int mStarsWidth = ViewGroup.LayoutParams.WRAP_CONTENT;//星星的宽度
    private int mPaddingLeft = 0;//左
    private int mPaddingTop = 0;//上
    private int mPaddingRight = 0;//右
    private int mPaddingBottom = 0;//下
    private int mImageYellow= R.mipmap.stars_yellow;//图片：默认是黄色星星
    private int mImageGray= R.mipmap.stars_gray;//图片：默认是黄色星星


    /**
     * 构造方法
     *
     * @param context
     * @param yellowNum 黄色星星数量
     * @param grayNum   灰色星星数量
     */
    public StarsAdapter(Context context, int yellowNum, int grayNum) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.yellowNum = yellowNum;
        this.grayNum = grayNum;
        for (int i = 0; i < yellowNum; i++) {
            listData.add(true);
        }
        if (grayNum - yellowNum > 0) {
            for (int j = 0; j < grayNum - yellowNum; j++) {
                listData.add(false);
            }
        }

    }

    /**
     * 构造方法
     * 使用默认星星数量，灰色5个，黄色0个。
     * 可以通过方法重新进行设置星星数量
     *
     * @param context
     */
    public StarsAdapter(Context context) {
        this.mContext = context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < yellowNum; i++) {
            listData.add(true);
        }
        if (grayNum - yellowNum > 0) {
            for (int j = 0; j < grayNum - yellowNum; j++) {
                listData.add(false);
            }
        }
    }

    /**
     * 设置图片
     * @param imageYellow 成功点评的黄色星星图片
     * @param imageGray 灰色星星图片
     */
    public void setImage(int imageYellow,int imageGray){
        this.mImageYellow = imageYellow;
        this.mImageGray = imageGray;
    }

    /**
     * 设置星星数量
     *
     * @param yellowNum 黄色星星数量
     * @param grayNum   灰色星星数量
     */
    public void setStarsNum(int yellowNum, int grayNum) {
        this.yellowNum = yellowNum;
        this.grayNum = grayNum;
        listData.clear();
        for (int i = 0; i < yellowNum; i++) {
            listData.add(true);
        }
        if (grayNum - yellowNum > 0) {
            for (int j = 0; j < grayNum - yellowNum; j++) {
                listData.add(false);
            }
        }
    }

    /**
     * 设置单个星星图片的宽高
     *
     * @param starsWidth
     * @param starsHeight
     */
    public void setStarsWidthAndHeight(int starsWidth, int starsHeight) {
        this.mStarsWidth = starsWidth;
        this.mStarsHeight = starsHeight;
    }


    /**
     * 设置单个星星的padding
     *
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.mPaddingLeft = paddingLeft;
        this.mPaddingTop = paddingTop;
        this.mPaddingRight = paddingRight;
        this.mPaddingBottom = paddingBottom;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.stars_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mStarsWidth, mStarsHeight);
        viewHolder.imageView.setLayoutParams(layoutParams);
        viewHolder.imageView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (listData.get(position)) {
            viewHolder.imageView.setImageResource(mImageYellow);
            viewHolder.layoutVeiw.addView(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(mImageGray);
            viewHolder.layoutVeiw.addView(viewHolder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    /**
     * adapter的ViewHolder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutVeiw;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            layoutVeiw = itemView.findViewById(R.id.layout_view);
            imageView = new ImageView(mContext);
        }
    }
}
