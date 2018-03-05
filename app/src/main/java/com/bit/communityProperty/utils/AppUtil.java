package com.bit.communityProperty.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;

import com.bit.communityProperty.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 获取已安装应用工具类
 *
 * @author ben
 */
public class AppUtil {

    private AppUtil() {
    }

    public static JSONArray Tj(String key, String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        JSONArray list = null;
        if (null != json) {
            list = json.optJSONArray(key);
        }
        return list;
    }

    /**
     * 获取已安装的APP列表
     *
     * @return
     */
    public static HashMap<String, HashMap<String, Object>> getAppList(Context context) {
        HashMap<String, HashMap<String, Object>> items = new HashMap<>();

        // 得到PackageManager对象
        PackageManager pm = context.getPackageManager();
        // 得到系统安装的所有程序包的PackageInfo对象
        // List<ApplicationInfo> packs = pm.getInstalledApplications(0);
        List<PackageInfo> packs = pm.getInstalledPackages(0);

        for (PackageInfo pi : packs) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            // 显示用户安装的应用程序，而不显示系统程序
            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0
                    && (pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0) {
                // 这将会显示所有安装的应用程序，包括系统应用程序
                map.put("icon", pi.applicationInfo.loadIcon(pm));// 图标
                map.put("appName", pi.applicationInfo.loadLabel(pm));// 应用程序名称
                map.put("packageName", pi.applicationInfo.packageName);// 应用程序包名
                map.put("versionName", pi.versionName);// 应用程序版本号

                items.put(pi.applicationInfo.packageName.toString(), map);
            }
        }
        return items;
    }

    /**
     * 获取内核版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取外部版本号
     *
     * @return
     */
    public static String getVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return info.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    /**
     * 获取应用包名
     *
     * @return
     */
    public static String getPackageName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return info.packageName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查版本号
     * @param context
     * @param code
     * @return
     */
    public static boolean checkVersionCode(Context context, String code) {
        boolean isUpdate = false;
        try {
            for (int i = 0; i < code.length(); i++) {
                if (!Character.isDigit(code.charAt(i))) {
                    return false;
                }
            }
            int versionCode = Integer.parseInt(code);
            isUpdate = versionCode > getVersionCode(context);
            return isUpdate;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return isUpdate;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取app名称
     * @param mContext
     * @return
     */
    public static String getAppName(Context mContext) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = mContext.getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    public static String getImei(){
        TelephonyManager TelephonyMgr = (TelephonyManager) MyApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }

}
