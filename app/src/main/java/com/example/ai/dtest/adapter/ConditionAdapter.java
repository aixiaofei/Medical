package com.example.ai.dtest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ai.dtest.PatientInformation;
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

/**
 * Created by ai on 2017/8/11.
 */

public class ConditionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getsickpic/";

    private List<Usersick> mList;

    private Context context;

    private int resourceId;

    private conditionListener listener;

    public void setListener(conditionListener listener){
        this.listener=listener;
    }

    public ConditionAdapter(Context context,int resourceId,List<Usersick> mList){
        this.context=context;
        this.resourceId= resourceId;
        this.mList=mList;
    }

    private static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView name;
        TextView male;
        TextView age;
        TextView dec;
        List<ImageView> fig=new ArrayList<>();
        TextView have;
        ImageView change;
        ImageView delete;
        TextView time;

        viewHolder(View itemView) {
            super(itemView);
            view= itemView;
            name= itemView.findViewById(R.id.patient_name);
            male= itemView.findViewById(R.id.patient_male);
            age= itemView.findViewById(R.id.patient_age);
            dec= itemView.findViewById(R.id.dec);
            fig.add((ImageView) itemView.findViewById(R.id.info1));
            fig.add((ImageView) itemView.findViewById(R.id.info2));
            fig.add((ImageView) itemView.findViewById(R.id.info3));
            fig.add((ImageView) itemView.findViewById(R.id.info4));
            have= itemView.findViewById(R.id.have_fig);
            change=itemView.findViewById(R.id.change);
            delete=itemView.findViewById(R.id.delete);
            time= itemView.findViewById(R.id.release_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        final viewHolder holder=new viewHolder(view);

        holder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                listener.change(position);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                listener.delete(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Usersick info= mList.get(position);
        viewHolder holder1= (viewHolder) holder;
        holder1.name.setText(info.getFamilyname());
        holder1.male.setText(info.getFamilymale());
        holder1.age.setText(info.getFamilyage());
        holder1.dec.setText(info.getUsersickdesc());
        String[] imagePaths= info.getUsersickpic().split(",");
        int num= 0;
        for(int i=0;i<imagePaths.length;i++){
            if(!TextUtils.isEmpty(imagePaths[i])) {
                loadImage(holder1.fig.get(i), IMAGEURI + imagePaths[i]);
                num+=1;
            }
        }
        if(num==0){
            holder1.have.setText("暂无图片信息");
        }else {
            holder1.have.setVisibility(View.GONE);
        }
        for(int i=num;i<holder1.fig.size();i++){
            holder1.fig.get(i).setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time= Long.parseLong(info.getUsersicktime());
        Date date= new Date();
        date.setTime(time);
        String eTime= sdf.format(date);
        holder1.time.setText(eTime+"发布");
    }


    private void loadImage(ImageView imageView,String path){
        Uri uri= Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface conditionListener{
        void change(int position);
        void delete(int position);
    }
}

