package com.bit.communityProperty.utils;

import com.google.gson.Gson;

/**
 * Created by 23 on 2018/2/8.
 */

public class GsonUtils {
    private static Gson instance;
    private GsonUtils(){}
    public static Gson getInstance(){
        if (instance == null) {
            instance = new Gson();
        }
        return instance;
    }
}
