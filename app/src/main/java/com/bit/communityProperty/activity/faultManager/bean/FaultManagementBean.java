package com.bit.communityProperty.activity.faultManager.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 故障管理列表的bean类
 * Created by kezhangzhao on 2018/3/9.
 */

public class FaultManagementBean {


    /**
     * currentPage : 1
     * records : [{"acceptId":null,"acceptName":null,"acceptTime":null,"communityId":"5a82adf3b06c97e0cd6c0f3d","contact":null,"createAt":1520510782902,"createId":"5a82a6129ce93e30677c3fa4","dataStatus":1,"evaluate":null,"evaluation":null,"evaluationAccessory":null,"evaluationGrade":null,"evaluationTime":null,"faultAccessory":["{[11111],[22222]}"],"faultAddress":null,"faultContent":"阿里巴巴123","faultItem":1,"faultStatus":3,"faultType":1,"id":"5aa1273f3ddeeea27144c524","identity":2,"no":null,"payStatus":null,"playTime":1520510782875,"rejectId":"5a82a6129ce93e30677c3fa4","rejectName":"方小宇","rejectReason":null,"rejectTime":1520511824257,"repairContact":"10086","repairId":null,"repairName":"张三","repairType":2,"roomId":"5a82b04bb06c97e0cd6c0ff0","updateAt":1520511824260,"userId":"5a82a6129ce93e30677c3fa4","userName":"方小宇"}]
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

    public static class RecordsBean implements Serializable{
        /**
         * acceptId : null
         * acceptName : null
         * acceptTime : null
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * contact : null
         * createAt : 1520510782902
         * createId : 5a82a6129ce93e30677c3fa4
         * dataStatus : 1
         * evaluate : null
         * evaluation : null
         * evaluationAccessory : null
         * evaluationGrade : null
         * evaluationTime : null
         * faultAccessory : ["{[11111],[22222]}"]
         * faultAddress : null
         * faultContent : 阿里巴巴123
         * faultItem : 1
         * faultStatus : 3
         * faultType : 1
         * id : 5aa1273f3ddeeea27144c524
         * identity : 2
         * no : null
         * payStatus : null
         * playTime : 1520510782875
         * rejectId : 5a82a6129ce93e30677c3fa4
         * rejectName : 方小宇
         * rejectReason : null
         * rejectTime : 1520511824257
         * repairContact : 10086
         * repairId : null
         * repairName : 张三
         * repairType : 2
         * roomId : 5a82b04bb06c97e0cd6c0ff0
         * updateAt : 1520511824260
         * userId : 5a82a6129ce93e30677c3fa4
         * userName : 方小宇
         */

        private Object acceptId;//
        private Object acceptName;//
        private Long acceptTime;//
        private String communityId;//
        private Object contact;//
        private long createAt;
        private String createId;
        private int dataStatus;
        private Object evaluate;
        private Object evaluation;
        private Object evaluationAccessory;
        private Object evaluationGrade;
        private Object evaluationTime;
        private String faultAddress;
        private String faultContent;
        private int faultItem;
        private int faultStatus;
        private int faultType;
        private String id;
        private int identity;
        private Object no;
        private Object payStatus;
        private long playTime;
        private String rejectId;
        private String rejectName;
        private Object rejectReason;
        private long rejectTime;
        private String repairContact;
        private Object repairId;
        private String repairName;
        private int repairType;
        private String roomId;
        private long updateAt;
        private String userId;
        private String userName;
        private List<String> faultAccessory;

        public Object getAcceptId() {
            return acceptId;
        }

        public void setAcceptId(Object acceptId) {
            this.acceptId = acceptId;
        }

        public Object getAcceptName() {
            return acceptName;
        }

        public void setAcceptName(Object acceptName) {
            this.acceptName = acceptName;
        }

        public Long getAcceptTime() {
            return acceptTime;
        }

        public void setAcceptTime(Long acceptTime) {
            this.acceptTime = acceptTime;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public Object getContact() {
            return contact;
        }

        public void setContact(Object contact) {
            this.contact = contact;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
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

        public Object getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(Object evaluate) {
            this.evaluate = evaluate;
        }

        public Object getEvaluation() {
            return evaluation;
        }

        public void setEvaluation(Object evaluation) {
            this.evaluation = evaluation;
        }

        public Object getEvaluationAccessory() {
            return evaluationAccessory;
        }

        public void setEvaluationAccessory(Object evaluationAccessory) {
            this.evaluationAccessory = evaluationAccessory;
        }

        public Object getEvaluationGrade() {
            return evaluationGrade;
        }

        public void setEvaluationGrade(Object evaluationGrade) {
            this.evaluationGrade = evaluationGrade;
        }

        public Object getEvaluationTime() {
            return evaluationTime;
        }

        public void setEvaluationTime(Object evaluationTime) {
            this.evaluationTime = evaluationTime;
        }

        public String getFaultAddress() {
            return faultAddress;
        }

        public void setFaultAddress(String faultAddress) {
            this.faultAddress = faultAddress;
        }

        public String getFaultContent() {
            return faultContent;
        }

        public void setFaultContent(String faultContent) {
            this.faultContent = faultContent;
        }

        public int getFaultItem() {
            return faultItem;
        }

        public void setFaultItem(int faultItem) {
            this.faultItem = faultItem;
        }

        public int getFaultStatus() {
            return faultStatus;
        }

        public void setFaultStatus(int faultStatus) {
            this.faultStatus = faultStatus;
        }

        public int getFaultType() {
            return faultType;
        }

        public void setFaultType(int faultType) {
            this.faultType = faultType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getIdentity() {
            return identity;
        }

        public void setIdentity(int identity) {
            this.identity = identity;
        }

        public Object getNo() {
            return no;
        }

        public void setNo(Object no) {
            this.no = no;
        }

        public Object getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(Object payStatus) {
            this.payStatus = payStatus;
        }

        public long getPlayTime() {
            return playTime;
        }

        public void setPlayTime(long playTime) {
            this.playTime = playTime;
        }

        public String getRejectId() {
            return rejectId;
        }

        public void setRejectId(String rejectId) {
            this.rejectId = rejectId;
        }

        public String getRejectName() {
            return rejectName;
        }

        public void setRejectName(String rejectName) {
            this.rejectName = rejectName;
        }

        public Object getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(Object rejectReason) {
            this.rejectReason = rejectReason;
        }

        public long getRejectTime() {
            return rejectTime;
        }

        public void setRejectTime(long rejectTime) {
            this.rejectTime = rejectTime;
        }

        public String getRepairContact() {
            return repairContact;
        }

        public void setRepairContact(String repairContact) {
            this.repairContact = repairContact;
        }

        public Object getRepairId() {
            return repairId;
        }

        public void setRepairId(Object repairId) {
            this.repairId = repairId;
        }

        public String getRepairName() {
            return repairName;
        }

        public void setRepairName(String repairName) {
            this.repairName = repairName;
        }

        public int getRepairType() {
            return repairType;
        }

        public void setRepairType(int repairType) {
            this.repairType = repairType;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public long getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(long updateAt) {
            this.updateAt = updateAt;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<String> getFaultAccessory() {
            return faultAccessory;
        }

        public void setFaultAccessory(List<String> faultAccessory) {
            this.faultAccessory = faultAccessory;
        }
    }
}
