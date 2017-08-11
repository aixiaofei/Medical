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
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.Usersick;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

/**
 * Created by ai on 2017/8/11.
 */

public class ConditionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Usersick> mList;

    private Context context;

    private int resourceId;

    public ConditionAdapter(Context context,int resourceId,List<Usersick> mList){
        this.context=context;
        this.resourceId= resourceId;
        this.mList=mList;
    }

    static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView name;
        TextView male;
        TextView age;
        TextView dec;
        ImageView fig1;
        ImageView fig2;
        ImageView fig3;
        ImageView fig4;

        viewHolder(View itemView) {
            super(itemView);
            view= itemView;
            name= itemView.findViewById(R.id.patient_name);
            male= itemView.findViewById(R.id.patient_male);
            age= itemView.findViewById(R.id.patient_age);
            dec= itemView.findViewById(R.id.dec);
            fig1= itemView.findViewById(R.id.info1);
            fig2= itemView.findViewById(R.id.info2);
            fig3= itemView.findViewById(R.id.info3);
            fig4= itemView.findViewById(R.id.info4);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }


//    private void loadImage(CircleImageView imageView, int doctorId){
//        Uri uri= Uri.parse(doctorId);
//        Glide.with(getContext())
//                .load(uri)
//                .error(R.drawable.defaultuserimage)
//                .into(imageView);
//    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

