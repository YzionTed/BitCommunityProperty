package com.bit.communityProperty.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 23 on 2018/2/8.
 */

public class LoginData implements Serializable{
    /**
     * email : 511782881@qq.com
     * iDCard : 452730199110130512
     * id : 5a73c8b29ce9fbd050a76980
     * name : 苏祖恩
     * nickName : suzuen
     * permissions : []
     * phone : 15918729265
     * roles : []
     * token : 09b9f067a99a4d91908a7e2d26b1157a
     */

    private String email;
    private String iDCard;
    private String id;
    private String name;
    private String nickName;
    private String phone;
    private String token;
    private List<String> permissions;
    private List<String> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIDCard() {
        return iDCard;
    }

    public void setIDCard(String iDCard) {
        this.iDCard = iDCard;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
