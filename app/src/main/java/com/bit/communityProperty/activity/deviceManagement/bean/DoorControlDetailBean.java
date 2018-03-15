package com.bit.communityProperty.activity.deviceManagement.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL60 on 2018/3/10.
 */

public class DoorControlDetailBean implements Serializable{

    /**
     * currentPage : 1
     * records : [{"communityId":"5a82adf3b06c97e0cd6c0f3d","communityName":"和谐景苑","createAt":1520563360938,"creatorId":"5a82a22c9ce93e30677c3f9a","dataStatus":1,"deviceId":"1678","id":"5aa271e65a71169d8972ec73","keyNo":"凭证","phone":"15913626271","result":"成功","time":1520574400938,"updateAt":1520563360938,"useStyle":1,"useType":1,"userCommand":"蓝牙开门","userId":"5a82a22c9ce93e30677c3f9a","userName":"王5"},{"communityId":"5a82adf3b06c97e0cd6c0f3d","communityName":"和谐景苑","createAt":1520563360938,"creatorId":"5a82a22c9ce93e30677c3f9a","dataStatus":1,"deviceId":"1678","id":"5aa1f4a0e3634c7ec56f6c3c","keyNo":"凭证","phone":"15913626271","result":"成功","time":1520563360938,"updateAt":1520563360938,"useStyle":1,"useType":1,"userCommand":"蓝牙开门","userId":"5a82a22c9ce93e30677c3f9a","userName":"李4"},{"communityId":"5a82adf3b06c97e0cd6c0f3d","communityName":"和谐景苑","createAt":1520563360938,"creatorId":"5a82a22c9ce93e30677c3f9a","dataStatus":1,"deviceId":"1678","id":"5aa2720d5a71169d8972edd9","keyNo":"凭证","phone":"15913626271","result":"成功","time":1520563360938,"updateAt":1520563360938,"useStyle":1,"useType":1,"userCommand":"远程开门","userId":"5a82a22c9ce93e30677c3f9a","userName":"赵6"},{"communityId":"5a82adf3b06c97e0cd6c0f3d","communityName":"和谐景苑","createAt":1520563360938,"creatorId":"5a82a22c9ce93e30677c3f9a","dataStatus":1,"deviceId":"1678","id":"5aa275f05a71169d89730dd3","keyNo":"凭证","phone":"15913626271","result":"成功","time":1520563360938,"updateAt":1520563360938,"useStyle":1,"useType":1,"userCommand":"视频开门","userId":"5a82a22c9ce93e30677c3f9a","userName":"谢刚"},{"communityId":"5a82adf3b06c97e0cd6c0f3d","communityName":"和谐景苑","createAt":1520563112129,"creatorId":"5a82a22c9ce93e30677c3f9a","dataStatus":1,"deviceId":"1678","id":"5aa1f3ace3634c7ec56f6c39","keyNo":"凭证","phone":"15913626271","result":"成功","time":1520563112129,"updateAt":1520563112129,"useStyle":1,"useType":1,"userCommand":"蓝牙开门","userId":"5a82a22c9ce93e30677c3f9a","userName":"张3"}]
     * total : 5
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
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * communityName : 和谐景苑
         * createAt : 1520563360938
         * creatorId : 5a82a22c9ce93e30677c3f9a
         * dataStatus : 1
         * deviceId : 1678
         * id : 5aa271e65a71169d8972ec73
         * keyNo : 凭证
         * phone : 15913626271
         * result : 成功
         * time : 1520574400938
         * updateAt : 1520563360938
         * useStyle : 1
         * useType : 1
         * userCommand : 蓝牙开门
         * userId : 5a82a22c9ce93e30677c3f9a
         * userName : 王5
         */

        private String communityId;
        private String communityName;
        private long createAt;
        private String creatorId;
        private int dataStatus;
        private String deviceId;
        private String id;
        private String keyNo;
        private String phone;
        private String result;
        private String time;
        private long updateAt;
        private int useStyle;
        private int useType;
        private String userCommand;
        private String userId;
        private String userName;

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public String getCommunityName() {
            return communityName;
        }

        public void setCommunityName(String communityName) {
            this.communityName = communityName;
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

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getKeyNo() {
            return keyNo;
        }

        public void setKeyNo(String keyNo) {
            this.keyNo = keyNo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public long getUpdateAt() {
            return updateAt;
        }

        public void setUpdateAt(long updateAt) {
            this.updateAt = updateAt;
        }

        public int getUseStyle() {
            return useStyle;
        }

        public void setUseStyle(int useStyle) {
            this.useStyle = useStyle;
        }

        public int getUseType() {
            return useType;
        }

        public void setUseType(int useType) {
            this.useType = useType;
        }

        public String getUserCommand() {
            return userCommand;
        }

        public void setUserCommand(String userCommand) {
            this.userCommand = userCommand;
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
}
