package com.example.ai.dtest.frag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.ai.dtest.MainActivity;
import com.example.ai.dtest.R;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.frag.DoctorList;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.ImgUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;

public class Map extends BaseFragment {

    private MapView mapView;

    private BaiduMap map;

    private boolean isFirstLocate= true;

    private LocationClient client;

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
                        Bitmap bitmap = zoonBitmap(R.drawable.doctor);
                        BitmapDescriptor bitmapDescriptor= BitmapDescriptorFactory.fromBitmap(bitmap);
                        List<Marker> markers= map.getMarkersInBounds(map.getMapStatus().bound);
                        if(markers!=null) {
                            for (Marker i : markers) {
                                i.remove();
                            }
                        }
                        for (DoctorCustom i : doctor) {
                            LatLng point = new LatLng(Double.valueOf(i.getDocloginlat()), Double.valueOf(i.getDocloginlon()));
                            OverlayOptions options= new MarkerOptions().position(point).title("haha").visible(true).perspective(true).icon(bitmapDescriptor);
                            map.addOverlay(options);
                        }
                        ImgUtils.recycleBitmap(bitmap);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client= new LocationClient(MyApplication.getContext());
        client.registerLocationListener(new LocationListener());
        LocationClientOption option= new LocationClientOption();
        option.setScanSpan(2000);
        option.setOpenGps(true);
        option.setNeedDeviceDirect(true);
        option.setIsNeedAddress(true);
        client.setLocOption(option);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_map,container,false);
        mapView= (MapView) view.findViewById(R.id.map);
        map= mapView.getMap();
        map.setOnMapStatusChangeListener(listener);
        map.setOnMapLoadedCallback(callback);
        return view;
    }

    @Override
    public void onStart() {
        map.setMyLocationEnabled(true);
        if(!client.isStarted()){
            client.start();
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            mapView.onPause();
            map.setMyLocationEnabled(false);
            if(client.isStarted()){
                client.stop();
            }
        }else {
            mapView.onResume();
            map.setMyLocationEnabled(true);
            if(!client.isStarted()){
                client.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        map.setMyLocationEnabled(false);
        if(client.isStarted()){
            client.stop();
        }
    }

    @Override
    public void onStop() {
        map.setMyLocationEnabled(false);
        if(client.isStarted()){
            client.stop();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        map.clear();
        mapView.onDestroy();
    }


    private class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                navigateTo(bdLocation);
            } else {
                Toast.makeText(MyApplication.getContext(),"获取位置信息失败",Toast.LENGTH_SHORT).show();
                MainActivity activity= (MainActivity) getActivity();
                activity.addFragment(new DoctorList());
            }
        }
    }

    private void navigateTo(BDLocation bdLocation){
        if(isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            map.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(17f);
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

    private Bitmap zoonBitmap(int resourceId){
        Bitmap bitmapBuf;
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(getResources(),resourceId,options);
        int width = options.outWidth;
        int height= options.outHeight;
        double widthScale= 1;
        double heightScale= 1;
        if(width>64){
            widthScale= (double) width/64;
        }
        if(height>64){
            heightScale= (double) height/64;
        }
        options.inJustDecodeBounds=false;
        options.inSampleSize=(int) Math.max(widthScale,heightScale);
        bitmapBuf= BitmapFactory.decodeResource(getResources(),resourceId,options);
        return bitmapBuf;
    }
}

