package com.bit.communityProperty.fragment.main.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/9.
 */

public class BannerBean {
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

    public static class RecordsBean implements Serializable {


        private String title;
        private String materialUrl;
        private int materialType;
        private String href;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMaterialUrl() {
            return materialUrl;
        }

        public void setMaterialUrl(String materialUrl) {
            this.materialUrl = materialUrl;
        }

        public int getMaterialType() {
            return materialType;
        }

        public void setMaterialType(int materialType) {
            this.materialType = materialType;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }
    }
}
