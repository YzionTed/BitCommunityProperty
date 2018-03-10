package com.bit.communityProperty.bean;

/**
 * 首页九宫格的item的bean类
 * Created by kezhangzhao on 2018/1/11.
 */

public class HomeMenuBean {

    private int mImageID;//图片id
    private String mImageUrl;//图片路径Url
    private String mText;//图片的文字说明

    public int getmImageID() {
        return mImageID;
    }

    public void setmImageID(int mImageID) {
        this.mImageID = mImageID;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

}
