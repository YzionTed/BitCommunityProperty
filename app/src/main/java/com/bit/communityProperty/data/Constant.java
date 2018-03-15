package com.bit.communityProperty.data;

import com.bit.communityProperty.MyApplication;

import java.io.File;

/**
 * Created by kezhangzhao on 2018/1/9.
 */

public class Constant {
    public static final String PATH_DATA = MyApplication.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";
}
