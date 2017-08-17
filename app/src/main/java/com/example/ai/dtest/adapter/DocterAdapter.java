package com.example.ai.dtest.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ai.dtest.R;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.complete;
import com.example.ai.dtest.view.load;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

/**
 * Created by ai on 2017/7/9.
 */

public class DocterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getdoctorpix/";

    private static final int TYPE_FOOT=0;

    private static final int TYPE_NORMAL=1;

    private List<DoctorCustom> mList;

    private int isLoad;

    private returnUp listener;

    public DocterAdapter(List<DoctorCustom> mList){
        this.mList=mList;
    }

    private int mFoot= 0;

    public int getIsLoad(){return isLoad;}

    public void setFootView(int type){
        isLoad = type;
        mFoot += 1;
    }

    public void clearFootView(){
        if(mFoot==1) {
            mFoot -= 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mFoot!=0 && position>=getItemCount()-mFoot){
            return TYPE_FOOT;
        }else {
            return TYPE_NORMAL;
        }
    }

    private static class normalViewHolder extends RecyclerView.ViewHolder{
        View docter_view;
        CircleImageView docter_fig;
        TextView docter_name;
        TextView docter_keshi;
        TextView docter_hosipital;
        TextView docter_specialty;
        TextView docter_distance;

        normalViewHolder(View view){
            super(view);
            docter_view=view;
            docter_fig= (CircleImageView) view.findViewById(R.id.doctor_photo);

            docter_name= (TextView) view.findViewById(R.id.docter_name);
            TextPaint paint= docter_name.getPaint();
            paint.setFakeBoldText(true);

            docter_keshi= (TextView) view.findViewById(R.id.docter_keshi);
            docter_hosipital= (TextView) view.findViewById(R.id.docter_hosipital);
            docter_specialty= (TextView) view.findViewById(R.id.docter_specialty);
            docter_distance= (TextView) view.findViewById(R.id.distance);
        }
    }

    private static class loadViewHolder extends RecyclerView.ViewHolder {
        load loading;
        complete complete;
        loadViewHolder(View itemView) {
            super(itemView);
            loading= itemView.findViewById(R.id.load);
            complete= itemView.findViewById(R.id.complete);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOT) {
            View loadView= LayoutInflater.from(getContext()).inflate(R.layout.loading,parent,false);
            loadViewHolder holder = new loadViewHolder(loadView);
            holder.loading.loadAnima();
            return holder;
        }  else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_info, parent, false);
            final normalViewHolder holder= new normalViewHolder(view);
            holder.docter_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_fig.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_hosipital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_keshi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_specialty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            holder.docter_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=holder.getAdapterPosition();
                    listener.goDoctorHomePage(position);
                }
            });
            return holder;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if(holder1 instanceof normalViewHolder){
            DoctorCustom docter= mList.get(position);
            normalViewHolder holder= (normalViewHolder) holder1;
            loadImage(holder.docter_fig,docter.getDocloginid());
            holder.docter_name.setText(docter.getDocname());
            holder.docter_keshi.setText(docter.getDocdept());
            holder.docter_hosipital.setText(docter.getDochosp());
            holder.docter_specialty.setText(docter.getDocexpert());
            Double distance= Double.valueOf(docter.getDocdistance());
            if(distance>=1000){
                holder.docter_distance.setText(new DecimalFormat("0.0").format(distance/1000)+"km");
            }else {
                holder.docter_distance.setText(new DecimalFormat("0").format(distance)+"m");
            }
        }else if(holder1 instanceof loadViewHolder){
            loadViewHolder holder= (loadViewHolder) holder1;
            if(isLoad==0){
                holder.loading.setVisibility(View.GONE);
                holder.complete.setVisibility(View.VISIBLE);
                listener.goUp();
            }else {
                holder.loading.setVisibility(View.VISIBLE);
                holder.complete.setVisibility(View.GONE);
            }
        }
    }

    public void setListener(returnUp listener){
        this.listener=listener;
    }

    public interface returnUp{
        void goDoctorHomePage(int position);
        void goUp();
    }

    private void loadImage(CircleImageView imageView,int doctorId){
        Uri uri= Uri.parse(IMAGEURI+doctorId);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .diskCacheStrategy( DiskCacheStrategy.NONE )
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size()+mFoot;
    }
}
