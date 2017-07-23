package com.example.ai.dtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.ai.dtest.commen.DoctorCustom;
import com.example.ai.dtest.commen.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class Map extends BaseActivity {

    private MapView mapView;

    private BaiduMap map;

    private boolean isFirstLocate= true;

    LocationClient client;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.UPDATEFAILURE:
                    break;
                case HttpUtils.UPDATESUCCESS:
                    Gson gson= new Gson();
                    Bundle update_bundle= msg.getData();
                    String buf= update_bundle.getString("result");
                    List<DoctorCustom> doctor= gson.fromJson(buf, new TypeToken<List<DoctorCustom>>(){}.getType());
                    if(doctor!=null) {
//                        BitmapFactory.Options option_1 = new BitmapFactory.Options();
//                        option_1.inSampleSize=4;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.doctor);
                        Bitmap res= scaleMatrixImage(bitmap,0.25f,0.25f);
                        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromBitmap(res);
                        List<Marker> markers= map.getMarkersInBounds(map.getMapStatus().bound);
                        if(markers!=null) {
                            for (Marker i : markers) {
                                i.remove();
                            }
                        }
                        for (DoctorCustom i : doctor) {
                            LatLng point = new LatLng(Double.valueOf(i.getDocloginlat()), Double.valueOf(i.getDocloginlon()));
                            OverlayOptions options= new MarkerOptions().position(point).title(i.getDocname()).animateType(MarkerOptions.MarkerAnimateType.none).visible(true).perspective(false)
                                    .icon(bitmapDescriptor);
                            map.addOverlay(options);
                        }
                        if (!bitmap.isRecycled()){
                            bitmap.recycle();
                        }
                        if(!res.isRecycled()){
                            res.recycle();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    BaiduMap.OnMapStatusChangeListener listener = new BaiduMap.OnMapStatusChangeListener() {
        public void onMapStatusChangeStart(MapStatus status){
        }
        public void onMapStatusChange(MapStatus status){
        }
        public void onMapStatusChangeFinish(MapStatus status){
            updateMarker(status.bound);
        }
    };

    BaiduMap.OnMapLoadedCallback callback = new BaiduMap.OnMapLoadedCallback() {
        /**
         * 地图加载完成回调函数
         */
        public void onMapLoaded(){
            updateMarker(map.getMapStatus().bound);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client= new LocationClient(Map.this);
        client.registerLocationListener(new LocationListener());
        LocationClientOption option= new LocationClientOption();
        option.setScanSpan(2000);
        option.setOpenGps(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        setContentView(R.layout.activity_map);
        mapView= (MapView) findViewById(R.id.map);
        map= mapView.getMap();
        map.setOnMapStatusChangeListener(listener);
        map.setOnMapLoadedCallback(callback);
    }

    @Override
    protected void onStart() {
        map.setMyLocationEnabled(true);
        if(!client.isStarted()){
            client.start();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        map.setMyLocationEnabled(false);
        if(client.isStarted()){
            client.stop();
        }
    }

    @Override
    protected void onStop() {
        map.setMyLocationEnabled(false);
        if(client.isStarted()){
            client.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.clear();
        mapView.onDestroy();
    }


    public class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                navigateTo(bdLocation);
            } else {
                Toast.makeText(Map.this,"获取位置信息失败",Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Map.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void navigateTo(BDLocation bdLocation){
        if(isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            map.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            map.animateMapStatus(update);
            isFirstLocate=false;
        }
        MyLocationData builder= new MyLocationData.Builder().accuracy(bdLocation.getRadius()).direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
        MyLocationConfiguration configuration= new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null);
        map.setMyLocationConfiguration(configuration);
        map.setMyLocationData(builder);
    }

    private void updateMarker(LatLngBounds bounds){
        LatLng buttom= bounds.southwest;
        LatLng top= bounds.northeast;
        StringBuilder result= new StringBuilder();
        if(top.latitude>buttom.latitude){
            result.append(buttom.latitude).append(",");
            result.append(top.latitude).append(":");
        }
        else {
            result.append(top.latitude).append(",");
            result.append(buttom.latitude).append(":");
        }
        if(top.longitude>buttom.longitude){
            result.append(buttom.longitude).append(",");
            result.append(top.longitude);
        }
        else {
            result.append(top.longitude).append(",");
            result.append(buttom.longitude);
        }
        HttpUtils.docterUpdataMap(result.toString(),handler);
    }

    public Bitmap scaleMatrixImage(Bitmap oldbitmap, float scaleWidth, float scaleHeight) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);// 放大缩小比例
        return Bitmap.createBitmap(oldbitmap, 0, 0, oldbitmap.getWidth(), oldbitmap.getHeight(), matrix, true);
    }
}

