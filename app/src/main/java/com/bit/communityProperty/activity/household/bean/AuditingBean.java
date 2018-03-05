package com.bit.communityProperty.activity.household.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 待审核的bean类
 * Created by kezhangzhao on 2018/2/8.
 */

public class AuditingBean implements Serializable{


    /**
     * currentPage : 1
     * records : [{"area":110,"auditStatus":0,"birthday":null,"buildingId":"5a82ae1db06c97e0cd6c0f41","canApply":true,"checkInTime":1516550400000,"closed":false,"communityId":"5a82adf3b06c97e0cd6c0f3d","contract":"5201314","contractPhone":null,"createAt":1519525782046,"createId":"5a8bd1c5d728e7a7da12e342","currentAddress":"广州市天河区","dataStatus":1,"householdAddress":"广东省广州市天河区","id":"5a921f96c966eb74b480364e","identityCard":"441622198903054238","miliUId":null,"name":"聪明的一休","nickName":null,"phone":"13610249538","politicsStatus":1,"proprietorId":"5a8bd1c5d728e7a7da12e342","relationship":1,"roomId":"5a82b06eb06c97e0cd6c1080","roomLocation":"和谐景苑2号楼3单元203","roomName":"3单元203","sex":null,"telPhone":"0766-6754355","updateAt":1519525782046,"updateBy":null,"userId":"5a8bd1c5d728e7a7da12e342","workUnit":"SB科技有限公司"},{"area":null,"auditStatus":0,"birthday":"1989-03-05","buildingId":"5a82ae1db06c97e0cd6c0f45","canApply":true,"checkInTime":1516521600000,"closed":false,"communityId":"5a82adf3b06c97e0cd6c0f3d","contract":"5201314","contractPhone":null,"createAt":1519703685402,"createId":"5a8bd1c5d728e7a7da12e342","currentAddress":null,"dataStatus":1,"householdAddress":null,"id":"5a94d6859ce9da3242584de2","identityCard":"441622198903054238","miliUId":null,"name":null,"nickName":null,"phone":"13610249538","politicsStatus":null,"proprietorId":"5a8bd1c5d728e7a7da12e342","relationship":1,"roomId":"5a82b088b06c97e0cd6c10f5","roomLocation":null,"roomName":"1单元501","sex":1,"telPhone":null,"updateAt":1519703685402,"updateBy":null,"userId":"5a8bd1c5d728e7a7da12e342","workUnit":null},{"area":110,"auditStatus":0,"birthday":"1989-03-05","buildingId":"5a82ae1db06c97e0cd6c0f41","canApply":true,"checkInTime":1516550400000,"closed":false,"communityId":"5a82adf3b06c97e0cd6c0f3d","contract":"5201314","contractPhone":"13610249538","createAt":1519718405497,"createId":"5a82a37d9ce93e30677c3f9c","currentAddress":"广州市天河区","dataStatus":1,"householdAddress":"广东省广州市天河区","id":"5a951005c966a38d7fe5f444","identityCard":"441622198903054238","miliUId":null,"name":"聪明的一休","nickName":"昵称","phone":"159******01","politicsStatus":1,"proprietorId":"5a82a37d9ce93e30677c3f9c","relationship":1,"roomId":"5a82b06db06c97e0cd6c1044","roomLocation":"和谐景苑2号楼1单元201","roomName":"1单元201","sex":1,"telPhone":"0766-6754355","updateAt":1519718405497,"updateBy":null,"userId":"5a82a37d9ce93e30677c3f9c","workUnit":"SB科技有限公司"}]
     * total : 3
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
         * area : 110
         * auditStatus : 0
         * birthday : null
         * buildingId : 5a82ae1db06c97e0cd6c0f41
         * canApply : true
         * checkInTime : 1516550400000
         * closed : false
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * contract : 5201314
         * contractPhone : null
         * createAt : 1519525782046
         * createId : 5a8bd1c5d728e7a7da12e342
         * currentAddress : 广州市天河区
         * dataStatus : 1
         * householdAddress : 广东省广州市天河区
         * id : 5a921f96c966eb74b480364e
         * identityCard : 441622198903054238
         * miliUId : null
         * name : 聪明的一休
         * nickName : null
         * phone : 13610249538
         * politicsStatus : 1
         * proprietorId : 5a8bd1c5d728e7a7da12e342
         * relationship : 1
         * roomId : 5a82b06eb06c97e0cd6c1080
         * roomLocation : 和谐景苑2号楼3单元203
         * roomName : 3单元203
         * sex : null
         * telPhone : 0766-6754355
         * updateAt : 1519525782046
         * updateBy : null
         * userId : 5a8bd1c5d728e7a7da12e342
         * workUnit : SB科技有限公司
         */

