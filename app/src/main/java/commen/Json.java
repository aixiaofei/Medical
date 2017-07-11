package commen;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by ai on 2017/7/9.
 */

public class Json {

    public static final int DOCTER_MORE_ONE=0;
    public static final int DOCTER_ONE=1;

    public static void getResult(String date,int index,final CallBackLister lister){
        switch (index){
            case DOCTER_MORE_ONE:
                 lister.process_more_one(date);
                break;
            case DOCTER_ONE:
                lister.precess_one(date);
                break;
            default:
                break;
        }
    }
}
