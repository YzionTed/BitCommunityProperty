package com.bit.communityProperty.activity.safetywarning.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/2/23.
 */

public class AlarmListBean implements Serializable{

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

    public static class RecordsBean implements Serializable{
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

        private String id;
        private String callerId;
        private String callerName;
        private String callerPhoneNum;
        private String callTime;
        private String communityId;
        private String communityName;
        private String buildingName;
        private String roomId;
        private String roomName;
        private int receiveStatus;
        private String receiverId;
        private String receiverName;
        private String receiverPhoneNum;
        private String receiveTime;
        private String troubleShootingTime;
        private String troubleShootingReport;
        private int dataStatus;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCallerId() {
            return callerId;
        }

        public void setCallerId(String callerId) {
            this.callerId = callerId;
        }

        public String getCallerName() {
            return callerName;
        }

        public void setCallerName(String callerName) {
            this.callerName = callerName;
        }

        public String getCallerPhoneNum() {
            return callerPhoneNum;
        }

        public void setCallerPhoneNum(String callerPhoneNum) {
            this.callerPhoneNum = callerPhoneNum;
        }

        public String getCallTime() {
            return callTime;
        }

        public void setCallTime(String callTime) {
            this.callTime = callTime;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public int getReceiveStatus() {
            return receiveStatus;
        }

        public void setReceiveStatus(int receiveStatus) {
            this.receiveStatus = receiveStatus;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
        }

        public String getReceiverPhoneNum() {
            return receiverPhoneNum;
        }

        public void setReceiverPhoneNum(String receiverPhoneNum) {
            this.receiverPhoneNum = receiverPhoneNum;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        public String getTroubleShootingTime() {
            return troubleShootingTime;
        }

        public void setTroubleShootingTime(String troubleShootingTime) {
            this.troubleShootingTime = troubleShootingTime;
        }

        public String getTroubleShootingReport() {
            return troubleShootingReport;
        }

        public void setTroubleShootingReport(String troubleShootingReport) {
            this.troubleShootingReport = troubleShootingReport;
        }

        public int getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(int dataStatus) {
            this.dataStatus = dataStatus;
        }
    }
}
