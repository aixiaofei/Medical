package com.example.ai.dtest.test;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by ai on 2017/7/10.
 */

public class Post {

    public static void main(String[] args){
        OkHttpClient client= new OkHttpClient();
        RequestBody responseBody= new FormBody.Builder().add("name","123").add("usermale","man").build();
        Request request= new Request.Builder().url("http://192.168.2.142:8080/internetmedical/user/useradd.do").post(responseBody).build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
