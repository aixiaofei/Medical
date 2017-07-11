package Service;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ai on 2017/7/9.
 */

public class Thread extends AsyncTask<String,Void,String>{

    private Listener listener;

    public Thread(Listener listener){
        this.listener=listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        String buf=null;
        OkHttpClient client= new OkHttpClient();
        Request request= new Request.Builder().url(strings[0]).build();
        try {
            Response  response= client.newCall(request).execute();
            buf=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.Json(s);
    }
}
