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
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.cityPopwindow;
import com.example.ai.dtest.view.department;
import com.example.ai.dtest.view.loadDialog;
import com.example.ai.dtest.view.locationDialog;
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

    private TextView city;

    private TextView office;

    private boolean isClick=false;

    private boolean isPull=false;

    private boolean isRefresh=false;

    private int tag=0;

    private int page=1;

    private static final int PageItem=10;

    private View loadView;

    private String dept;

    private loadDialog dialog;

    private View split;

    private boolean canUpdate=true;

    private int lastVisibleItem = 0;

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
                        isClick=false;
                        mList.clear();
                        Bundle update_bundle= msg.getData();
                        String buf1= update_bundle.getString("result");
                        List<DoctorCustom> doctor= gson.fromJson(buf1, new TypeToken<List<DoctorCustom>>(){}.getType());
                        deepclone(doctor);
                        adapter.clearFootView();
                        if(!doctor.isEmpty()) {
                            if(doctor.size()==PageItem){
                                adapter.setFootView(1);
                            }
                            else {
                                adapter.setFootView(0);
                            }
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(0);
                        canUpdate=true;
                    }else if(isPull){
                        isPull=false;
                        Bundle update_bundle= msg.getData();
                        String buf1= update_bundle.getString("result");
                        List<DoctorCustom> doctor= gson.fromJson(buf1, new TypeToken<List<DoctorCustom>>(){}.getType());
                        deepclone(doctor);
                        if(!doctor.isEmpty()){
                            adapter.clearFootView();
                            if(doctor.size()==PageItem){
                                adapter.setFootView(1);
                            }
                            else {
                                adapter.setFootView(0);
                            }
                            adapter.notifyItemRangeChanged((page-1)*PageItem,doctor.size()+1);
                        }
                        else{
                            if(adapter.getIsLoad()==1) {
                                adapter.clearFootView();
                                adapter.setFootView(0);
                                adapter.notifyItemChanged(linearLayoutManager.getItemCount()-1);
                            }else {
                                adapter.notifyItemChanged(linearLayoutManager.getItemCount()-1);
                            }
                        }
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
                if(canUpdate) {
                    canUpdate=false;
                    refreshDoctor();
                }
            }
        });

        office= (TextView) view.findViewById(R.id.offices);
        office.setOnClickListener(this);

        recyclerView= (RecyclerView) view.findViewById(R.id.doctor_list);

        recommendDoctor= (TextView) view.findViewById(R.id.recommend_doctor);
        recommendDoctor.setOnClickListener(this);
        recommendDoctor.setSelected(true);
        city= view.findViewById(R.id.city);
        city.setOnClickListener(this);

        initRecycleview();
        dialog= new loadDialog(getContext());

        isClick=true;
        update();
        return view;
    }

    private void initRecycleview(){
        adapter= new DocterAdapter(mList);
        linearLayoutManager= new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setListener(new DocterAdapter.returnUp() {
            @Override
            public void goUp() {
                if(linearLayoutManager.findFirstVisibleItemPosition()>0) {
                    int childHeight = recyclerView.getChildAt(linearLayoutManager.findLastVisibleItemPosition() - linearLayoutManager.findFirstVisibleItemPosition()).getBottom();
                    int height = recyclerView.getHeight() - childHeight;
                    recyclerView.smoothScrollBy(0, -1 * height);
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    loadMoreDate();
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
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
        if (isClick && !isRefresh) {
            dialog.show();
        }
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
                if(canUpdate){
                    canUpdate=false;
                    isClick = true;
                    page = 1;
                    tag = 0;
                    recommendDoctor.setSelected(true);
                    office.setText("科室");
                    office.setSelected(false);
                    city.setText("城市");
                    city.setSelected(false);
                    update();
                }
                break;
            case R.id.offices:
                final View target= LayoutInflater.from(getContext()).inflate(R.layout.department,null);
                final department selectDepart= new department(getContext());
                int height= getResources().getDisplayMetrics().heightPixels/3*2;
                selectDepart.setContentView(target);
                selectDepart.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                selectDepart.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                selectDepart.showAsDropDown(split);
                selectDepart.setListener(new department.departmentListener() {
                    @Override
                    public void getResult(String result) {
                        if(selectDepart.isShowing()){
                            selectDepart.dismiss();
                        }
                        String[] buf= result.split(",");
                        if(buf.length==1){
                            office.setText(buf[0]);
                            office.setSelected(true);
                            dept=buf[0];
                        }else {
                            office.setText(buf[1]);
                            office.setSelected(true);
                            dept=buf[1];
                        }
                        isClick=true;
                        page=1;
                        tag=1;
                        update();

                        recommendDoctor.setSelected(false);
                        city.setText("城市");
                        city.setSelected(false);
                    }
                });
                break;
            case R.id.city:
                final View cityView= LayoutInflater.from(getContext()).inflate(R.layout.location,null);
                final cityPopwindow cityPopwindowShow = new cityPopwindow(getContext());
                cityPopwindowShow.setContentView(cityView);
                cityPopwindowShow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                cityPopwindowShow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                cityPopwindowShow.showAsDropDown(split);
                cityPopwindowShow.setListener(new locationDialog.selectLocationListener() {
                    @Override
                    public void complete(String location) {
                        if(cityPopwindowShow.isShowing()){
                            cityPopwindowShow.dismiss();
                        }

                        String[] bufLocation= location.split("-");
                        city.setText(bufLocation[bufLocation.length-1]);
                        city.setSelected(true);

                        recommendDoctor.setSelected(false);
                        office.setText("科室");
                        office.setSelected(false);
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
