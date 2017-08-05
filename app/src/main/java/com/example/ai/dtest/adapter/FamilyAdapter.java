package com.example.ai.dtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ai.dtest.R;
import com.example.ai.dtest.data.FamilyInfo;

import java.util.List;

/**
 * Created by ai on 2017/8/3.
 */

public class FamilyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FamilyInfo> mList;

    private Context context;

    private int resourceId;

    private familyListener listener;

    public void setListener(familyListener listener){
        this.listener=listener;
    }

    public FamilyAdapter(Context context, int resourceId, List<FamilyInfo> list){
        this.context=context;
        this.resourceId=resourceId;
        mList=list;
    }

    static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView name;
        TextView male;
        TextView age;
        ImageView change;
        ImageView delete;

        viewHolder(View itemView) {
            super(itemView);
            view= itemView;
            name= itemView.findViewById(R.id.family_name);
            male= itemView.findViewById(R.id.family_male);
            age = itemView.findViewById(R.id.family_age);
            change= itemView.findViewById(R.id.change_info);
            delete= itemView.findViewById(R.id.delete_info);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        final viewHolder holder= new viewHolder(view);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {
        FamilyInfo info= mList.get(position);
        viewHolder holder1= (viewHolder) holder;
        holder1.name.setText(info.getFamilyname());
        holder1.male.setText(info.getFamilymale());
        holder1.age.setText(info.getFamilyage());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface familyListener{
        void change(int position);
        void delete(int position);
    }
}
