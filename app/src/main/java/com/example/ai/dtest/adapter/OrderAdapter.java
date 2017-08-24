package com.example.ai.dtest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.OrderInfo;
import com.example.ai.dtest.util.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

///**
// * Created by ai on 2017/8/23.
// */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getdoctorpix/";

    private Context context;

    private List<OrderInfo> mList;

    private int resourceId;

    public OrderAdapter(Context context,List<OrderInfo> mList,int resourceId) {
        this.context=context;
        this.mList=mList;
        this.resourceId=resourceId;
    }

    private static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView state;
        CircleImageView docFig;
        TextView docName;
        TextView time;
        TextView hosipital;
        TextView rTime;
        TextView sTime;
        TextView cTime;
        RelativeLayout process;
        TextView sure;
        TextView cancel;

        viewHolder(View itemView) {
            super(itemView);
            view=itemView;
            state= itemView.findViewById(R.id.state);
            docFig= itemView.findViewById(R.id.doctor_photo);
            docName= itemView.findViewById(R.id.docter_name);
            time= itemView.findViewById(R.id.time);
            hosipital= itemView.findViewById(R.id.hosipital);
            rTime= itemView.findViewById(R.id.release_time);
            sTime= itemView.findViewById(R.id.sure_time);
            cTime= itemView.findViewById(R.id.cancel_time);
            process= itemView.findViewById(R.id.process);
            sure= itemView.findViewById(R.id.sure);
            cancel= itemView.findViewById(R.id.cancel);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resourceId,parent,false);
        viewHolder holder= new viewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderInfo info= mList.get(position);
        viewHolder holder1= (viewHolder) holder;

        switch (info.getUserorderstate()){
            case 1:
                holder1.state.setText("等待医生确认");
                break;
            case 2:
                holder1.state.setText("等待医生完善信息");
                break;
            case 3:
                holder1.state.setText("医生已完善信息，请确认");
                holder1.process.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder1.state.setText("已被医生取消");
                break;
            case 5:
                holder1.state.setText("已取消");
                break;
            case 6:
                holder1.state.setText("代付款");
                holder1.process.setVisibility(View.VISIBLE);
                holder1.cancel.setVisibility(View.GONE);
                holder1.sure.setText("确认付款");
                break;
            case 7:
                holder1.state.setText("订单已完成");
                break;
            default:
                break;
        }

        loadImage(holder1.docFig,IMAGEURI+info.getDocloginid());

        holder1.docName.setText(info.getDocname());

        holder1.time.setText("");

        holder1.hosipital.setText(info.getHospname());

        holder1.rTime.setText(getTime(info.getUserorderptime())+" 提交");

        if(TextUtils.isEmpty(info.getUserorderrtime())){
            holder1.sTime.setVisibility(View.GONE);
        }else {
            holder1.sTime.setText(getTime(info.getUserorderrtime())+" 确认");
        }

        if(TextUtils.isEmpty(info.getUserorderetime())){
            holder1.sTime.setVisibility(View.GONE);
        }else {
            holder1.sTime.setText(getTime(info.getUserorderetime())+" 结束");
        }
    }

    private void loadImage(ImageView imageView, String path){
        Uri uri= Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    private String getTime(String oTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time= Long.parseLong(oTime);
        Date date= new Date();
        date.setTime(time);
        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
