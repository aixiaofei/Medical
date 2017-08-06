package com.example.ai.dtest.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ai.dtest.R;
import com.example.ai.dtest.base.MyApplication;

import java.util.List;

/**
 * Created by ai on 2017/8/5.
 */

public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mList;

    private int resourceId;

    private int currentPostion=-1;

    private locationListener listener;

    public LocationAdapter(int resourceId, List<String> list){
        this.resourceId=resourceId;
        mList=list;
    }

    static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView info;

        viewHolder(View itemView) {
            super(itemView);
            view= itemView;
            info= itemView.findViewById(R.id.info);
        }
    }

    public void setCurrentPostion(int postion){
        currentPostion=postion;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(resourceId,parent,false);
        final DepartmentAdapter.viewHolder holder= new DepartmentAdapter.viewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                listener.process(position);
            }
        });
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                listener.process(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String buf= mList.get(position);
        DepartmentAdapter.viewHolder holderBuf= (DepartmentAdapter.viewHolder) holder;
        holderBuf.info.setText(buf);
        if(currentPostion==position){
            holderBuf.info.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorPrimary));
        }else {
            holderBuf.info.setTextColor(MyApplication.getContext().getResources().getColor(R.color.colorBackground));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(locationListener listener){
        this.listener=listener;
    }

    public interface locationListener{
        void process(int position);
    }
}

