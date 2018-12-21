package com.coolweather.android.db;

import org.litepal.crud.DataSupport;

/**
 * Created by HMF on 2018/12/18.
 *  LitePal 中的每一个实体类都是必须要继承自 DataSupport类的
 */

public class Province extends DataSupport {
    private int id;                // id 是每个实体类中都应该有的字段
    private String provinceName;   // 记录省的名字
    private int provinceCode;      // 记录省的代号

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }
    public void setProvinceCode(int proviceCode) {
        this.provinceCode = proviceCode;
    }

}



