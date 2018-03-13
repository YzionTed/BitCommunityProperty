package com.bit.communityProperty.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2018/2/28.
 */

public class StoreDoorMILiBeanList {

    private List<DoorMILiBean> doorMILiBeans;

    private long storeTime;

    public List<DoorMILiBean> getDoorMILiBeans() {
        return doorMILiBeans;
    }

    public void setDoorMILiBeans(List<DoorMILiBean> doorMILiBeans) {
        this.doorMILiBeans = doorMILiBeans;
    }

    public long getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(long storeTime) {
        this.storeTime = storeTime;
    }


    /**
     * 是否保存时间超过半个小时
     *
     * @return
     */
    public boolean isTimeOutNow() {
        if (storeTime > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            long time = new Date().getTime();
//            Log.e("时间", "保存时间：" + dateFormat.format(storeTime));
//            Log.e("时间", "现在时间：" + dateFormat.format(time));

            long distanceTime = new Date().getTime() - storeTime;
            if (distanceTime > 30 * 60 * 1000) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

}
