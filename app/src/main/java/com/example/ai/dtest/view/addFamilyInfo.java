package com.example.ai.dtest.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.dtest.R;
import com.example.ai.dtest.util.FormatCheckUtils;

/**
 * Created by ai on 2017/8/3.
 */

public class addFamilyInfo extends Dialog implements View.OnClickListener{

    private Context context;

    private EditText name;

    private TextView male;

    private EditText age;

    private Button add;

    private addFamilyInfoListener listener;

    public void setListener(addFamilyInfoListener listener){
        this.listener=listener;
    }

    public addFamilyInfo(@NonNull Context context) {
        super(context, R.style.changePW);
        this.context=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_family_info);
        name= (EditText) findViewById(R.id.patient_name);
        male= (TextView) findViewById(R.id.patient_male);
        age= (EditText) findViewById(R.id.patient_age);
        add= (Button) findViewById(R.id.add);
        male.setOnClickListener(this);
        add.setOnClickListener(this);
        listener.init(name,male,age);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.patient_male:
                showDialog();
                break;
            case R.id.add:
                if(TextUtils.isEmpty(name.getText().toString()) || TextUtils.isEmpty(male.getText().toString()) || TextUtils.isEmpty(age.getText().toString())){
                    Toast.makeText(context,"必须全部如实填写",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!FormatCheckUtils.isLegalName(name.getText().toString())){
                    Toast.makeText(context,"姓名不合法",Toast.LENGTH_SHORT).show();
                }
                if(Integer.parseInt(age.getText().toString())<12){
                    Toast.makeText(context,"年龄必须大于12岁",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Integer.parseInt(age.getText().toString())>100){
                    Toast.makeText(context,"年龄是否有效?",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.add(name.getText().toString(),male.getText().toString(),age.getText().toString());
                break;
            default:
                break;
        }
    }

    private void showDialog(){
        String[] items={"男","女"};
        AlertDialog dialog = new AlertDialog.Builder(context,R.style.myDialog).setTitle("选择性别")
                .setSingleChoiceItems(items,-1, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            male.setText("男");
                        }else {
                            male.setText("女");
                        }
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    public interface addFamilyInfoListener{
        void add(String name,String male,String age);
        void init(EditText name,TextView male,EditText age);
    }
}
