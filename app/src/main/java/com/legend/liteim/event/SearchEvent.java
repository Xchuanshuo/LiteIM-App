package com.legend.liteim.event;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class SearchEvent {

    private String keyword;

    public SearchEvent(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}
