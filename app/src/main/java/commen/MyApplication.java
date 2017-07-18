package commen;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePalApplication;

/**
 * Created by ai on 2017/7/11.
 */

public class MyApplication extends Application{

    private static Context context;

    private static String userName=null;

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

    public static Context getContext(){
        return context;
    }
}
