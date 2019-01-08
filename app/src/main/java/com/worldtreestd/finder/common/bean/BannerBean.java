package com.worldtreestd.finder.common.bean;

/**
 * @author Legend
 * @data by on 18-7-16.
 * @description
 */
public class BannerBean implements IBannerBean {

    private String name;
    private String url;

    public BannerBean(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImageUrl() {
        return url;
    }
}
