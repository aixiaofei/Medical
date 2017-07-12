package com.example.ai.dtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import Adapter.DocterAdapter;
import Service.DownService;
import commen.Docter;

public class MainActivity extends BaseActivity {


    private static final int DOWN_LOAD_COMPLETED=0;

    private DownService.DownBinder downBinder;

    private List<Docter> mList= new ArrayList<>();

    private DocterAdapter adapter= new DocterAdapter(mList);;

//    public Listener listener=new Listener() {
//        @Override
//        public void Json(String result) {
//            Gson gson= new Gson();
//            List<Docter> buf=gson.fromJson(result,new TypeToken<List<Docter>>(){}.getType());
//            deepclone(buf);
//            adapter.notifyDataSetChanged();
//        }
//    };

//    private Handler handler= new Handler() {
//        @Override
//        public void handleMessage(Message message){
//            switch (message.what){
//                case DOWN_LOAD_COMPLETED:
//                    Bundle bundle= message.getData();
//                    Gson gson= new Gson();
//                    List<Docter> buf=gson.fromJson(bundle.get("data").toString(),new TypeToken<List<Docter>>(){}.getType());
//                    deepclone(buf);
//                    adapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    private ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downBinder= (DownService.DownBinder) iBinder;
            Log.d("ai","bind");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client= new OkHttpClient();
//                Request request= new Request.Builder().url("http://10.0.2.2/data.json").build();
//                try {
//                    Response response= client.newCall(request).execute();
//                    Message message= new Message();
//                    message.what= DOWN_LOAD_COMPLETED;
//                    Bundle bundle= new Bundle();
//                    bundle.putString("data",response.body().string());
//                    message.setData(bundle);
//                    handler.sendMessage(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();;
//        Log.d("ai",Integer.toString(mList.size()));
//        HttpUtils.httpGetResult("http://10.0.2.2/data.json",new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result= response.body().string();
//                Gson gson= new Gson();
//                List<Docter> buf=gson.fromJson(result,new TypeToken<List<Docter>>(){}.getType());
//                deepclone(buf);
//                adapter.notifyDataSetChanged();
//            }
//        });
        Intent intent= new Intent(this, DownService.class);
        startService(intent);
        bindService(intent,connection, Context.BIND_AUTO_CREATE);
        if(downBinder==null){
            Log.d("ai","empty");
        }
        downBinder.StartDown("http://10.0.2.2/data. json");
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void deepclone(List<Docter> origin){
        for(Docter i:origin){
            try {
                mList.add((Docter) i.deepClone());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
