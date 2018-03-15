package com.bit.communityProperty.fragment.main.bean;

import java.util.List;

/**
 * Created by DELL60 on 2018/2/5.
 */

public class MainNewsBean {


    /**
     * currentPage : 1
     * records : [{"body":"由于自来水使用过量，造成低压，所以暂时一段时间断水","communityId":"5a9152909ce95a94f49e6ea2","createAt":1519525819380,"dataStatus":1,"editorId":"5a82a22c9ce93e30677c3f9a","editorName":"系统管理员","id":"5a921fbb288b402c769ec01c","noticeTypeId":"5a911526288b0e80da37af62","publishAt":1519525819380,"publishStatus":0,"thumbnail":"ap15a8fb5340cf2835206a3aabc_20180225130421690.jpg","title":"自来水低压通告","updateAt":1519526056215,"url":"http://www.xxx.com"}]
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

    public static class RecordsBean {
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

        public String getThumbnail() {
            return thumbnailUrl;
        }

        public void setThumbnail(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
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
}
