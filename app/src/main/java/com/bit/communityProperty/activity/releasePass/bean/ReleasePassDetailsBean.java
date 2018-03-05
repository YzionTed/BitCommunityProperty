package com.bit.communityProperty.activity.releasePass.bean;

import java.io.Serializable;

/**
 * Created by kezhangzhao on 2018/2/26.
 */

public class ReleasePassDetailsBean implements Serializable{

    /**
     * auditStatus : 1
     * beginAt : 1519536841000
     * building : null
     * communityId : 5a82adf3b06c97e0cd6c0f3d
     * createAt : 1519542118181
     * creatorId : 5a82a37d9ce93e30677c3f9c
     * dataStatus : 1
     * endAt : 1519812610000
     * id : 5a925f666951ff48ffd1c5cd
     * items : 箱子
     * phone : 15900010001
     * releaseStatus : 1
     * remark : null
     * room : null
     * updateAt : 1519549335756
     * url : https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=750865922,118698653&fm=27&gp=0.jpg
     * userId : 5a82a37d9ce93e30677c3f9c
     * userName : 业主1号
     */

    private int auditStatus;
    private long beginAt;
    private Object building;
    private String communityId;
    private long createAt;
    private String creatorId;
    private int dataStatus;
    private long endAt;
    private String id;
    private String items;
    private String phone;
    private int releaseStatus;
    private Object remark;
    private Object room;
    private long updateAt;
    private String url;
    private String userId;
    private String userName;

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public long getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(long beginAt) {
        this.beginAt = beginAt;
    }

    public Object getBuilding() {
        return building;
    }

    public void setBuilding(Object building) {
        this.building = building;
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

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(int releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
