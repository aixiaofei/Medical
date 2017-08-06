package com.example.ai.dtest.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ai.dtest.MainActivity;
import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.LocationAdapter;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.db.City;
import com.example.ai.dtest.db.Country;
import com.example.ai.dtest.db.Province;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ai on 2017/8/5.
 */

public class locationDialog extends Dialog implements View.OnClickListener{

    private RecyclerView recFirst;

    private RecyclerView recSecond;

    private RecyclerView recThird;

    private LocationAdapter adapterFirst;

    private LocationAdapter adapterSrcond;

    private LocationAdapter adapterThird;

    private List<String> provinceList;

    private List<String> cityList;

    private List<String> countryList;

    private TextView province;

    private TextView city;

    private TextView country;

    private TextView autoLocation;

    private TextView select;

    private Province selectProvince;

    private City selectCity;

    private Context context;

    private selectLocationListener listener;

    private LocationClient client;

    public void setListener(selectLocationListener listener){
        this.listener= listener;
    }

    public locationDialog(@NonNull Context context) {
        super(context,R.style.myDialog);
        this.context= context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        setCanceledOnTouchOutside(false);
        setCancelable(true);

        recFirst= findViewById(R.id.rec_first);
        recSecond= findViewById(R.id.rec_second);
        recThird= findViewById(R.id.rec_third);

        provinceList= new ArrayList<>();
        cityList= new ArrayList<>();
        countryList= new ArrayList<>();

        adapterFirst= new LocationAdapter(R.layout.department_item,provinceList);
        adapterSrcond= new LocationAdapter(R.layout.department_item,cityList);
        adapterThird= new LocationAdapter(R.layout.department_item,countryList);

        LinearLayoutManager manager1= new LinearLayoutManager(context);
        LinearLayoutManager manager2= new LinearLayoutManager(context);
        LinearLayoutManager manager3= new LinearLayoutManager(context);

        recFirst.setLayoutManager(manager1);
        recSecond.setLayoutManager(manager2);
        recThird.setLayoutManager(manager3);

        recFirst.setAdapter(adapterFirst);
        recSecond.setAdapter(adapterSrcond);
        recThird.setAdapter(adapterThird);

        province= findViewById(R.id.province);
        city= findViewById(R.id.city);
        country= findViewById(R.id.country);

        autoLocation= findViewById(R.id.auto_location);
        select= findViewById(R.id.select);
        autoLocation.setOnClickListener(this);
        select.setOnClickListener(this);

        initListrner();
        autoLocation();
        queryProvince();
    }

    private void autoLocation(){
        client= new LocationClient(MyApplication.getContext());
        client.registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        client.start();
    }

    private class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                String location = bdLocation.getProvince()+"-"+bdLocation.getCity()+"-"+bdLocation.getDistrict();
                autoLocation.setText(location);
            } else {
                Toast.makeText(MyApplication.getContext(),"获取位置信息失败",Toast.LENGTH_SHORT).show();
            }
            if (client.isStarted()) {
                client.stop();
            }
        }
    }

    private void initListrner(){
        adapterFirst.setListener(new LocationAdapter.locationListener() {
            @Override
            public void process(int position) {
                city.setText(null);
                country.setText(null);
                province.setText(provinceList.get(position));
                selectProvince= DataSupport.where("name=?",provinceList.get(position)).findFirst(Province.class);
                adapterFirst.setCurrentPostion(position);
                adapterFirst.notifyDataSetChanged();
                adapterSrcond.setCurrentPostion(-1);
                adapterSrcond.notifyDataSetChanged();
                adapterThird.setCurrentPostion(-1);
                adapterThird.notifyDataSetChanged();
                queryCity();
            }
        });

        adapterSrcond.setListener(new LocationAdapter.locationListener() {
            @Override
            public void process(int position) {
                country.setText(null);
                city.setText(cityList.get(position));
                selectCity= DataSupport.where("name=?",cityList.get(position)).findFirst(City.class);
                adapterSrcond.setCurrentPostion(position);
                adapterSrcond.notifyDataSetChanged();
                adapterThird.setCurrentPostion(-1);
                adapterThird.notifyDataSetChanged();
                queryCountry();
            }
        });
        adapterThird.setListener(new LocationAdapter.locationListener() {
            @Override
            public void process(int position) {
                country.setText(countryList.get(position));
                adapterThird.setCurrentPostion(position);
                adapterThird.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auto_location:
                if(!TextUtils.isEmpty(autoLocation.getText())){
                    listener.complete(autoLocation.getText().toString());
                }
                break;
            case R.id.select:
                if(TextUtils.isEmpty(province.getText()) || TextUtils.isEmpty(city.getText())){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(country.getText()) && !countryList.isEmpty()){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }else {
                    String location= province.getText().toString()+"-"+city.getText().toString()+"-"+country.getText().toString();
                    listener.complete(location);
                }
                break;
            default:
                break;
        }
    }

    private void queryProvince(){
        List<Province> data= DataSupport.findAll(Province.class);
        provinceList.clear();
        for (Province province : data) {
            provinceList.add(province.getName());
        }
        adapterFirst.notifyDataSetChanged();
        recFirst.scrollToPosition(0);
    }

    private void queryCity(){
        List<City> data= DataSupport.where("parent_code=?",String.valueOf(selectProvince.getCode())).find(City.class);
        cityList.clear();
        for (City city : data) {
            cityList.add(city.getName());
        }
        adapterSrcond.notifyDataSetChanged();
        recSecond.scrollToPosition(0);
    }

    private void queryCountry(){
        List<Country> data= DataSupport.where("parent_code=?",String.valueOf(selectCity.getCode())).find(Country.class);
        countryList.clear();
        for (Country country : data) {
            countryList.add(country.getName());
        }
        adapterThird.notifyDataSetChanged();
        recThird.scrollToPosition(0);
    }

    public interface selectLocationListener{
        void complete(String location);
    }

}
