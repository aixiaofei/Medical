package commen;

/**
 * Created by ai on 2017/7/10.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import yanzhengma.CheckSumBuilder;

public class HttpUtils {
    private static final String SERVER_URL_SEND = "https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_URL_VERIFY = "https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
    private static final String SERVER_URL_LOGIN = "http://192.168.2.2:8080/internetmedical/user/login.do"; //用户登录接口
    private static final String SERVER_URL_CANREGISTER= "http://192.168.2.2:8080/internetmedical/user/phonetest";
    private static final String SERVER_URL_REGISTER = "http://192.168.2.2:8080/internetmedical/user/register"; //用户注册接口
    private static final String DOCTERUPDATEDEFAULT = "http://192.168.2.2:8080/internetmedical/user/doctors"; //主页面查询接口
    private static final String DOCTERUPDATEMAP = "http://192.168.2.2:8080/internetmedical/user/mapdoctors"; //地图模式查询接口
    private static final String APP_KEY = "69faeb15aa2238ed28ebfebfc52b23c5";//网易云信分配的账号
    private static final String APP_SECRET = "4f0a0a22be5d";//网易云信分配的密钥
    private static final String NONCE = "123456";//随机数
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TEMPLATEID = "3064776";
    public static final int SENDFAILURE = 1;
    public static final int SENDSUCCESS = 2;
    public static final int CHECKFAILURE = 3;
    public static final int CHECKSUCESS = 4;
    public static final int REGISTERFAILURE = 5;
    public static final int REGISTERSUCESS = 6;
    public static final int LOGINFAILURE = 7;
    public static final int LOGINSUCESS = 8;
    public static final int UPDATEFAILURE = 9;
    public static final int UPDATESUCCESS = 10;
    public static final int CANREGISTER=11;
    public static final int NOCANREGISTER=12;


    public static void sendMsg(String phone, final Handler handler) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
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
        OkHttpClient okHttpClient = new OkHttpClient();
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
        OkHttpClient okHttpClient = new OkHttpClient();
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
                ligin_state state = gson.fromJson(buf, ligin_state.class);
                if (state.state.equals("1")) {
                    Log.d("ai", "成功1");
                    Message message = new Message();
                    message.what = LOGINSUCESS;
                    Bundle bundle = new Bundle();
                    bundle.putString("token", state.token);
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

    private class ligin_state {
        String state;
        String token;
    }

    public static void canRegister(String user, final Handler handler) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
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
                register_state res= gson.fromJson(buf,register_state.class);
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
        OkHttpClient okHttpClient = new OkHttpClient();
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
                register_state res= gson.fromJson(buf,register_state.class);
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

    private class register_state{
        String state;
    }

    public static void docterUpdataDefault(final Handler handler) {
        OkHttpClient okHttpClient = new OkHttpClient();
//        String s=null;
//        RequestBody responseBodyponseBody=  new FormBody.Builder().add("request",s).build();
        Request request = new Request.Builder().url(DOCTERUPDATEDEFAULT).build();
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

    public static void docterUpdataMap(String user, final Handler handler) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
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

    private class docterList{
        String state;
        List<DoctorCustom> result;
    }
}
