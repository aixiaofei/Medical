package com.example.ai.dtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.ai.dtest.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

///**
// * Created by ai on 2017/8/15.
// */

public class DateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int[] defaultPosition= {0,1,2,3,4,5,6,7,8,16};

    private static final String[] date={"周一","周二","周三","周四","周五","周六","周日"};

    private int currentDay;

    private List<String> week;

    private Context context;

    private List<Integer> mList;

    private int resourceId;

    public DateAdapter(int resourceId,Context context,List<Integer> mList) {
        this.context=context;
        this.resourceId=resourceId;
        this.mList=mList;
        week= new ArrayList<>();
        getTime();
    }

    private static class viewHolder extends RecyclerView.ViewHolder{
        View dateView;
        TextView desc;

        viewHolder(View itemView) {
            super(itemView);
            dateView= itemView;
            desc= itemView.findViewById(R.id.desc);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int tag= mList.get(position);
        viewHolder holder1= (viewHolder) holder;
        if(isDefault(position)){
            if(position==0){
                holder1.desc.setText("日期");
            }else if(position==8){
                holder1.desc.setText("上午");
            }else if(position==16) {
                holder1.desc.setText("下午");
            }else if(position==currentDay-1){
                holder1.desc.setText(week.get(position - 1) + " " + date[position - 1]);
                holder1.desc.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder1.desc.setTextColor(context.getResources().getColor(R.color.white));
            }else {
                holder1.desc.setText(week.get(position - 1) + " " + date[position - 1]);
            }
        }else {
            if (tag == 1) {
                holder1.desc.setText("可以接诊");
                holder1.desc.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder1.desc.setTextColor(context.getResources().getColor(R.color.white));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private boolean isDefault(int position){
        for(int i:defaultPosition){
            if(position==i){
                return true;
            }
        }
        return false;
    }

    public List<String> getTime(){
        Date time= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd"); // 设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        currentDay=day;
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        for(int i=0;i<7;i++) {
            week.add(sdf.format(cal.getTime()));
            cal.add(Calendar.DATE,1);
        }
        return week;
    }
}
