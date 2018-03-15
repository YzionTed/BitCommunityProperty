package com.bit.communityProperty.Bluetooth.mili;

/**
 * Created by Dell on 2018/2/11.
 */

public class MiLiState {

    public static final int MILI_NOFIND_DEVICE = 2001;//2001  未发现门锁设备
    public static final int MILI_CONNECT_FAULSE = 2002;//2002  门锁连接失败
    public static final int MILI_CONNECT_OUTTIME = 2003;//2003  门锁连接超时
    public static final int MILI_NOFIND_SERVICE = 2004;//2004  未发现蓝牙服务
    public static final int MILI_NOFIND_FEATURE = 2005;//2005  未发现蓝牙特征
    public static final int MILI_NOTICE_FAULSE = 2006;//2006  蓝牙通知失败
    public static final int MILI_YANZHENG_FAULSE = 2010;//2010  蓝牙验证失败
    public static final int MILI_SUCCESS = 1020;//2010  操作成功

    public static String getCodeDesc(String code) {
        String desc = "";
        try {
            int i = Integer.parseInt(code);
            switch (i) {
                case MILI_NOFIND_DEVICE:
                    desc = "未发现门锁设备";
                    break;
                case MILI_CONNECT_FAULSE:
                    desc = "门锁连接失败";
                    break;
                case MILI_CONNECT_OUTTIME:
                    desc = "门锁连接超时";
                    break;
                case MILI_NOFIND_FEATURE:
                    desc = "未发现蓝牙特征";
                    break;
                case MILI_NOTICE_FAULSE:
                    desc = "蓝牙通知失败";
                    break;
                case MILI_NOFIND_SERVICE:
                    desc = "未发现蓝牙服务";
                    break;
                case MILI_YANZHENG_FAULSE:
                    desc = "蓝牙验证失败";
                    break;
                case MILI_SUCCESS:
                    desc = "操作成功";
                    break;
            }
        } catch (Exception e) {
            return code;
        }
        return desc;
    }


}
