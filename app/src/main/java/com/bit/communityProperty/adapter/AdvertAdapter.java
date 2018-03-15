package com.bit.communityProperty.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 广告栏的ViewPager Adapter
 * Created by kezhangzhao on 2018/1/11.
 */

public class AdvertAdapter extends PagerAdapter{

    private List<View> mListViews;//view的list数据


    /**
     * 构造方法
     * @param listViews 传过来的view的list
     */
    public AdvertAdapter(List<View> listViews){
        this.mListViews = listViews;
    }

    /**
     * 这个方法来实例化一个view
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mListViews.get(position),0);//添加view
        return mListViews.get(position);
    }

    /**
     * 删除页面
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mListViews.get(position));//删除view
    }

    /**
     * 数量
     * @return
     */
    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }
}
