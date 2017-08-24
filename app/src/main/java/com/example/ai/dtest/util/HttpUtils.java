package com.example.ai.dtest.util;

/**
 * Created by ai on 2017/7/10.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.data.OrderInfo;
import com.example.ai.dtest.data.UerInfo;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.db.Province;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    public static final String SOURCEIP= "http://192.168.2.2:8080"; //远程主机IP
    private static final String SERVER_URL_SEND = "https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_URL_VERIFY = "https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
    private static final String SERVER_URL_LOGIN = SOURCEIP+"/internetmedical/user/login.do"; //用户登录接口
    private static final String SERVER_URL_CANREGISTER= SOURCEIP+"/internetmedical/user/phonetest"; // 判断是否可注册接口
    private static final String SERVER_URL_REGISTER = SOURCEIP+"/internetmedical/user/register"; //用户注册接口
    private static final String DOCTERUPDATEDEFAULT = SOURCEIP+"/internetmedical/user/doctors"; //主页面查询接口
    private static final String SINGLEDOCTOR = SOURCEIP+"/internetmedical/user/doctor"; //单个医生查询接口
    private static final String DOCTERUPDATEMAP = SOURCEIP+"/internetmedical/user/mapdoctors"; //地图模式查询接口
    private static final String PUSHIMAGE = SOURCEIP+"/internetmedical/user/pullpix"; // 上传图片接口
    private static final String PULLIMAGE= SOURCEIP+"/internetmedical/user/pushpix"; //下载图片接口
    private static final String CHANGENICKNAME= SOURCEIP+"/internetmedical/user/editusername"; //修改昵称接口
    private static final String EXITLOGIN= SOURCEIP+"/internetmedical/user/exit"; //退出登录接口
    private static final String CANCHANGEPASSWORD= SOURCEIP+"/internetmedical/user/checkpassword"; //能否修改密码接口
    private static final String CHANGEPASSWORD= SOURCEIP+"/internetmedical/user/editpassword"; //修改密码接口
    private static final String GETUSERINFO= SOURCEIP+"/internetmedical/user/getinfo"; //获取用户信息接口
    private static final String PUSHUSERINFO= SOURCEIP+"/internetmedical/user/editinfo"; //上传用户信息接口
    private static final String PULLFAMILYINFO= SOURCEIP+"/internetmedical/user/findfamily"; //同步家人信息接口
    private static final String ADDFAMILYINFO= SOURCEIP+"/internetmedical/user/addfamily"; //添加家人信息接口
    private static final String DEFAMILYINFO= SOURCEIP+"/internetmedical/user/deletefamily"; //删除家人信息接口
    private static final String GETLOCATION = SOURCEIP + "/internetmedical/user/findcities"; // 查询位置信息接口
    private static final String PUSHCHANNELID = SOURCEIP + "/internetmedical/user/pullhannelId"; // 推送客户端ID接口
    private static final String PULLCONDITION= SOURCEIP +"/internetmedical/user/getsick"; // 查询病情接口
    private static final String PULLONECONDITION= SOURCEIP +"/internetmedical/user/getonesick"; // 查询单个病情接口
    private static final String ADDCONDITION= SOURCEIP +"/internetmedical/user/addsick"; // 添加病情接口
    private static final String DECONDITION= SOURCEIP +"/internetmedical/user/deletesick"; // 删除病情接口
    private static final String PULLDATE= SOURCEIP+ "/internetmedical/user/getschedule"; // 获取日程接口
    private static final String PULLREDOCTOR= SOURCEIP+ "/internetmedical/user/getredoctor"; // 获取推荐医生接口
    private static final String PUSHCONDITION= SOURCEIP + "/internetmedical/user/publishsick"; // 发布病情接口
    private static final String UNPUSHCONDITION= SOURCEIP + "/internetmedical/user/cancelsick"; // 取消发布病情接口
    private static final String TOCANDIDATE = SOURCEIP + "/internetmedical/user/optdoctor"; // 选定医生为候选医生接口
    private static final String CREATEORDER = SOURCEIP + "/internetmedical/user/createorder"; // 签订订单接口
    private static final String PULLORDER = SOURCEIP + "/internetmedical/user/order"; // 同步订单接口

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
    public static final int GETLOCATIONSU=36; //获取位置信息成功
//    public static final int PUSHCHANNELIDFA=37;  // 推送客户端ID失败
//    public static final int PUSHCHANNELIDSU= 38; // 推送客户端ID成功
    public static final int PULLCONDITIONFA =39; // 查询病情失败
    public static final int PULLCONDITIONSU =40; // 查询病情成功
    public static final int PULLONECONFA=41; // 查询单个病情失败
    public static final int PULLONECONSU=42; // 查询单个病情成功
    public static final int ADDCONDITIONFA= 43; // 添加病情失败
    public static final int ADDCONDITIONSU= 44;// 添加病情成功
    public static final int DECONDITIONFA=45; // 删除病情失败
    public static final int DECONDITIONSU=46; // 删除病情成功
    public static final int SINGLEDOFA= 47; // 查询单个医生失败
    public static final int SINGLEDOSU=48; // 查询单个医生成功
    public static final int PULLDATEFA=49; // 获取日程失败
    public static final int PULLDATESU=50; // 获取日程成功
    public static final int PULLREDOFA=51; // 获取推荐医生失败
    public static final int PULLREDOSU=52; // 获取推荐医生成功
    public static final int PUSHCOFA= 53; // 发布病情失败
    public static final int PUSHCOSU= 54; // 发布病情成功
    public static final int UNPUSHCOFA=55; // 取消发布病情失败
    public static final int UNPUSHCOSU=56; // 取消发布病情成功
    public static final int TOCANDIDATEFA=57; // 选定医生为候选医生失败
    public static final int TOCANDIDATESU=58; // 选定医生为候选医生成功
    public static final int CREATEORDERFA=59; // 生成订单失败
    public static final int CREATEORDERSU=60; // 生成订单成功
    public static final int PULLORDERFA=61; // 同步订单失败
    public static final int PULLORDERSU=62; // 同步订单成功


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
                    String date = gson.toJson(docter.data);
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

    //单个医生请求
    public static void singleDocterUpdata(int id,final Handler handler) {
        Id id1= new Id();
        id1.id= Integer.toString(id);
        Gson gson= new Gson();
        String res= gson.toJson(id1);
        RequestBody responseBody = RequestBody.create(JSON, res);
        Request request = new Request.Builder().url(SINGLEDOCTOR).post(responseBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Message message = new Message();
                message.what = SINGLEDOFA;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf = response.body().string();
                Gson gson = new Gson();
                singleDoctor docter = gson.fromJson(buf, singleDoctor.class);
                if (docter.state.equals("1")) {
                    Log.d("ai", "成功");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("result",docter.data);
                    Message message = new Message();
                    message.what = SINGLEDOSU;
                    message.setData(bundle);
                    handler.sendMessage(message);
                } else {
                    Log.d("ai", "失败");
                    Message message = new Message();
                    message.what = SINGLEDOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class singleDoctor{
        String state;
        DoctorCustom data;
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
                    String date = gson.toJson(docter.data);
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
        List<DoctorCustom> data;
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
    public static void pushUserInfo(UerInfo info, String[] figPath, final Handler handler){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",info.getUserphone())
                .addFormDataPart("username",info.getUsername())
                .addFormDataPart("usercardnum",info.getUsercardnum())
                .addFormDataPart("userage",info.getUserage())
                .addFormDataPart("usermale",info.getUsermale())
                .addFormDataPart("useradr",info.getUseradr())
                .addFormDataPart("pictureFile", MyApplication.getUserPhone()+"top.png",RequestBody.create(MEDIA_TYPE_PNG, new File(figPath[0])))
                .addFormDataPart("pictureFile", MyApplication.getUserPhone()+"down.png",RequestBody.create(MEDIA_TYPE_PNG, new File(figPath[1])))
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

    //查询位置信息
    public static void getLocation(String code,final Handler handler){
        Gson gson =new Gson();
        ParentCode parentcode= new ParentCode();
        parentcode.parentcode=code;
        String res= gson.toJson(parentcode);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(GETLOCATION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                location location= gson.fromJson(res,location.class);
                String data= gson.toJson(location.data);
                if(!TextUtils.isEmpty(res)) {
                    Message message = new Message();
                    message.what= GETLOCATIONSU;
                    Bundle bundle= new Bundle();
                    bundle.putString("result",data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }
        });
    }

    private static class ParentCode{
        String parentcode;
    }

    private class location{
        String state;
        List<Province> data;
    }

//    //推送客户端ID信息
//    static void pushChannelId(String info, final Handler handler){
//        RequestBody requestBody = RequestBody.create(JSON,info);
//        Request request = new Request.Builder().url(PUSHCHANNELID).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Message message = new Message();
//                message.what= PUSHCHANNELIDFA;
//                handler.sendMessage(message);
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String res= response.body().string();
//                if("1".equals(res)) {
//                    Message message = new Message();
//                    message.what= PUSHCHANNELIDSU;
//                    handler.sendMessage(message);
//                }else {
//                    Message message = new Message();
//                    message.what= PUSHCHANNELIDFA;
//                    handler.sendMessage(message);
//                }
//            }
//        });
//    }


    //查询病情信息
    public static void pullCondition(String phone,final Handler handler){
        Gson gson =new Gson();
        Phone phone1= new Phone();
        phone1.phone=phone;
        String res= gson.toJson(phone1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PULLCONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PULLCONDITIONFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                pullCondition condition= gson.fromJson(res,pullCondition.class);
                String data= gson.toJson(condition.data);
                if(condition.state.equals("1")) {
                    Message message = new Message();
                    message.what= PULLCONDITIONSU;
                    Bundle bundle= new Bundle();
                    bundle.putString("result",data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PULLCONDITIONFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class pullCondition{
        String state;
        List<Usersick> data;
    }

    //查询单个病情信息
    public static void pullOneCondition(String id,final Handler handler){
        Gson gson =new Gson();
        Id id1= new Id();
        id1.id=id;
        String res= gson.toJson(id1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PULLONECONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PULLONECONFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                singleUsersick usersick= gson.fromJson(res,singleUsersick.class);
                if(usersick.state.equals("1")) {
                    Message message = new Message();
                    message.what= PULLONECONSU;
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("result",usersick.data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PULLONECONFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class singleUsersick{
        String state;
        Usersick data;
    }

    //添加病情信息
    public static void addCondition(Usersick info,String[] paths,final Handler handler){
        String[] Path={"first.png","second.png","third.png","forth.png"};
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("phone",info.getPhone())
                .addFormDataPart("familyid",info.getFamliyid())
                .addFormDataPart("usersickdesc",info.getUsersickdesc())
                .addFormDataPart("usersicktime",info.getUsersicktime());
        if(!TextUtils.isEmpty(info.getUsersickid())){
            builder.addFormDataPart("usersickid",info.getUsersickid());
        }
        for(int i=0;i<paths.length;i++){
            if(!TextUtils.isEmpty(paths[i])){
                Log.d("ai",paths[i]);
                builder.addFormDataPart("pictureFile",MyApplication.getUserPhone()+Path[i],RequestBody.create(MEDIA_TYPE_PNG, new File(paths[i])));
            }
        }
        RequestBody requestBody= builder.build();
        Request request = new Request.Builder().url(ADDCONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("ai","failure");
                Message message = new Message();
                message.what= ADDCONDITIONFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res= response.body().string();
                Gson gson= new Gson();
                State state= gson.fromJson(res,State.class);
                if("1".equals(state.state)) {
                    Log.d("ai","sucess");
                    Message message = new Message();
                    message.what= ADDCONDITIONSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= ADDCONDITIONFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //删除病情信息
    public static void deleteCondition(String id,final Handler handler){
        Gson gson =new Gson();
        Id id1= new Id();
        id1.id=id;
        String res= gson.toJson(id1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(DECONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= DECONDITIONFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                if(state.state.equals("1")) {
                    Message message = new Message();
                    message.what= DECONDITIONSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= DECONDITIONFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //获取日程信息
    public static void pullDate(String id,final Handler handler){
        Gson gson =new Gson();
        Id id1= new Id();
        id1.id=id;
        String res= gson.toJson(id1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PULLDATE).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PULLDATEFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                pullDate date= gson.fromJson(res,pullDate.class);
                if(date.state.equals("1")) {
                    Message message = new Message();
                    message.what= PULLDATESU;
                    Bundle bundle= new Bundle();
                    bundle.putIntegerArrayList("result",(ArrayList<Integer>) date.data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PULLDATEFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class pullDate{
        String state;
        List<Integer> data;
    }

    //获取推荐医生信息
    public static void pullReDoctor(String res,final Handler handler){
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PULLREDOCTOR).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PULLREDOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                docterList docter= gson.fromJson(res,docterList.class);
                if(docter.state.equals("1")){
                    Message message = new Message();
                    message.what= PULLREDOSU;
                    Bundle bundle= new Bundle();
                    String data= gson.toJson(docter.data);
                    bundle.putString("result",data);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PULLREDOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //发布病情
    public static void pushCondition(String id,final Handler handler){
        Gson gson= new Gson();
        Id id1= new Id();
        id1.id=id;
        String res= gson.toJson(id1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PUSHCONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PUSHCOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                if(state.state.equals("1")){
                    Message message = new Message();
                    message.what= PUSHCOSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PUSHCOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //取消发布病情
    public static void unPushCondition(String id,final Handler handler){
        Gson gson= new Gson();
        Id id1= new Id();
        id1.id=id;
        String res= gson.toJson(id1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(UNPUSHCONDITION).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= UNPUSHCOFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                if(state.state.equals("1")){
                    Message message = new Message();
                    message.what= UNPUSHCOSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= UNPUSHCOFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //选定医生为候选医生
    public static void toCandidate(String res,final Handler handler){
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(TOCANDIDATE).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= TOCANDIDATEFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                if(state.state.equals("1")){
                    Message message = new Message();
                    message.what= TOCANDIDATESU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= TOCANDIDATEFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //生成订单
    public static void createOrder(String res,final Handler handler){
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(CREATEORDER).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= CREATEORDERFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                State state= gson.fromJson(res,State.class);
                if(state.state.equals("1")){
                    Message message = new Message();
                    message.what= CREATEORDERSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= CREATEORDERFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    //同步订单
    public static void pullOrder(String phone,final Handler handler){
        Phone phone1= new Phone();
        phone1.phone=phone;
        Gson gson= new Gson();
        String res= gson.toJson(phone1);
        RequestBody requestBody = RequestBody.create(JSON,res);
        Request request = new Request.Builder().url(PULLORDER).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what= PULLORDERFA;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson= new Gson();
                String res= response.body().string();
                orderInfo info= gson.fromJson(res,orderInfo.class);
                if(info.state.equals("1")){
                    String data= gson.toJson(info.data);
                    Message message = new Message();
                    Bundle bundle= new Bundle();
                    bundle.putString("result",data);
                    message.setData(bundle);
                    message.what= PULLORDERSU;
                    handler.sendMessage(message);
                }else {
                    Message message = new Message();
                    message.what= PULLORDERFA;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private class orderInfo{
        String state;
        List<OrderInfo> data;
    }

}
