package com.bit.communityProperty.utils;

import java.util.Random;

/**
 * 字符串工具类
 * Created by kezhangzhao on 2018/1/9.
 */

public class StringUtils {


    /**
     * 产生随机字符串
     */
    private static Random randGen = null;
    private static char[] numbersAndLetters = null;

    public static final String randomString(int length) {
        if (length < 1) {
            return null;
        }
        if (randGen == null) {
            randGen = new Random();
            numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
                    .toCharArray();
        }
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }
    public static boolean isBlank(String s){
        if(s == null || s.length() <= 0){
            return true;
        }else
            return false;
    }


    public static String getBucket(String str){
        try {
            if (str!=null){
                String[] sResult = str.split("_");
                if (sResult!=null&&sResult.length>=3){
                    return sResult[1];
                }
            }
        }catch (Exception e){

        }
        return "bit-smcm-img";
    }
}
