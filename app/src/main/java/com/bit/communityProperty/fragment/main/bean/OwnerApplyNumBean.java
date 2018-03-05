package com.bit.communityProperty.fragment.main.bean;

/**
 * 业主申请数量的接口bean类
 * Created by kezhangzhao on 2018/3/2.
 */

public class OwnerApplyNumBean {


    /**
     * total : 0
     * auditStatus : 0
     * communityId : 5a82adf3b06c97e0cd6c0f3d
     */

    private int total;//总数
    private int auditStatus;//0：未审核；1：审核通过；-1：驳回；-2：违规; 2: 已注销; 3: 已解绑;
    private String communityId;//社区id

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
