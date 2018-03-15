package com.bit.communityProperty.activity.household.bean;

import java.util.List;

/**
 * 已经审核的bean类
 * 对应接口：根据社区统计各楼宇有效业主数量
 * Created by kezhangzhao on 2018/2/27.
 */

public class AuditedBean {


    /**
     * communityId : 5a82adf3b06c97e0cd6c0f3d
     * buildingEntity : [{"_id":"5a82ae1db06c97e0cd6c0f3f","name":"1号楼","code":"1","communityId":"5a82adf3b06c97e0cd6c0f3d","dataStatus":1,"miliBId":136}]
     * total : 1
     */

    private String communityId;
    private int total;
    private List<BuildingEntityBean> buildingEntity;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BuildingEntityBean> getBuildingEntity() {
        return buildingEntity;
    }

    public void setBuildingEntity(List<BuildingEntityBean> buildingEntity) {
        this.buildingEntity = buildingEntity;
    }

    public static class BuildingEntityBean {
        /**
         * _id : 5a82ae1db06c97e0cd6c0f3f
         * name : 1号楼
         * code : 1
         * communityId : 5a82adf3b06c97e0cd6c0f3d
         * dataStatus : 1
         * miliBId : 136
         */

        private String _id;
        private String name;
        private String code;
        private String communityId;
        private int dataStatus;
        private int miliBId;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCommunityId() {
            return communityId;
        }

        public void setCommunityId(String communityId) {
            this.communityId = communityId;
        }

        public int getDataStatus() {
            return dataStatus;
        }

        public void setDataStatus(int dataStatus) {
            this.dataStatus = dataStatus;
        }

        public int getMiliBId() {
            return miliBId;
        }

        public void setMiliBId(int miliBId) {
            this.miliBId = miliBId;
        }
    }
}
