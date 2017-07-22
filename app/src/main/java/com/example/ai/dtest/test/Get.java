package com.example.ai.dtest.test;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ai on 2017/7/10.
 */

public class Get {
    public static void main(String[] args){
        OkHttpClient client= new OkHttpClient();
        //RequestBody responseBody= new FormBody.Builder().add("name","123").add("age","12").build();
        Request request= new Request.Builder().url("http://192.168.2.142:8080/internetmedical/doctor/doctors.do").build();
        try {
            Response response = client.newCall(request).execute();
            if(response.body().string()!=null){
                System.out.println(response.body().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
