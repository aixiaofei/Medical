package com.example.ai.dtest.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.example.ai.dtest.MainActivity;
import com.example.ai.dtest.base.Access;
import com.example.ai.dtest.base.ActivityCollector;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.frag.OrderShow;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/8/7.
 */

public class PushInfo extends PushMessageReceiver{

//    private Handler handler= new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what){
//                case HttpUtils.PUSHCHANNELIDFA:
//                    break;
//                case HttpUtils.PUSHCHANNELIDSU:
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    private class channelId{
//        String phone;
//        String id;
//    }

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
//        channelId channel= new channelId();
//        channel.phone= MyApplication.getUserPhone();
//        channel.id= s3;
//        Gson gson= new Gson();
//        String res= gson.toJson(channel);
//        HttpUtils.pushChannelId(res,handler);
        if(i==0) {
            PushManager.listTags(context);
        }else {
            Toast.makeText(context,"推送服务启动失败",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {
        PushManager.delTags(context,list);
        List<String> tags= new ArrayList<>();
        tags.add(MyApplication.getUserPhone());
        tags.add("sick");
        PushManager.setTags(context,tags);
    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        Gson gson=new Gson();
        State state= gson.fromJson(s2,State.class);
        if("2".equals(state.state)){
            returnOrder(context);
        }else if("3".equals(state.state)){
            returnOrder(context);
        }
    }

    private void returnOrder(Context context){
        MainActivity activity= (MainActivity) ActivityCollector.getMain();
        if(activity!=null){
            Intent intent= new Intent(context,MainActivity.class);
            context.startActivity(intent);
            activity.goOrder();
            FragmentManager manager= activity.getSupportFragmentManager();
            OrderShow target= (OrderShow) manager.findFragmentByTag(OrderShow.class.getName());
            if(target!=null){
                target.refleshOrder();
            }
        }else {
            Intent intent= new Intent(context,Access.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }

    private class State{
        String state;
    }
}
