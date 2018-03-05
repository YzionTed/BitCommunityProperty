package com.bit.communityProperty.utils;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;

/**
 * Created by DELL60 on 2018/3/3.
 */

public class CacheGlideUrl extends GlideUrl {
    private String mUrl;

    public CacheGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return checkUrl() && !TextUtils.isEmpty(getGCacheKey()) ? getGCacheKey() : super.getCacheKey();
    }

    private String getGCacheKey() {
        String cacheKey = null;
        int index = mUrl.indexOf("?");
        if (index != -1) {
            cacheKey = mUrl.substring(0, index);
        }
        return cacheKey;
    }

    public boolean checkUrl() {
        if (!TextUtils.isEmpty(mUrl) && (mUrl.contains("&OSSAccessKeyId=") || mUrl.contains("?OSSAccessKeyId="))) {
            return true;
        }
        return false;
    }
}
