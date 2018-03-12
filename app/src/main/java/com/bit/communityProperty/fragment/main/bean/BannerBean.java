package com.bit.communityProperty.fragment.main.bean;

import java.util.List;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class BannerBean {

    /**
     * beginAt : 1520416445363
     * client : 1000
     * createAt : 1520416482521
     * creatorId : 5a82a22c9ce93e30677c3f9a
     * dataStatus : 1
     * deadline : 1520416445363
     * href : 跳转链接
     * id : 5a9fb6e2e3634e0809774564
     * materialType : 2
     * materialUrl : 素材链接
     * publishAt : 1520416517441
     * published : true
     * rank : 1
     * tags : ["标签"]
     * title : 标题
     * updateAt : 1520416698921
     */

    private String beginAt;
    private int client;
    private String createAt;
    private String creatorId;
    private int dataStatus;
    private String deadline;
    private String href;
    private String id;
    private int materialType;
    private String materialUrl;
    private String publishAt;
    private boolean published;
    private int rank;
    private String title;
    private String updateAt;
    private List<String> tags;

    public String getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(String beginAt) {
        this.beginAt = beginAt;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaterialType() {
        return materialType;
    }

    public void setMaterialType(int materialType) {
        this.materialType = materialType;
    }

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
