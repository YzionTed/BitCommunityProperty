package com.bit.communityProperty.bean;

/**
 * "communityId": "5a82adf3b06c97e0cd6c0f3d",
 "communityName": "和谐景苑",
 "createAt": 1518353391414,
 "dataStatus": 1,
 "id": "5a8b098f94766d7177722e57",
 "nickName": "昵称",
 "phone": "15900020005",
 "postCode": "SUPPORTSTAFF",
 "propertyId": "5a82adee9ce976452b7001ee",
 "propertyName": "物业公司1号",
 "userId": "5a8b0679c772b3f8fdef7643",
 "userName": "物业5号"
 */

public class OnlineData {

    private String communityId;
    private String communityName;
    private long createAt;
    private int dataStatus;
    private String id;
    private String nickName;
    private String phone;
    private String postCode;
    private String propertyId;
    private String propertyName;
    private String userId;
    private String userName;

    public int getDataStatus() {
        return dataStatus;
    }

    public String getId() {
        return id;
    }

    public String getCommunityId() {
        return communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public long getCreateAt() {
        return createAt;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPhone() {
        return phone;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
