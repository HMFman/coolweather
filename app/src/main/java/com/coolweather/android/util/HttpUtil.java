package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by HMF on 2018/12/19.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();   // 在 build()方法之前可以连缀其他的方法来丰富这个 Request 对象
        client.newCall(request).enqueue(callback);
    }
}



