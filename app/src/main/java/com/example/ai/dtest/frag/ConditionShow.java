package com.example.ai.dtest.frag;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ai.dtest.R;
import com.example.ai.dtest.ReleaseCondition;
import com.example.ai.dtest.base.BaseFragment;
import com.example.ai.dtest.data.Usersick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ai on 2017/8/11.
 */

public class ConditionShow extends BaseFragment{

    private List<Usersick> mList;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList=new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.condition_show,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.condition_show);
        LinearLayoutManager manager= new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(manager);

        TextView release= (TextView) view.findViewById(R.id.release);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getActivity(), ReleaseCondition.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
