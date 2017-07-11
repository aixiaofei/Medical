package com.example.ai.dtest;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import yanzhengma.MobileMessage;
import yanzhengma.PhoneFormatCheckUtils;

public class register extends AppCompatActivity implements View.OnClickListener{

    Button send_message; //发送验证码按钮

    Button register;

    EditText user_name;

    EditText user_yanzhengma;

    EditText user_password;

    TextView return_Login;

    private static final int SUM_TIME= 100000;

    private static final int TIME= 1000;

    private static final int FAILURE=0;

    private static final int SUCCESS=1;

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

    final Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FAILURE:
                    Toast.makeText(register.this,"发送失败，请稍后重试",Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS:
                    Toast.makeText(register.this,"发送成功",Toast.LENGTH_SHORT).show();
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
                send_message();
                break;
            case R.id.register:
                try_rerister();
                break;
            default:
                break;
        }
    }

    private void send_message(){
        String phone = user_name.getText().toString();
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            Toast.makeText(register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        countDownTimer.start();
        try {
            MobileMessage.sendMsg(phone, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void try_rerister(){
        String phone = user_name.getText().toString();
        String yanzhengma = user_yanzhengma.getText().toString();
        String password= user_password.getText().toString();
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            Toast.makeText(register.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

    }
}
