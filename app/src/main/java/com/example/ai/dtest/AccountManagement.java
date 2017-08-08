package com.example.ai.dtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.util.ImgUtils;
import com.example.ai.dtest.view.changePW;
import com.example.ai.dtest.util.FormatCheckUtils;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.MD5;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.db.OffLineUser;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountManagement extends BaseActivity implements View.OnClickListener{

    private changePW change;

    private String tryChangeNickname;

    private TextView userNickname;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.CHANGENICKNAMEFAILURE:
                    Toast.makeText(AccountManagement.this,"修改昵称失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHANGENICKNAMESUCESS:
                    Toast.makeText(AccountManagement.this,"修改昵称成功",Toast.LENGTH_SHORT).show();
                    MyApplication.setUserName(tryChangeNickname);
                    userNickname.setText(tryChangeNickname);
                    break;
                case HttpUtils.EXITFAILURE:
                    Toast.makeText(AccountManagement.this,"退出登录失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.EXITSUCESS:
                    Toast.makeText(AccountManagement.this,"退出登录成功",Toast.LENGTH_SHORT).show();
                    clearUserInfo();
                    ImgUtils.recycleBitmap(MyApplication.getBitmap());
                    MyApplication.setBitmap(null);
                    if(PushManager.isPushEnabled(getApplicationContext())){
                        PushManager.stopWork(getApplicationContext());
                    }
                    Login.actionStart(AccountManagement.this,MyApplication.getUserPhone(),"");
                    finish();
                    break;
                case HttpUtils.SENDFAILURE:
                    Toast.makeText(AccountManagement.this,"验证码发送失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.SENDSUCCESS:
                    Toast.makeText(AccountManagement.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHECKFAILURE:
                    Toast.makeText(AccountManagement.this,"验证码错误",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHECKSUCESS:
                    change.startChange();
                    break;
                case HttpUtils.NOCANCHANGEPW:
                    Toast.makeText(AccountManagement.this,"密码不能相同",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CANCHANGEPW:
                    change.reStartChange();
                    break;
                case HttpUtils.CHANGEPWFAILURE:
                    Toast.makeText(AccountManagement.this,"修改密码失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHANGEPWSUCESS:
                    Toast.makeText(AccountManagement.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    change.updatePassword();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management);
        CircleImageView userSculpture = (CircleImageView) findViewById(R.id.user_sculpture);
        if(MyApplication.getBitmap()!=null){
            userSculpture.setImageBitmap(MyApplication.getBitmap());
        }
        else {
            userSculpture.setImageResource(R.drawable.defaultuserimage);
        }
        ImageView backFig= (ImageView) findViewById(R.id.back_fig);
        TextView backText= (TextView) findViewById(R.id.back_text);
        userNickname = (TextView) findViewById(R.id.user_nickname);
        TextView userPhone= (TextView) findViewById(R.id.user_phone);
        TextView revisepassword= (TextView) findViewById(R.id.revise_password);
        TextView exitLogin= (TextView) findViewById(R.id.exit_login);
        userPhone.setText(MyApplication.getUserPhone());
        userNickname.setText(MyApplication.getUserName());
        backFig.setOnClickListener(this);
        backText.setOnClickListener(this);
        userNickname.setOnClickListener(this);
        revisepassword.setOnClickListener(this);
        exitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_fig:
                finish();
                break;
            case R.id.back_text:
                finish();
                break;
            case R.id.user_nickname:
                showChagngeDialog();
                break;
            case R.id.revise_password:
                showChangePWDialog();
                break;
            case R.id.exit_login:
                showExitDialog();
                break;
            default:
                break;
        }
    }

    private void showChagngeDialog() {
        final EditText editText = new EditText(AccountManagement.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(AccountManagement.this,R.style.myDialog);
        inputDialog.setTitle("请输入昵称").setView(editText,60,0,60,0);
        inputDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(editText.getText().toString())){
                            Toast.makeText(AccountManagement.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                        }else if(FormatCheckUtils.isNumber(editText.getText().toString())){
                            Toast.makeText(AccountManagement.this,"昵称不能全为数字",Toast.LENGTH_SHORT).show();
                        }else if(editText.getText().toString().equals(MyApplication.getUserName())){
                            Toast.makeText(AccountManagement.this,"昵称不能相同",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            dialog.cancel();
                            tryChangeNickname= editText.getText().toString();
                            HttpUtils.changeNickname(MyApplication.getUserPhone(),tryChangeNickname,handler);
                        }
                    }
                }).show();
    }

    private void showExitDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(AccountManagement.this,R.style.myDialog);
        builder.setMessage("是否退出登录")
                .setTitle("提示")
                .setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        HttpUtils.exitLogin(MyApplication.getUserPhone(),handler);
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

    private void showChangePWDialog(){
        change= new changePW(this);
        change.setListener(new changePW.changeListener() {
            @Override
            public void seedmessage() {
                try {
                    HttpUtils.sendMsg(MyApplication.getUserPhone(),handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void checkmessage(String yanzhengma) {
                try {
                    HttpUtils.checkMsg(MyApplication.getUserPhone(),yanzhengma,handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void canChange(String password) {
                String passwordBuf= null;
                try {
                    passwordBuf = MD5.get_Md5(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpUtils.canChangePassword(MyApplication.getUserPhone(),passwordBuf,handler);
            }

            @Override
            public void change(String password) {
                String passwordBuf= null;
                try {
                    passwordBuf = MD5.get_Md5(password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpUtils.changePassword(MyApplication.getUserPhone(),passwordBuf,handler);
            }
        });
        change.show();
    }


    private void clearUserInfo(){
        OffLineUser user= new OffLineUser();
        user.setToDefault("Token");
        user.setToDefault("isAutoLogin");
        user.setToDefault("password");
        user.setToDefault("lastLoginTime");
        user.updateAll("userPhone=?", MyApplication.getUserPhone());
    }

}
