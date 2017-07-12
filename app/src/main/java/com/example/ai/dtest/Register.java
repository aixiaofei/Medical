package com.example.ai.dtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;
import commen.TUserlogininfo;
import commen.HttpUtils;
import yanzhengma.FormatCheckUtils;

public class Register extends BaseActivity implements View.OnClickListener{

    Button send_message; //发送验证码按钮

    Button register;     //注册按钮

    EditText user_name;  //用户名文本框，也就是手机号

    EditText user_yanzhengma;  // 验证码文本框

    EditText user_password;   // 密码文本框

    TextView return_Login;  //返回登陆

    private static final int SUM_TIME= 60000;  // 验证码按钮重新发送间隔时间

    private static final int TIME= 1000;  //配合上面参数，倒计时

    private static final int SENDFAILURE=1;  //请求验证码失败标识

    private static final int SENDSUCCESS=2; // 请求验证码成功标识

    private static final int CHECKFAILURE=3; //检验失败标识

    private static final int CHECKSUCESS=4; // 检验成功标识

    private static final int REGISTERFAILURE=5;

    private static final int REGISTERSUCESS=6;

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
                case SENDFAILURE:
                    Toast.makeText(Register.this,"发送失败，请稍后重试",Toast.LENGTH_SHORT).show();
                    break;
                case SENDSUCCESS:
                    Toast.makeText(Register.this,"发送成功",Toast.LENGTH_SHORT).show();
                    break;
                case CHECKFAILURE:
//                    Log.d("ai","11");
                    Toast.makeText(Register.this,"验证码不正确",Toast.LENGTH_SHORT).show();
                    break;
                case CHECKSUCESS:
//                    Log.d("ai","22");
                    register();
                    break;
                case REGISTERFAILURE:
                    Toast.makeText(Register.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
                case REGISTERSUCESS:
                    Toast.makeText(Register.this,"注册成功,转向主页",Toast.LENGTH_SHORT).show();
//                    Intent intent =new Intent(Register.this,Login.class);
                    break;
                default:
                    break;
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
        user_name= (EditText) findViewById(R.id.user_name);
        user_yanzhengma= (EditText) findViewById(R.id.user_yanzhengma);
        user_password= (EditText) findViewById(R.id.user_password);
        return_Login= (TextView) findViewById(R.id.return_login);
        send_message.setOnClickListener(this);
        register.setOnClickListener(this);
        return_Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_meassage:
                String phone = user_name.getText().toString();
                //正则判断手机号是否有效
                if (!FormatCheckUtils.isPhoneLegal(phone)) {
                    Toast.makeText(Register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog.Builder builder= new AlertDialog.Builder(Register.this);
                builder.setMessage("请确认您要向手机号"+phone+"发送验证码")
                        .setTitle("提示")
                        .setIcon(R.drawable.alert)
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
                //尝试登陆
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
        String phone = user_name.getText().toString();
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
        String phone = user_name.getText().toString();
        String yanzhengma = user_yanzhengma.getText().toString();
        String password= user_password.getText().toString();
        //同
        if (!FormatCheckUtils.isPhoneLegal(phone)) {
            Toast.makeText(Register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!FormatCheckUtils.isPassword(password)){
            Toast.makeText(Register.this, "密码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        //检验验证码
        try {
            HttpUtils.checkMsg(phone,yanzhengma,handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void register(){
        String phone = user_name.getText().toString();
        String password= user_password.getText().toString();
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUsername(phone);
        userlogininfo.setUserpassword(password);
        Gson gson= new Gson();
        String buf= gson.toJson(userlogininfo);
        try {
            HttpUtils.register(buf,handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
