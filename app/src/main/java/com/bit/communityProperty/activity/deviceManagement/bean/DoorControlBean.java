package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 门禁列表的bean类
 * Created by kezhangzhao on 2018/3/1.
 */

public class DoorControlBean {


    /**
     * currentPage : 1
     * records : [{"buildingId":"5a82ae1db06c97e0cd6c0f41","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519622879388,"creatorId":"5a82a4ac9ce93e30677c3fa0","dataStatus":1,"deviceCode":"123412412341","deviceId":1670,"deviceName":"蓝牙001","doorStatus":null,"doorType":2,"guardSwitch":null,"id":"5a939adf9ce97b250640f325","mac":"4D4C00000067","name":"蓝牙001设备","onlineStatus":1,"pin":"Bh8YtN0d","rank":0,"serialNo":"100000000001","serviceId":1,"terminalCode":null,"terminalPort":null,"updateAt":1519633035878,"yunDeviceId":null},{"buildingId":"5a82ae1db06c97e0cd6c0f43","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519803669434,"creatorId":"5a8bd1c5d728e7a7da12e342","dataStatus":1,"deviceCode":null,"deviceId":1936,"deviceName":"蓝牙测试1","doorStatus":null,"doorType":2,"guardSwitch":null,"id":"5a965d159ce9abb08ccf3e4a","mac":"4D4C00000067","name":"蓝牙测试1","onlineStatus":2,"pin":"Bh8YtN0d","rank":0,"serialNo":"4D4C00000067","serviceId":1,"terminalCode":null,"terminalPort":null,"updateAt":1519804044942,"yunDeviceId":null}]
     * total : 2
     * totalPage : 1
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
         * buildingId : 5a82ae1db06c97e0cd6c0f41
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * createAt : 1519622879388
         * creatorId : 5a82a4ac9ce93e30677c3fa0
         * dataStatus : 1
         * deviceCode : 123412412341
         * deviceId : 1670
         * deviceName : 蓝牙001
         * doorStatus : null
         * doorType : 2
         * guardSwitch : null
         * id : 5a939adf9ce97b250640f325
         * mac : 4D4C00000067
         * name : 蓝牙001设备
         * onlineStatus : 1
         * pin : Bh8YtN0d
         * rank : 0
         * serialNo : 100000000001
         * serviceId : 1
         * terminalCode : null
         * terminalPort : null
         * updateAt : 1519633035878
         * yunDeviceId : null
         */

        private String buildingId;
        private String communityId;
        private long createAt;
        private String creatorId;
        private int dataStatus;
        private String deviceCode;
        private int deviceId;
        private String deviceName;
        private int doorStatus;
        private int doorType;
        private Object guardSwitch;
        private String id;
        private String mac;
        private String name;
        private int onlineStatus;
        private String pin;
        private int rank;
        private String serialNo;
        private int serviceId;
        private Object terminalCode;
        private Object terminalPort;
        private long updateAt;
        private Object yunDeviceId;

        public String getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(String buildingId) {
            this.buildingId = buildingId;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public int getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(int dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public int getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(int deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getDoorStatus() {
            return doorStatus;
        }

        public void setDoorStatus(int doorStatus) {
            this.doorStatus = doorStatus;
        }

        public int getDoorType() {
            return doorType;
        }

        public void setDoorType(int doorType) {
            this.doorType = doorType;
        }

        public Object getGuardSwitch() {
            return guardSwitch;
        }

        public void setGuardSwitch(Object guardSwitch) {
            this.guardSwitch = guardSwitch;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(int onlineStatus) {
            this.onlineStatus = onlineStatus;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public int getServiceId() {
            return serviceId;
        }

        public void setServiceId(int serviceId) {
            this.serviceId = serviceId;
        }

        public Object getTerminalCode() {
            return terminalCode;
        }

        public void setTerminalCode(Object terminalCode) {
            this.terminalCode = terminalCode;
        }

        public Object getTerminalPort() {
            return terminalPort;
        }

        public void setTerminalPort(Object terminalPort) {
            this.terminalPort = terminalPort;
        }

        public long getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(long updateAt) {
            this.updateAt = updateAt;
        }

        public Object getYunDeviceId() {
            return yunDeviceId;
        }

        public void setYunDeviceId(Object yunDeviceId) {
            this.yunDeviceId = yunDeviceId;
        }
    }
}
