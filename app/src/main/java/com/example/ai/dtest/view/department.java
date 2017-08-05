package com.example.ai.dtest.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.dtest.R;
import com.example.ai.dtest.adapter.DepartmentAdapter;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.DepartmentInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ai on 2017/8/2.
 */

public class department extends basePopwindow {

    private List<String> first;

    private List<String> second;

    private Map<String,List<String>> second_buf;

    private RecyclerView recFirst;

    private RecyclerView recSecond;

    private TextView select;

    private DepartmentAdapter firstAdapter;

    private DepartmentAdapter secondAdapter;

    private TextView departmentFirst;

    private TextView departmentSecond;

    private departmentListener listener;

    private Context context;

    public department(Context context) {
        super(context);
        this.context= context;
        initData();
    }

    private void initData(){
        first= new ArrayList<>();
        second= new ArrayList<>();
        second_buf= new HashMap<>();
        Gson gson= new Gson();
        InputStreamReader is = new InputStreamReader(MyApplication.getContext().getResources().openRawResource(R.raw.department));
        BufferedReader reader = new BufferedReader(is);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                is.close();
                reader.close();
            } catch (IOException e) {
                 e.printStackTrace();
            }
        String res= stringBuilder.toString();
        List<DepartmentInfo> data = gson.fromJson(res,new TypeToken<List<DepartmentInfo>>(){}.getType());
        for(DepartmentInfo i:data){
            first.add(i.getFirst());
            second_buf.put(i.getFirst(),i.getSecond());
        }
    }

    public void setListener(departmentListener listener){
        this.listener=listener;
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        recFirst= (RecyclerView) contentView.findViewById(R.id.rec_first);
        recSecond= (RecyclerView) contentView.findViewById(R.id.rec_second);
        LinearLayoutManager manager1= new LinearLayoutManager(context);
        LinearLayoutManager manager2= new LinearLayoutManager(context);
        firstAdapter= new DepartmentAdapter(R.layout.department_item,first);
        secondAdapter= new DepartmentAdapter(R.layout.department_item,second);
        recFirst.setAdapter(firstAdapter);
        recSecond.setAdapter(secondAdapter);
        recFirst.setLayoutManager(manager1);
        recSecond.setLayoutManager(manager2);
        departmentFirst= (TextView) contentView.findViewById(R.id.department_first);
        departmentSecond= (TextView) contentView.findViewById(R.id.department_second);
        select= (TextView) contentView.findViewById(R.id.select);
        initListener();
    }

    private void initListener(){
        firstAdapter.setListener(new DepartmentAdapter.departmentListener() {
            @Override
            public void process(int position) {
                departmentFirst.setText(first.get(position));
                departmentSecond.setText(null);
                firstAdapter.setCurrentPostion(position);
                firstAdapter.notifyDataSetChanged();
                second.clear();
                second.addAll(second_buf.get(first.get(position)));
                secondAdapter.setCurrentPostion(-1);
                secondAdapter.notifyDataSetChanged();
            }
        });
        secondAdapter.setListener(new DepartmentAdapter.departmentListener() {
            @Override
            public void process(int position) {
                departmentSecond.setText(second.get(position));
                secondAdapter.setCurrentPostion(position);
                secondAdapter.notifyDataSetChanged();
            }
        });
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(departmentFirst.getText().toString())){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(departmentSecond.getText().toString()) && !second_buf.get(departmentFirst.getText().toString()).isEmpty()){
                    Toast.makeText(context,"请继续选择",Toast.LENGTH_SHORT).show();
                }
                else {
                    String res = departmentFirst.getText().toString() + "," + departmentSecond.getText().toString();
                    listener.getResult(res);
                }
            }
        });
    }

    public interface departmentListener{
        void getResult(String result);
    }

}
