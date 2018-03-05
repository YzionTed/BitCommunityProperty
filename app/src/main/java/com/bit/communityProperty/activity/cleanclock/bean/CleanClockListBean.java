package com.bit.communityProperty.activity.cleanclock.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/2/9.
 */

public class CleanClockListBean {
    /**
     * total : 1
     * totalPage : 2
     * currentPage : 88
     * records : [{"id":"Google","callerId":"http://www.google.com","callerName":"fadf","callerPhoneNum":"fef","callTime":"asdf","communityId":"fead","communityName":"fead","buildingName":"fea","roomId":"fea","roomName":"efad","receiveStatus":1,"receiverId":"fefad","receiverName":"fef","receiverPhoneNum":"fef","receiveTime":"fef","troubleShootingTime":"fef","troubleShootingReport":"fef","dataStatus":1},{"id":"Google","callerId":"http://www.google.com","callerName":"fadf","callerPhoneNum":"fef","callTime":"asdf","communityId":"fead","communityName":"fead","buildingName":"fea","roomId":"fea","roomName":"efad","receiveStatus":"fedf","receiverId":"fefad","receiverName":"fef","receiverPhoneNum":"fef","receiveTime":"fef","troubleShootingTime":"fef","troubleShootingReport":"fef","dataStatus":"fef"}]
     */

    private int total;
    private int totalPage;
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

    public static class RecordsBean implements Serializable {
        /**
         * id : Google
         * callerId : http://www.google.com
         * callerName : fadf
         * callerPhoneNum : fef
         * callTime : asdf
         * communityId : fead
         * communityName : fead
         * buildingName : fea
         * roomId : fea
         * roomName : efad
         * receiveStatus : 1
         * receiverId : fefad
         * receiverName : fef
         * receiverPhoneNum : fef
         * receiveTime : fef
         * troubleShootingTime : fef
         * troubleShootingReport : fef
         * dataStatus : 1
         */

        private String userId;
        private String username;
        private String communityId;
        private String taskType;
        private String remark;
        private String buckName;
        private String key;
        private String url;
        private String deviceId;
        private String coordinate;
        private String creatorId;
        private String createAt;
        private String updateAt;
        private String dataStatus;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getTaskType() {
            return taskType;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getBuckName() {
            return buckName;
        }

        public void setBuckName(String buckName) {
            this.buckName = buckName;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getCoordinate() {
            return coordinate;
        }

        public void setCoordinate(String coordinate) {
            this.coordinate = coordinate;
        }

        public String getCreatorId() {
            return creatorId;
        }

        public void setCreatorId(String creatorId) {
            this.creatorId = creatorId;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public String getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(String dataStatus) {
            this.dataStatus = dataStatus;
        }
    }
}
