package com.example.ai.dtest.base;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePalApplication;

/**
 * Created by ai on 2017/7/11.
 */

public class MyApplication extends Application{

    private static Context context;

    private static String userName=null;

    private static String userPhone=null;

    private static Bitmap bitmap;

    @Override
    public void onCreate() {
        super.onCreate();
        context= getApplicationContext();
        LitePalApplication.initialize(context);
        SDKInitializer.initialize(context);
    }

    public static String getUserName(){
        return userName;
    }

    public static void setUserName(String name){
        userName=name;
    }

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        MyApplication.userPhone = userPhone;
    }

    public static Context getContext(){
        return context;
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        MyApplication.bitmap = bitmap;
    }
}
