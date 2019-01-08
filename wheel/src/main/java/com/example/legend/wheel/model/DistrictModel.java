package com.example.legend.wheel.model;

/**
 * @author legend
 */
public class DistrictModel {
    private String name;
    private String zipCode;

    public DistrictModel() { }

    public DistrictModel(String str, String str2) {
        this.name = str;
        this.zipCode = str2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getZipcode() {
        return this.zipCode;
    }

    public void setZipcode(String str) {
        this.zipCode = str;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append("DistrictModel [name=").append(this.name).toString()).append(", zipcode=").toString()).append(this.zipCode).toString()).append("]").toString();
    }
}