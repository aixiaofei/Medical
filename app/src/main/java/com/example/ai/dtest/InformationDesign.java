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

    private ImageView[] imageViews=new ImageView[2];

    private ImageView addIdentify;

    private String[] identifyFig= new String[2];

    private int figNumber=0;

    private Bitmap[] fig= new Bitmap[2];

    private ProgressDialog dialog;

    private locationDialog lDialog;

    private int currentFig;

    private static final String[] paths={"top","down"};

    private static final int[] signal={1,2};

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.GETUSERINFOFAILURE:
                    break;
                case HttpUtils.GETUSERINFOSUCESS:
                    Bundle bundle= msg.getData();
                    UerInfo info = (UerInfo) bundle.getSerializable("info");
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
                    for(String name:paths){
                        File file= new File(identifyPath,MyApplication.getUserPhone() + name+".png");
                        if(file.exists()){
                            file.delete();
                        }
                    }
                    File file= new File(cameraIdentify,"camera.png");
                    if(file.exists()){
                        file.delete();
                    }
                    Intent intent= new Intent(InformationDesign.this,InformationShow.class);
                    startActivity(intent);
                    finish();
                    break;
                case 1:
                    identifyFig[0]=(identifyPath + File.separator + MyApplication.getUserPhone() + paths[0]+".png");
                    break;
                case 2:
                    identifyFig[1]=(identifyPath + File.separator + MyApplication.getUserPhone() + paths[1]+".png");
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

        imageViews[0]=(ImageView) findViewById(R.id.identify_top);
        imageViews[1]= (ImageView) findViewById(R.id.identify_down);
        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);

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
                showLocationDialog();
                break;
            case R.id.select_address:
                showLocationDialog();
                break;
            case R.id.next_step:
                canNextStep();
                break;
            case R.id.select_sex:
                showDialog();
                break;
            case R.id.identify_top:
                showDeleteDialog(1);
                break;
            case R.id.identify_down:
                showDeleteDialog(2);
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

    private void showDeleteDialog(final int position) {
        if(imageViews[position-1].getDrawable()!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myDialog);
            builder.setMessage("确认删除此照片!")
                    .setTitle("提示")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            ImgUtils.recycleBitmap(fig[position-1]);
                            fig[position-1]=null;
                            imageViews[position-1].setImageDrawable(null);
                            identifyFig[position-1]=null;
                            figNumber-=1;
                            if(figNumber<2){
                                addIdentify.setClickable(true);
                            }
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.create().show();
        }
    }

    private void captureIdentify(){
        ImgUtils.getImageFromCamera(this,cameraIdentify);
        for(int i=0;i<imageViews.length;i++){
            if(imageViews[i].getDrawable()==null){
                currentFig=i+1;
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ImgUtils.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24){
                        Uri imageUri= FileProvider.getUriForFile(this,"com.example.ai.dtest.fileprovider",new File(cameraIdentify,"camera.png"));
                        processFig(imageUri);
                    }
                    else {
                        Uri imageUri= Uri.fromFile(new File(cameraIdentify,"camera.png"));
                        processFig(imageUri);
                    }
                }
        }
    }

    private void processFig(Uri imageUri){
        figNumber+=1;
        if(figNumber==2){
            addIdentify.setClickable(false);
        }
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            fig[currentFig-1]=bitmap;
            imageViews[currentFig-1].setImageBitmap(bitmap);
            final String fileName = MyApplication.getUserPhone()+paths[currentFig-1];
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ImgUtils.saveImageToGallery(fileName,identifyPath, bitmap);
                    Message message= new Message();
                    message.what= signal[currentFig-1];
                    handler.sendMessage(message);
                }
            }).start();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


//    private void isOnN(){
//        figNumber+=1;
//        if(figNumber==2){
//            addIdentify.setClickable(false);
//        }
//        Uri imageUri= FileProvider.getUriForFile(this,"com.example.ai.dtest.fileprovider",new File(cameraIdentify,"camera.png"));
//        try {
//            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//            if(fig==null){
//                fig=new ArrayList<>();
//            }
//            fig.add(bitmap);
//            if (figNumber == 1) {
//                identifyTop.setImageBitmap(bitmap);
//                final String fileName = MyApplication.getUserPhone() + "top";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
//                        Message message= new Message();
//                        message.what= firstFigCompleted;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            } else {
//                identifyDown.setImageBitmap(bitmap);
//                final String fileName = MyApplication.getUserPhone() + "down";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
//                        Message message= new Message();
//                        message.what= secondFigCompleted;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            }
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void isOffN(){
//        figNumber+=1;
//        if(figNumber==2){
//            addIdentify.setClickable(false);
//        }
//        Uri imageUri= Uri.fromFile(new File(cameraIdentify,"camera.png"));
//        try {
//            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//            if(fig==null){
//                fig=new ArrayList<>();
//            }
//            fig.add(bitmap);
//            if (figNumber == 1) {
//                identifyTop.setImageBitmap(bitmap);
//                final String fileName = MyApplication.getUserPhone() + "top";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
//                        Message message= new Message();
//                        message.what= firstFigCompleted;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            } else {
//                identifyDown.setImageBitmap(bitmap);
//                final String fileName = MyApplication.getUserPhone() + "down";
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        ImgUtils.saveImageToGallery(fileName, identifyPath, bitmap);
//                        Message message= new Message();
//                        message.what= secondFigCompleted;
//                        handler.sendMessage(message);
//                    }
//                }).start();
//            }
//        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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
        boolean can= true;
        for(String path:identifyFig){
            if(TextUtils.isEmpty(path)){
                can=false;
                break;
            }
        }
        if(!can){
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
}
