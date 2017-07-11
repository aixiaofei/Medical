package yanzhengma;

/**
 * Created by ai on 2017/7/10.
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {
    private static final String SERVER_URL_SEND="https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_URL_VERIFY="https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
    private static final String SERVICE_URL_REGISTER="http://192.168.2.142:8080/internetmedical/user/adduser.do";
    private static final String APP_KEY="69faeb15aa2238ed28ebfebfc52b23c5";//网易云信分配的账号
    private static final String APP_SECRET="4f0a0a22be5d";//网易云信分配的密钥
    private static final String NONCE="123456";//随机数
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TEMPLATEID="3064776";
    private static final int SENDFAILURE=1;
    private static final int SENDSUCCESS=2;
    private static final int CHECKFAILURE=3;
    private static final int CHECKSUCESS= 4;
    private static final int REGISTERFAILURE=5;
    private static final int REGISTERSUCESS=6;



    public static void sendMsg(String phone,final Handler handler) throws IOException {
        OkHttpClient okHttpClient= new OkHttpClient();
        String curTime=String.valueOf((new Date().getTime()/1000L));
        String checkSum=CheckSumBuilder.getCheckSum(APP_SECRET,NONCE,curTime);
        RequestBody responseBody=  new FormBody.Builder().add("mobile",phone).add("templateid",TEMPLATEID).add("codeLen","6").build();
        Request request= new Request.Builder().url(SERVER_URL_SEND).
                                                addHeader("AppKey",APP_KEY).
                                                addHeader("Nonce",NONCE)
                                                .addHeader("CurTime",curTime)
                                                .addHeader("CheckSum",checkSum)
                                                .addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
                                                .post(responseBody).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= SENDFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf= response.body().string();
                Gson gson= new Gson();
                code i= gson.fromJson(buf,code.class);
                if(i.code.equals("200")){
                    Log.d("ai",i.code);
                    Message message= new Message();
                    message.what= SENDSUCCESS;
                    handler.sendMessage(message);
                }
                else {
                    Log.d("ai",i.code);
                    Message message= new Message();
                    message.what= SENDFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void checkMsg(String phone,String sum,final Handler handler) throws IOException {
        OkHttpClient okHttpClient= new OkHttpClient();
        String curTime=String.valueOf((new Date().getTime()/1000L));
        String checkSum=CheckSumBuilder.getCheckSum(APP_SECRET,NONCE,curTime);
        RequestBody responseBody=  new FormBody.Builder().add("mobile",phone).add("code",sum).build();
        Request request= new Request.Builder().url(SERVER_URL_VERIFY).
                addHeader("AppKey",APP_KEY).
                addHeader("Nonce",NONCE)
                .addHeader("CurTime",curTime)
                .addHeader("CheckSum",checkSum)
                .addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8")
                .post(responseBody).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message= new Message();
                message.what= CHECKFAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf= response.body().string();
                Gson gson= new Gson();
                code i= gson.fromJson(buf,code.class);
                if(i.code.equals("200")){
                    Log.d("ai",i.code);
                    Message message= new Message();
                    message.what= CHECKSUCESS;
                    handler.sendMessage(message);
                }
                else {
                    Log.d("ai",i.code);
                    Message message= new Message();
                    message.what= CHECKFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

    public static void register(String user,final Handler handler) throws IOException{
        OkHttpClient okHttpClient= new OkHttpClient();
        RequestBody responseBody=  RequestBody.create(JSON,user);
        Request request= new Request.Builder().url(SERVICE_URL_REGISTER).post(responseBody).build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String buf= response.body().string();
                if(buf.equals("1")){
                    Log.d("ai",buf);
                    Message message= new Message();
                    message.what= REGISTERSUCESS;
                    handler.sendMessage(message);
                }
                else {
                    Log.d("ai",buf);
                    Message message= new Message();
                    message.what= REGISTERFAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }
}
