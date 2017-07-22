package com.example.ai.dtest.commen;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

/**
 * Created by ai on 2017/7/12.
 */

public class MyLocationListener implements BDLocationListener{

    private static final int LOCATION=1;

    private static Handler handler;

    public MyLocationListener(Handler handler){
        MyLocationListener.handler =handler;
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
    }
}
