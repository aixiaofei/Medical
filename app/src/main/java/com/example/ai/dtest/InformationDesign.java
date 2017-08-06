package com.example.ai.dtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.UerInfo;
import com.example.ai.dtest.db.City;
import com.example.ai.dtest.db.Country;
import com.example.ai.dtest.db.Province;
import com.example.ai.dtest.util.FormatCheckUtils;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.ImgUtils;
import com.example.ai.dtest.view.loadDialog;
import com.example.ai.dtest.view.locationDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.ai.dtest.util.ImgUtils.cameraIdentify;
import static com.example.ai.dtest.util.ImgUtils.identifyPath;

public class InformationDesign extends BaseActivity implements View.OnClickListener{

    private EditText userName;

    private EditText identify;

    private TextView sex;

    private TextView addressTop;

    private EditText addressDown;

    private ImageView identifyTop;

    private ImageView identifyDown;

    private ImageView addIdentify;

    private List<String> identifyFig= new ArrayList<>();

    private int figNumber=0;

    private static final int firstFigCompleted=1;

    private static final int secondFigCompleted=2;

    private List<Bitmap> fig;

    private ProgressDialog dialog;

    private locationDialog lDialog;

    private boolean isEnd= false;

    private UerInfo info;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.GETUSERINFOFAILURE:
                    break;
                case HttpUtils.GETUSERINFOSUCESS:
                    Bundle bundle= msg.getData();
                    info = (UerInfo) bundle.getSerializable("info");
                    setInfo(info);
                    break;
                case HttpUtils.PUSHINFOFAILURE:
                    dialog.cancel();
                    Toast.makeText(InformationDesign.this, "上传失败", Toast.LENGTH_LONG).show();
                    break;
                case HttpUtils.PUSHINFOSUCESS:
                    dialog.cancel();
                    Toast.makeText(InformationDesign.this, "上传成功", Toast.LENGTH_LONG).show();
                    for(Bitmap bitmap:fig){
                        ImgUtils.recycleBitmap(bitmap);
                    }
                    File top= new File(identifyPath,MyApplication.getUserPhone() + "top.png");
                    if(top.exists()){
                        top.delete();
                    }
                    File down= new File(identifyPath,MyApplication.getUserPhone() + "down.png");
                    if(down.exists()){
                        down.delete();
                    }
                    Intent intent= new Intent(InformationDesign.this,InformationShow.class);
                    startActivity(intent);
                    finish();
                    break;
                case firstFigCompleted:
                    identifyFig.add(identifyPath + File.separator + MyApplication.getUserPhone() + "top.png");
                    break;
                case secondFigCompleted:
                    identifyFig.add(identifyPath + File.separator + MyApplication.getUserPhone() + "down.png");
                    break;
                case 100:
                    isEnd=true;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_design);
        userName= (EditText) findViewById(R.id.enter_name);
        identify= (EditText) findViewById(R.id.enter_identify);
        sex= (TextView) findViewById(R.id.select_sex);
        addressTop= (TextView) findViewById(R.id.address_top);
        addressDown= (EditText) findViewById(R.id.address_down);
        addIdentify= (ImageView) findViewById(R.id.add_identify);
        identifyTop= (ImageView) findViewById(R.id.identify_top);
        identifyDown= (ImageView) findViewById(R.id.identify_down);
        ImageView backFig= (ImageView) findViewById(R.id.design_back_fig);
        TextView backText= (TextView) findViewById(R.id.design_back_text);
        ImageView selectAddress= (ImageView) findViewById(R.id.select_address);
        Button nextStep= (Button) findViewById(R.id.next_step);
        backFig.setOnClickListener(this);
        backText.setOnClickListener(this);
        sex.setOnClickListener(this);
        addIdentify.setOnClickListener(this);
        addressTop.setOnClickListener(this);
        selectAddress.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        initData();
        HttpUtils.getUserInfo(MyApplication.getUserPhone(),handler);
    }

    private void setInfo(UerInfo info){
        if(!TextUtils.isEmpty(info.getUsername())){
            userName.setText(info.getUsername());
        }
        if(!TextUtils.isEmpty(info.getUsercardnum())){
            identify.setText(info.getUsercardnum());
        }
        if(!TextUtils.isEmpty(info.getUsermale())){
            sex.setText(info.getUsermale());
        }
        if(!TextUtils.isEmpty(info.getUseradr())){
            String[] test= info.getUseradr().split("\\|");
            addressTop.setText(test[0]);
            addressDown.setText(test[1]);
        }
    }

    private void initData() {
        final List<Province> provinces = DataSupport.findAll(Province.class);
        final List<City> cities = DataSupport.findAll(City.class);
        final List<Country> countries = DataSupport.findAll(Country.class);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(provinces.isEmpty()) {
                    handleProvince();
                }
                if(cities.isEmpty()) {
                    handleCity();
                }
                if(countries.isEmpty()) {
                    handleCountry();
                }
                Message message = new Message();
                message.what= 100;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.design_back_fig:
                finish();
                break;
            case R.id.design_back_text:
                finish();
                break;
            case R.id.add_identify:
                captureIdentify();
                break;
            case R.id.address_top:
                if(!isEnd){
                    Toast.makeText(this,"数据正在初始化...",Toast.LENGTH_SHORT).show();
                }else {
                    showLocationDialog();
                }
                break;
            case R.id.select_address:
                if(!isEnd){
                    Toast.makeText(this,"数据正在初始化...",Toast.LENGTH_SHORT).show();
                }else {
                    showLocationDialog();
                }
                break;
            case R.id.next_step:
                canNextStep();
                break;
            case R.id.select_sex:
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showLocationDialog(){
        if(lDialog==null){
            lDialog= new locationDialog(this);
        }
        lDialog.setListener(new locationDialog.selectLocationListener() {
            @Override
            public void complete(String location) {
                lDialog.dismiss();
                addressTop.setText(location);
            }
        });
        lDialog.show();
    }

    private void showDialog(){
        String[] items={"男","女"};
        AlertDialog dialog = new AlertDialog.Builder(this,R.style.myDialog).setTitle("选择性别")
                .setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            sex.setText("男");
                        }else {
                            sex.setText("女");
                        }
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void captureIdentify(){
        ImgUtils.getImageFromCamera(this,cameraIdentify);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ImgUtils.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24){
                        isOnN();
                    }
                    else {
                        isOffN();
                    }
                }
        }
    }

    private void isOnN(){
        figNumber+=1;
        if(figNumber==2){
            addIdentify.setClickable(false);
        }
        Uri imageUri= FileProvider.getUriForFile(this,"com.example.ai.dtest.fileprovider",new File(cameraIdentify,"camera.png"));
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            if(fig==null){
                fig=new ArrayList<>();
            }
            fig.add(bitmap);
            if (figNumber == 1) {
                identifyTop.setImageBitmap(bitmap);
                final String fileName = MyApplication.getUserPhone() + "top";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
                        Message message= new Message();
                        message.what= firstFigCompleted;
                        handler.sendMessage(message);
                    }
                }).start();
            } else {
                identifyDown.setImageBitmap(bitmap);
                final String fileName = MyApplication.getUserPhone() + "down";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
                        Message message= new Message();
                        message.what= secondFigCompleted;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void isOffN(){
        figNumber+=1;
        if(figNumber==2){
            addIdentify.setClickable(false);
        }
        Uri imageUri= Uri.fromFile(new File(cameraIdentify,"camera.png"));
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            if(fig==null){
                fig=new ArrayList<>();
            }
            fig.add(bitmap);
            if (figNumber == 1) {
                identifyTop.setImageBitmap(bitmap);
                final String fileName = MyApplication.getUserPhone() + "top";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
                        Message message= new Message();
                        message.what= firstFigCompleted;
                        handler.sendMessage(message);
                    }
                }).start();
            } else {
                identifyDown.setImageBitmap(bitmap);
                final String fileName = MyApplication.getUserPhone() + "down";
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
                        Message message= new Message();
                        message.what= secondFigCompleted;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void canNextStep(){
        if(TextUtils.isEmpty(userName.getText().toString())||TextUtils.isEmpty(identify.getText().toString())){
            Toast.makeText(InformationDesign.this, "姓名或身份证号码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(addressTop.getText().toString())||TextUtils.isEmpty(addressDown.getText().toString())){
            Toast.makeText(InformationDesign.this, "地址不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if(!FormatCheckUtils.isLegalName(userName.getText().toString())||!FormatCheckUtils.isIdentify(identify.getText().toString())){
            Toast.makeText(InformationDesign.this, "姓名或身份证不合法", Toast.LENGTH_LONG).show();
            return;
        }
        if(identifyFig.size()<2){
            Toast.makeText(InformationDesign.this, "请上传身份证照片", Toast.LENGTH_LONG).show();
            return;
        }
        UerInfo info= new UerInfo();
        info.setUsername(userName.getText().toString());
        info.setUsercardnum(identify.getText().toString());
        String age= FormatCheckUtils.returnAge(identify.getText().toString());
        info.setUserage(age);
        info.setUsermale(sex.getText().toString());
        String address= addressTop.getText().toString()+"|"+addressDown.getText().toString();
        info.setUseradr(address);
        info.setUserphone(MyApplication.getUserPhone());
        HttpUtils.pushUserInfo(info,identifyFig,handler);
        dialog= new ProgressDialog(this,R.style.myDialog);
        dialog.setMessage("上传中...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
    }


    private void handleProvince(){
        try {
            InputStreamReader is = new InputStreamReader(MyApplication.getContext().getResources().openRawResource(R.raw.provinces));
            BufferedReader reader = new BufferedReader(is);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            is.close();
            reader.close();
            String res= stringBuilder.toString();
            Gson gson= new Gson();
            List<Province> data= gson.fromJson(res,new TypeToken<List<Province>>(){}.getType());
            for (Province province : data) {
                province.save();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleCity(){
        try {
            InputStreamReader is = new InputStreamReader(MyApplication.getContext().getResources().openRawResource(R.raw.cities));
            BufferedReader reader = new BufferedReader(is);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            is.close();
            reader.close();
            String res= stringBuilder.toString();
            Gson gson= new Gson();
            List<City> data= gson.fromJson(res,new TypeToken<List<City>>(){}.getType());
            for(City city:data){
                city.save();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void handleCountry(){
        try {
            InputStreamReader is = new InputStreamReader(MyApplication.getContext().getResources().openRawResource(R.raw.areas));
            BufferedReader reader = new BufferedReader(is);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            is.close();
            reader.close();
            String res= stringBuilder.toString();
            Gson gson= new Gson();
            List<Country> data= gson.fromJson(res,new TypeToken<List<Country>>(){}.getType());
            for(Country country:data){
                country.save();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
