package com.bit.communityProperty.activity.userinfo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 23 on 2018/2/8.
 */

public class UserData implements Serializable {
    /**
     * age : 23
     * attach : fefe
     * bdaddr : efef
     * email : fefefe
     * headImg : adfd
     * id : 5a9aae8824b4e6bf7afc2cf0
     * identityCard : fdfefadf
     * name : efdfe
     * nickName : efdfa
     * permissions : ["a","b"]
     * phone : 13726216149
     * roles : ["a","b"]
     * sex : 1
     * token : ffefef
     */

    private String age;
    private String attach;
    private String bdaddr;
    private String email;
    private String headImg;
    private String id;
    private String identityCard;
    private String name;
    private String nickName;
    private String phone;
    private int sex;
    private String token;
    private List<String> permissions;
    private List<String> roles;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getBdaddr() {
        return bdaddr;
    }

    public void setBdaddr(String bdaddr) {
        this.bdaddr = bdaddr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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
