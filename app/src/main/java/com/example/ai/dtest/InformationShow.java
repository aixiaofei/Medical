package com.example.ai.dtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.UerInfo;
import com.example.ai.dtest.util.HttpUtils;

public class InformationShow extends BaseActivity implements View.OnClickListener{

    private TextView userName;

    private TextView identify;

    private TextView identifyFig;

    private TextView sex;

    private TextView age;

    private TextView addressTop;

    private TextView addressDown;

    private UerInfo info;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.GETUSERINFOFAILURE:
                    break;
                case HttpUtils.GETUSERINFOSUCESS:
                    Bundle bundle= msg.getData();
                    info = (UerInfo) bundle.getSerializable("info");
                    setInfo(info);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personl_info_show);
        ImageView back_fig= (ImageView) findViewById(R.id.design_back_fig);
        TextView back_text= (TextView) findViewById(R.id.design_back_text);
        userName = (TextView) findViewById(R.id.enter_name);
        identify = (TextView) findViewById(R.id.enter_identify);
        identifyFig= (TextView) findViewById(R.id.enter_identify_fig);
        sex= (TextView) findViewById(R.id.select_sex);
        age= (TextView) findViewById(R.id.select_age);
        addressTop = (TextView) findViewById(R.id.addressTop);
        addressDown= (TextView) findViewById(R.id.addressDown);
        Button perfect= (Button) findViewById(R.id.perfect);
        back_fig.setOnClickListener(this);
        back_text.setOnClickListener(this);
        perfect.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        HttpUtils.getUserInfo(MyApplication.getUserPhone(),handler);
    }

    private void setInfo(UerInfo info){
        if(!TextUtils.isEmpty(info.getUsername())){
            userName.setText(info.getUsername());
        }
        if(!TextUtils.isEmpty(info.getUsercardnum())){
            identify.setText(info.getUsercardnum());
        }
        if(!TextUtils.isEmpty(info.getUsercardphoto())){
            identifyFig.setText("已上传");
            identifyFig.setTextColor(Color.BLUE);
        }
        if(!TextUtils.isEmpty(info.getUsermale())){
            sex.setText(info.getUsermale());
        }
        if(!TextUtils.isEmpty(info.getUserage())){
            age.setText(info.getUserage()+"岁");
        }
        if(!TextUtils.isEmpty(info.getUseradr())){
            String[] test= info.getUseradr().split("\\|");
            addressTop.setText(test[0]);
            addressDown.setText(test[1]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.design_back_fig:
                finish();
                break;
            case R.id.design_back_text:
                finish();
                break;
            case R.id.perfect:
                Intent intent= new Intent(this,InformationDesign.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
