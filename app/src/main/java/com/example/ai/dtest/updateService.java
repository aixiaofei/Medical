package com.example.ai.dtest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.ai.dtest.data.DoctorCustom;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ai on 2017/7/9.
 */

public class updateService extends Service{

    public static final int UPDATEFAILURE=9;

    public static final int UPDATESUCCESS=10;

    private OkHttpClient okHttpClient= new OkHttpClient();

    private static final String docterUpdataUrl= "http://192.168.2.2:8080/internetmedical/user/doctors";

    private IBinder downBinder = new DownBinder();

    public class DownBinder extends Binder{

        public void docterUpdataDefault(String select, final Handler handler){
//            RequestBody responseBodyponseBody=  new FormBody.Builder().add("mobile",phone).add("templateid",TEMPLATEID).add("codeLen","6").build();
            Request request = new Request.Builder().url(docterUpdataUrl).build();
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message= new Message();
                    message.what= UPDATEFAILURE;
                    handler.sendMessage(message);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String buf= response.body().string();
                    Gson gson= new Gson();
                    docterList docter= gson.fromJson(buf,docterList.class);
                    if(docter.state.equals("1")){
                        Log.d("ai",docter.state);
                        String date= gson.toJson(docter.result);
                        Bundle bundle= new Bundle();
                        bundle.putString("result",date);
                        Message message= new Message();
                        message.what= UPDATESUCCESS;
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                    else {
                        Log.d("ai",docter.state);
                        Message message= new Message();
                        message.what= UPDATEFAILURE;
                        handler.sendMessage(message);
                    }
                }
            });
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ai","binder2");
        return downBinder;
    }
    private class docterList{
        public String state;
        public List<DoctorCustom> result;
    }
}
