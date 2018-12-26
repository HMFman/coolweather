package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HMF on 2018/12/22.
 */

public class Basic {
    // 使用 @SerializedName 注解的方式来让“JSON字段”和“Java字段”之间建立映射关系
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{   // 内部类
        @SerializedName("loc")
        public String updateTime;
    }

}
