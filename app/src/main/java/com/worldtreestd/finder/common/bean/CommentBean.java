package com.worldtreestd.finder.common.bean;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class CommentBean {

    private int id;
    private String url;
    private String name;
    private String content;
    private String praiseCount;

    public CommentBean(String name, String praiseCount) {
        this.name = name;
        this.praiseCount = praiseCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }
}
