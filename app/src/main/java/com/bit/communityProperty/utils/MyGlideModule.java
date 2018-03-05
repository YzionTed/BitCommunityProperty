package com.bit.communityProperty.utils;

import android.content.Context;

import com.bit.communityProperty.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

/**
 * 预防glide的图片setTag异常
 * Created by kezhangzhao on 2018/1/29.
 */

public class MyGlideModule implements GlideModule{
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }
}
