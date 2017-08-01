package com.example.ai.dtest.frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ai.dtest.adapter.DocterAdapter;
import com.example.ai.dtest.R;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DoctorList extends Fragment {

    private List<DoctorCustom> mList;

    private DocterAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private boolean isRefresh=false;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.UPDATEFAILURE:
                    Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.UPDATESUCCESS:
                    Bundle update_bundle= msg.getData();
                    String buf1= update_bundle.getString("result");
                    List<DoctorCustom> doctor= gson.fromJson(buf1, new TypeToken<List<DoctorCustom>>(){}.getType());
                    deepclone(doctor);
                    adapter.notifyDataSetChanged();
                    if(isRefresh){
                        refreshLayout.setRefreshing(false);
                        isRefresh=false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList= new ArrayList<>();
        adapter= new DocterAdapter(mList);
        HttpUtils.docterUpdataDefault(handler);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.doctor_frag,container,false);
        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                refreshDoctor();
            }
        });
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.doctor_list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void refreshDoctor(){
        mList.clear();
        HttpUtils.docterUpdataDefault(handler);
    }

    private void deepclone(List<DoctorCustom> origin){
        for(DoctorCustom i:origin){
            try {
                mList.add((DoctorCustom) i.deepCopy());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
