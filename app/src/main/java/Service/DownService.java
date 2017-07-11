package Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import commen.Docter;

/**
 * Created by ai on 2017/7/9.
 */

public class DownService extends Service{

    private Thread thread;

    private String downurl;

    private Listener listener= new Listener() {
        @Override
        public void Json(String result) {
            Toast.makeText(DownService.this,"11",Toast.LENGTH_SHORT).show();
        }
    };

    private IBinder downBinder= new DownBinder();

    public class DownBinder extends Binder{
        public void StartDown(String url){
            if(thread==null){
                downurl=url;
                thread= new Thread(listener);
                thread.execute(downurl);
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return downBinder;
    }
}
