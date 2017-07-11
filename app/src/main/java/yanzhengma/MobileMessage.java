package yanzhengma;

/**
 * Created by ai on 2017/7/10.
 */

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MobileMessage {
    private static final String SERVER_URL_SEND="https://api.netease.im/sms/sendcode.action";//发送验证码的请求路径URL
    private static final String SERVER_URL_VERIFY="https://api.netease.im/sms/verifycode.action";//校验验证码的请求路径URL
    private static final String APP_KEY="69faeb15aa2238ed28ebfebfc52b23c5";//网易云信分配的账号
    private static final String APP_SECRET="4f0a0a22be5d";//网易云信分配的密钥
    private static final String NONCE="123456";//随机数
//    private static final String TEMPLATEID="3057527";
    private static final int FAILURE=0;
    private static final int SUCCESS=1;



    public static void sendMsg(String phone,final Handler handler) throws IOException {
        OkHttpClient okHttpClient= new OkHttpClient();
        String curTime=String.valueOf((new Date().getTime()/1000L));
        String checkSum=CheckSumBuilder.getCheckSum(APP_SECRET,NONCE,curTime);
        RequestBody responseBody=  new FormBody.Builder().add("mobile",phone).build();
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
                message.what= FAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Message message= new Message();
                    message.what= SUCCESS;
                    handler.sendMessage(message);
                }
                else {
                    Message message= new Message();
                    message.what= FAILURE;
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
                message.what= FAILURE;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    Message message= new Message();
                    message.what= SUCCESS;
                    handler.sendMessage(message);
                }
                else {
                    Message message= new Message();
                    message.what= FAILURE;
                    handler.sendMessage(message);
                }
            }
        });
    }

}
