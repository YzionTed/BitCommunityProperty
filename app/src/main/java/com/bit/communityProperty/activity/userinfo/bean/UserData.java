package com.bit.communityProperty.activity.userinfo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 23 on 2018/2/8.
 */

public class UserData implements Serializable {
    /**
     * email : 511782881@qq.com
     * iDCard : 452730199110130512
     * id : 5a73c8b29ce9fbd050a76980
     * name : 苏祖恩
     * nickName : suzuen
     * permissions : []
     * phone : 15918729265
     * roles : []
     * token : null
     */

    private String email;
    private String identityCard;
    private String id;
    private String name;
    private String nickName;
    private String phone;
    private Object token;
    private List<String> permissions;
    private List<String> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getToken() {
        return token;
    }

    public void setToken(Object token) {
        this.token = token;
    }


}
