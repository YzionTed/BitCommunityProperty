package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class CarBrakeDetailBean {
    /**
     * currentPage : 1
     * records : [{"computerName":"PC-20171213BHNH","gateNO":2,"gateName":"出口车道1","gateStatus":1,"id":6,"inOutTag":2},{"computerName":"PC-20171213BHNH","gateNO":1,"gateName":"入口车道1","gateStatus":1,"id":5,"inOutTag":1}]
     * total : 100
     * totalPage : 10
     */

    private int currentPage;
    private int total;
    private int totalPage;
    private List<RecordsBean> records;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean implements Serializable {
        private String carNo;
        private int chargeType;
        private String inTime;

        public String getCarNo() {
            return carNo;
        }

        public void setCarNo(String carNo) {
            this.carNo = carNo;
        }

        public int getChargeType() {
            return chargeType;
        }

        public void setChargeType(int chargeType) {
            this.chargeType = chargeType;
        }

        public String getInTime() {
            return inTime;
        }

        public void setInTime(String inTime) {
            this.inTime = inTime;
        }
    }
}
