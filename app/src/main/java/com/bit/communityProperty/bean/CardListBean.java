package com.bit.communityProperty.bean;

import com.litesuits.orm.db.annotation.NotNull;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by DELL60 on 2018/3/10.
 */

@Table("card_list")
public class CardListBean {

    /**
     * communityId : 5a82adf3b06c97e0cd6c0f3d
     * controlType : 8
     * createAt : 1520659123778
     * createId : 5a9a68960cf2378eab90c4b9
     * dataStatus : 1
     * endDate : null
     * id : 5aa36ab39ce9a7a747688c4b
     * isProcessed : null
     * keyId : 5aa36ab25136a3a8634424db
     * keyNo : 08482E7A3900
     * keyType : 1
     * name : 李达
     * processTime : 3097459123778
     * relevanceId : null
     * remark : null
     * startDate : 1520659123778
     * updateAt : null
     * useTimes : 0
     * userId : 5a9a68960cf2378eab90c4b9
     */

    public static String USER_ID = "userId";
    public static String COMMUNITY_ID = "communityId";

    @NotNull
    private String communityId;
    private int controlType;
    private String createAt;
    private String createId;
    private int dataStatus;
    private String endDate;

    @PrimaryKey(AssignType.BY_MYSELF)
    private String id;
    private int isProcessed;
    private String keyId;

    @NotNull
    private String keyNo;
    private int keyType;
    private String name;
    private String processTime;
    private String relevanceId;
    private String remark;
    private String startDate;
    private String updateAt;
    private int useTimes;
    @NotNull
    private String userId;

    @Override
    public String toString() {
        return "CardListBean{" +
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

    public int getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(int isProcessed) {
        this.isProcessed = isProcessed;
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
