package com.legend.liteim.bean;

import com.legend.liteim.common.bean.IFlowDataBean;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description 历史搜索关键词
 */
public class SearchKeyword extends LitePalSupport implements IFlowDataBean {

    private int id;
    private String content;
    private Date date;

    public SearchKeyword(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getName() {
        return content;
    }
}
