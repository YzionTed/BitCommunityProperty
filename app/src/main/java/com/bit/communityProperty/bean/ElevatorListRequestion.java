package com.bit.communityProperty.bean;



/**
 * Created by Dell on 2018/3/3.
 */

public class ElevatorListRequestion  {

    private String communityId;
    private String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ElevatorListRequestion(){
        super();
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

}
