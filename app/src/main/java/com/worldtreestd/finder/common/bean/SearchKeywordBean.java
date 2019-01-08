package com.worldtreestd.finder.common.bean;

/**
 * @author Legend
 * @data by on 18-7-18.
 * @description
 */
public class SearchKeywordBean implements IFlowDataBean {

    private int id;
    private String content;

    public SearchKeywordBean(String content) {
        this.content = content;
    }

    @Override
    public String getName() {
        return content;
    }
}
