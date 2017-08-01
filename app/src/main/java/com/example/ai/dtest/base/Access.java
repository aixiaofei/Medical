package com.example.ai.dtest.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import com.example.ai.dtest.Login;
import com.example.ai.dtest.MainActivity;
import com.example.ai.dtest.db.OffLineUser;

import org.litepal.crud.DataSupport;


public class Access extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OffLineUser isAutoLogin = DataSupport.where("isAutoLogin =?","1").findFirst(OffLineUser.class);
        if(isAutoLogin==null){
            OffLineUser isPassword = DataSupport.where("password !=?", "").findFirst(OffLineUser.class);
            if(isPassword!=null){
                String resPassword= new String(Base64.decode(isPassword.getPassword(), Base64.DEFAULT));
                Login.actionStart(Access.this,isPassword.getUserPhone(),resPassword);
            }
            else {
                OffLineUser isLastLogin= DataSupport.order("lastLoginTime").findFirst(OffLineUser.class);
                if(isLastLogin==null){
                    Login.actionStart(Access.this,"","");
                }
                else {
                    Login.actionStart(Access.this,isLastLogin.getUserPhone(),"");
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
