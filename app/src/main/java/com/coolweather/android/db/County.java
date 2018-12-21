package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by HMF on 2018/12/18.
 *
 * LitePal 中的每一个实体类都是必须要继承自 DataSupport类的
 */

public class County extends DataSupport {
    private int id;              // id 是每个实体类中都应该有的字段
    private String countyName;   // 记录县的名字
    private String weatherId;    // 记录县所对应的天气 id
    private int cityId;          // 记录当前县所属市的 id 值

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }
    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }
    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }
    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

}



