package com.coolweather.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.android.db.City;
import com.coolweather.android.db.County;
import com.coolweather.android.db.Province;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *  这是一个碎片
 * Created by HMF on 2018/12/19.
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;  // 活动页面标题
    private Button backButton;   // 返回按钮
    private ListView listView;   // 下列列表
    private ArrayAdapter<String> adapter;    // 数组适配器
    private List<String> dataList = new ArrayList<>();   // 数组集合

    private List<Province> provinceList;   // 省列表
    private List<City> cityList;           // 市列表
    private List<County> countyList;       // 县列表
    private Province selectedProvince;     // 选中的省份
    private City selectedCity;             // 选中的城市
    private int currentLevel;              // 当前选中的级别， 与前面定义的常量有关

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = (TextView)view.findViewById(R.id.title_text);   // 获取 TextViw 实例
        backButton = (Button)view.findViewById(R.id.back_button);   // 获取 Button 实例
        listView = (ListView)view.findViewById(R.id.list_view);     // 获取 ListView 实例
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList); // 设置ListView的子项
        listView.setAdapter(adapter);   // 为 ListView 控件设置适配器
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // 为 ListView 下拉列表注册点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }else if(currentLevel == LEVEL_COUNTY){
                    String weatherId = countyList.get(position).getWeatherId();
                    if(getActivity() instanceof MainActivity){        // 加入滑动菜单时，添加的内容
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);   // 跳转活动页面
                        getActivity().finish();  // 关闭当前页面
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity)getActivity();
                        activity.drawerLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {   // 为 Button 返回按钮注册点击事件
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    /*** 查询全国所有的“省”，优先从数据库查询，如果没有查询到再去服务器上查询*/
    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);  // 隐藏返回按钮
        provinceList = DataSupport.findAll(Province.class);    // 查询数据
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();   // 通知数据已发生更改
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else {
            String address = "http://guolin.tech/api/china";  // 访问服务器获取中国的省份的地址
            queryFromServer(address, "province");
        }
    }

    /*** 查询选中省内所有的“市”，优先从数据库查询，如果没有查询到再去服务器上查询 */
    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);   // 显示返回按钮
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /*** 查询选中市内所有的“县”，优先从数据库查询，如果没有查询到再去服务器上查询 */
    private void queryCounties(){
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);   // 显示返回按钮
        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
            queryFromServer(address, "county");
        }
    }

    /** 根据传入的地址和类型从服务器上查询省市县数据 */
    private void queryFromServer(String address, final String type){
        showProgressDialog();  // 显示进度条对话框
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 通过 runOnUiThread() 方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {    // 这里注册了一个线程
                    @Override
                    public void run() {
                        closeProgressDialog();  // 关闭进度条对话框
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();   // 把服务器响应回来的数据转换成字符串
                boolean result = false;
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText, selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountryResponse(responseText, selectedCity.getId());
                }
                if(result){
                    getActivity().runOnUiThread(new Runnable() {    // 这里注册了一个线程
                        @Override
                        public void run() {
                            closeProgressDialog();   // 关闭进度条对话框
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /*** 显示进度对话框 */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());   // 创建 ProgressDialog 实例
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    /*** 关闭进度对话框 */
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}




