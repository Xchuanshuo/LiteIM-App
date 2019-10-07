package com.legend.liteim.bean;

/**
 * @author Legend
 * @data by on 19-1-21.
 * @description
 */
public class Comment {

    /**
     * id : 5
     * userId : 6
     * targetId : 41
     * content : emmmmmmmmmmm
     * favorNum : 0
     * username : legend
     * portrait : http://192.168.43.21:8080/6/portraits/3e3455ec332f4a22af0b5f93218c1eac.jpg
     * background : http://192.168.43.21:8080/6/backgrounds/68838a11fe68497aa4961ad8757cd27a.png
     * signature : string
     * timeAgoStr : 9天前
     * favor : false
     */

    private long id;
    private long userId;
    private long targetId;
    private String content;
    private int favorNum;
    private String username;
    private String portrait;
    private String background;
    private String signature;
    private String timeAgoStr;
    private boolean favor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavorNum() {
        return favorNum;
    }

    public void setFavorNum(int favorNum) {
        this.favorNum = favorNum;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getTimeAgoStr() {
        return timeAgoStr;
    }

    public void setTimeAgoStr(String timeAgoStr) {
        this.timeAgoStr = timeAgoStr;
    }

    public boolean isFavor() {
        return favor;
    }

    public void setFavor(boolean favor) {
        this.favor = favor;
    }
}
