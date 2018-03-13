package com.bit.communityProperty.bean;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

import java.util.List;

/**
 * Created by DELL60 on 2018/3/10.
 */


public class CardListBean {

    public static String USER_ID = "userId";
    public static String COMMUNITY_ID = "communityId";


    /**
     * currentPage : 1
     * records : [{"communityId":"5a82adf3b06c97e0cd6c0f3d","controlType":8,"createAt":1520659123778,"createId":"5a9a68960cf2378eab90c4b9","dataStatus":1,"endDate":"fef","id":"5aa36ab39ce9a7a747688c4b","isProcessed":false,"keyId":"5aa36ab25136a3a8634424db","keyNo":"08482E7A3900","keyType":1,"name":"李达","processTime":3097459123778,"relevanceId":"fefe","remark":"fadsf","roomId":"fadd","roomName":"zxcv","startDate":1520659123778,"updateAt":"qwed","useTimes":0,"userId":"5a9a68960cf2378eab90c4b9"}]
     * total : 1
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

    @Table("card_list")
    public static class RecordsBean {
        /**
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * controlType : 8
         * createAt : 1520659123778
         * createId : 5a9a68960cf2378eab90c4b9
         * dataStatus : 1
         * endDate : fef
         * id : 5aa36ab39ce9a7a747688c4b
         * isProcessed : false
         * keyId : 5aa36ab25136a3a8634424db
         * keyNo : 08482E7A3900
         * keyType : 1
         * name : 李达
         * processTime : 3097459123778
         * relevanceId : fefe
         * remark : fadsf
         * roomId : fadd
         * roomName : zxcv
         * startDate : 1520659123778
         * updateAt : qwed
         * useTimes : 0
         * userId : 5a9a68960cf2378eab90c4b9
         */

        @NotNull
        private String communityId;
        private int controlType;
        private String createAt;
        private String createId;
        private int dataStatus;
        private String endDate;

        @PrimaryKey(AssignType.BY_MYSELF)
        private String id;
        private boolean isProcessed;
        private String keyId;
        @NotNull
        private String keyNo;
        private int keyType;
        private String name;
        private String processTime;
        private String relevanceId;
        private String remark;
        private String roomId;
        private String roomName;
        private String startDate;
        private String updateAt;
        private int useTimes;
        @NotNull
        private String userId;

        @Override
        public String toString() {
            return "RecordsBean{" +
                    "communityId='" + communityId + '\'' +
                    ", controlType=" + controlType +
                    ", createAt='" + createAt + '\'' +
                    ", createId='" + createId + '\'' +
                    ", dataStatus=" + dataStatus +
                    ", endDate='" + endDate + '\'' +
                    ", id='" + id + '\'' +
                    ", isProcessed=" + isProcessed +
                    ", keyId='" + keyId + '\'' +
                    ", keyNo='" + keyNo + '\'' +
                    ", keyType=" + keyType +
                    ", name='" + name + '\'' +
                    ", processTime='" + processTime + '\'' +
                    ", relevanceId='" + relevanceId + '\'' +
                    ", remark='" + remark + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", roomName='" + roomName + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", updateAt='" + updateAt + '\'' +
                    ", useTimes=" + useTimes +
                    ", userId='" + userId + '\'' +
                    '}';
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public int getControlType() {
            return controlType;
        }

        public void setControlType(int controlType) {
            this.controlType = controlType;
        }

        public String getCreateAt() {
            return createAt;
        }

        public void setCreateAt(String createAt) {
            this.createAt = createAt;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public int getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(int dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isProcessed() {
            return isProcessed;
        }

        public void setProcessed(boolean processed) {
            isProcessed = processed;
        }

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getKeyNo() {
            return keyNo;
        }

        public void setKeyNo(String keyNo) {
            this.keyNo = keyNo;
        }

        public int getKeyType() {
            return keyType;
        }

        public void setKeyType(int keyType) {
            this.keyType = keyType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProcessTime() {
            return processTime;
        }

        public void setProcessTime(String processTime) {
            this.processTime = processTime;
        }

        public String getRelevanceId() {
            return relevanceId;
        }

        public void setRelevanceId(String relevanceId) {
            this.relevanceId = relevanceId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(String updateAt) {
            this.updateAt = updateAt;
        }

        public int getUseTimes() {
            return useTimes;
        }

        public void setUseTimes(int useTimes) {
            this.useTimes = useTimes;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
