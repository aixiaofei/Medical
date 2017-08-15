package com.example.ai.dtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ai.dtest.adapter.FamilyAdapter;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.addFamilyInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientInformation extends BaseActivity implements View.OnClickListener{

    private RecyclerView recyclerView;

    private TextView add;

    private FamilyAdapter adapter;

    private List<FamilyInfo> mList;

    private addFamilyInfo info;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.PULLFAMILYINFOFA:
                    Toast.makeText(getBaseContext(),"更新病人信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLFAMILYINFOSU:
                    Bundle bundle= msg.getData();
                    Gson gson= new Gson();
                    String result= bundle.getString("result");
                    List<FamilyInfo> familyInfo= gson.fromJson(result,new TypeToken<List<FamilyInfo>>(){}.getType());
                    mList.clear();
                    deepclone(familyInfo);
                    adapter.notifyDataSetChanged();
                    break;
                case HttpUtils.ADDFAMILYINFOFA:
                    Toast.makeText(getBaseContext(),"添加病人信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.ADDFAMILYINFOSU:
                    Toast.makeText(getBaseContext(),"添加病人信息成功",Toast.LENGTH_SHORT).show();
                    HttpUtils.pullFamilyInfo(MyApplication.getUserPhone(),handler);
                    break;
                case HttpUtils.DEFAMILYINFOFA:
                    Toast.makeText(getBaseContext(),"删除病人信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.DEFAMILYINFOSU:
                    Toast.makeText(getBaseContext(),"删除病人信息成功",Toast.LENGTH_SHORT).show();
                    HttpUtils.pullFamilyInfo(MyApplication.getUserPhone(),handler);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_information);

        ImageView backFig= (ImageView) findViewById(R.id.design_back_fig);
        TextView backText= (TextView) findViewById(R.id.design_back_text);
        backFig.setOnClickListener(this);
        backText.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.patient_show);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        mList = new ArrayList<>();
        adapter = new FamilyAdapter(this,R.layout.patient_info_show, mList,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        add = (TextView) findViewById(R.id.add_info);
        add.setOnClickListener(this);
        adapter.setListener(new FamilyAdapter.familyListener() {
            @Override
            public void change(int position) {
                FamilyInfo info= mList.get(position);
                showChangeDialog(info);
            }

            @Override
            public void delete(int position) {
                FamilyInfo info = mList.get(position);
                showDEDialog(info);
            }

            @Override
            public void click(int position) {

            }
        });

        HttpUtils.pullFamilyInfo(MyApplication.getUserPhone(),handler);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.design_back_fig:
                finish();
                break;
            case R.id.design_back_text:
                finish();
                break;
            case R.id.add_info:
                showADDDialog();
            default:
                break;
        }
    }


    private void showADDDialog(){
        info= new addFamilyInfo(this);
        info.setListener(new addFamilyInfo.addFamilyInfoListener() {
            @Override
            public void init(EditText name, TextView male, EditText age,Button add) {
            }
            @Override
            public void add(String name, String male, String age) {
                FamilyInfo familyInfo= new FamilyInfo();
                familyInfo.setFamilyname(name);
                familyInfo.setFamilymale(male);
                familyInfo.setFamilyage(age);
                familyInfo.setPhone(MyApplication.getUserPhone());
                Gson gson= new Gson();
                String res= gson.toJson(familyInfo);
                HttpUtils.addFamilyInfo(res,handler);
                info.cancel();
            }
        });
        info.show();
    }

    private void showChangeDialog(final FamilyInfo familyInfo_O){
        info= new addFamilyInfo(this);
        final String id= familyInfo_O.getFamilyid();
        info.setListener(new addFamilyInfo.addFamilyInfoListener() {
            @Override
            public void init(EditText name, TextView male, EditText age, Button add) {
                name.setText(familyInfo_O.getFamilyname());
                male.setText(familyInfo_O.getFamilymale());
                age.setText(familyInfo_O.getFamilyage());
                add.setText("修改");
            }
            @Override
            public void add(String name, String male, String age) {
                FamilyInfo familyInfo= new FamilyInfo();
                familyInfo.setFamilyname(name);
                familyInfo.setFamilymale(male);
                familyInfo.setFamilyage(age);
                familyInfo.setPhone(MyApplication.getUserPhone());
                familyInfo.setFamilyid(id);
                Gson gson= new Gson();
                String res= gson.toJson(familyInfo);
                HttpUtils.addFamilyInfo(res,handler);
                info.cancel();
            }
        });
        info.show();
    }


    private void showDEDialog(final FamilyInfo info){
        AlertDialog.Builder builder= new AlertDialog.Builder(this,R.style.myDialog);
        builder.setMessage("确认删除此病人信息")
                .setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        HttpUtils.deleteFamilyInfo(info.getFamilyid(),handler);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        builder.create().show();
    }

    private void deepclone(List<FamilyInfo> origin){
        for(FamilyInfo i:origin){
            try {
                mList.add((FamilyInfo) i.deepCopy());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
