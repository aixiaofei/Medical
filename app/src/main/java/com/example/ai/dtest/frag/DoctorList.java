package com.example.ai.dtest.frag;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ai.dtest.adapter.DocterAdapter;
import com.example.ai.dtest.R;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.util.EndLessOnScrollListener;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.department;
import com.example.ai.dtest.view.loadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DoctorList extends Fragment implements View.OnClickListener{

    private LocationClient client;

    private List<DoctorCustom> mList;

    private RecyclerView recyclerView;

    private DocterAdapter adapter;

    private LinearLayoutManager linearLayoutManager;

    private SwipeRefreshLayout refreshLayout;

    private TextView recommendDoctor;

    private TextView office;

    private boolean isClick=false;

    private boolean isPull=false;

    private boolean isRefresh=false;

    private int tag=0;

    private int page=1;

    private static final int PageItem=10;

    private View footView;

    private String dept;

    private loadDialog dialog;

    private View split;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.UPDATEFAILURE:
                    Toast.makeText(getActivity(),"更新失败",Toast.LENGTH_SHORT).show();
                    isClick=false;
                    if(isRefresh) {
                        isRefresh = false;
                    }
                    break;
                case HttpUtils.UPDATESUCCESS:
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if(isClick){
                        mList.clear();
                        Bundle update_bundle= msg.getData();
                        String buf1= update_bundle.getString("result");
                        List<DoctorCustom> doctor= gson.fromJson(buf1, new TypeToken<List<DoctorCustom>>(){}.getType());
                        deepclone(doctor);
                        if(!doctor.isEmpty()){
                            adapter.setFootView(footView);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(0);
                        isClick=false;
                        recyclerView.addOnScrollListener(new EndLessOnScrollListener(linearLayoutManager) {
                            @Override
                            public void onLoadMore() {
                                loadMoreDate();
                            }
                        });
                    }else if(isPull){
                        Bundle update_bundle= msg.getData();
                        String buf1= update_bundle.getString("result");
                        List<DoctorCustom> doctor= gson.fromJson(buf1, new TypeToken<List<DoctorCustom>>(){}.getType());
                        deepclone(doctor);
                        if(!doctor.isEmpty()){
                            adapter.setFootView(footView);
                        }
                        adapter.notifyDataSetChanged();
                        isPull=false;
                    }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.doctor_frag,container,false);

        split= view.findViewById(R.id.split);

        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshDoctor();
            }
        });
        office= (TextView) view.findViewById(R.id.offices);
        office.setOnClickListener(this);
        recommendDoctor= (TextView) view.findViewById(R.id.recommend_doctor);
        recommendDoctor.setOnClickListener(this);
        recommendDoctor.setSelected(true);
        recyclerView= (RecyclerView) view.findViewById(R.id.doctor_list);
        linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        footView= LayoutInflater.from(getContext()).inflate(R.layout.foot,recyclerView,false);
        dialog= new loadDialog(getContext());
        isClick=true;
        update();
        return view;
    }

    private void loadMoreDate(){
        isPull=true;
        page++;
        update();
    }


    private void refreshDoctor(){
        isClick=true;
        isRefresh=true;
        page=1;
        update();
    }

    private void update(){
        if(isClick && !isRefresh){
            dialog.show();
        }
        adapter.clearFootView();
        defaultUpdate();
    }

    private void defaultUpdate(){
        client= new LocationClient(getContext());
        client.registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        client.start();
    }

    private class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                defaultInfo info= new defaultInfo();
                info.lat=Double.toString(bdLocation.getLatitude());
                info.lon=Double.toString(bdLocation.getLongitude());
                info.page=Integer.toString(page);
                if(tag==1){
                    info.dept=dept;
                }
                Gson gson= new Gson();
                String res= gson.toJson(info);
                HttpUtils.docterUpdata(res,handler);
            }else {
                Toast.makeText(MyApplication.getContext(),"获取位置信息失败",Toast.LENGTH_SHORT).show();
            }
            if (client.isStarted()) {
                client.stop();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.recommend_doctor:
                isClick=true;
                page=1;
                tag=0;
                recommendDoctor.setSelected(true);
                office.setText("科室");
                office.setSelected(false);
                update();
                break;
            case R.id.offices:
                final View target= LayoutInflater.from(getContext()).inflate(R.layout.department,null);
                final department selectDepart= new department(getContext());
                selectDepart.setContentView(target);
                selectDepart.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                selectDepart.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                selectDepart.showAsDropDown(split);
                selectDepart.setListener(new department.departmentListener() {
                    @Override
                    public void getResult(String result) {
                        String[] buf= result.split(",");
                        if(buf.length==1){
                            office.setText(buf[0]);
                            office.setSelected(true);
                            recommendDoctor.setSelected(false);
                            dept=buf[0];
                        }else {
                            office.setText(buf[1]);
                            office.setSelected(true);
                            recommendDoctor.setSelected(false);
                            dept=buf[1];
                        }
                        if(selectDepart.isShowing()){
                            selectDepart.dismiss();
                        }
                        isClick=true;
                        page=1;
                        tag=1;
                        update();
                    }
                });
                break;
            default:
                break;
        }
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


    private class defaultInfo{
        String lat;
        String lon;
        String page;
        String dept;
    }
}
