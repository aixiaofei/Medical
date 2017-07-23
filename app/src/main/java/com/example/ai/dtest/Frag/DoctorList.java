package com.example.ai.dtest.Frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ai.dtest.Adapter.DocterAdapter;
import com.example.ai.dtest.R;
import com.example.ai.dtest.commen.DoctorCustom;
import com.example.ai.dtest.commen.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoctorList extends Fragment {

    private List<DoctorCustom> mList;

    private DocterAdapter adapter;

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
                    break;
                default:
                    break;
            }
        }
    };

//    public static DoctorList newInstance(List<TDoctorCustom> doctor){
//        DoctorList list= new DoctorList();
//        Bundle bundle= new Bundle();
//        Gson gson= new Gson();
//        String res= gson.toJson(doctor);
//        bundle.putString("data",res);
//        list.setArguments(bundle);
//        return list;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList= new ArrayList<>();
        adapter= new DocterAdapter(mList);
        HttpUtils.docterUpdataDefault(handler);
//        Bundle bundle= getArguments();
//        String data= bundle.getString("data");
//        Gson gson= new Gson();
//        List<TDoctorCustom> buf= gson.fromJson(data,new TypeToken<List<TDoctorCustom>>(){}.getType());
//        mList=buf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.doctor_frag,container,false);
        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.doctor_list);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return view;
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
