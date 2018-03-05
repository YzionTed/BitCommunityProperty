package com.bit.communityProperty.bean;

import java.util.ArrayList;

/**
 * Created by Dell on 2018/2/27.
 */

public class DoorOpenRequestBean {

    private String communityId;
    private String doorType;
   private ArrayList<String> doorMacs;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public ArrayList<String> getDoorMacs() {
        return doorMacs;
    }

    public void setDoorMacs(ArrayList<String> doorMacs) {
        this.doorMacs = doorMacs;
    }
}
