package com.bit.communityProperty.net;

/**
 * Created by 23 on 2018/2/10.
 */

public class Api {
    private static Api instance;

    private Api() {
    }

    /**
     * @return
     */
    public static HttpService getInstance() {
        if (instance == null) {
            instance = new Api();
        }

        return RetrofitManage.getInstance().getRetrofit().create(HttpService.class);
    }
}
