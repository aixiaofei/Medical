package com.example.ai.dtest;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.example.ai.dtest.util.FormatCheckUtils;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.ImgUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static com.example.ai.dtest.util.ImgUtils.cameraIdentify;
import static com.example.ai.dtest.util.ImgUtils.identifyPath;

public class InformationDesign extends BaseActivity implements View.OnClickListener{

    private EditText userName;

    private EditText identify;

    private Spinner sex;

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

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_design);
        Intent intent= getIntent();
        UerInfo info= (UerInfo) intent.getSerializableExtra("info");
        userName= (EditText) findViewById(R.id.enter_name);
        identify= (EditText) findViewById(R.id.enter_identify);
        sex= (Spinner) findViewById(R.id.select_sex);
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
        addIdentify.setOnClickListener(this);
        selectAddress.setOnClickListener(this);
        nextStep.setOnClickListener(this);
        if(info!=null) {
            if (!TextUtils.isEmpty(info.getUsername())) {
                userName.setText(info.getUsername());
            }
            if (!TextUtils.isEmpty(info.getUsercardnum())) {
                identify.setText(info.getUsercardnum());
            }
            if (!TextUtils.isEmpty(info.getUsermale())) {
                if (info.getUsermale().equals("男")) {
                    sex.setSelection(0);
                } else {
                    sex.setSelection(1);
                }
            }
            if (!TextUtils.isEmpty(info.getUseradr())) {
                String top = info.getUseradr().split("\\|")[0];
                String down = info.getUseradr().split("\\|")[1];
                addressTop.setText(top);
                addressDown.setText(down);
            }
        }
    }

    public static void actionStart(Context context, UerInfo userinfo){
        Intent intent= new Intent(context,InformationDesign.class);
        intent.putExtra("info",userinfo);
        context.startActivity(intent);
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
            case R.id.select_address:
                showCityPicker();
                break;
            case R.id.next_step:
                canNextStep();
                break;
            default:
                break;
        }
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
//        Log.d("ai",age);
        info.setUserage(age);
        info.setUsermale(sex.getSelectedItem().toString());
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




    private void showCityPicker(){
        CityPicker cityPicker = new CityPicker.Builder(InformationDesign.this)
                .textSize(15)
                .title("地址选择")
                .titleBackgroundColor("#6393FF")
                .titleTextColor("#000000")
                .backgroundPop(0x00ffffff)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("安徽省")
                .city("合肥市")
                .district("蜀山区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                String province = citySelected[0];
                String city = citySelected[1];
                String district = citySelected[2];
                addressTop.setText(province+"-"+city+"-"+district);
            }

            @Override
            public void onCancel() {
                Toast.makeText(InformationDesign.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
}
