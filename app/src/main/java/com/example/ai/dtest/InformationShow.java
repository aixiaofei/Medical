package com.example.ai.dtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.Userinfo;
import com.example.ai.dtest.util.HttpUtils;

public class InformationShow extends BaseActivity implements View.OnClickListener{

    private TextView userName;

    private TextView identify;

    private TextView identifyFig;

    private TextView sex;

    private TextView age;

    private TextView addressTop;

    private TextView addressDown;

    private Userinfo info;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.GETUSERINFOFAILURE:
                    Toast.makeText(InformationShow.this,"获取信息失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.GETUSERINFOSUCESS:
                    Bundle bundle= msg.getData();
                    info = (Userinfo) bundle.getSerializable("info");
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
        setContentView(R.layout.information_show);
        ImageView back_fig= (ImageView) findViewById(R.id.design_back_fig);
        TextView back_text= (TextView) findViewById(R.id.design_back_text);
        userName = (TextView) findViewById(R.id.enter_name);
        identify = (TextView) findViewById(R.id.enter_identify);
        identifyFig= (TextView) findViewById(R.id.enter_identify_fig);
        sex= (TextView) findViewById(R.id.select_sex);
        age= (TextView) findViewById(R.id.select_age);
        addressTop = (TextView) findViewById(R.id.addressTop);
        addressDown= (TextView) findViewById(R.id.addressDown);
        HttpUtils.getUserInfo(MyApplication.getUserPhone(),handler);
        Button perfect= (Button) findViewById(R.id.perfect);
        back_fig.setOnClickListener(this);
        back_text.setOnClickListener(this);
        perfect.setOnClickListener(this);
    }

    private void setInfo(Userinfo info){
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
                InformationDesign.actionStart(this,info);
                finish();
                break;
            default:
                break;
        }
    }
}
