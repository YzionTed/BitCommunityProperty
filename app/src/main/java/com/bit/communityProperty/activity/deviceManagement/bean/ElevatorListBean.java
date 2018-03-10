package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class ElevatorListBean implements Serializable{

    /**
     * total : 6
     * records : [{"deviceNum":"460060514066888","houseName":"办公楼","brandName":"鑫诺","buildName":"1","name":"一号梯","elevatorStatus":"0","id":"5a9408db94766d7177907908","elevatorTypeName":"MRGL-P"},{"deviceNum":"5a1b7c2e0cf29ea40f583b16","houseName":"物联网卡测试2","brandName":"鑫诺","buildName":"1","name":null,"elevatorStatus":"0","id":"5a9408dc94766d7177907935","elevatorTypeName":"MRGL-P"},{"deviceNum":"460060288080289","houseName":"办公楼","brandName":"鑫诺","buildName":"1","name":"一号梯","elevatorStatus":"0","id":"5a96ac5694766d71779c6547","elevatorTypeName":"MRGL-P"},{"deviceNum":"460060288080289","houseName":"办公楼","brandName":"鑫诺","buildName":"1","name":"一号梯","elevatorStatus":"0","id":"5a9f53bc94766d7177c53344","elevatorTypeName":"MRGL-P"},{"deviceNum":"460060288080289","houseName":"办公楼","brandName":"鑫诺","buildName":"1","name":"一号梯","elevatorStatus":"0","id":"5a9f53f594766d7177c534a7","elevatorTypeName":"MRGL-P"},{"deviceNum":"460067113096170","houseName":"办公楼","brandName":"鑫诺","buildName":"1","name":"5号梯","elevatorStatus":"0","id":"5a9f879394766d7177c63a01","elevatorTypeName":"MRGL-P"}]
     * totalPage : 1
     * pageSize : 10
     * currentPage : 1
     */

    private int total;
    private int totalPage;
    private int pageSize;
    private int currentPage;
    private List<RecordsBean> records;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean implements Serializable{
        /**
         * deviceNum : 460060514066888
         * houseName : 办公楼
         * brandName : 鑫诺
         * buildName : 1
         * name : 一号梯
         * elevatorStatus : 0
         * id : 5a9408db94766d7177907908
         * elevatorTypeName : MRGL-P
         */

        private String deviceNum;
        private String houseName;
        private String brandName;
        private String buildName;
        private String name;
        private int elevatorStatus;
        private String id;
        private String elevatorTypeName;

        public String getDeviceNum() {
            return deviceNum;
        }

        public void setDeviceNum(String deviceNum) {
            this.deviceNum = deviceNum;
        }

        public String getHouseName() {
            return houseName;
        }

        public void setHouseName(String houseName) {
            this.houseName = houseName;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getBuildName() {
            return buildName;
        }

        public void setBuildName(String buildName) {
            this.buildName = buildName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getElevatorStatus() {
            return elevatorStatus;
        }

        public void setElevatorStatus(int elevatorStatus) {
            this.elevatorStatus = elevatorStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getElevatorTypeName() {
            return elevatorTypeName;
        }

        public void setElevatorTypeName(String elevatorTypeName) {
            this.elevatorTypeName = elevatorTypeName;
        }
    }
}
