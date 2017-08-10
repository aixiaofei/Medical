package com.example.ai.dtest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.LocationAdapter;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.db.City;
import com.example.ai.dtest.db.Country;
import com.example.ai.dtest.db.Province;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;
import java.util.ArrayList;
import java.util.List;
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

    private Province selectProvince;

    private City selectCity;

    private Context context;

    private selectLocationListener listener;

    private LocationClient client;

    private int level;

    private boolean auto;



    public void setListener(selectLocationListener listener){
        this.listener= listener;
    }

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==HttpUtils.GETLOCATIONSU) {
                Bundle bundle = msg.getData();
                Gson gson = new Gson();
                String res = bundle.getString("result");
                if (!TextUtils.isEmpty(res)) {
                    if (level == 0) {
                        List<Province> data = gson.fromJson(res, new TypeToken<List<Province>>(){}.getType());
                        for (Province province : data) {
                            province.save();
                        }
                        queryProvince();
                    } else if (level == 1) {
                        List<City> data = gson.fromJson(res, new TypeToken<List<City>>(){}.getType());
                        if(!data.isEmpty()) {
                            for (City city : data) {
                                city.save();
                            }
                            queryCity();
                        }else {
                            cityList.clear();
                            adapterSrcond.notifyDataSetChanged();
                        }
                    } else {
                        List<Country> data = gson.fromJson(res, new TypeToken<List<Country>>(){}.getType());
                        if(!data.isEmpty()) {
                            for (Country country : data) {
                                country.save();
                            }
                            queryCountry();
                        }else {
                            countryList.clear();
                            adapterThird.notifyDataSetChanged();
                        }
                    }
                }
            }else if(msg.what==100){
                Bundle bundle= msg.getData();
                String location= bundle.getString("location");
                autoLocation.setText(location);
                auto=true;
            }else {
                auto=false;
                autoLocation.setText("定位失败,点击重新获取");
            }
        }
    };

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
        TextView select = findViewById(R.id.select);
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

    private class LocationListener implements BDLocationListener{
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation){
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                String location = bdLocation.getProvince()+"-"+bdLocation.getCity()+"-"+bdLocation.getDistrict();
                Message message= new Message();
                Bundle bundle= new Bundle();
                bundle.putString("location",location);
                message.setData(bundle);
                message.what=100;
                handler.sendMessage(message);
            } else {
                Message message= new Message();
                message.what=200;
                handler.sendMessage(message);
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
                if(selectProvince==null || !provinceList.get(position).equals(selectProvince.getCityname())) {
                    city.setText(null);
                    country.setText(null);
                    province.setText(provinceList.get(position));
                    selectProvince = DataSupport.where("cityname=?", provinceList.get(position)).findFirst(Province.class);
                    adapterFirst.setCurrentPostion(position);
                    adapterFirst.notifyDataSetChanged();

                    if (!cityList.isEmpty()) {
                        adapterSrcond.setCurrentPostion(-1);
                        cityList.clear();
                        adapterSrcond.notifyDataSetChanged();
                    }

                    if (!countryList.isEmpty()) {
                        adapterThird.setCurrentPostion(-1);
                        countryList.clear();
                        adapterThird.notifyDataSetChanged();
                    }
                    queryCity();
                }
            }
        });

        adapterSrcond.setListener(new LocationAdapter.locationListener() {
            @Override
            public void process(int position) {
                if(selectCity==null || !cityList.get(position).equals(selectCity.getCityname())) {
                    country.setText(null);
                    city.setText(cityList.get(position));
                    selectCity = DataSupport.where("cityname=?", cityList.get(position)).findFirst(City.class);
                    adapterSrcond.setCurrentPostion(position);
                    adapterSrcond.notifyDataSetChanged();

                    if (!countryList.isEmpty()) {
                        adapterThird.setCurrentPostion(-1);
                        countryList.clear();
                        adapterThird.notifyDataSetChanged();
                    }

                    queryCountry();
                }
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
                if(auto){
                    listener.complete(autoLocation.getText().toString());
                }else {
                    autoLocation();
                }
                break;
            case R.id.select:
                if(TextUtils.isEmpty(province.getText())){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(city.getText()) && !cityList.isEmpty()){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(country.getText()) && !countryList.isEmpty()){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }else {
                    StringBuilder location= new StringBuilder();
                    if(!TextUtils.isEmpty(province.getText())){
                        location.append(province.getText().toString());
                    }
                    if(!TextUtils.isEmpty(city.getText())){
                        location.append("-").append(city.getText().toString());
                    }
                    if(!TextUtils.isEmpty(country.getText())){
                        location.append("-").append(country.getText().toString());
                    }
                    listener.complete(location.toString());
                }
                break;
            default:
                break;
        }
    }

    private void queryProvince(){
        List<Province> data= DataSupport.findAll(Province.class);
        if(data.size()>0) {
            provinceList.clear();
            for (Province province : data) {
                provinceList.add(province.getCityname());
            }
            adapterFirst.notifyDataSetChanged();
            recFirst.smoothScrollToPosition(0);
        }else {
            level=0;
            HttpUtils.getLocation("0",handler);
        }
    }

    private void queryCity(){
        List<City> data= DataSupport.where("cityparentcode=?",String.valueOf(selectProvince.getCitycode())).find(City.class);
        if(data.size()>0) {
            cityList.clear();
            for (City city : data) {
                cityList.add(city.getCityname());
            }
            adapterSrcond.notifyDataSetChanged();
            recSecond.smoothScrollToPosition(0);
        }else {
            level=1;
            HttpUtils.getLocation(selectProvince.getCitycode(),handler);
        }
    }

    private void queryCountry(){
        List<Country> data= DataSupport.where("cityparentcode=?",String.valueOf(selectCity.getCitycode())).find(Country.class);
        if(data.size()>0) {
            countryList.clear();
            for (Country country : data) {
                countryList.add(country.getCityname());
            }
            adapterThird.notifyDataSetChanged();
            recThird.smoothScrollToPosition(0);
        }else {
            level=2;
            HttpUtils.getLocation(selectCity.getCitycode(),handler);
        }
    }

    public interface selectLocationListener{
        void complete(String location);
    }

}
