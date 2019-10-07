package com.legend.liteim.db;

import com.legend.liteim.bean.SearchKeyword;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-10-6.
 * @description 搜索关键词
 */
public class SearchHelper {

    public static SearchHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static SearchHelper INSTANCE = new SearchHelper();
    }

    public List<SearchKeyword> getKeywordList() {
        return LitePal.limit(20).order("date desc").find(SearchKeyword.class);
    }

    public void clearAll() {
        LitePal.deleteAll(SearchKeyword.class);
    }
}
