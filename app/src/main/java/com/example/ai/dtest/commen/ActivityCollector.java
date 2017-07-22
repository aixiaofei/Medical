package com.example.ai.dtest.commen;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/7/12.
 */

public class ActivityCollector {
    public static List<Activity> activities= new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.add(activity);
    }

    public static void finishAll(){
        for(Activity i:activities){
            if(!i.isFinishing()){
                i.finish();
            }
        }
    }
}
