package com.example.ai.dtest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import commen.ActivityCollector;
import commen.DoctorCustom;
import commen.HttpUtils;
import commen.MD5;
import commen.MacAddressUtils;
import commen.MyApplication;
import commen.OffLineUser;;
import commen.Userlogininfo;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private String tryAutoLoginUser=null;

    private LocationClient client;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.LOGINSUCESS:
                    Bundle login_sucess_bundle= msg.getData();
                    String buf2= login_sucess_bundle.getString("user");
                    Userlogininfo userlogininfo= gson.fromJson(buf2,Userlogininfo.class);
                    saveOfflineUser(userlogininfo,login_sucess_bundle.getString("token"));
                    MyApplication.setUserName(userlogininfo.getUserloginname());
                    break;
                case HttpUtils.LOGINFAILURE:
                    Toast.makeText(MainActivity.this,"请重新登录",Toast.LENGTH_SHORT).show();
                    clearInfo();
                    Login.actionStart(MainActivity.this,tryAutoLoginUser,null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_design);
        ImageView map_fig = (ImageView) findViewById(R.id.map);
        TextView map_text = (TextView) findViewById(R.id.top_first_address);
        Button myPage= (Button) findViewById(R.id.my_page_pic);
        myPage.setOnClickListener(this);
        map_fig.setOnClickListener(this);
        map_text.setOnClickListener(this);
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions= permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions,1);
        }
        else {
            init();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCollector.finishAll();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.map:
                Intent intent1= new Intent(MainActivity.this,Map.class);
                startActivity(intent1);
                break;
            case R.id.top_first_address:
                Intent intent2= new Intent(MainActivity.this,Map.class);
                startActivity(intent2);
                break;
            case R.id.my_page_pic:
                Intent intent= new Intent(MainActivity.this,MyInformation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int i:grantResults){
                        if(i!=PackageManager.PERMISSION_GRANTED){
                            permissionAlert(permissions);
                        }
                    }
                    init();
                }
                break;
            default:
                break;
        }
    }

    private void permissionAlert(final String[] permissions){
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("请授予软件所有权限，这些权限都是必须的")
                .setTitle("警示")
                .setIcon(R.drawable.alert)
                .setCancelable(false)
                .setPositiveButton("继续授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, permissions,1);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        finish();
                    }
                });
        builder.create().show();
    }

    private void init(){
        if(MyApplication.getUserName()==null){
            OffLineUser offLineUsers = DataSupport.where("isAutoLogin =?","1").findFirst(OffLineUser.class);
            if (offLineUsers!=null){
                client= new LocationClient(MainActivity.this);
                tryAutoLoginUser= offLineUsers.getUserName();
                client.registerLocationListener(new MainActivity.LocationListener());
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                client.setLocOption(option);
                client.start();
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager= getSupportFragmentManager();
        FragmentTransaction transaction= manager.beginTransaction();
        transaction.replace(R.id.main,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    private void isLocationLogin(String location,String latitude_longitude){
        OffLineUser offLineUsers = DataSupport.where("userName =?",tryAutoLoginUser).findFirst(OffLineUser.class);
        String password_buf= new String(Base64.decode(offLineUsers.getPassWord().getBytes(),Base64.DEFAULT));
        String password= null;
        try {
            password = MD5.get_Md5(password_buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        Userlogininfo userlogininfo= new Userlogininfo();
        userlogininfo.setUserloginname(tryAutoLoginUser);
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
        userlogininfo.setUserlogintoken(offLineUsers.getToken());
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    private void noLocationLogin(){
        OffLineUser offLineUsers = DataSupport.where("userName =?",tryAutoLoginUser).findFirst(OffLineUser.class);
        String password_buf= new String(Base64.decode(offLineUsers.getPassWord().getBytes(),Base64.DEFAULT));
        String password= null;
        try {
            password = MD5.get_Md5(password_buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        Userlogininfo userlogininfo= new Userlogininfo();
        userlogininfo.setUserloginname(tryAutoLoginUser);
        userlogininfo.setUserloginpwd(password);
        userlogininfo.setUserlogintime(date);
        userlogininfo.setUserlogindev(loginDevice);
        userlogininfo.setUserloginmodel(userphonemodel);
        userlogininfo.setUserloginpver(userphoneversion);
        userlogininfo.setUserloginmac(usermac);
        userlogininfo.setUserloginip(userip);
        userlogininfo.setUserlogintype(0);
        userlogininfo.setUserlogintoken(offLineUsers.getToken());
        Gson gson= new Gson();
        String user= gson.toJson(userlogininfo);
        HttpUtils.login(user,handler);
    }

    private void saveOfflineUser(Userlogininfo userlogininfo,String token) {
            OffLineUser user= new OffLineUser();
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.updateAll("userName=?", userlogininfo.getUserloginname());
    }

    private void clearInfo(){
        OffLineUser user= new OffLineUser();
        user.setToDefault("Token");
        user.setToDefault("isAutoLogin");
        user.setToDefault("passWord");
        user.setToDefault("lastLoginTime");
        user.updateAll("userName=?", tryAutoLoginUser);
    }
}
