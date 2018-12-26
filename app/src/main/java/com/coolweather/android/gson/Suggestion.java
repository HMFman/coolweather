package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HMF on 2018/12/22.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;    // 字段

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("cw")
    public CarWash carWash; // 字段

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public Sport sport;   // 字段

    public class Sport{
        @SerializedName("txt")
        public String info;
    }

}
