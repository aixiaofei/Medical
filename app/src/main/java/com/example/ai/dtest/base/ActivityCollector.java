package com.example.ai.dtest.base;

import android.app.Activity;

import com.example.ai.dtest.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/7/12.
 */

public class ActivityCollector {
    private static List<Activity> activities= new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.add(activity);
    }

    public static Activity getMain(){
        Activity myActivity=null;
        for(Activity activity:activities){
            if(activity instanceof MainActivity){
                myActivity=activity;
            }
        }
        return myActivity;
    }

    public static void finishAll(){
        for(Activity i:activities){
            if(!i.isFinishing()){
                i.finish();
            }
        }
    }
}
