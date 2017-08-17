package com.example.ai.dtest.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.dtest.MainActivity;
import com.example.ai.dtest.R;
import com.example.ai.dtest.ReleaseCondition;
import com.example.ai.dtest.adapter.ConditionAdapter;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.view.loadDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/8/11.
 */

public class ConditionShow extends BaseFragment{

    private SwipeRefreshLayout refreshLayout;

    public static final String ADD = "add";

    public static final String CHANGE= "change";

    private List<Usersick> mList;

    private RecyclerView recyclerView;

    private ConditionAdapter adapter;

    private boolean isRefresh;

    private loadDialog dialog;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HttpUtils.PULLCONDITIONFA:
                    Toast.makeText(getContext(),"更新信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLCONDITIONSU:
                    mList.clear();
                    Bundle bundle= msg.getData();
                    String res= bundle.getString("result");
                    Gson gson= new Gson();
                    List<Usersick> data= gson.fromJson(res,new TypeToken<List<Usersick>>(){}.getType());
                    deepclone(data);
                    adapter.notifyDataSetChanged();
                    if(refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                        isRefresh=false;
                    }
                    if(dialog.isShowing()){
                        recyclerView.smoothScrollToPosition(0);
                        dialog.dismiss();
                    }
                    break;
                case HttpUtils.DECONDITIONFA:
                    Toast.makeText(getContext(),"删除病情失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.DECONDITIONSU:
                    Toast.makeText(getContext(),"删除病情成功",Toast.LENGTH_SHORT).show();
                    HttpUtils.pullCondition(MyApplication.getUserPhone(),handler);
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList=new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.condition_show,container,false);

        refreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                reflesh();
            }
        });

        recyclerView= (RecyclerView) view.findViewById(R.id.condition_show);
        adapter= new ConditionAdapter(getContext(),R.layout.condition_item,mList);
        LinearLayoutManager manager= new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        initListener();

        TextView release= (TextView) view.findViewById(R.id.release);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleaseCondition.actionStart(getActivity(),ADD,null);
            }
        });

        reflesh();
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
        HttpUtils.pullCondition(MyApplication.getUserPhone(),handler);
    }


    private void initListener(){
        adapter.setListener(new ConditionAdapter.conditionListener() {
            @Override
            public void change(int position) {
                String id= mList.get(position).getUsersickid();
                ReleaseCondition.actionStart(getActivity(),CHANGE,id);
            }

            @Override
            public void delete(int position) {
                showDEDialog(mList.get(position));
            }
        });
    }
    
    private void showDEDialog(final Usersick info){
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext(),R.style.myDialog);
        builder.setMessage("确认删除此病情")
                .setTitle("提示")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        HttpUtils.deleteCondition(info.getUsersickid(),handler);
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

    private void deepclone(List<Usersick> origin){
        for(Usersick i:origin){
            try {
                mList.add((Usersick) i.deepCopy());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
