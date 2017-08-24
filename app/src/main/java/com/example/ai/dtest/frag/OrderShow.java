package com.example.ai.dtest.frag;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.OrderAdapter;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DoctorCustom;
import com.example.ai.dtest.data.OrderInfo;
import com.example.ai.dtest.util.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderShow extends BaseFragment {

    private List<OrderInfo> mList;

    private OrderAdapter adapter;

    private SwipeRefreshLayout refreshLayout;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpUtils.PULLONECONFA:
                    break;
                case HttpUtils.PULLORDERSU:
                    mList.clear();
                    Bundle bundle = msg.getData();
                    Gson gson = new Gson();
                    String res = bundle.getString("result");
                    List<OrderInfo> data = gson.fromJson(res, new TypeToken<List<OrderInfo>>() {
                    }.getType());
                    deepclone(data);
                    if(refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_show, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycle);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        adapter = new OrderAdapter(getContext(), mList, R.layout.order_show_item);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refleshOrder();
            }
        });

        refleshOrder();
        return view;
    }

    public void refleshOrder(){
        HttpUtils.pullOrder(MyApplication.getUserPhone(), handler);
    }

    private void deepclone(List<OrderInfo> origin) {
        for (OrderInfo i : origin) {
            try {
                mList.add((OrderInfo) i.deepCopy());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
