package com.example.ai.dtest.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import com.example.ai.dtest.OrderDetail;
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

    private orderShowListener listener;

    public void setListener(orderShowListener listener){
        this.listener=listener;
    }

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
        final viewHolder holder= new viewHolder(view);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                OrderDetail.actionStart(context,mList.get(position).getDocloginid(),mList.get(position).getUserorderid(),mList.get(position).getUserorderstate());
            }
        });

        holder.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                if(mList.get(position).getUserorderstate()==3){//确认信息
                    listener.sureInfo(mList.get(position).getUserorderid());
                }
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                if(mList.get(position).getUserorderstate()==6){//付款
                }
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
                hideRela(holder1);
                break;
            case 2:
                holder1.state.setText("等待医生完善信息");
                hideRela(holder1);
                break;
            case 3:
                holder1.state.setText("医生已完善信息，请确认");
                showRela(holder1);
                break;
            case 4:
                holder1.state.setText("已被医生取消");
                hideRela(holder1);
                break;
            case 5:
                holder1.state.setText("已取消");
                hideRela(holder1);
                break;
            case 6:
                holder1.state.setText("代付款");
                showRelaPay(holder1);
                break;
            case 7:
                holder1.state.setText("订单已完成");
                hideRela(holder1);
                break;
            default:
                break;
        }

        loadImage(holder1.docFig,IMAGEURI+info.getDocloginid());

        holder1.docName.setText(info.getDocname());

        holder1.time.setText("");

        holder1.hosipital.setText(info.getHospname());

        if(TextUtils.isEmpty(info.getUserorderetime())){
            holder1.cTime.setVisibility(View.GONE);
            if(TextUtils.isEmpty(info.getUserorderrtime())){
                holder1.sTime.setVisibility(View.GONE);
                holder1.rTime.setText(getTime(info.getUserorderptime())+" 提交");
            }else {
                holder1.rTime.setVisibility(View.GONE);
                holder1.sTime.setVisibility(View.VISIBLE);
                holder1.sTime.setText(getTime(info.getUserorderrtime())+" 确认");
            }
        }else {
            holder1.sTime.setVisibility(View.GONE);
            holder1.rTime.setVisibility(View.GONE);
            holder1.cTime.setVisibility(View.VISIBLE);
            holder1.cTime.setText(getTime(info.getUserorderetime())+" 结束");
        }
    }

    private void showRela(viewHolder holder){
        holder.process.setVisibility(View.VISIBLE);
        holder.sure.setText("确定");
        holder.sure.setVisibility(View.VISIBLE);
        holder.cancel.setVisibility(View.VISIBLE);
    }

    private void showRelaPay(viewHolder holder){
        holder.process.setVisibility(View.VISIBLE);
        holder.cancel.setText("确认付款");
        holder.sure.setVisibility(View.GONE);
        holder.cancel.setVisibility(View.VISIBLE);
    }

    private void hideRela(viewHolder holder){
        holder.process.setVisibility(View.GONE);
        holder.sure.setVisibility(View.GONE);
        holder.cancel.setVisibility(View.GONE);
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

    public interface orderShowListener{
        void sureInfo(int id);
        void surePay();
        void cancel(int id);
    }
}
