package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 摄像头bean类
 * 对应获取摄像头列表接口
 * Created by kezhangzhao on 2018/3/1.
 */

public class CameraBean {


    /**
     * currentPage : 1
     * records : [{"brand":"美国TEO","buildingId":"5a82ae1db06c97e0cd6c0f3f","callName":"Lusifer","callPassword":"123","callURL":"摄像机接口","cameraCode":"F600","cameraStatus":1,"cameraType":"数字摄像机","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519460149651,"creatorId":"5a82a37d9ce93e30677c3f9c","dataStatus":1,"deviceId":"6c82ae1db06c97e0cd6c0f3d","id":"5a911f35e939efffd944baa6","mac":"64646","name":"摄像头3","rank":1,"temporaryAuthorized":false,"updateAt":1519460149651},{"brand":"美国TEO","buildingId":"5a82ae1db06c97e0cd6c0f3f","callName":"Lusifer","callPassword":"123","callURL":"摄像机接口","cameraCode":"F600","cameraStatus":1,"cameraType":"数码摄像机","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519526310753,"creatorId":"5a82a37d9ce93e30677c3f9c","dataStatus":1,"deviceId":"5a82adf3b06c97e0cd6c0f3d","id":"5a9221a6e939a7e81169e574","mac":"64646","name":"摄像头3","rank":1,"temporaryAuthorized":false,"updateAt":1519526310753},{"brand":"美国TEO","buildingId":"5a82ae1db06c97e0cd6c0f3f","callName":"Lusifer","callPassword":"123","callURL":"摄像机接口","cameraCode":"F600","cameraStatus":1,"cameraType":"数码摄像机B","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519526323062,"creatorId":"5a82a37d9ce93e30677c3f9c","dataStatus":1,"deviceId":"5a82adf3b06c97e0cd6c0f3d","id":"5a9221b3e939a7e81169e578","mac":"64646","name":"摄像头3","rank":1,"temporaryAuthorized":false,"updateAt":1519526323062},{"brand":"美国TEO","buildingId":"5a82ae1db06c97e0cd6c0f3f","callName":"Lusifer","callPassword":"123","callURL":"摄像机接口","cameraCode":"F600","cameraStatus":1,"cameraType":"数码摄像机C","communityId":"5a82adf3b06c97e0cd6c0f3d","createAt":1519526330040,"creatorId":"5a82a37d9ce93e30677c3f9c","dataStatus":1,"deviceId":"5a82adf3b06c97e0cd6c0f3d","id":"5a9221bae939a7e81169e57c","mac":"64646","name":"摄像头3","rank":1,"temporaryAuthorized":false,"updateAt":1519526330040}]
     * total : 4
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

    public static class RecordsBean implements Serializable{
        /**
         * brand : 美国TEO
         * buildingId : 5a82ae1db06c97e0cd6c0f3f
         * callName : Lusifer
         * callPassword : 123
         * callURL : 摄像机接口
         * cameraCode : F600
         * cameraStatus : 1
         * cameraType : 数字摄像机
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * createAt : 1519460149651
         * creatorId : 5a82a37d9ce93e30677c3f9c
         * dataStatus : 1
         * deviceId : 6c82ae1db06c97e0cd6c0f3d
         * id : 5a911f35e939efffd944baa6
         * mac : 64646
         * name : 摄像头3
         * rank : 1
         * temporaryAuthorized : false
         * updateAt : 1519460149651
         */

        private String brand;
        private String buildingId;
        private String callName;
        private String callPassword;
        private String callURL;
        private String cameraCode;
        private int cameraStatus;
        private String cameraType;
        private String communityId;
        private long createAt;
        private String creatorId;
        private int dataStatus;
        private String deviceId;
        private String id;
        private String mac;
        private String name;
        private int rank;
        private boolean temporaryAuthorized;
        private long updateAt;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(String buildingId) {
            this.buildingId = buildingId;
        }

        public String getCallName() {
            return callName;
        }

        public void setCallName(String callName) {
            this.callName = callName;
        }

        public String getCallPassword() {
            return callPassword;
        }

        public void setCallPassword(String callPassword) {
            this.callPassword = callPassword;
        }

        public String getCallURL() {
            return callURL;
        }

        public void setCallURL(String callURL) {
            this.callURL = callURL;
        }

        public String getCameraCode() {
            return cameraCode;
        }

        public void setCameraCode(String cameraCode) {
            this.cameraCode = cameraCode;
        }

        public int getCameraStatus() {
            return cameraStatus;
        }

        public void setCameraStatus(int cameraStatus) {
            this.cameraStatus = cameraStatus;
        }

        public String getCameraType() {
            return cameraType;
        }

        public void setCameraType(String cameraType) {
            this.cameraType = cameraType;
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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
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

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public boolean isTemporaryAuthorized() {
            return temporaryAuthorized;
        }

        public void setTemporaryAuthorized(boolean temporaryAuthorized) {
            this.temporaryAuthorized = temporaryAuthorized;
        }

        public long getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(long updateAt) {
            this.updateAt = updateAt;
        }
    }
}
