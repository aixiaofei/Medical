package com.example.ai.dtest;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.ai.dtest.base.ActivityCollector;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.frag.ConditionShow;
import com.example.ai.dtest.frag.DoctorList;
import com.example.ai.dtest.frag.Map;
import com.example.ai.dtest.frag.OrderShow;
import com.example.ai.dtest.frag.RecommendDoctor;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.ImgUtils;
import com.example.ai.dtest.util.MD5;
import com.example.ai.dtest.util.MacAddressUtils;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.db.OffLineUser;
import com.example.ai.dtest.data.UserLoginInfo;
import com.example.ai.dtest.view.loadDialog;
import com.google.gson.Gson;
import org.litepal.crud.DataSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private String tryAutoLoginPhone=null;

    private LocationClient client;

    private ImageButton homePageFig;

    private TextView homePageText;

    private ImageButton releaseConditionFig;

    private TextView releaseConditionText;

    private ImageButton recommendDoctorFig;

    private TextView recommendDoctorText;

    private ImageButton orderFig;

    private TextView orderText;

    private ImageView mapFig;

    private TextView mapText;

    private int tag=0;

    private static final String MAP="地图模式";

    private static final String LIST="列表模式";

    public static final int RELEASE=100;

    public static final int TOCANDIDATE=101;

    public static final int ADDMENU=102;

    private DoctorList list;

    private ConditionShow conditionShow;

    private RecommendDoctor recommendDoctor;

    private OrderShow orderShow;

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Gson gson=new Gson();
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.LOGINSUCESS:
                    Bundle login_sucess_bundle= msg.getData();
                    String buf2= login_sucess_bundle.getString("user");
                    UserLoginInfo userlogininfo= gson.fromJson(buf2,UserLoginInfo.class);
                    saveOfflineUser(userlogininfo,login_sucess_bundle.getString("token"));
                    MyApplication.setUserPhone(userlogininfo.getUserloginphone());
                    MyApplication.setUserName(login_sucess_bundle.getString("username"));
                    startPushService();
                    break;
                case HttpUtils.LOGINFAILURE:
                    Toast.makeText(MainActivity.this,"请重新登录",Toast.LENGTH_SHORT).show();
                    clearInfo();
                    Login.actionStart(MainActivity.this,tryAutoLoginPhone,null);
                    if(PushManager.isPushEnabled(getApplicationContext())){
                        PushManager.stopWork(getApplicationContext());
                    }
                    break;
                default:
                    break;
            }
        }
    };


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
        setContentView(R.layout.doctor_design);
        Intent intent=getIntent();

        mapFig= (ImageView) findViewById(R.id.map_fig);
        mapText=(TextView) findViewById(R.id.map_text);
        mapFig.setOnClickListener(this);
        mapText.setOnClickListener(this);


        RelativeLayout fragFirst= (RelativeLayout) findViewById(R.id.frag_first);
        fragFirst.setOnClickListener(this);

        homePageFig= (ImageButton) findViewById(R.id.home_page_pic);
        Drawable srcHome = homePageFig.getBackground();
        ImgUtils.tintDrawable(srcHome, getResources().getColorStateList(R.color.button_selector));
        startAnima(homePageFig);
        homePageFig.setOnClickListener(this);

        homePageText= (TextView) findViewById(R.id.home_page_text);
        homePageText.setOnClickListener(this);
        homePageText.setSelected(true);


        RelativeLayout fragSecond= (RelativeLayout) findViewById(R.id.frag_second);
        fragSecond.setOnClickListener(this);

        releaseConditionFig= (ImageButton) findViewById(R.id.search_doctor_page_pic);
        Drawable srcCondition = releaseConditionFig.getBackground();
        ImgUtils.tintDrawable(srcCondition, getResources().getColorStateList(R.color.button_selector));
        releaseConditionFig.setOnClickListener(this);

        releaseConditionText= (TextView) findViewById(R.id.search_doctor_page_text);
        releaseConditionText.setOnClickListener(this);


        RelativeLayout fragThird= (RelativeLayout) findViewById(R.id.frag_third);
        fragThird.setOnClickListener(this);

        recommendDoctorFig= (ImageButton) findViewById(R.id.my_doctor_page_pic);
        Drawable srcRecommend = recommendDoctorFig.getBackground();
        ImgUtils.tintDrawable(srcRecommend, getResources().getColorStateList(R.color.button_selector));
        recommendDoctorFig.setOnClickListener(this);

        recommendDoctorText= (TextView) findViewById(R.id.my_doctor_page_text);
        recommendDoctorText.setOnClickListener(this);


        RelativeLayout fragForth= (RelativeLayout) findViewById(R.id.frag_forth);
        fragForth.setOnClickListener(this);

        orderFig= (ImageButton) findViewById(R.id.search_menu_pic);
        Drawable srcOrder = orderFig.getBackground();
        ImgUtils.tintDrawable(srcOrder, getResources().getColorStateList(R.color.button_selector));
        orderFig.setOnClickListener(this);

        orderText= (TextView) findViewById(R.id.search_menu_text);
        orderText.setOnClickListener(this);


        RelativeLayout myPage= (RelativeLayout) findViewById(R.id.my_page);
        myPage.setOnClickListener(this);

        ImageButton myPageFig= (ImageButton) findViewById(R.id.my_page_pic);
        myPageFig.setOnClickListener(this);

        TextView myPageText= (TextView) findViewById(R.id.my_page_text);
        myPageText.setOnClickListener(this);


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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.map_fig:
                goMap();
                break;
            case R.id.map_text:
                goMap();
                break;

            case R.id.my_page:
                Intent intent= new Intent(MainActivity.this,MyAccount.class);
                startActivity(intent);
                break;
            case R.id.my_page_pic:
                Intent intent1= new Intent(MainActivity.this,MyAccount.class);
                startActivity(intent1);
                break;
            case R.id.my_page_text:
                Intent intent2= new Intent(MainActivity.this,MyAccount.class);
                startActivity(intent2);
                break;

            case R.id.frag_first:
                goHomePage();
                break;
            case R.id.home_page_pic:
                goHomePage();
                break;
            case R.id.home_page_text:
                goHomePage();
                break;

            case R.id.frag_second:
                goRelease();
                break;
            case R.id.search_doctor_page_pic:
                goRelease();
                break;
            case R.id.search_doctor_page_text:
                goRelease();
                break;

            case R.id.frag_third:
                goRecommend();
                break;
            case R.id.my_doctor_page_pic:
                goRecommend();
                break;
            case R.id.my_doctor_page_text:
                goRecommend();
                break;

            case R.id.frag_forth:
                goOrder();
                break;
            case R.id.search_menu_pic:
                goOrder();
                break;
            case R.id.search_menu_text:
                goOrder();
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
        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this,R.style.myDialog);
        builder.setMessage("请授予软件所有权限，这些权限都是必须的")
                .setTitle("警示")
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
        if(TextUtils.isEmpty(MyApplication.getUserPhone())){
            OffLineUser offLineUsers = DataSupport.where("isAutoLogin =?","1").findFirst(OffLineUser.class);
            if (offLineUsers!=null){
                client= new LocationClient(MainActivity.this);
                tryAutoLoginPhone= offLineUsers.getUserPhone();
                client.registerLocationListener(new MainActivity.LocationListener());
                LocationClientOption option = new LocationClientOption();
                option.setIsNeedAddress(true);
                client.setLocOption(option);
                client.start();
            }
        }
        list= new DoctorList();
        addFragment(list);
    }

    public void addFragment(Fragment fragment){
        FragmentManager manager= getSupportFragmentManager();
        FragmentTransaction transaction= manager.beginTransaction();
        Fragment target= manager.findFragmentByTag(fragment.getClass().getName());
        if(target!=null){
            for(Fragment other:manager.getFragments()){
                if(other!=target){
                    transaction.hide(other);
                }
            }
            transaction.show(target);
        }else {
            for(Fragment other:manager.getFragments()){
                transaction.hide(other);
            }
            transaction.add(R.id.main,fragment,fragment.getClass().getName());
            transaction.show(fragment);
        }
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
        OffLineUser offLineUsers = DataSupport.where("userPhone =?",tryAutoLoginPhone).findFirst(OffLineUser.class);
        String password_buf= new String(Base64.decode(offLineUsers.getPassword().getBytes(),Base64.DEFAULT));
        String password= null;
        try {
            password = MD5.get_Md5(password_buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        UserLoginInfo userlogininfo= new UserLoginInfo();
        userlogininfo.setUserloginphone(tryAutoLoginPhone);
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
        OffLineUser offLineUsers = DataSupport.where("userPhone =?",tryAutoLoginPhone).findFirst(OffLineUser.class);
        String password_buf= new String(Base64.decode(offLineUsers.getPassword().getBytes(),Base64.DEFAULT));
        String password= null;
        try {
            password = MD5.get_Md5(password_buf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(buf_date);
        String loginDevice= "手机端";
        String userphonemodel= android.os.Build.MODEL;
        String userphoneversion= android.os.Build.VERSION.RELEASE;
        String usermac= MacAddressUtils.getMacAddress(MainActivity.this);
        String userip= MacAddressUtils.getIpAddress(MainActivity.this);
        UserLoginInfo userlogininfo= new UserLoginInfo();
        userlogininfo.setUserloginphone(tryAutoLoginPhone);
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

    private void saveOfflineUser(UserLoginInfo userlogininfo, String token) {
            OffLineUser user= new OffLineUser();
            user.setToken(token);
            user.setLastLoginTime(userlogininfo.getUserlogintime());
            user.updateAll("userPhone=?", userlogininfo.getUserloginphone());
    }

    private void clearInfo(){
        OffLineUser user= new OffLineUser();
        user.setToDefault("Token");
        user.setToDefault("isAutoLogin");
        user.setToDefault("password");
        user.setToDefault("lastLoginTime");
        user.updateAll("userPhone=?", tryAutoLoginPhone);
    }

    private void goHomePage(){
        if(tag==1){
            mapFig.setVisibility(View.VISIBLE);
            mapText.setText(MAP);
        }else if(tag==2){
            stopAnima(releaseConditionFig);
            releaseConditionText.setSelected(false);
        } else if(tag==3){
            stopAnima(recommendDoctorFig);
            recommendDoctorText.setSelected(false);
        }else if(tag==4){
            stopAnima(orderFig);
            orderText.setSelected(false);
        }

        startAnima(homePageFig);
        homePageText.setSelected(true);
        addFragment(list);
        tag=0;
    }

    private void goMap(){
        if(mapText.getText().toString().equals(MAP)) {
            if(tag==0) {
                stopAnima(homePageFig);
                homePageText.setSelected(false);
            }else if(tag==2){
                stopAnima(releaseConditionFig);
                releaseConditionText.setSelected(false);
            } else if(tag==3){
                stopAnima(recommendDoctorFig);
                recommendDoctorText.setSelected(false);
            }else if(tag==4){
                stopAnima(orderFig);
                orderText.setSelected(false);
            }

            mapFig.setVisibility(View.INVISIBLE);
            mapText.setText(LIST);
            addFragment(new Map());
            tag=1;
        }else {
            mapFig.setVisibility(View.VISIBLE);
            mapText.setText(MAP);
            goHomePage();
        }
    }

    private void goRelease(){
        if(tag==0){
            stopAnima(homePageFig);
            homePageText.setSelected(false);
        }else if(tag==1){
            mapFig.setVisibility(View.VISIBLE);
            mapText.setText(MAP);
        } else if(tag==3){
            stopAnima(recommendDoctorFig);
            recommendDoctorText.setSelected(false);
        }else if(tag==4){
            stopAnima(orderFig);
            orderText.setSelected(false);
        }

        startAnima(releaseConditionFig);
        releaseConditionText.setSelected(true);
        if(conditionShow==null) {
            conditionShow = new ConditionShow();
        }
        addFragment(conditionShow);
        tag=2;
    }

    private void goRecommend(){
        if(tag==0){
            stopAnima(homePageFig);
            homePageText.setSelected(false);
        }else if(tag==1) {
            mapFig.setVisibility(View.VISIBLE);
            mapText.setText(MAP);
        }else if(tag==2){
            stopAnima(releaseConditionFig);
            releaseConditionText.setSelected(false);
        }else if(tag==4){
            stopAnima(orderFig);
            orderText.setSelected(false);
        }

        startAnima(recommendDoctorFig);
        recommendDoctorText.setSelected(true);

        if(recommendDoctor==null){
            recommendDoctor= new RecommendDoctor();
        }
        addFragment(recommendDoctor);
        tag=3;
    }

    public void goOrder(){
        if(tag==0){
            stopAnima(homePageFig);
            homePageText.setSelected(false);
        }else if(tag==1) {
            mapFig.setVisibility(View.VISIBLE);
            mapText.setText(MAP);
        }else if(tag==2){
            stopAnima(releaseConditionFig);
            releaseConditionText.setSelected(false);
        }else if(tag==3) {
            stopAnima(recommendDoctorFig);
            recommendDoctorText.setSelected(false);
        }

        startAnima(orderFig);
        orderText.setSelected(true);

        if(orderShow==null){
            orderShow= new OrderShow();
        }
        addFragment(orderShow);
        tag=4;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RELEASE:
                if(resultCode==RESULT_OK){
                    conditionShow.reflesh();
                }
                break;
            case TOCANDIDATE:
                if(resultCode==RESULT_OK){
                    if(recommendDoctor!=null){
                        recommendDoctor.reflesh();
                    }
                }
                break;
            case ADDMENU:
                if(resultCode==RESULT_OK){
                    if(orderShow!=null){
                        orderShow.refleshOrder();
                    }
                    if(conditionShow!=null){
                        conditionShow.reflesh();
                    }
                }
            default:
                break;
        }
    }

    private void startAnima(View view){
        AnimatorSet set= new AnimatorSet();
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(view,"scaleX",1f,0.6f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"scaleY",1f,0.6f,1f);
        view.setSelected(true);
        set.setDuration(400);
        set.playTogether(scaleX,scaleY);
        set.start();
    }

    private void stopAnima(View view){
        AnimatorSet set= new AnimatorSet();
        ObjectAnimator scaleX= ObjectAnimator.ofFloat(view,"scaleX",1f,0.6f,1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view,"scaleY",1f,0.6f,1f);
        view.setSelected(false);
        set.setDuration(400);
        set.playTogether(scaleX,scaleY);
        set.start();
    }
}