        private int area;//面积
        private int auditStatus;//0未审核，1审核通过，-1驳回，-2违规
        private String birthday;//生日
        private String buildingId;//楼栋ID
        private boolean canApply;
        private long checkInTime;
        private boolean closed;
        private String communityId;
        private String contract;
        private Object contractPhone;
        private long createAt;
        private String createId;
        private String currentAddress;
        private int dataStatus;
        private String householdAddress;
        private String id;
        private String identityCard;
        private Object miliUId;
        private String name;
        private Object nickName;
        private String phone;
        private int politicsStatus;
        private String proprietorId;
        private int relationship;
        private String roomId;
        private String roomLocation;
        private String roomName;
        private Object sex;
        private String telPhone;
        private long updateAt;
        private Object updateBy;
        private String userId;
        private String workUnit;

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(String buildingId) {
            this.buildingId = buildingId;
        }

        public boolean isCanApply() {
            return canApply;
        }

        public void setCanApply(boolean canApply) {
            this.canApply = canApply;
        }

        public long getCheckInTime() {
            return checkInTime;
        }

        public void setCheckInTime(long checkInTime) {
            this.checkInTime = checkInTime;
        }

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getContract() {
            return contract;
        }

        public void setContract(String contract) {
            this.contract = contract;
        }

        public Object getContractPhone() {
            return contractPhone;
        }

        public void setContractPhone(Object contractPhone) {
            this.contractPhone = contractPhone;
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

        public String getCurrentAddress() {
            return currentAddress;
        }

        public void setCurrentAddress(String currentAddress) {
            this.currentAddress = currentAddress;
        }

        public int getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(int dataStatus) {
            this.dataStatus = dataStatus;
        }

        public String getHouseholdAddress() {
            return householdAddress;
        }

        public void setHouseholdAddress(String householdAddress) {
            this.householdAddress = householdAddress;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIdentityCard() {
            return identityCard;
        }

        public void setIdentityCard(String identityCard) {
            this.identityCard = identityCard;
        }

        public Object getMiliUId() {
            return miliUId;
        }

        public void setMiliUId(Object miliUId) {
            this.miliUId = miliUId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getNickName() {
            return nickName;
        }

        public void setNickName(Object nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getPoliticsStatus() {
            return politicsStatus;
        }

        public void setPoliticsStatus(int politicsStatus) {
            this.politicsStatus = politicsStatus;
        }

        public String getProprietorId() {
            return proprietorId;
        }

        public void setProprietorId(String proprietorId) {
            this.proprietorId = proprietorId;
        }

        public int getRelationship() {
            return relationship;
        }

        public void setRelationship(int relationship) {
            this.relationship = relationship;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomLocation() {
            return roomLocation;
        }

        public void setRoomLocation(String roomLocation) {
            this.roomLocation = roomLocation;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public Object getSex() {
            return sex;
        }

        public void setSex(Object sex) {
            this.sex = sex;
        }

        public String getTelPhone() {
            return telPhone;
        }

        public void setTelPhone(String telPhone) {
            this.telPhone = telPhone;
        }

        public long getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(long updateAt) {
            this.updateAt = updateAt;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWorkUnit() {
            return workUnit;
        }

        public void setWorkUnit(String workUnit) {
            this.workUnit = workUnit;
        }
    }
}
