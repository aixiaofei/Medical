package com.example.ai.dtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ai.dtest.adapter.DateAdapter;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.util.HttpUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ai.dtest.base.MyApplication.getContext;

public class DoctorHomePage extends BaseActivity {

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getdoctorpix/";

    private RecyclerView recyclerView;

    private List<Integer> mList;

    private DateAdapter adapter;

    private CircleImageView doctorFig;

    private TextView docterName;

    private TextView doctorDepartment;

    private TextView doctorPosition;

    private TextView doctorComment;

    private TextView doctorDesc;

    private TextView doctorHosipital;

    private DoctorCustom info;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HttpUtils.SINGLEDOFA:
                    break;
                case HttpUtils.SINGLEDOSU:
                    Bundle bundle= msg.getData();
                    info= (DoctorCustom) bundle.getSerializable("result");
                    initData();
                    break;
            }
        }
    };

    public static void actionStart(Context context,int id){
        Intent intent =new Intent(context,DoctorHomePage.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home_page);
        Intent intent= getIntent();
        int id= intent.getIntExtra("id",-1);

        doctorFig= (CircleImageView) findViewById(R.id.doctor_photo);
        docterName= (TextView) findViewById(R.id.docter_name);
        doctorDepartment= (TextView) findViewById(R.id.docter_keshi);
        doctorPosition= (TextView) findViewById(R.id.docter_position);
        doctorDesc= (TextView) findViewById(R.id.desc);
        doctorComment= (TextView) findViewById(R.id.docter_comment);
        doctorHosipital= (TextView) findViewById(R.id.hosipital);

        recyclerView= (RecyclerView) findViewById(R.id.date);
        initDate();
        StaggeredGridLayoutManager manager= new StaggeredGridLayoutManager(8,StaggeredGridLayoutManager.VERTICAL){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        adapter= new DateAdapter(R.layout.date_item,this,mList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        HttpUtils.singleDocterUpdata(id,handler);
        loadImage(doctorFig,id);
    }

    private void initData(){
        docterName.setText(info.getDocname());
        doctorDepartment.setText(info.getDocdept());
        doctorPosition.setText(info.getDoctitlename());
        doctorDesc.setText("擅长:"+"\n"+info.getDocexpert()+"\n"+"简介:"+"\n"+info.getDocabs());
        doctorHosipital.setText(info.getDochosp());
    }

    private void loadImage(CircleImageView imageView,int doctorId){
        Uri uri= Uri.parse(IMAGEURI+doctorId);
        Glide.with(getContext())
                .load(uri)
                .error(R.drawable.defaultuserimage)
                .into(imageView);
    }

    private void initDate(){
        mList= new ArrayList<>();
        for(int i=0;i<=8;i++){
            mList.add(-1);
        }
        for(int i=9;i<=15;i++){
            mList.add(0);
        }
        mList.add(-1);
        for(int i=17;i<=23;i++){
            mList.add(0);
        }
    }
}
