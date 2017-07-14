package com.example.ai.dtest;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import Adapter.DocterAdapter;
import commen.HttpUtils;
import commen.MD5;
import commen.MacAddressUtils;
import commen.MyApplication;
import commen.OffLineUser;
import commen.TDoctorCustom;
import commen.TDoctorinfo;
import commen.TUserlogininfo;

public class MainActivity extends BaseActivity {

    private String tryAutoLoginUser=null;

    private RecyclerView recyclerView;

    private updateService.DownBinder binder;

    private List<TDoctorCustom> mList;

    private DocterAdapter adapter;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            super.handleMessage(msg);
            switch (msg.what){
                case updateService.UPDATEFAILURE:
                    Toast.makeText(MainActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
                    break;
                case updateService.UPDATESUCCESS:
                    Bundle update_bundle= msg.getData();
                    String buf= update_bundle.getString("result");
                    List<TDoctorCustom> doctor= gson.fromJson(buf, new TypeToken<List<TDoctorCustom>>(){}.getType());
                    for(TDoctorCustom i:doctor){
                        Log.d("ai", i.getDocname());
                        Log.d("ai",i.getDocage().toString());
                        Log.d("ai",i.getHospitaladdress());
                    }
                    deepclone(doctor);
                    adapter.notifyDataSetChanged();
                    break;
                case HttpUtils.LOGINSUCESS:
                    Bundle login_bundle= msg.getData();
                    String buf_user= login_bundle.getString("user");
                    TUserlogininfo userlogininfo= gson.fromJson(buf_user,TUserlogininfo.class);
                    saveOfflineUser(userlogininfo,login_bundle.getString("token"));
                    MyApplication.setUserName(userlogininfo.getUserloginusername());
                    break;
                default:
                    break;
            }
        }
    };

    private ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("ai","binder1");
            binder= (updateService.DownBinder) iBinder;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent= new Intent(MainActivity.this,updateService.class);
//        startService(intent);
//        bindService(intent,connection,BIND_AUTO_CREATE);
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        List<String> permissionList = new ArrayList<>();
        mList= new ArrayList<>();
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
//        Log.d("ai","start");
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.d("ai","resume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int i:grantResults){
                        if(i!=PackageManager.PERMISSION_GRANTED){
                            AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("请授予软件所有权限，这些权限都是必须的")
                                    .setTitle("警示")
                                    .setIcon(R.drawable.alert)
                                    .setPositiveButton("继续授权", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            ActivityCompat.requestPermissions(MainActivity.this, permissions,1);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            finish();
                                        }
                                    });
                            builder.create().show();
                        }
                    }
                    init();
                }
                break;
            default:
                break;
        }
    }

    private void init(){
        if(MyApplication.getUserName()==null){
            OffLineUser offLineUsers = DataSupport.where("isAutoLogin =?","true").findFirst(OffLineUser.class);
            if (offLineUsers!=null){
                tryAutoLoginUser= offLineUsers.getUserName();
                MyApplication.getClient().registerLocationListener(new MainActivity.LocationListener());
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                MyApplication.getClient().setLocOption(option);
                MyApplication.getClient().start();
            }
        }
        adapter= new DocterAdapter(mList);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        HttpUtils.docterUpdataDefault(handler);
//        binder.docterUpdataDefault("default",handler);
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

    private void isLocationLogin(String location,String latitude_longitude) throws ParseException {
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
        String date_1 = sdf.format(buf_date);
        Date date = sdf.parse(date_1);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUserloginusername(tryAutoLoginUser);
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
        String date_1 = sdf.format(buf_date);
        Date date = sdf.parse(date_1);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        TUserlogininfo userlogininfo= new TUserlogininfo();
        userlogininfo.setUserloginusername(tryAutoLoginUser);
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

    private void saveOfflineUser(TUserlogininfo userlogininfo,String token) {
            OffLineUser user= new OffLineUser();
            user.setToken(token);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String curTime = sdf.format(userlogininfo.getUserloginlastlogintime());
            user.setLastLoginTime(curTime);
            user.updateAll("userName=?", userlogininfo.getUserloginusername());
    }

    private void deepclone(List<TDoctorCustom> origin){
        for(TDoctorCustom i:origin){
            try {
                mList.add((TDoctorCustom) i.deepCopy());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
