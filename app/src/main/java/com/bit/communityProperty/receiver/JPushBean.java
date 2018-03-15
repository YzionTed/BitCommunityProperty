package com.bit.communityProperty.receiver;

import java.io.Serializable;

/**
 * Created by DELL60 on 2018/2/28.
 */

public class JPushBean implements Serializable{

    /**
     * action : 100301
     * data : {"title":"安防警报：2号楼1单元202","police_state":1,"police_id":"5a96a6918d6ac17c91ed08c7","address":"2号楼1单元202","police_type":"","communityId":"5a82adf3b06c97e0cd6c0f3d","police_time":""}
     */

    private String action;
    private DataBean data;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * title : 安防警报：2号楼1单元202
         * police_state : 1
         * police_id : 5a96a6918d6ac17c91ed08c7
         * address : 2号楼1单元202
         * police_type :
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * police_time :
         */

        private String title;
        private int police_state;
        private String police_id;
        private String address;
        private String police_type;
        private String communityId;
        private String police_time;
        private int type;
        private String notice_id;
        private String url;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getNotice_id() {
            return notice_id;
        }

        public void setNotice_id(String notice_id) {
            this.notice_id = notice_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getPolice_state() {
            return police_state;
        }

        public void setPolice_state(int police_state) {
            this.police_state = police_state;
        }

        public String getPolice_id() {
            return police_id;
        }

        public void setPolice_id(String police_id) {
            this.police_id = police_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPolice_type() {
            return police_type;
        }

        public void setPolice_type(String police_type) {
            this.police_type = police_type;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getPolice_time() {
            return police_time;
        }

        public void setPolice_time(String police_time) {
            this.police_time = police_time;
        }
    }
}
