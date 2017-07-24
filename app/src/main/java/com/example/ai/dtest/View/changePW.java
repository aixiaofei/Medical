package com.example.ai.dtest.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.ai.dtest.R;
import com.example.ai.dtest.commen.MyApplication;
import com.example.ai.dtest.commen.OffLineUser;
import com.example.ai.dtest.yanzhengma.FormatCheckUtils;

import org.litepal.crud.DataSupport;

/**
 * Created by ai on 2017/7/24.
 */

public class changePW extends Dialog implements View.OnClickListener{

    private static final int SUM_TIME= 60000;  // 验证码按钮重新发送间隔时间

    private static final int TIME= 1000;  //配合上面参数，倒计时

    private Context context;

    private Button sendMessage;

    private EditText yanzhengma;

    private EditText password;

    private ImageView eye;

    private changeListener listener;

    private boolean isShow= false;

    private static String originPW;

    private static String tryChangePW;

    //实现倒计时
    private CountDownTimer countDownTimer=new CountDownTimer(SUM_TIME,TIME) {
        // 第一个参数是总的倒计时时间
        // 第二个参数是每隔多少时间 (ms) 调用一次 onTick() 方法
        public void onTick(long millisUntilFinished) {
            sendMessage.setText(millisUntilFinished / 1000 + "s 后重新发送");
            sendMessage.setEnabled(false);
        }
        public void onFinish() {
            sendMessage.setText("重新获取验证码");
            sendMessage.setEnabled(true);
        }
    };

    public void setListener(changeListener listener){
        this.listener=listener;
    }

    public changePW(Context context) {
        super(context,R.style.changePW);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change);
        setCanceledOnTouchOutside(false);
        sendMessage= (Button) findViewById(R.id.send_meassage);
        yanzhengma= (EditText) findViewById(R.id.user_yanzhengma);
        password= (EditText) findViewById(R.id.user_password);
        eye= (ImageView) findViewById(R.id.eye);
        Button change = (Button) findViewById(R.id.change);
        sendMessage.setOnClickListener(this);
        change.setOnClickListener(this);
        initOnclick();
        initData();
    }

    private void initData(){
        OffLineUser offLineUser = DataSupport.where("userPhone=?", MyApplication.getUserPhone()).findFirst(OffLineUser.class);
        if(!TextUtils.isEmpty(offLineUser.getPassword())) {
            originPW = new String(Base64.decode(offLineUser.getPassword(), Base64.DEFAULT));
        }
    }

    private void initOnclick(){
        password.addTextChangedListener(new TextWatcher() {
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
                if(editable.length()<0){
                    eye.setVisibility(View.GONE);
                }
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isShow){
                    isShow=true;
                    eye.setImageResource(R.drawable.eye_open);
                    password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    password.setSelection(password.getText().length());
                }
                else {
                    isShow=false;
                    eye.setImageResource(R.drawable.eye_close);
                    password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_meassage:
                countDownTimer.start();
                listener.seedmessage();
                break;
            case R.id.change:
                tryChangePW= password.getText().toString();
                if(TextUtils.isEmpty(tryChangePW)|| !FormatCheckUtils.isPassword(tryChangePW)){
                    Toast.makeText(context,"密码格式不正确",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(yanzhengma.getText().toString())){
                    Toast.makeText(context,"验证码不能为空",Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isEmpty(originPW)&&originPW.equals(tryChangePW)) {
                    Toast.makeText(context, "密码不能相同", Toast.LENGTH_SHORT).show();
                } else {
                    listener.checkmessage(yanzhengma.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    public void startChange(){
        if(!TextUtils.isEmpty(originPW) && !originPW.equals(tryChangePW)){
            listener.change(tryChangePW);
        }
        else {
            listener.canChange(tryChangePW);
        }
    }

    public void reStartChange(){
        listener.change(tryChangePW);
    }

    public void updatePassword(){
        String passwordBuf= Base64.encodeToString(tryChangePW.getBytes(), Base64.DEFAULT);
        OffLineUser offLineUser= new OffLineUser();
        offLineUser.setPassword(passwordBuf);
        offLineUser.updateAll("userPhone=?",MyApplication.getUserPhone());
    }

    public interface changeListener{
        void seedmessage();
        void checkmessage(String yanzhengma);
        void canChange(String password);
        void change(String password);
    }
}
