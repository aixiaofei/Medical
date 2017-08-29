package com.example.ai.dtest;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushNotificationBuilder;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.load.engine.Resource;
import com.example.ai.dtest.base.ActivityCollector;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.util.FormatCheckUtils;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.MD5;
import com.example.ai.dtest.util.MacAddressUtils;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.db.OffLineUser;
import com.example.ai.dtest.data.UserLoginInfo;
import com.example.ai.dtest.view.eye;
import com.example.ai.dtest.view.load;
import com.example.ai.dtest.view.loadDialog;
import com.example.ai.dtest.view.okView;
import com.google.gson.Gson;
import org.litepal.crud.DataSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Login extends BaseActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private Button login;

    private EditText user_phone;

    private EditText user_password;

    private eye eye;

    private CheckBox hold_password;

    private CheckBox hold_autologin;

    private TextView forget_password;

    private TextView return_register;

    private okView ok;

    private LocationClient client;

    private String saveUserPhone=null;

    private String savePassword=null;

    private boolean isFirst= true;

    private loadDialog dialog;

    //Handle处理线程消息
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HttpUtils.LOGINFAILURE:
                    dialog.dismiss();
                    Toast.makeText(Login.this, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.LOGINSUCESS:
                    dialog.dismiss();
                    Bundle bundle= msg.getData();
                    String buf_user= bundle.getString("user");
                    Gson gson= new Gson();
                    UserLoginInfo userlogininfo= gson.fromJson(buf_user,UserLoginInfo.class);
                    saveOfflineUser(userlogininfo,bundle.getString("token"));
                    MyApplication.setUserPhone(userlogininfo.getUserloginphone());
                    MyApplication.setUserName(bundle.getString("username"));
                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                    startPushService();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    //启动时 携带不同的数据
    public static void actionStart(Context context,String user,String password){
        Intent intent= new Intent(context,Login.class);
        intent.putExtra("userName",user);
        intent.putExtra("password",password);
        context.startActivity(intent);
    }

    //开启百度推送服务
    private void startPushService(){
        BasicPushNotificationBuilder cBuilder= new BasicPushNotificationBuilder();
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(R.drawable.notification);
        PushManager.setDefaultNotificationBuilder(getApplicationContext(),cBuilder);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "GnIsxXgAHX6U2NsyMgP91o3n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent= getIntent();
        String name= intent.getStringExtra("userName");
        String password= intent.getStringExtra("password");

        login = (Button) findViewById(R.id.login);
        user_phone = (EditText) findViewById(R.id.name);
        user_password = (EditText) findViewById(R.id.password);
        ok= (okView) findViewById(R.id.okview);
        eye= (eye) findViewById(R.id.eye);
        hold_password = (CheckBox) findViewById(R.id.hold_password);
        hold_autologin = (CheckBox) findViewById(R.id.hold_autoLogin);
        forget_password = (TextView) findViewById(R.id.forget_password);
        return_register = (TextView) findViewById(R.id.return_register);
        user_phone.addTextChangedListener(phoneWather);
        user_password.addTextChangedListener(passwordWatcher);
        login.setOnClickListener(this);
        return_register.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        hold_password.setOnCheckedChangeListener(this);
        hold_autologin.setOnCheckedChangeListener(this);
        if(!TextUtils.isEmpty(name)){
            user_phone.setText(name);
            user_phone.setSelection(name.length());
        }
        if(!TextUtils.isEmpty(password)){
            user_password.setText(password);
            user_password.setSelection(password.length());
        }

        //暴露接口修改 密码是否可以显示
        eye.setListener(new eye.openListener() {
            @Override
            public void openEye() {
                user_password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                user_password.setSelection(user_password.getText().length());
            }
            @Override
            public void closeEye() {
                user_password.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
                user_password.setSelection(user_password.getText().length());
            }
        });
    }

    //号码框 监听
    private TextWatcher phoneWather= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(FormatCheckUtils.isPhoneLegal(editable.toString())){
                if(!isFirst) {
                    ok.start();
                }
                else {
                    isFirst=false;
                }
            }else {
                isFirst=false;
                ok.clear();
            }
        }
    };

    //密码框 监听
    private TextWatcher passwordWatcher= new TextWatcher() {
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


    private void showLoad(){
        if(dialog==null){
            dialog= new loadDialog(this);
            dialog.show();
        }else {
            dialog.show();
        }
    }

    //单选框 监听
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
        ok.clear();
    }

    //尝试登陆
    private void tryLogin() {
        if (TextUtils.isEmpty(user_phone.getText().toString()) || TextUtils.isEmpty(user_password.getText().toString())) {
            Toast.makeText(MyApplication.getContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        showLoad();
        //记录下尝试登陆用户名和密码
        saveUserPhone=user_phone.getText().toString();
        savePassword=user_password.getText().toString();

        //启动百度定位服务
        client= new LocationClient(Login.this);
        client.registerLocationListener(new LocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        client.setLocOption(option);
        client.start();
    }

    //登录成功 存储本地用户数据库
    private void saveOfflineUser(UserLoginInfo userlogininfo, String token) {
        String userPhone = saveUserPhone;
        String userPassword_buf = savePassword;
        String userPassword = Base64.encodeToString(userPassword_buf.getBytes(), Base64.DEFAULT);
        OffLineUser offLineUsers=DataSupport.where("userPhone =?", saveUserPhone).findFirst(OffLineUser.class);
        OffLineUser user = new OffLineUser();
        if (offLineUsers == null) {
            user.setUserPhone(userPhone);
            if (hold_autologin.isChecked()) {
                user.setPassword(userPassword);
                user.setIsAutoLogin(1);
            } else if (hold_password.isChecked()) {
                user.setPassword(userPassword);
                user.setToDefault("isAutoLogin");
            } else {
                user.setToDefault("password");
                user.setToDefault("isAutoLogin");
            }
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.save();
        } else {
            String userPhone_buf = offLineUsers.getUserPhone();
            if (hold_autologin.isChecked()) {
                user.setPassword(userPassword);
                user.setIsAutoLogin(1);
            } else if (hold_password.isChecked()) {
                user.setToDefault("isAutoLogin");
            } else {
                user.setToDefault("password");
                user.setToDefault("isAutoLogin");
            }
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.updateAll("userPhone=?", userPhone_buf);
        }
    }

    //携带地址信息登陆
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
        UserLoginInfo userlogininfo= new UserLoginInfo();
        userlogininfo.setUserloginphone(saveUserPhone);
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

    //无地址信息登陆
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
        UserLoginInfo userlogininfo= new UserLoginInfo();
        userlogininfo.setUserloginphone(saveUserPhone);
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

    //百度定位服务回调接口
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

