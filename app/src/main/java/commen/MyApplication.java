package commen;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;

import org.litepal.LitePalApplication;

/**
 * Created by ai on 2017/7/11.
 */

public class MyApplication extends Application{

    private static Context context;

    private static String userName=null;

    private static LocationClient client;
    @Override
    public void onCreate() {
        super.onCreate();
        context= getApplicationContext();
        LitePalApplication.initialize(context);
        client= new LocationClient(context);
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

    public static LocationClient getClient() {
        return client;
    }

    public static void setClient(LocationClient client) {
        MyApplication.client = client;
    }
}
