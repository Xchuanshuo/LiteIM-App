package com.example.legend.wheel.model;

import java.util.List;

/**
 * @author legend
 */
public class ProvinceModel {
    private List<CityModel> cityList;
    private String name;

    public ProvinceModel() { }

    public ProvinceModel(String str, List<CityModel> list) {
        this.name = str;
        this.cityList = list;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<CityModel> getCityList() {
        return this.cityList;
    }

    public void setCityList(List<CityModel> list) {
        this.cityList = list;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("ProvinceModel [name=").append(this.name).toString()).append(", cityList=").toString()).append(this.cityList).toString()).append("]").toString();
    }
}