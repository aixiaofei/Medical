package com.example.ai.dtest;

import android.app.Activity;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ai.dtest.adapter.DateAdapter;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private TextView select;

    private int mode;

    private int loginId;

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
                case HttpUtils.PULLDATEFA:
                    break;
                case HttpUtils.PULLDATESU:
                    Bundle bundle1= msg.getData();
                    List<Integer> data= bundle1.getIntegerArrayList("result");
                    for(int i=0;i<=6;i++){
                        mList.set(i+9,data.get(i));
                    }
                    for(int i=7;i<data.size();i++){
                        mList.set(i+10,data.get(i));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case HttpUtils.PULLCONDITIONFA:
                    Toast.makeText(getContext(),"更新信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLCONDITIONSU:
                    Bundle bundle2= msg.getData();
                    String res1= bundle2.getString("result");
                    Gson gson= new Gson();
                    List<Usersick> data1= gson.fromJson(res1,new TypeToken<List<Usersick>>(){}.getType());
                    if(data1.isEmpty()){
                        Toast.makeText(getContext(),"请先添加新的病情并发布",Toast.LENGTH_SHORT).show();
                    }else if(data1.get(0).getUsersickstateid()==1){
                        Toast.makeText(getContext(),"请先发布病情",Toast.LENGTH_SHORT).show();
                    }else if(data1.get(0).getUsersickstateid()==3){
                        Toast.makeText(getContext(),"已有病情签订订单",Toast.LENGTH_SHORT).show();
                    }else {
                        if(mode==0){
                            addCandidate(loginId,Integer.parseInt(data1.get(0).getUsersickid()));
                        }else if(mode==1){
                            addMenu(loginId,Integer.parseInt(data1.get(0).getUsersickid()));
                        }
                    }
                    break;
                case HttpUtils.TOCANDIDATEFA:
                    break;
                case HttpUtils.TOCANDIDATESU:
                    Toast.makeText(getContext(),"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(DoctorHomePage.this,MainActivity.class);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case HttpUtils.CREATEORDERFA:
                    break;
                case HttpUtils.CREATEORDERSU:
                    Toast.makeText(getContext(),"生成订单成功",Toast.LENGTH_SHORT).show();
                    Intent intent1= new Intent(DoctorHomePage.this,MainActivity.class);
                    setResult(RESULT_OK,intent1);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    public static void actionStart(Activity activity, int id, int mode){
        Intent intent =new Intent(activity,DoctorHomePage.class);
        intent.putExtra("id",id);
        intent.putExtra("mode",mode);
        if(mode==0) {
            activity.startActivityForResult(intent,MainActivity.TOCANDIDATE);
        }else if(mode==1) {
            activity.startActivityForResult(intent, MainActivity.ADDMENU);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_home_page);
        Intent intent= getIntent();
        loginId= intent.getIntExtra("id",-1);
        mode= intent.getIntExtra("mode",-1);

        doctorFig= (CircleImageView) findViewById(R.id.doctor_photo);
        docterName= (TextView) findViewById(R.id.docter_name);
        doctorDepartment= (TextView) findViewById(R.id.docter_keshi);
        doctorPosition= (TextView) findViewById(R.id.docter_position);
        doctorDesc= (TextView) findViewById(R.id.desc);
        doctorComment= (TextView) findViewById(R.id.docter_comment);
        doctorHosipital= (TextView) findViewById(R.id.hosipital);
        ImageView returnFig= (ImageView) findViewById(R.id.design_back_fig);
        TextView returnText= (TextView) findViewById(R.id.design_back_text);
        returnFig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        returnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select= (TextView) findViewById(R.id.select);
        if(mode==1){
            select.setText("确认与医生签订订单");
        }
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpUtils.pullCondition(MyApplication.getUserPhone(),handler);
            }
        });

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
        HttpUtils.singleDocterUpdata(loginId,handler);
        HttpUtils.pullDate(Integer.toString(loginId),handler);
        loadImage(doctorFig,loginId);
    }

    private void initData(){
        docterName.setText(info.getDocname());
        doctorDepartment.setText(info.getDocdept());
        doctorPosition.setText(info.getDoctitlename());
        doctorDesc.setText("擅长:"+"\n"+info.getDocexpert()+"\n"+"简介:"+"\n"+info.getDocabs());
        doctorHosipital.setText(info.getDochosp());
    }

    private void addCandidate(int loginId,int userSickId){
        Id id= new Id();
        id.docloginid=loginId;
        id.userscikid=userSickId;
        Gson gson= new Gson();
        String res= gson.toJson(id);
        HttpUtils.toCandidate(res,handler);
    }

    private class Id{
        int docloginid;
        int userscikid;
    }

    private void addMenu(int loginId,int userSickId){
        Id id= new Id();
        id.docloginid=loginId;
        id.userscikid=userSickId;
        Gson gson= new Gson();
        String res= gson.toJson(id);
        HttpUtils.createOrder(res,handler);
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
