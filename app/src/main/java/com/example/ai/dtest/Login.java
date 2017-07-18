package com.example.ai.dtest;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import org.litepal.crud.DataSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import commen.ActivityCollector;
import commen.HttpUtils;
import commen.MD5;
import commen.MacAddressUtils;
import commen.MyApplication;
import commen.OffLineUser;
import commen.Userlogininfo;

public class Login extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    Button login;

    EditText user_name;

    EditText user_password;

    ImageView eye;

    CheckBox hold_password;

    CheckBox hold_autologin;

    TextView forget_password;

    TextView return_register;

    private LocationClient client;

    private String saveUserName=null;

    private String savePassword=null;

    private boolean isShow= false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpUtils.LOGINFAILURE:
                    Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.LOGINSUCESS:
                    Bundle bundle= msg.getData();
                    String buf_user= bundle.getString("user");
                    Gson gson= new Gson();
                    Userlogininfo userlogininfo= gson.fromJson(buf_user,Userlogininfo.class);
                    saveOfflineUser(userlogininfo,bundle.getString("token"));
                    MyApplication.setUserName(userlogininfo.getUserloginname());
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public static void actionStart(Context context,String user,String password){
        Intent intent= new Intent(context,Login.class);
        intent.putExtra("userName",user);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent= getIntent();
        String name= intent.getStringExtra("userName");
        String password= intent.getStringExtra("password");
        login = (Button) findViewById(R.id.login);
        user_name = (EditText) findViewById(R.id.name);
        user_password = (EditText) findViewById(R.id.password);
        eye= (ImageView) findViewById(R.id.eye);
        hold_password = (CheckBox) findViewById(R.id.hold_password);
        hold_autologin = (CheckBox) findViewById(R.id.hold_autoLogin);
        forget_password = (TextView) findViewById(R.id.forget_password);
        return_register = (TextView) findViewById(R.id.return_register);
        user_password.addTextChangedListener(watcher);
        eye.setOnClickListener(this);
        login.setOnClickListener(this);
        return_register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        hold_password.setOnCheckedChangeListener(this);
        hold_autologin.setOnCheckedChangeListener(this);
        if(!TextUtils.isEmpty(name)){
            user_name.setText(name);
            user_name.setSelection(name.length());
        }
        if(!TextUtils.isEmpty(password)){
            user_password.setText(password);
            user_password.setSelection(password.length());
        }
    }

    TextWatcher watcher= new TextWatcher() {
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
            case R.id.login:
                tryLogin();
                break;
            case R.id.forget_password:
                break;
            case R.id.return_register:
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.hold_password:
                if(!b){
                hold_autologin.setChecked(false);
                }
                break;
            case R.id.hold_autoLogin:
                if (b) {
                    hold_password.setChecked(true);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.finishAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void tryLogin() {
        if (TextUtils.isEmpty(user_name.getText().toString()) || TextUtils.isEmpty(user_password.getText().toString())) {
            Toast.makeText(MyApplication.getContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        saveUserName=user_name.getText().toString();
        savePassword=user_password.getText().toString();
        client= new LocationClient(Login.this);
        client.registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        client.start();
    }

    private void saveOfflineUser(Userlogininfo userlogininfo,String token) {
        String userName = saveUserName;
        String userPassword_buf = savePassword;
        String userPassword = Base64.encodeToString(userPassword_buf.getBytes(), Base64.DEFAULT);
        OffLineUser offLineUsers = DataSupport.where("userName =?", userName).findFirst(OffLineUser.class);
        OffLineUser user = new OffLineUser();
        if (offLineUsers == null) {
            user.setUserName(userName);
            if (hold_autologin.isChecked()) {
                user.setPassWord(userPassword);
                user.setIsAutoLogin(1);
            } else if (hold_password.isChecked()) {
                user.setPassWord(userPassword);
                user.setToDefault("isAutoLogin");
            } else {
                user.setToDefault("passWord");
                user.setToDefault("isAutoLogin");
            }
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.save();
        } else {
            String userName_buf = offLineUsers.getUserName();
            if (hold_autologin.isChecked()) {
                user.setPassWord(userPassword);
                user.setIsAutoLogin(1);
            } else if (hold_password.isChecked()) {
                user.setPassWord(userPassword);
                user.setToDefault("isAutoLogin");
            } else {
                user.setToDefault("passWord");
                user.setToDefault("isAutoLogin");
            }
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.updateAll("userName=?", userName_buf);
        }
    }

    private void isLocationLogin(String location,String latitude_longitude){
        String password= null;
        try {
            password = MD5.get_Md5(savePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(Login.this);
        String userip= MacAddressUtils.getIpAddress(Login.this);
        Userlogininfo userlogininfo= new Userlogininfo();
        userlogininfo.setUserloginname(saveUserName);
        userlogininfo.setUserloginpwd(password);
        userlogininfo.setUserloginlat(latitude_longitude.split(",")[0]);
        userlogininfo.setUserloginlon(latitude_longitude.split(",")[1]);
        userlogininfo.setUserlogintime(date);
        userlogininfo.setUserloginloc(location);
        userlogininfo.setUserlogindev(loginDevice);
        userlogininfo.setUserloginmodel(userphonemodel);
        userlogininfo.setUserloginpver(userphoneversion);
        userlogininfo.setUserloginmac(usermac);
        userlogininfo.setUserloginip(userip);
        userlogininfo.setUserlogintype(0);
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    private void noLocationLogin() {
        String password= null;
        try {
            password = MD5.get_Md5(savePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(Login.this);
        String userip= MacAddressUtils.getIpAddress(Login.this);
        Userlogininfo userlogininfo= new Userlogininfo();
        userlogininfo.setUserloginname(saveUserName);
        userlogininfo.setUserloginpwd(password);
        userlogininfo.setUserlogintime(date);
        userlogininfo.setUserlogindev(loginDevice);
        userlogininfo.setUserloginmodel(userphonemodel);
        userlogininfo.setUserloginpver(userphoneversion);
        userlogininfo.setUserloginmac(usermac);
        userlogininfo.setUserloginip(userip);
        userlogininfo.setUserlogintype(0);
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    private class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                String location = bdLocation.getAddrStr();
                String latitude_longitude = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
                isLocationLogin(location, latitude_longitude);
            } else {
                noLocationLogin();
            }
            if (client.isStarted()) {
                client.stop();
            }
        }
    }
}

