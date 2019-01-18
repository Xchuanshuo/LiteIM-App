package com.worldtreestd.finder.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * @author legend
 */
public class User extends LitePalSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private Integer userId;

    private String username;

    private String password;

    private String signature;

    private String portrait;

    private String background;

    private Integer sex;

    private String email;

    private String stuNum;

    private String openId;

    private Integer fansNum;

    public User() {}

    public User(String openId, String password) {
        this.openId = openId;
        this.password = password;
    }

    public Integer getId() {
        return userId;
    }

    public void setId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", signature='" + signature + '\'' +
                ", portrait='" + portrait + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", stuNum='" + stuNum + '\'' +
                ", openId='" + openId + '\'' +
                ", fansNum=" + fansNum +
                '}';
    }
}
