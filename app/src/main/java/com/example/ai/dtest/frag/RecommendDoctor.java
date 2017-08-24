package com.example.ai.dtest.frag;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ai.dtest.DoctorHomePage;
import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.DocterAdapter;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.loadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecommendDoctor extends BaseFragment {

    private SwipeRefreshLayout refreshLayout;

    private List<DoctorCustom> systemRecommendList;

    private List<DoctorCustom> recommendList;

    private List<DoctorCustom> acceptList;

    private List<DoctorCustom> candidateList;

    private NestedScrollView scrollView;

    private DocterAdapter systemRecommendAdapter;

    private DocterAdapter recommendAdapter;

    private DocterAdapter acceptAdapter;

    private DocterAdapter candidateAdapter;

    private int familyId=-1;

    private LocationClient client;

    private boolean isRefresh;

    private loadDialog dialog;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson= new Gson();
            switch (msg.what){
                case HttpUtils.PULLCONDITIONFA:
                    Toast.makeText(getContext(),"更新信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLCONDITIONSU:
                    Bundle bundle1= msg.getData();
                    String res1= bundle1.getString("result");
                    List<Usersick> data1= gson.fromJson(res1,new TypeToken<List<Usersick>>(){}.getType());
                    if(!data1.isEmpty()) {
                        familyId = Integer.parseInt(data1.get(0).getUsersickid());
                    }
                    reflesh();
                    break;
                case HttpUtils.PULLREDOFA:
                    break;
                case HttpUtils.PULLREDOSU:
                    systemRecommendList.clear();
                    recommendList.clear();
                    acceptList.clear();
                    candidateList.clear();
                    Bundle bundle= msg.getData();
                    String res= bundle.getString("result");
                    List<DoctorCustom> data= gson.fromJson(res,new TypeToken<List<DoctorCustom>>(){}.getType());
                    deepclone(data);
                    systemRecommendAdapter.notifyDataSetChanged();
                    recommendAdapter.notifyDataSetChanged();
                    acceptAdapter.notifyDataSetChanged();
                    candidateAdapter.notifyDataSetChanged();
                    if(refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                        isRefresh=false;
                    }
                    if(dialog.isShowing()){
                        scrollView.smoothScrollBy(0,0);
                        dialog.dismiss();
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
        systemRecommendList= new ArrayList<>();
        recommendList=new ArrayList<>();
        acceptList= new ArrayList<>();
        candidateList= new ArrayList<>();
        systemRecommendAdapter= new DocterAdapter(systemRecommendList);
        recommendAdapter= new DocterAdapter(recommendList);
        acceptAdapter= new DocterAdapter(acceptList);
        candidateAdapter= new DocterAdapter(candidateList);
        initAdapter();
    }


    private void initAdapter(){
        systemRecommendAdapter.setListener(new DocterAdapter.returnUp() {
            @Override
            public void goDoctorHomePage(int position) {
                DoctorHomePage.actionStart(getActivity(),systemRecommendList.get(position).getDocloginid(),1);
            }

            @Override
            public void goUp() {

            }
        });

        recommendAdapter.setListener(new DocterAdapter.returnUp() {
            @Override
            public void goDoctorHomePage(int position) {
                DoctorHomePage.actionStart(getActivity(),recommendList.get(position).getDocloginid(),1);
            }

            @Override
            public void goUp() {

            }
        });

        acceptAdapter.setListener(new DocterAdapter.returnUp() {
            @Override
            public void goDoctorHomePage(int position) {
                DoctorHomePage.actionStart(getActivity(),acceptList.get(position).getDocloginid(),1);
            }

            @Override
            public void goUp() {

            }
        });

        candidateAdapter.setListener(new DocterAdapter.returnUp() {
            @Override
            public void goDoctorHomePage(int position) {
                DoctorHomePage.actionStart(getActivity(),candidateList.get(position).getDocloginid(),1);
            }

            @Override
            public void goUp() {

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.recommend_doctor,container,false);

        scrollView= (NestedScrollView) view.findViewById(R.id.main);

        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                reflesh();
            }
        });

        RecyclerView systemRecommend = (RecyclerView) view.findViewById(R.id.system_recommend_doctor);
        LinearLayoutManager manager1= new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        systemRecommend.setLayoutManager(manager1);
        systemRecommend.setAdapter(systemRecommendAdapter);

        RecyclerView recommend= (RecyclerView) view.findViewById(R.id.recommend_doctor);
        LinearLayoutManager manager2= new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recommend.setLayoutManager(manager2);
        recommend.setAdapter(recommendAdapter);

        RecyclerView accept= (RecyclerView) view.findViewById(R.id.accept_doctor);
        LinearLayoutManager manager3= new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        accept.setLayoutManager(manager3);
        accept.setAdapter(acceptAdapter);

        RecyclerView candidate= (RecyclerView) view.findViewById(R.id.candidate_doctor);
        LinearLayoutManager manager4= new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        candidate.setLayoutManager(manager4);
        candidate.setAdapter(candidateAdapter);

        HttpUtils.pullCondition(MyApplication.getUserPhone(),handler);

        return view;
    }

    public void reflesh(){
        if(!isRefresh){
            if(dialog==null){
                dialog= new loadDialog(getContext());
                dialog.show();
            }else {
                dialog.show();
            }
        }
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
                quest info= new quest();
                info.id=Integer.toString(familyId);
                info.lat=Double.toString(bdLocation.getLatitude());
                info.lon=Double.toString(bdLocation.getLongitude());
                Gson gson= new Gson();
                String res= gson.toJson(info);
                HttpUtils.pullReDoctor(res,handler);
            }else {
                Toast.makeText(MyApplication.getContext(),"获取位置信息失败",Toast.LENGTH_SHORT).show();
            }
            if (client.isStarted()) {
                client.stop();
            }
        }
    }

    private class quest{
        String id;
        String lat;
        String lon;
    }

    private void deepclone(List<DoctorCustom> origin){
        for(DoctorCustom i:origin){
            try {
                DoctorCustom buf= (DoctorCustom) i.deepCopy();
                if(i.getPreordertype()==1){
                    systemRecommendList.add(buf);
                }else if(i.getPreordertype()==2){
                    acceptList.add(buf);
                }else if(i.getPreordertype()==3){
                    recommendList.add(buf);
                }else {
                    candidateList.add(buf);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
