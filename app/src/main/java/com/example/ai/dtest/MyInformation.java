package com.example.ai.dtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ai.dtest.commen.HttpUtils;
import com.example.ai.dtest.commen.ImgUtils;
import com.example.ai.dtest.commen.MyApplication;
import com.example.ai.dtest.commen.getImagePath;
import java.io.File;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.example.ai.dtest.commen.ImgUtils.mCameraFile;
import static com.example.ai.dtest.commen.ImgUtils.mCropFile;

public class MyInformation extends BaseActivity implements View.OnClickListener{

    private TextView userName;

    private CircleImageView userImage;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HttpUtils.PUSHSUCESS:
                    Log.d("ai","22");
                    Toast.makeText(MyInformation.this,"修改头像成功",Toast.LENGTH_SHORT).show();
                    final Bitmap bitmapBuf1 = BitmapFactory.decodeFile(mCropFile);
                    userImage.setImageBitmap(bitmapBuf1);
                    Bitmap bitmap= MyApplication.getBitmap();
                    recycleBitmap(bitmap);
                    MyApplication.setBitmap(bitmapBuf1);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ImgUtils.saveImageToGallery(MyApplication.getUserPhone(),bitmapBuf1);
                        }
                    }).start();
                    break;
                case HttpUtils.PUSHFAILURE:
                    Toast.makeText(MyInformation.this,"上传失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PUSHNOFILE:
                    Toast.makeText(MyInformation.this,"选择图片失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLFAILURE:
                    final Bitmap bitmapDefault=BitmapFactory.decodeResource(getResources(),R.drawable.defaultuserimage);
                    MyApplication.setBitmap(bitmapDefault);
                    userImage.setImageBitmap(bitmapDefault);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ImgUtils.saveImageToGallery(MyApplication.getUserPhone(),bitmapDefault);
                        }
                    }).start();
                    break;
                case HttpUtils.PULLSUCESS:
                    Bundle bundle= msg.getData();
                    byte[] resImage= bundle.getByteArray("pix");
                    final Bitmap bitmapBuf= BitmapFactory.decodeByteArray(resImage,0,resImage.length);
                    MyApplication.setBitmap(bitmapBuf);
                    userImage.setImageBitmap(bitmapBuf);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ImgUtils.saveImageToGallery(MyApplication.getUserPhone(),bitmapBuf);
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);
        userImage= (CircleImageView) findViewById(R.id.user_image);
        userName= (TextView) findViewById(R.id.user_name);
        if(MyApplication.getBitmap()==null) {
            Bitmap bitmapBuf = ImgUtils.getImageFromSD(MyApplication.getUserPhone());
            if (bitmapBuf == null) {
                HttpUtils.pullImage(MyApplication.getUserPhone(), handler);
            }
            else {
                MyApplication.setBitmap(bitmapBuf);
                userImage.setImageBitmap(bitmapBuf);
            }
        }else {
            userImage.setImageBitmap(MyApplication.getBitmap());
        }
        userName.setText(MyApplication.getUserName());
        TextView userInformation= (TextView) findViewById(R.id.user_information);
        TextView personalInformation= (TextView) findViewById(R.id.personal_information);
        TextView changePersonalInformation= (TextView) findViewById(R.id.change_personal_information);
        TextView myPurse= (TextView) findViewById(R.id.my_purse);
        TextView changeMyPurse = (TextView) findViewById(R.id.change_my_purse);
        TextView myOrder= (TextView) findViewById(R.id.my_order);
        TextView changeMyOrder= (TextView) findViewById(R.id.change_my_order);
        TextView mySetting= (TextView) findViewById(R.id.my_setting);
        TextView change_my_setting= (TextView) findViewById(R.id.change_my_setting);
        userImage.setOnClickListener(this);
        userInformation.setOnClickListener(this);
        personalInformation.setOnClickListener(this);
        changePersonalInformation.setOnClickListener(this);
        myPurse.setOnClickListener(this);
        changeMyPurse.setOnClickListener(this);
        myOrder.setOnClickListener(this);
        changeMyOrder.setOnClickListener(this);
        mySetting.setOnClickListener(this);
        change_my_setting.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!userName.equals(MyApplication.getUserName())){
            userName.setText(MyApplication.getUserName());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_information:
                Intent intent= new Intent(MyInformation.this,AccountInformation.class);
                startActivity(intent);
                break;
            case R.id.user_image:
                showListDialog();
                break;
            case R.id.personal_information:
                break;
            case R.id.change_personal_information:
                break;
            case R.id.my_purse:
                break;
            case R.id.change_my_purse:
                break;
            case R.id.my_order:
                break;
            case R.id.change_my_order:
                break;
            case R.id.my_setting:
                break;
            case R.id.change_my_setting:
                break;
            default:
                break;
        }
    }

    private void showListDialog() {
        final String[] items = { "拍照","从相册选取"};
        final AlertDialog.Builder listDialog = new AlertDialog.Builder(MyInformation.this);
        listDialog.setTitle("选择图片");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.cancel();
                        ImgUtils.getImageFromCamera(MyInformation.this);
                        break;
                    case 1:
                        dialog.cancel();
                        ImgUtils.getImageFromAlbum(MyInformation.this);
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleBitmap(MyApplication.getBitmap());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case ImgUtils.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24){
                        Uri imageUri= FileProvider.getUriForFile(this,"com.example.ai.dtest.fileprovider",new File(mCameraFile));
                        ImgUtils.startPhotoZoom(imageUri,this);
                    }
                    else {
                        Uri imageUri= Uri.fromFile(new File(mCameraFile));
                        ImgUtils.startPhotoZoom(imageUri,this);
                    }
                }
                break;
            case ImgUtils.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24) {
                        String imagePath = getImagePath.getPath(MyInformation.this, data.getData());
                        assert imagePath != null;
                        Uri imageUri= FileProvider.getUriForFile(MyInformation.this,"com.example.ai.dtest.fileprovider",new File(imagePath));
                        ImgUtils.startPhotoZoom(imageUri,this);
                    }
                    else {
                        Uri imageUri= data.getData();
                        ImgUtils.startPhotoZoom(imageUri,this);
                    }
                }
                break;
            case ImgUtils.CROP_PHOTO:
                if(resultCode==RESULT_OK) {
                    HttpUtils.pushImage(MyApplication.getUserPhone(),mCropFile,handler);
                }
                break;
            default:
                break;
        }
    }

    private void recycleBitmap(Bitmap bitmap){
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
        }
        System.gc();
    }
}
