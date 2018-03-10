package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class CarBrakeBean implements Serializable{

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

    public static class RecordsBean implements Serializable{
        /**
         * computerName : PC-20171213BHNH
         * gateNO : 2
         * gateName : 出口车道1
         * gateStatus : 1
         * id : 6
         * inOutTag : 2
         */

        private String computerName;
        private String gateNO;
        private String gateName;
        private int gateStatus;
        private int id;
        private int inOutTag;

        public String getComputerName() {
            return computerName;
        }

        public void setComputerName(String computerName) {
            this.computerName = computerName;
        }

        public String getGateNO() {
            return gateNO;
        }

        public void setGateNO(String gateNO) {
            this.gateNO = gateNO;
        }

        public String getGateName() {
            return gateName;
        }

        public void setGateName(String gateName) {
            this.gateName = gateName;
        }

        public int getGateStatus() {
            return gateStatus;
        }

        public void setGateStatus(int gateStatus) {
            this.gateStatus = gateStatus;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInOutTag() {
            return inOutTag;
        }

        public void setInOutTag(int inOutTag) {
            this.inOutTag = inOutTag;
        }
    }
}
