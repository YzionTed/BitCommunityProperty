package com.bit.communityProperty.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bit.communityProperty.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * 图片加载工具类glide
 * Created by kezhangzhao on 2018/1/25.
 */

public class GlideUtils {
    /**
     * Glide特点
     * 使用简单
     * 可配置度高，自适应程度高
     * 支持常见图片格式 Jpg png gif webp
     * 支持多种数据源  网络、本地、资源、Assets 等
     * 高效缓存策略    支持Memory和Disk图片缓存 默认Bitmap格式采用RGB_565内存使用至少减少一半
     * 生命周期集成   根据Activity/Fragment生命周期自动管理请求
     * 高效处理Bitmap  使用Bitmap Pool使Bitmap复用，主动调用recycle回收需要回收的Bitmap，减小系统回收压力
     * 这里默认支持Context，Glide支持Context,Activity,Fragment，FragmentActivity
     */

    //默认加载 网络
    public static void loadImageView(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.icon_about_us);
        options.error(R.mipmap.icon_about_us);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.centerCrop();
        options.dontAnimate();
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    public static void loadImage(final Context mContext, final String path, final ImageView mImageView) {
        final RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mContext).load(new CacheGlideUrl(path)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                Glide.with(mContext).load(path).apply(options).into(mImageView);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).apply(options).into(mImageView);
    }

    //默认加载 本地
    public static void loadImageView(Context mContext, int recourceId, ImageView mImageView) {
        Glide.with(mContext).load(recourceId).into(mImageView);
    }

    //滑翔渐入渐出
    public static void loadImageViewCross(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    //加载指定大小
    public static void loadImageViewSize(Context mContext, String path, int width, int height, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.override(width, height);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置加载中以及加载失败图片
    public static void loadImageViewLoading(Context mContext, String path, ImageView mImageView, int loadingImage, int errorImageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(loadingImage);
        options.error(errorImageView);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }


    //设置加载中以及加载失败图片
    public static void loadImageViewLoadingCircle(final Context mContext, String path, final ImageView mImageView, int loadingImage, int errorImageView) {
        try {
            RequestOptions options = new RequestOptions();
            options.placeholder(loadingImage);
            options.error(errorImageView);
            options.centerCrop();
            Glide.with(mContext).load(path).apply(options).into(mImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadImageViewLoadingSize(Context mContext, String path, int width, int height, ImageView mImageView, int loadingImage, int errorImageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(loadingImage);
        options.error(errorImageView);
        options.override(width, height);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }


    //设置跳过内存缓存
    public static void loadImageViewCache(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(true);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置下载优先级
    public static void loadImageViewPriority(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.priority(Priority.NORMAL);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadImageViewDiskCache(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }


    /**
     * 会先加载缩略图
     */

    //设置缩略图支持
    public static void loadImageViewThumbnail(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).thumbnail(0.1f).into(mImageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */

    //设置动态转换
    public static void loadImageViewCrop(Context mContext, String path, ImageView mImageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        Glide.with(mContext).load(path).apply(options).into(mImageView);
    }

    //设置动态GIF加载方式
    public static void loadImageViewDynamicGif(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(mImageView);
    }

    //清理磁盘缓存
    public static void guideClearDiskCache(Context mContext) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(mContext).clearDiskCache();
    }

    //清理内存缓存
    public static void guideClearMemory(Context mContext) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(mContext).clearMemory();
    }
}
