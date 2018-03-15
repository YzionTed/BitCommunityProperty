package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/10.
 */

public class ElevatorDetailBean {
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
        /**
         * computerName : PC-20171213BHNH
         * gateNO : 2
         * gateName : 出口车道1
         * gateStatus : 1
         * id : 6
         * inOutTag : 2
         */

        private String userName;
        private String time;
        private String userCommand;
        private int id;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUserCommand() {
            return userCommand;
        }

        public void setUserCommand(String userCommand) {
            this.userCommand = userCommand;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
