package com.example.legend.wheel.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.legend.wheel.OnWheelChangedListener;
import com.example.legend.wheel.R;
import com.example.legend.wheel.WheelView;
import com.example.legend.wheel.adapters.ArrayWheelAdapter;
import com.example.legend.wheel.handler.XmlParserHandler;
import com.example.legend.wheel.model.CityModel;
import com.example.legend.wheel.model.DistrictModel;
import com.example.legend.wheel.model.ProvinceModel;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Legend
 * @data by on 19-1-7.
 * @description
 */
public class PlaceSelectorDialog implements View.OnClickListener
        , OnWheelChangedListener {

    private AlertDialog mAlertDialog;
    private Context mContext;
    private EditText editText;
    private String mCurrentProvinceName;
    private String mCurrentCityName;
    private String mCurrentDistrictName;
    private String mCurrentZipCode;
    private List<String> provinceData = new ArrayList<>();
    private Button mConfirmBtn;
    private WheelView mProvinceView;
    private WheelView mCityView;
    private WheelView mDistrictView;
    private Map<String, List<String>> provinceDataMap = new HashMap<>();
    private Map<String, List<String>> cityDataMap = new HashMap<>();
    private String result = "";
    private TextView mTextView;


    public PlaceSelectorDialog(Context context, TextView textView) {
        this.mContext = context;
        this.mTextView = textView;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.dialog_place_main, null);
        setUpViews(viewGroup);
        setUpListener();
        setUpData();
        builder.setView(viewGroup);
        this.mAlertDialog = builder.create();
    }

    private void setUpViews(View view) {
        this.mProvinceView = view.findViewById(R.id.wheel_province);
        this.mCityView = view.findViewById(R.id.wheel_city);
        this.mDistrictView = view.findViewById(R.id.wheel_district);
        this.mConfirmBtn = view.findViewById(R.id.btn_confirm);
        this.editText = view.findViewById(R.id.tv_detail_place);
    }

    private void setUpListener() {
        this.mProvinceView.addChangingListener(this);
        this.mCityView.addChangingListener(this);
        this.mDistrictView.addChangingListener(this);
        this.mConfirmBtn.setOnClickListener(this);
    }

    private void setUpData() {
        updateProvinces();
        this.mProvinceView.setViewAdapter(new ArrayWheelAdapter<>(this.mContext, this.provinceData.toArray()));
        this.mProvinceView.setVisibleItems(7);
        this.mCityView.setVisibleItems(7);
        this.mDistrictView.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    public String getResult() {
        return result;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {
            this.result = mCurrentProvinceName + mCurrentCityName +
                    mCurrentDistrictName + editText.getText().toString();
            this.mTextView.setText(result);
            dismiss();
        }
    }

    public void show() {
        this.mAlertDialog.show();
    }

    public void dismiss() {
        this.mAlertDialog.dismiss();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == this.mProvinceView) {
            updateCities();
        } else if (wheel == this.mCityView) {
            updateAreas();
        } else if (wheel == this.mDistrictView) {
            this.mCurrentDistrictName = cityDataMap.get(mCurrentCityName).get(newValue);
        }
    }

    /** 更新地区信息 **/
    private void updateAreas() {
        this.mCurrentCityName = provinceDataMap
                .get(mCurrentProvinceName).get(mCityView.getCurrentItem());
        List<String> districtList = cityDataMap.get(mCurrentCityName);
        this.mDistrictView.setViewAdapter(new ArrayWheelAdapter<>(mContext, districtList.toArray()));
        this.mDistrictView.setCurrentItem(0);
    }

    /** 更新城市信息 **/
    private void updateCities() {
        this.mCurrentProvinceName = provinceData.get(mProvinceView.getCurrentItem());
        List<String> cityList = provinceDataMap.get(mCurrentProvinceName);
        this.mCityView.setViewAdapter(new ArrayWheelAdapter<>(mContext, cityList.toArray()));
        this.mCityView.setCurrentItem(0);
        updateAreas();
    }

    /** 更新省份信息 **/
    private void updateProvinces() {
        try {
            InputStream inputStream = mContext.getAssets().open("province_data.xml");
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            // 解析处理器
            XmlParserHandler xmlParserHandler = new XmlParserHandler();
            saxParser.parse(inputStream, xmlParserHandler);
            inputStream.close();
            List<ProvinceModel> dataList = xmlParserHandler.getDataList();
            if (dataList!=null && dataList.size()>0) {
                this.mCurrentProvinceName = dataList.get(0).getName();
                List<CityModel> cityList = dataList.get(0).getCityList();
                if (cityList!=null && cityList.size()>0) {
                    this.mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    this.mCurrentDistrictName = districtList.get(0).getName();
                    this.mCurrentZipCode = districtList.get(0).getZipcode();
                }
                for (int i=0;i<dataList.size();i++) {
                    String provinceName = dataList.get(i).getName();
                    List<CityModel> cities = dataList.get(i).getCityList();
                    provinceData.add(provinceName);
                    provinceDataMap.put(provinceName, getCityNameList(cities));
                    for (int j=0;j<cities.size();j++) {
                        List<DistrictModel> districtList = cities.get(j).getDistrictList();
                        cityDataMap.put(cities.get(j).getName(), getDistrictNameList(districtList));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private List<String> getCityNameList(List<CityModel> cityModels) {
        List<String> list = new ArrayList<>();
        for (CityModel model: cityModels) {
            list.add(model.getName());
        }
        return list;
    }

    private List<String> getDistrictNameList(List<DistrictModel> districtModels) {
        List<String> list = new ArrayList<>();
        for (DistrictModel model: districtModels) {
            list.add(model.getName());
        }
        return list;
    }
}
