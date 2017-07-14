package com.example.ai.dtest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
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
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import org.litepal.crud.DataSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import commen.HttpUtils;
import commen.MD5;
import commen.MacAddressUtils;
import commen.MyApplication;
import commen.OffLineUser;
import commen.TUserlogininfo;

public class Login extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    Button login;

    EditText user_name;

    EditText user_password;

    ImageView eye;

    CheckBox hold_password;

    CheckBox hold_autologin;

    TextView forget_password;

    TextView return_register;

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
                    TUserlogininfo userlogininfo= gson.fromJson(buf_user,TUserlogininfo.class);
                    saveOfflineUser(userlogininfo,bundle.getString("token"));
                    MyApplication.setUserName(userlogininfo.getUserloginusername());
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
//                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        MyApplication.getClient().registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        MyApplication.getClient().setLocOption(option);
        MyApplication.getClient().start();
    }

    private void saveOfflineUser(TUserlogininfo userlogininfo,String token) {
//        Log.d("ai","11");
        String userName = saveUserName;
        String userPassword_buf = savePassword;
        String userPassword = Base64.encodeToString(userPassword_buf.getBytes(), Base64.DEFAULT);
        OffLineUser offLineUsers = DataSupport.where("userName =?", userName).findFirst(OffLineUser.class);
        OffLineUser user = new OffLineUser();
        if (offLineUsers == null) {
//            Log.d("ai","22");
            user.setUserName(userName);
            if (hold_autologin.isChecked()) {
                user.setPassWord(userPassword);
                user.setAutoLogin(true);
            } else if (hold_password.isChecked()) {
                user.setPassWord(userPassword);
                user.setAutoLogin(false);
            } else {
                user.setAutoLogin(false);
            }
            user.setToken(token);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String curTime = sdf.format(userlogininfo.getUserloginlastlogintime());
            user.setLastLoginTime(curTime);
            user.save();
        } else {
//            Log.d("ai","33");
            String userName_buf = offLineUsers.getUserName();
            if (hold_autologin.isChecked()) {
                user.setPassWord(userPassword);
                user.setAutoLogin(true);
            } else if (hold_password.isChecked()) {
                user.setPassWord(userPassword);
                user.setAutoLogin(false);
            } else {
                user.setToDefault("passWord");
                user.setAutoLogin(false);
            }
            user.setToken(token);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String curTime = sdf.format(userlogininfo.getUserloginlastlogintime());
            user.setLastLoginTime(curTime);
            user.updateAll("userName=?", userName_buf);
        }
    }

    private void isLocationLogin(String location,String latitude_longitude) throws ParseException{
        String password= null;
        try {
            password = MD5.get_Md5(saveUserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date_1 = sdf.format(buf_date);
        Date date = sdf.parse(date_1);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(Login.this);
        String userip= MacAddressUtils.getIpAddress(Login.this);
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUserloginusername(saveUserName);
        userlogininfo.setUserloginpassword(password);
        userlogininfo.setUserloginlatitude(latitude_longitude.split(",")[0]);
        userlogininfo.setUserloginlongitude(latitude_longitude.split(",")[1]);
        userlogininfo.setUserloginlastlogintime(date);
        userlogininfo.setUserloginlastloginlocation(location);
        userlogininfo.setUserlogindevice(loginDevice);
        userlogininfo.setUserloginphonemodel(userphonemodel);
        userlogininfo.setUserloginbrowserversion(userphoneversion);
        userlogininfo.setUserloginmac(usermac);
        userlogininfo.setUserloginip(userip);
        userlogininfo.setUserlogintype(0);
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    private void noLocationLogin() throws ParseException{
        String password= null;
        try {
            password = MD5.get_Md5(saveUserName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date_1 = sdf.format(buf_date);
        Date date = sdf.parse(date_1);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(Login.this);
        String userip= MacAddressUtils.getIpAddress(Login.this);
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUserloginusername(saveUserName);
        userlogininfo.setUserloginpassword(password);
        userlogininfo.setUserloginlastlogintime(date);
        userlogininfo.setUserlogindevice(loginDevice);
        userlogininfo.setUserloginphonemodel(userphonemodel);
        userlogininfo.setUserloginbrowserversion(userphoneversion);
        userlogininfo.setUserloginmac(usermac);
        userlogininfo.setUserloginip(userip);
        userlogininfo.setUserlogintype(0);
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    public class LocationListener implements BDLocationListener {
        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if ((bdLocation.getLocType() == BDLocation.TypeGpsLocation) || (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) || (bdLocation.getLocType() == BDLocation.TypeOffLineLocation)) {
                Log.d("ai", Integer.toString(bdLocation.getLocType()));
                String location = bdLocation.getAddrStr();
                String latitude_longitude = bdLocation.getLatitude() + "," + bdLocation.getLongitude();
                try {
                    isLocationLogin(location, latitude_longitude);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d("ai", Integer.toString(bdLocation.getLocType()));
                try {
                    noLocationLogin();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (MyApplication.getClient().isStarted()) {
                MyApplication.getClient().stop();
            }
        }
    }
}
