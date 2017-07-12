package com.example.ai.dtest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import java.util.Date;

import commen.MyApplication;
import commen.OffLineUser;
import commen.TUserlogininfo;

public class Login extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

//    private static final String SEED= "seed";

    Button login;

    EditText user_name;

    EditText user_password;

    CheckBox hold_password;

    CheckBox hold_autologin;

    TextView forget_password;

    TextView return_register;

    Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login= (Button) findViewById(R.id.login);
        user_name= (EditText) findViewById(R.id.name);
        user_password= (EditText) findViewById(R.id.password);
        hold_password= (CheckBox) findViewById(R.id.hold_password);
        hold_autologin= (CheckBox) findViewById(R.id.hold_autoLogin);
        forget_password= (TextView) findViewById(R.id.forget_password);
        return_register= (TextView) findViewById(R.id.return_register);
        login.setOnClickListener(this);
        return_register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        hold_autologin.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                tryLogin();
                saveOfflineUser();
                break;
            case R.id.forget_password:
                break;
            case R.id.return_register:
                Intent intent= new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            hold_password.setChecked(true);
        }
        else {
            hold_password.setChecked(false);
        }
    }

    private void tryLogin(){
        if(TextUtils.isEmpty(user_name.getText().toString())||TextUtils.isEmpty(user_password.getText().toString())){
            Toast.makeText(MyApplication.getContext(),"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        String userName= user_name.getText().toString();
        String passWord= user_password.getText().toString();
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUsername(userName);
    }

    private void saveOfflineUser(){
//        Log.d("ai","11");
        String userName= user_name.getText().toString();
        String userPassword_buf= user_password.getText().toString();
        String userPassword = Base64.encodeToString(userPassword_buf.getBytes(),Base64.DEFAULT);
        OffLineUser offLineUsers= DataSupport.where("userName =?",userName).findFirst(OffLineUser.class);
        OffLineUser user= new OffLineUser();
        if(offLineUsers==null){
//            Log.d("ai","22");
            user.setUserName(userName);
            if(hold_autologin.isChecked()){
                user.setPassWord(userPassword);
                user.setAutoLogin(true);
            }
            else if(hold_password.isChecked()){
                user.setPassWord(userPassword);
                user.setAutoLogin(false);
            }
            else {
                user.setAutoLogin(false);
            }
            String curTime=String.valueOf((new Date().getTime()/1000L));
            user.setLastLoginTime(curTime);
            user.save();
        }
        else {
//            Log.d("ai","33");
            String userName_buf= offLineUsers.getUserName();
            if(hold_autologin.isChecked()){
                user.setPassWord(userPassword);
                user.setAutoLogin(true);
            }
            else if(hold_password.isChecked()){
                user.setPassWord(userPassword);
                user.setAutoLogin(false);
            }
            else {
                user.setToDefault("passWord");
                user.setAutoLogin(false);
            }
            String curTime=String.valueOf((new Date().getTime()/1000L));
            user.setLastLoginTime(curTime);
            user.updateAll("userName=?",userName_buf);
        }
    }
}
