package com.example.ai.dtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ai.dtest.R;

import java.util.List;

/**
 * Created by ai on 2017/8/30.
 */

public class DatePopwindowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mList;

    private Context context;

    private int resourceId;

    private datePopwindowListener listener;

    public void setListener(datePopwindowListener listener){
        this.listener=listener;
    }

    public DatePopwindowAdapter(Context context,List<String> mList,int resourceId) {
        super();
        this.context=context;
        this.mList=mList;
        this.resourceId=resourceId;
    }

    private static class viewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView date;

        viewHolder(View itemView) {
            super(itemView);
            view= itemView;
            date= itemView.findViewById(R.id.info);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        final viewHolder holder= new viewHolder(view);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                listener.select(position+1);
            }
        });
        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                listener.select(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String buf= mList.get(position);
        viewHolder holder1= (viewHolder) holder;
        holder1.date.setText(buf);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface datePopwindowListener{
        void select(int num);
    }
}
