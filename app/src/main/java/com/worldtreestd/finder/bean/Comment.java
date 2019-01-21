package com.worldtreestd.finder.bean;

/**
 * @author Legend
 * @data by on 19-1-21.
 * @description
 */
public class Comment {


    /**
     * id : 4
     * userId : 6
     * targetId : 37
     * content : 但使东山谢安石搜索
     * favorNum : 0
     * createTime : 2019-01-22T07:55:43
     * username : legend
     * portrait : /6/portraits/3e3455ec332f4a22af0b5f93218c1eac.jpg
     * timeAgoStr : 11天前
     * favor : false
     */

    private int id;
    private int userId;
    private int targetId;
    private String content;
    private int favorNum;
    private String createTime;
    private String username;
    private String portrait;
    private String timeAgoStr;
    private boolean favor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
