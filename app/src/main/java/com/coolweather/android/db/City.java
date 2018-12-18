package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by HMF on 2018/12/18.
 *
 * LitePal 中的每一个实体类都是必须要继承自 DataSupport类的
 */

public class City extends DataSupport {
    private int id;            // id 是每个实体类中都应该有的字段
    private String cityName;   // 记录市的名字
    private int cityCode;      // 记录市的代号
    private int provinceId;    // 记录当前市所属省的 id 值

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }
    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }
    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

}


