package commen;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by ai on 2017/7/9.
 */

public class HttpUtils {
    public static void httpGetResult(String url,okhttp3.Callback callback){
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
}
