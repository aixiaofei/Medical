package com.example.ai.dtest.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ai.dtest.PatientInformation;
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;

import java.util.ArrayList;
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
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        return new viewHolder(view);
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
        if(imagePaths.length>0){
            for(int i=0;i<imagePaths.length;i++){
                loadImage(holder1.fig.get(i),IMAGEURI+imagePaths[i]);
            }
        }
    }


    private void loadImage(ImageView imageView,String path){
        Uri uri= Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

