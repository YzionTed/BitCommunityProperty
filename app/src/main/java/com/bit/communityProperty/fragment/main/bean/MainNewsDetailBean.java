package com.bit.communityProperty.fragment.main.bean;

/**
 * Created by DELL60 on 2018/2/26.
 */

public class MainNewsDetailBean {

    /**
     * body : 由于自来水使用过量，造成低压，所以暂时一段时间断水
     * communityId : 5a9152909ce95a94f49e6ea2
     * createAt : 1519525819380
     * dataStatus : 1
     * editorId : 5a82a22c9ce93e30677c3f9a
     * editorName : 系统管理员
     * id : 5a921fbb288b402c769ec01c
     * noticeTypeId : 5a911526288b0e80da37af62
     * publishAt : 1519525819380
     * publishStatus : 0
     * thumbnail : ap15a8fb5340cf2835206a3aabc_20180225130421690.jpg
     * title : 自来水低压通告
     * updateAt : 1519526056215
     * url : http://www.xxx.com
     */

    private String body;
    private String communityId;
    private long createAt;
    private int dataStatus;
    private String editorId;
    private String editorName;
    private String id;
    private String noticeTypeId;
    private String publishAt;
    private int publishStatus;
    private String thumbnailUrl;
    private String title;
    private long updateAt;
    private String url;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getEditorId() {
        return editorId;
    }

    public void setEditorId(String editorId) {
        this.editorId = editorId;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeTypeId() {
        return noticeTypeId;
    }

    public void setNoticeTypeId(String noticeTypeId) {
        this.noticeTypeId = noticeTypeId;
    }

    public String getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(String publishAt) {
        this.publishAt = publishAt;
    }

    public int getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(int publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
