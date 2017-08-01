package com.example.ai.dtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ai.dtest.base.ActivityCollector;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.util.FormatCheckUtils;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.MD5;
import com.example.ai.dtest.data.Userlogininfo;
import com.google.gson.Gson;
import java.io.IOException;

public class Register extends BaseActivity implements View.OnClickListener{

    Button send_message; //发送验证码按钮

    Button register;     //注册按钮

    EditText user_phone;  //用户名文本框，也就是手机号

    EditText user_yanzhengma;  // 验证码文本框

    ImageView eye;

    EditText user_password;   // 密码文本框

    TextView return_Login;  //返回登陆

    private String saveUserPhone=null;

    private String savePassword=null;

    private static final int SUM_TIME= 60000;  // 验证码按钮重新发送间隔时间

    private static final int TIME= 1000;  //配合上面参数，倒计时

    private boolean isShow= false;

    //实现倒计时
    CountDownTimer countDownTimer=new CountDownTimer(SUM_TIME,TIME) {
        // 第一个参数是总的倒计时时间
        // 第二个参数是每隔多少时间 (ms) 调用一次 onTick() 方法
        public void onTick(long millisUntilFinished) {
            send_message.setText(millisUntilFinished / 1000 + "s 后重新发送");
            send_message.setEnabled(false);
        }
        public void onFinish() {
            send_message.setText("重新获取验证码");
            send_message.setEnabled(true);
        }
    };

    //Handler处理所有子线程返回的消息，更新UI
    final Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.SENDFAILURE:
                    Toast.makeText(Register.this,"发送失败，请稍后重试",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.SENDSUCCESS:
                    Toast.makeText(Register.this,"发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHECKFAILURE:
//                    Log.d("ai","11");
                    Toast.makeText(Register.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.CHECKSUCESS:
//                    Log.d("ai","22");
                    register();
                    break;
                case HttpUtils.REGISTERFAILURE:
                    Toast.makeText(Register.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.REGISTERSUCESS:
                    Toast.makeText(Register.this,"注册成功,转向登陆",Toast.LENGTH_SHORT).show();
                    Login.actionStart(Register.this,saveUserPhone,savePassword);
                    finish();
                    break;
                case HttpUtils.CANREGISTER:
                    Toast.makeText(Register.this,"该手机号可以注册",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.NOCANREGISTER:
                    Toast.makeText(Register.this,"该手机号已被注册",Toast.LENGTH_SHORT).show();
                    user_phone.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    TextWatcher watcher_user= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(FormatCheckUtils.isPhoneLegal(editable.toString())){
                Gson gson= new Gson();
                User user= new User();
                user.user= editable.toString();
                String buf= gson.toJson(user);
                try {
                    HttpUtils.canRegister(buf,handler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private class User{
        String user;
    }

    TextWatcher watcher_password= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()>0){
                eye.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length()==0){
                eye.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //获取所有控件实例
        send_message= (Button) findViewById(R.id.send_meassage);
        register= (Button) findViewById(R.id.register);
        user_phone= (EditText) findViewById(R.id.user_name);
        user_phone.addTextChangedListener(watcher_user);
        user_yanzhengma= (EditText) findViewById(R.id.user_yanzhengma);
        eye= (ImageView) findViewById(R.id.eye);
        eye.setOnClickListener(this);
        user_password= (EditText) findViewById(R.id.user_password);
        user_password.addTextChangedListener(watcher_password);
        return_Login= (TextView) findViewById(R.id.return_login);
        send_message.setOnClickListener(this);
        register.setOnClickListener(this);
        return_Login.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.finishAll();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eye:
                if(!isShow){
                    isShow=true;
                    eye.setImageResource(R.drawable.eye_open);
                    user_password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    user_password.setSelection(user_password.getText().length());
                }
                else {
                    isShow=false;
                    eye.setImageResource(R.drawable.eye_close);
                    user_password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    user_password.setSelection(user_password.getText().length());
                }
                break;
            case R.id.send_meassage:
                String phone = user_phone.getText().toString();
                //正则判断手机号是否有效
                if (!FormatCheckUtils.isPhoneLegal(phone)) {
                    Toast.makeText(Register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder= new AlertDialog.Builder(Register.this,R.style.myDialog);
                builder.setMessage("请确认您要向手机号"+phone+"发送验证码")
                        .setTitle("提示")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            //尝试发送验证码
                            send_message();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                builder.create().show();
                break;
            case R.id.register:
                //尝试注册
                try_rerister();
                break;
            case R.id.return_login:
                Intent intent= new Intent(Register.this,Login.class);
                startActivity(intent);
            default:
                break;
        }
    }

    private void send_message(){
        String phone = user_phone.getText().toString();
        //开启倒计时
        countDownTimer.start();
        try {
            //发送验证码
            HttpUtils.sendMsg(phone, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void try_rerister(){
        saveUserPhone = user_phone.getText().toString();
        String yanzhengma = user_yanzhengma.getText().toString();
        savePassword = user_password.getText().toString();

        //同
        if (!FormatCheckUtils.isPhoneLegal(saveUserPhone)) {
            Toast.makeText(Register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!FormatCheckUtils.isPassword(savePassword)){
            Toast.makeText(Register.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        //检验验证码
        try {
            HttpUtils.checkMsg(saveUserPhone,yanzhengma,handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(){
        String phone = saveUserPhone;
        String password= null;
        try {
            password = MD5.get_Md5(savePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Userlogininfo userlogininfo= new Userlogininfo();
        userlogininfo.setUserloginphone(phone);
        userlogininfo.setUserloginpwd(password);
        Gson gson= new Gson();
        String buf= gson.toJson(userlogininfo);
        try {
            HttpUtils.register(buf,handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
