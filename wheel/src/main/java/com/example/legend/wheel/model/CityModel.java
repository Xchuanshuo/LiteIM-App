package com.example.legend.wheel.model;

import java.util.List;

/**
 * @author legend
 */
public class CityModel {
    private List<DistrictModel> districtList;
    private String name;

    public CityModel() {}

    public CityModel(String str, List<DistrictModel> list) {
        this.name = str;
        this.districtList = list;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public List<DistrictModel> getDistrictList() {
        return this.districtList;
    }

    public void setDistrictList(List<DistrictModel> list) {
        this.districtList = list;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("CityModel [name=").append(this.name).toString()).append(", districtList=").toString()).append(this.districtList).toString()).append("]").toString();
    }
}