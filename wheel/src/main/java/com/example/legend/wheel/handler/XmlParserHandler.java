package com.example.legend.wheel.handler;

import com.example.legend.wheel.model.CityModel;
import com.example.legend.wheel.model.DistrictModel;
import com.example.legend.wheel.model.ProvinceModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author legend
 */
public class XmlParserHandler extends DefaultHandler {

    CityModel cityModel = new CityModel();
    DistrictModel districtModel = new DistrictModel();
    private List<ProvinceModel> provinceList = new ArrayList();
    ProvinceModel provinceModel = new ProvinceModel();

    @Override
    public void characters(char[] cArr, int i, int i2) throws SAXException {
    }

    @Override
    public void startDocument() throws SAXException {
    }

    public List<ProvinceModel> getDataList() {
        return this.provinceList;
    }

    @Override
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if ("province".equals(str3)) {
            this.provinceModel = new ProvinceModel();
            this.provinceModel.setName(attributes.getValue(0));
            this.provinceModel.setCityList(new ArrayList());
        } else if ("city".equals(str3)) {
            this.cityModel = new CityModel();
            this.cityModel.setName(attributes.getValue(0));
            this.cityModel.setDistrictList(new ArrayList());
        } else if ("district".equals(str3)) {
            this.districtModel = new DistrictModel();
            this.districtModel.setName(attributes.getValue(0));
            this.districtModel.setZipcode(attributes.getValue(1));
        }
    }

    @Override
    public void endElement(String str, String str2, String str3) throws SAXException {
        if ("district".equals(str3)) {
            this.cityModel.getDistrictList().add(this.districtModel);
        } else if ("city".equals(str3)) {
            this.provinceModel.getCityList().add(this.cityModel);
        } else if ("province".equals(str3)) {
            this.provinceList.add(this.provinceModel);
        }
    }
}