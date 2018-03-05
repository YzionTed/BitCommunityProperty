package com.bit.communityProperty.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.bit.communityProperty.data.PreferenceConst;

/**
 * 存储xml的一些字段工具类
 * Created by kezhangzhao on 2018/1/9.
 */

public class PreferenceUtils {

    /**
     * 获取别名
     *
     * @return
     */
    public static String createAlias(Context context) {
        String Alias = "";
//        Alias = decryptDes(PreferenceUtils.getPrefString(context, PreferenceConst.PRE_JPUT, PreferenceConst.ALIAS, ""));
        Alias = PreferenceUtils.getPrefString(context, PreferenceConst.PRE_JPUT, PreferenceConst.ALIAS, "");
        if ("".equals(Alias)) {
            Alias = StringUtils.randomString(40);
            PreferenceUtils.setPrefString(context, PreferenceConst.PRE_JPUT, PreferenceConst.ALIAS, Alias);
        }
        return Alias;
    }

    private static String getPrefString(Context context, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue);
    }

    public static String getPrefString(Context context, String prefName, String key, String defValue) {
        if (TextUtils.isEmpty(prefName)) {
            return getPrefString(context, key, defValue);
        } else {

            String val = context.getSharedPreferences(prefName, 0).getString(key, defValue);
            return val;
        }
    }

    private static void setPrefString(Context context, String key, String value) {

        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static void setPrefString(Context context, String prefName, String key, String value) {

        if (TextUtils.isEmpty(prefName)) {
            setPrefString(context, key, value);
        } else {
            context.getSharedPreferences(prefName, 0).edit().putString(key, value).commit();
        }
    }
//    private static String encryptStr(String str) {
//        String s = "";
//        try {
//            s = DesUtil.encryptDES(str, DesUtil.DESKey);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return s;
//    }
//    private static String decryptDes(String str) {
//        String decrypt = "";
//        try {
//            decrypt = DesUtil.decryptDES(str, DesUtil.DESKey);
//        } catch (Exception e) {
////            LogManager.writeErrorLog("PreferenceUtils-decryptDes-String is " + str + e.toString());
//            e.printStackTrace();
//        }
//
//        return decrypt;
//    }
}
