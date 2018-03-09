package com.bit.communityProperty.utils;

import android.content.Context;

import com.litesuits.orm.LiteOrm;

/**
 * Created by DELL60 on 2018/3/8.
 */

public class LiteOrmUtil {

    private LiteOrm liteOrm;

    public static LiteOrmUtil getInstance() {
        return getInstance.instance;
    }

    private static class getInstance {
        private static final LiteOrmUtil instance = new LiteOrmUtil();
    }

    public LiteOrm getOrm(){
        return liteOrm;
    }

    private LiteOrmUtil() {
    }

    public LiteOrmUtil init(Context context) {
        if (liteOrm == null) {
            liteOrm = LiteOrm.newSingleInstance(context, "bitorm.db");
        }
        liteOrm.setDebugged(true); // open the log
        return getInstance.instance;
    }
}
