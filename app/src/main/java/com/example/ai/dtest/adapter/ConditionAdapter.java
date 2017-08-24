package com.example.ai.dtest.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.ai.dtest.PatientInformation;
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.load;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ImageView fullImage;

    private conditionListener listener;

    private Dialog dialog;

    private load load;

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
        TextView release;
        TextView change;
        TextView delete;
        TextView state;
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
            release= itemView.findViewById(R.id.release);
            change=itemView.findViewById(R.id.change);
            delete=itemView.findViewById(R.id.delete);
            state= itemView.findViewById(R.id.state);
            time= itemView.findViewById(R.id.release_time);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(resourceId,parent,false);
        final viewHolder holder=new viewHolder(view);

        holder.release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position= holder.getAdapterPosition();
                listener.release(position);
            }
        });

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

        for(int i=0;i<holder.fig.size();i++){
            final int buf=i;
            holder.fig.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= holder.getAdapterPosition();
                    String path=mList.get(position).getUsersickpic().split(",")[buf];
                    if(holder.fig.get(buf).getDrawable()!=null){
                        showFullDialog(path);
                    }
                }
            });
        }

        return holder;
    }

    private void showFullDialog(String path){
        if(dialog==null) {
            dialog = new Dialog(context,R.style.fullDialog);
            dialog.show();
            Window window= dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawableResource(android.R.color.black);
            window.setWindowAnimations(R.style.fullWindowAnim);
            View view = LayoutInflater.from(context).inflate(R.layout.full_screen_image, null);
            fullImage = view.findViewById(R.id.full_image);
            load= view.findViewById(R.id.load);
            load.loadAnima();
            loadFullImage(fullImage,IMAGEURI+path);
            fullImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);
        }else {
            fullImage.setVisibility(View.GONE);
            load.setVisibility(View.VISIBLE);
            load.loadAnima();
            loadFullImage(fullImage,IMAGEURI+path);
            dialog.show();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Usersick info= mList.get(position);
        viewHolder holder1= (viewHolder) holder;
        holder1.name.setText(info.getFamilyname());
        holder1.male.setText(info.getFamilymale());
        holder1.age.setText(info.getFamilyage());
        holder1.dec.setText(info.getUsersickdesc());

        if(info.getUsersickstateid()==1){
            holder1.state.setText("已添加");
            holder1.release.setText("发布");
            holder1.change.setText("修改");
            holder1.delete.setText("删除");
        }else if(info.getUsersickstateid()==2){
            holder1.state.setText("已发布");
            holder1.release.setText("取消发布");
            holder1.change.setText("不可修改");
            holder1.delete.setText("不可删除");
        }else {
            holder1.state.setText("已签订订单");
            holder1.release.setText("不可取消发布");
            holder1.change.setText("不可修改");
            holder1.delete.setText("不可删除");
        }

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


    private void loadImage(ImageView imageView, String path){
        Uri uri= Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    private void loadFullImage(final ImageView imageView, String path){
        Uri uri = Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        load.clearAnima();
                        load.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface conditionListener{
        void release(int position);
        void change(int position);
        void delete(int position);
    }
}

