package com.example.ai.dtest.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;

import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.DatePopwindowAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ai on 2017/8/30.
 */

public class DatePopwindow extends basePopwindow {

    private Context context;

    private List<String> mList;

    private List<Integer> nums;

    private dateListener listener;

    private static final String[] week={"周一","周二","周三","周四","周五","周六","周日"};

    public void setListener(dateListener listener){
        this.listener=listener;
    }

    public DatePopwindow(Context context) {
        super(context);
        this.context=context;
        initData();
    }

    private void initData(){
        mList=new ArrayList<>();
        nums=new ArrayList<>();
        Date time= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
//        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
//        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
//        if (1 == dayWeek) {
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//        }
//        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK)-1;// 获得当前日期是一个星期的第几天
        if(day==0) {
            day = 7;
        }
//        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        for(int i=0;i<7;i++) {
            if(day==8){
                day=1;
            }
            mList.add(sdf.format(cal.getTime())+" "+week[day-1]+" 上午");
            mList.add(sdf.format(cal.getTime())+" "+week[day-1]+" 下午");
            nums.add(2*day-1);
            nums.add(2*day);
            cal.add(Calendar.DATE,1);
            day++;
        }
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        RecyclerView recyclerView= contentView.findViewById(R.id.recycle);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DatePopwindowAdapter adapter= new DatePopwindowAdapter(context,mList,R.layout.department_item);

        adapter.setListener(new DatePopwindowAdapter.datePopwindowListener() {
            @Override
            public void select(int num) {
                listener.getResult(mList.get(num).split(" ")[0]+" "+mList.get(num).split(" ")[2],nums.get(num));
            }
        });

        recyclerView.setAdapter(adapter);

    }

    public interface dateListener{
        void getResult(String cDate,int num);
    }
}
