package com.bit.communityProperty.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DELL60 on 2018/2/5.
 */

public class TimeUtils {
    /**
     * 将毫秒转换为mm:ss
     * @param duration
     * @return
     */
    public static String timeParse(long duration) {
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";

        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSSS");
        return sdf.format(new java.util.Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentDateWithHMS() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTimeWithT() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return sdf.format(new java.util.Date());
    }

    /**
     * 是否过期
     * @param endTime
     * @return
     */
    public static boolean isExpiration(String endTime){
        if (getCurrentTimeWithT().compareTo(endTime)>0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(new java.util.Date());
    }

    public static String getCurrentWeek(){
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        return dateFm.format(date);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        if (s==null){
            return "";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateWithHm(String s){
        if (s==null){
            return "0";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateWithHms(String s){
        if (s==null){
            return "0";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static String stampToDateWithHms(long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateWithHm(long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToMonthDayWithHm(String s){
        if (s==null){
            return "0";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    /*
     * 将时间戳转换为时间
     */
    public static String stampToMonthDayWithHm(Long lt){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
}
