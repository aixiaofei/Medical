package com.example.ai.dtest.util;

/**
 * Created by ai on 2017/7/10.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.data.UerInfo;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String SERVER_URL_SEND = "https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_URL_VERIFY = "https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
    private static final String SERVER_URL_LOGIN = "http://192.168.2.2:8080/internetmedical/user/login.do"; //用户登录接口
    private static final String SERVER_URL_CANREGISTER= "http://192.168.2.2:8080/internetmedical/user/phonetest"; // 判断是否可注册接口
    private static final String SERVER_URL_REGISTER = "http://192.168.2.2:8080/internetmedical/user/register"; //用户注册接口
    private static final String DOCTERUPDATEDEFAULT = "http://192.168.2.2:8080/internetmedical/user/doctors"; //主页面查询接口
    private static final String DOCTERUPDATEMAP = "http://192.168.2.2:8080/internetmedical/user/mapdoctors"; //地图模式查询接口
    private static final String PUSHIMAGE = "http://192.168.2.2:8080/internetmedical/user/pullpix"; // 上传图片接口
    private static final String PULLIMAGE= "http://192.168.2.2:8080/internetmedical/user/pushpix"; //下载图片接口
    private static final String CHANGENICKNAME= "http://192.168.2.2:8080/internetmedical/user/editusername"; //修改昵称接口
    private static final String EXITLOGIN= "http://192.168.2.2:8080/internetmedical/user/exit"; //退出登录接口
    private static final String CANCHANGEPASSWORD= "http://192.168.2.2:8080/internetmedical/user/checkpassword"; //能否修改密码接口
    private static final String CHANGEPASSWORD= "http://192.168.2.2:8080/internetmedical/user/editpassword"; //修改密码接口
    private static final String GETUSERINFO= "http://192.168.2.2:8080/internetmedical/user/getinfo"; //获取用户信息接口
    private static final String PUSHUSERINFO= "http://192.168.2.2:8080/internetmedical/user/editinfo"; //上传用户信息接口
    private static final String PULLFAMILYINFO= "http://192.168.2.2:8080/internetmedical/user/findfamily"; //同步家人信息接口
    private static final String ADDFAMILYINFO= "http://192.168.2.2:8080/internetmedical/user/addfamily"; //添加家人信息接口
    private static final String DEFAMILYINFO= "http://192.168.2.2:8080/internetmedical/user/deletefamily"; //删除家人信息接口
    private static final String APP_KEY = "69faeb15aa2238ed28ebfebfc52b23c5";//网易云信分配的账号
    private static final String APP_SECRET = "4f0a0a22be5d";//网易云信分配的密钥
    private static final String NONCE = "123456";//随机数
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final String TEMPLATEID = "3064776";
    public static final int SENDFAILURE = 1; //发送验证码失败
    public static final int SENDSUCCESS = 2; // 发送验证码成功
    public static final int CHECKFAILURE = 3; // 校验验证码失败
    public static final int CHECKSUCESS = 4; // 校验验证码成功
    public static final int REGISTERFAILURE = 5;// 注册失败
    public static final int REGISTERSUCESS = 6; // 注册成功
    public static final int LOGINFAILURE = 7; // 登陆失败
    public static final int LOGINSUCESS = 8; // 登陆成功
    public static final int UPDATEFAILURE = 9; // 更新信息失败
    public static final int UPDATESUCCESS = 10; // 更新信息成功
    public static final int NOCANREGISTER=11; // 号码不可以注册
    public static final int CANREGISTER=12; // 号码可以注册
    public static final int PUSHIMAGEFAILURE =13; // 上传照片失败
    public static final int PUSHIMAGENOFILE =14; //上传头像不存在
    public static final int PUSHIMAGESUCESS =15; //上传头像成功
    public static final int PULLIMAGEFAILURE =16; // 下载头像失败
    public static final int PULLIMAGESUCESS=17; // 下载头像成功
    public static final int CHANGENICKNAMEFAILURE=18; // 更改昵称失败
    public static final int CHANGENICKNAMESUCESS=19; // 更改昵称成功
    public static final int EXITFAILURE= 20; // 退出登陆失败
    public static final int EXITSUCESS=21; //  退出登录成功
    public static final int NOCANCHANGEPW= 22; // 不可以修改密码
    public static final int CANCHANGEPW= 23; // 可以修改密码
    public static final int CHANGEPWFAILURE=24; //更改密码失败
    public static final int CHANGEPWSUCESS=25; // 更改密码成功
    public static final int GETUSERINFOFAILURE=26; //获取用户信息失败
    public static final int GETUSERINFOSUCESS=27; // 获取用户信息成功
    public static final int PUSHINFOFAILURE=28; // 上传用户信息失败
    public static final int PUSHINFOSUCESS=29; // 上传用户信息成功
    public static final int PULLFAMILYINFOFA=30; // 更新家人信息失败
    public static final int PULLFAMILYINFOSU=31; // 更新家人信息成功
    public static final int ADDFAMILYINFOFA=32; // 添加家人信息失败
    public static final int ADDFAMILYINFOSU=33; // 添加家人信息成功
    public static final int DEFAMILYINFOFA=34; // 删除家人信息失败
    public static final int DEFAMILYINFOSU=35; // 删除家人信息成功


    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    public static void sendMsg(String phone, final Handler handler) throws IOException {
        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        RequestBody responseBody = new FormBody.Builder().add("mobile", phone).add("templateid", TEMPLATEID).add("codeLen", "6").build();
        Request request = new Request.Builder().url(SERVER_URL_SEND).
                addHeader("AppKey", APP_KEY).
                addHeader("Nonce", NONCE)
                .addHeader("CurTime", curTime)
                .addHeader("CheckSum", checkSum)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = SENDFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson = new Gson();
                code i = gson.fromJson(buf, code.class);
                if (i.code.equals("200")) {
                    Log.d("ai", "成功");
                    Message message = new Message();
                    message.what = SENDSUCCESS;
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败");
                    Message message = new Message();
                    message.what = SENDFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void checkMsg(String phone, String sum, final Handler handler) throws IOException {
        String curTime = String.valueOf((new Date().getTime() / 1000L));
        String checkSum = CheckSumBuilder.getCheckSum(APP_SECRET, NONCE, curTime);
        RequestBody responseBody = new FormBody.Builder().add("mobile", phone).add("code", sum).build();
        Request request = new Request.Builder().url(SERVER_URL_VERIFY).
                addHeader("AppKey", APP_KEY).
                addHeader("Nonce", NONCE)
                .addHeader("CurTime", curTime)
                .addHeader("CheckSum", checkSum)
                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = CHECKFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson = new Gson();
                code i = gson.fromJson(buf, code.class);
                if (i.code.equals("200")) {
                    Log.d("ai", "成功"+i.code);
                    Message message = new Message();
                    message.what = CHECKSUCESS;
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败"+i.code);
                    Message message = new Message();
                    message.what = CHECKFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class code {
        String code;
    }

    public static void login(final String user, final Handler handler) {
        RequestBody responseBody = RequestBody.create(JSON, user);
        Request request = new Request.Builder().url(SERVER_URL_LOGIN).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = LOGINFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String buf = response.body().string();
                login_state state = gson.fromJson(buf, login_state.class);
                if (state.state.equals("1")) {
                    Log.d("ai", "成功1");
                    Message message = new Message();
                    message.what = LOGINSUCESS;
                    Bundle bundle = new Bundle();
                    bundle.putString("token", state.token);
                    bundle.putString("username",state.username);
                    bundle.putString("user", user);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败");
                    Message message = new Message();
                    message.what = LOGINFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class login_state {
        String state;
        String token;
        String username;
    }

    public static void canRegister(String user, final Handler handler) throws IOException {
        RequestBody responseBody = RequestBody.create(JSON, user);
        Request request = new Request.Builder().url(SERVER_URL_CANREGISTER).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = NOCANREGISTER;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson= new Gson();
                State res= gson.fromJson(buf,State.class);
                if (res.state.equals("1")) {
                    Log.d("ai", "成功"+buf);
                    Message message = new Message();
                    message.what = CANREGISTER;
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败"+buf);
                    Message message = new Message();
                    message.what = NOCANREGISTER;
                    handler.sendMessage(message);
                }
            }
        });
    }


    public static void register(String user, final Handler handler) throws IOException {
        RequestBody responseBody = RequestBody.create(JSON, user);
        Request request = new Request.Builder().url(SERVER_URL_REGISTER).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = REGISTERFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson= new Gson();
                State res= gson.fromJson(buf,State.class);
                if (res.state.equals("1")) {
                    Log.d("ai", "成功"+buf);
                    Message message = new Message();
                    message.what = REGISTERSUCESS;
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败"+buf);
                    Message message = new Message();
                    message.what = REGISTERFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void docterUpdata(String info,final Handler handler) {
        RequestBody responseBody = RequestBody.create(JSON, info);
        Request request = new Request.Builder().url(DOCTERUPDATEDEFAULT).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = UPDATEFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson = new Gson();
                docterList docter = gson.fromJson(buf, docterList.class);
                if (docter.state.equals("1")) {
                    Log.d("ai", "成功");
                    String date = gson.toJson(docter.result);
                    Bundle bundle = new Bundle();
                    bundle.putString("result", date);
                    Message message = new Message();
                    message.what = UPDATESUCCESS;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败");
                    Message message = new Message();
                    message.what = UPDATEFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private static class date{
        String lat;
        String lon;
    }

    //地图模式获取医生列表
    public static void docterUpdataMap(String user, final Handler handler) {
        date res= new date();
        res.lat= user.split(":")[0];
        res.lon= user.split(":")[1];
        Gson gson= new Gson();
        String s= gson.toJson(res);
        RequestBody responseBody = RequestBody.create(JSON, s);
        Request request = new Request.Builder().url(DOCTERUPDATEMAP).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = UPDATEFAILURE;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                String buf = response.body().string();
                docterList docter = gson.fromJson(buf, docterList.class);
                if (docter.state.equals("1")) {
                    Log.d("ai", "成功");
                    String date = gson.toJson(docter.result);
                    Bundle bundle = new Bundle();
                    bundle.putString("result", date);
                    Message message = new Message();
                    message.what = UPDATESUCCESS;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败");
                    Message message = new Message();
                    message.what = UPDATEFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //获取医生列表Gson解释类
    private class docterList{
        String state;
        List<DoctorCustom> result;
    }

    //用户上传头像
    public static void pushImage(String userPhone, String path, final Handler handler){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",userPhone)
                .addFormDataPart("pictureFile",userPhone+".png",RequestBody.create(MEDIA_TYPE_PNG, new File(path)))
                .build();
        Request request = new Request.Builder().url(PUSHIMAGE).post(requestBody).build();
        Call call= okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    Log.d("ai","成功");
                    message.what= PUSHIMAGESUCESS;
                    handler.sendMessage(message);
                }
                else {
                    message.what= PUSHIMAGEFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //所有状态位Gson解释类
    private class State{
        String state;
    }

    //用户向服务器同步头像
    public static void pullImage(String userPhone,final Handler handler){
        RequestBody requestBody = new FormBody.Builder().add("phone",userPhone).build();
        Request request = new Request.Builder().url(PULLIMAGE).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= PULLIMAGEFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] res= response.body().bytes();
                Bitmap drawable = BitmapFactory.decodeByteArray(res,0,res.length);
                Message message= new Message();
                if(drawable!=null){
                    message.what= PULLIMAGESUCESS;
                    Bundle bundle= new Bundle();
                    bundle.putByteArray("pix",res);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                else {
                    message.what= PULLIMAGEFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //修改用户昵称
    public static void changeNickname(String userPhone,String nickname,final Handler handler){
        change change= new change();
        change.phone=userPhone;
        change.username=nickname;
        final Gson gson= new Gson();
        String resChange= gson.toJson(change);
        RequestBody requestBody = RequestBody.create(JSON, resChange);
        Request request = new Request.Builder().url(CHANGENICKNAME).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= CHANGENICKNAMEFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= CHANGENICKNAMESUCESS;
                    handler.sendMessage(message);
                }
                else {
                    message.what= CHANGENICKNAMEFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //修改昵称Gson包装类
    private static class change{
        String phone;
        String username;
    }

    //退出登录
    public static void exitLogin(String userPhone,final Handler handler){
        Phone exit= new Phone();
        exit.phone=userPhone;
        final Gson gson= new Gson();
        String resExit= gson.toJson(exit);
        RequestBody requestBody = RequestBody.create(JSON, resExit);
        Request request = new Request.Builder().url(EXITLOGIN).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= EXITFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= EXITSUCESS;
                    handler.sendMessage(message);
                }
                else {
                    message.what= EXITFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //所有phone Gson包装类
    private static class Phone{
        String phone;
    }

    //密码能否修改
    public static void canChangePassword(String userPhone,String password,final Handler handler){
        ChangePassword change= new ChangePassword();
        change.phone= userPhone;
        change.password=password;
        final Gson gson= new Gson();
        String res= gson.toJson(change);
        RequestBody requestBody = RequestBody.create(JSON, res);
        Request request = new Request.Builder().url(CANCHANGEPASSWORD).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= NOCANCHANGEPW;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= CANCHANGEPW;
                    handler.sendMessage(message);
                }
                else {
                    message.what= NOCANCHANGEPW;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //密码修改
    public static void changePassword(String userPhone,String password,final Handler handler){
        ChangePassword change= new ChangePassword();
        change.phone= userPhone;
        change.password=password;
        final Gson gson= new Gson();
        String res= gson.toJson(change);
        RequestBody requestBody = RequestBody.create(JSON, res);
        Request request = new Request.Builder().url(CHANGEPASSWORD).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= CHANGEPWFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= CHANGEPWSUCESS;
                    handler.sendMessage(message);
                }
                else {
                    message.what= CHANGEPWFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private static class ChangePassword{
        String phone;
        String password;
    }

    //查询用户信息
    public static void getUserInfo(String userPhone,final Handler handler){
        Phone info= new Phone();
        info.phone= userPhone;
        final Gson gson= new Gson();
        String res= gson.toJson(info);
        RequestBody requestBody = RequestBody.create(JSON, res);
        Request request = new Request.Builder().url(GETUSERINFO).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= GETUSERINFOFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                userInfo state= gson.fromJson(res,userInfo.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= GETUSERINFOSUCESS;
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("info",state.data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                else {
                    message.what= GETUSERINFOFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class userInfo{
        String state;
        UerInfo data;
    }

    //上传用户信息
    public static void pushUserInfo(UerInfo info, List<String> figPath, final Handler handler){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",info.getUserphone())
                .addFormDataPart("username",info.getUsername())
                .addFormDataPart("usercardnum",info.getUsercardnum())
                .addFormDataPart("userage",info.getUserage())
                .addFormDataPart("usermale",info.getUsermale())
                .addFormDataPart("useradr",info.getUseradr())
                .addFormDataPart("pictureFile", MyApplication.getUserPhone()+"top.png",RequestBody.create(MEDIA_TYPE_PNG, new File(figPath.get(0))))
                .addFormDataPart("pictureFile", MyApplication.getUserPhone()+"down.png",RequestBody.create(MEDIA_TYPE_PNG, new File(figPath.get(1))))
                .build();
        Request request = new Request.Builder().url(PUSHUSERINFO).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= PUSHINFOFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= PUSHINFOSUCESS;
                    handler.sendMessage(message);
                }
                else {
                    message.what= PUSHINFOFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //同步家人信息
    public static void pullFamilyInfo( String userPhone, final Handler handler){
        Phone phone= new Phone();
        phone.phone=userPhone;
        Gson gson= new Gson();
        String res= gson.toJson(phone);
        RequestBody requestBody = RequestBody.create(JSON, res);
        Request request = new Request.Builder().url(PULLFAMILYINFO).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        Log.d("ai","22");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= PULLFAMILYINFOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                familyInfo info= gson.fromJson(res,familyInfo.class);
                Message message= new Message();
                if(info.state.equals("1")){
                    Log.d("ai","33");
                    String result= gson.toJson(info.data);
                    message.what= PULLFAMILYINFOSU;
                    Bundle bundle= new Bundle();
                    bundle.putString("result",result);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                else {
                    message.what= PULLFAMILYINFOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class familyInfo{
        String state;
        List<FamilyInfo> data;
    }

    //添加家人信息
    public static void addFamilyInfo( String info, final Handler handler){
        RequestBody requestBody = RequestBody.create(JSON,info);
        Request request = new Request.Builder().url(ADDFAMILYINFO).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= ADDFAMILYINFOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= ADDFAMILYINFOSU;
                    handler.sendMessage(message);
                }
                else {
                    message.what= ADDFAMILYINFOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //添加家人信息
    public static void deleteFamilyInfo(String id, final Handler handler){
        Gson gson =new Gson();
        Id familyId= new Id();
        familyId.id=id;
        String res= gson.toJson(familyId);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(DEFAMILYINFO).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= DEFAMILYINFOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                State state= gson.fromJson(res,State.class);
                Message message= new Message();
                if(state.state.equals("1")){
                    message.what= DEFAMILYINFOSU;
                    handler.sendMessage(message);
                }
                else {
                    message.what= DEFAMILYINFOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private static class Id{
        String id;
    }
}
