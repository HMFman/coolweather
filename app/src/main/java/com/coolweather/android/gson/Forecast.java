package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HMF on 2018/12/22.
 */

public class Forecast {

    public String date;

    @SerializedName("cond")
    public More more;

    public class More{   // 内部类
        @SerializedName("txt_d")
        public String info;
    }

    @SerializedName("tmp")
    public Temperature temperature;

    public class Temperature{  // 内部类
        public String max;
        public String min;
    }

}
