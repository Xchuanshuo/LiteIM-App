package com.legend.liteim.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.legend.liteim.ui.contacts.adapter.ContactsAdapter;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;


/**
 * @author legend
 */
public class User extends LitePalSupport implements Serializable, MultiItemEntity {

    @SerializedName("id")
    private Long userId;

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

    private boolean friend;

    public User() {}

    public User(String openId, String password) {
        this.openId = openId;
        this.password = password;
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long userId) {
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
        if (signature == null) return "";
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
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

    @Override
    public int getItemType() {
        return ContactsAdapter.TYPE_USER;
    }
}
