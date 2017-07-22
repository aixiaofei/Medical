package com.example.ai.dtest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import org.litepal.crud.DataSupport;

import java.util.Arrays;

import commen.OffLineUser;

public class Access extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OffLineUser isAutoLogin = DataSupport.where("isAutoLogin =?","1").findFirst(OffLineUser.class);
        if(isAutoLogin==null){
            OffLineUser isPassword = DataSupport.where("passWord !=?", "").findFirst(OffLineUser.class);
            if(isPassword!=null){
                String resPassword= Arrays.toString((Base64.decode(isPassword.getPassWord(), Base64.DEFAULT)));
                Login.actionStart(Access.this,isPassword.getUserName(),resPassword);
            }
            else {
                OffLineUser isLastLogin= DataSupport.order("lastLoginTime").findFirst(OffLineUser.class);
                if(isLastLogin==null){
                    Login.actionStart(Access.this,"","");
                }
                else {
                    Login.actionStart(Access.this,isLastLogin.getUserName(),"");
                }
            }
        }
        else {
            Intent intent= new Intent(Access.this,MainActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
