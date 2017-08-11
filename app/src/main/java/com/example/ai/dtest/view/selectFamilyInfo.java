package com.example.ai.dtest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.FamilyAdapter;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/8/11.
 */

public class selectFamilyInfo extends Dialog {

    private List<FamilyInfo> mList;

    private FamilyAdapter adapter;

    private int currentInfo=-1;

    private selectFamily listener;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HttpUtils.PULLFAMILYINFOFA:
                    Toast.makeText(MyApplication.getContext(),"更新病人信息失败",Toast.LENGTH_SHORT).show();
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
            }
        }
    };

    public void setListener(selectFamily listener){
        this.listener=listener;
    }

    public selectFamilyInfo(@NonNull Context context) {
        super(context, R.style.myDialog);
    }

    public selectFamilyInfo(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_family_info);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.patient_show);
        LinearLayoutManager manager= new LinearLayoutManager(getContext());
        mList= new ArrayList<>();
        adapter= new FamilyAdapter(getContext(),R.layout.patient_info_show,mList,true);
        adapter.setListener(new FamilyAdapter.familyListener() {
            @Override
            public void change(int position) {

            }

            @Override
            public void delete(int position) {

            }

            @Override
            public void click(int position) {
                currentInfo=position;
            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Button sure = (Button) findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.select(mList.get(currentInfo));
            }
        });

        HttpUtils.pullFamilyInfo(MyApplication.getUserPhone(),handler);
    }


    public interface selectFamily{
        void select(FamilyInfo info);
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
