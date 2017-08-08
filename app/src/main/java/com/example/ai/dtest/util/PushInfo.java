package com.example.ai.dtest.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.example.ai.dtest.base.MyApplication;
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
        PushManager.listTags(context);
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
        PushManager.setTags(context,tags);
    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String s, String s1, String s2) {
        String notifyString = "通知点击 title=\"" + s + "\" description=\""
                + s1 + "\" customContent=" + s2;
        Log.d("ai",notifyString);

//        // 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
//        if (!TextUtils.isEmpty(customContentString)) {
//            JSONObject customJson = null;
//            try {
//                customJson = new JSONObject(customContentString);
//                String myvalue = null;
//                if (customJson.isNull("mykey")) {
//                    myvalue = customJson.getString("mykey");
//                }
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
//        updateContent(context, notifyString);
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
}
