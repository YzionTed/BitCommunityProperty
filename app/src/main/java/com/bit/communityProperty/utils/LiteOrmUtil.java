package com.bit.communityProperty.utils;

import android.content.Context;

import com.bit.communityProperty.bean.CardListBean;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

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

    public CardListBean.RecordsBean queryById(String userId,String communityId){
        QueryBuilder<CardListBean.RecordsBean> qb = new QueryBuilder<CardListBean.RecordsBean>(CardListBean.RecordsBean.class)
                .whereEquals(CardListBean.USER_ID, userId)
                .whereAppendAnd()
                .whereEquals(CardListBean.COMMUNITY_ID, communityId);
        if (liteOrm.query(qb)!=null&&liteOrm.query(qb).size()>0){
            CardListBean.RecordsBean result = liteOrm.query(qb).get(0);
            return result;
        }
        return null;
    }
}
