package com.example.ai.dtest;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.example.ai.dtest.commen.ImgUtils.mCameraFile;
import static com.example.ai.dtest.commen.ImgUtils.mCropFile;

public class MyInformation extends BaseActivity implements View.OnClickListener{

    private TextView userName;

    private CircleImageView userImage;

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HttpUtils.PUSHSUCESS:
                    Log.d("ai","22");
                    Toast.makeText(MyInformation.this,"修改头像成功",Toast.LENGTH_SHORT).show();
//                    String imagePath= getImagePath.getPath(MyInformation.this,imageUri);
//                    Bitmap bitmapBuf = BitmapFactory.decodeFile(imagePath);
//                    ImgUtils.saveImageToGallery(MyInformation.this,MyApplication.getUserPhone(),bitmapBuf);
//                    Bitmap bitmapOrigin= MyApplication.getBitmap();
//                    recycleBitmap(bitmapOrigin);
//                    MyApplication.setBitmap(bitmapBuf);
//                    userImage.setImageBitmap(MyApplication.getBitmap());
                    break;
                case HttpUtils.PUSHFAILURE:
                    Toast.makeText(MyInformation.this,"上传失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PUSHNOFILE:
                    Toast.makeText(MyInformation.this,"选择图片失败",Toast.LENGTH_SHORT).show();
                    break;
                case HttpUtils.PULLFAILURE:
                    Bitmap bitmapDefault=BitmapFactory.decodeResource(getResources(),R.drawable.defaultuserimage);
                    ImgUtils.saveImageToGallery(MyInformation.this,MyApplication.getUserPhone(),bitmapDefault);
                    MyApplication.setBitmap(bitmapDefault);
                    userImage.setImageBitmap(bitmapDefault);
                    break;
                case HttpUtils.PULLSUCESS:
                    Log.d("ai","55");
                    Bundle bundle= msg.getData();
                    byte[] resImage= bundle.getByteArray("pix");
                    Bitmap bitmapBuf= BitmapFactory.decodeByteArray(resImage,0,resImage.length);
                    MyApplication.setBitmap(bitmapBuf);
                    userImage.setImageBitmap(bitmapBuf);
                    ImgUtils.saveImageToGallery(MyInformation.this,MyApplication.getUserPhone(),bitmapBuf);
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
            Log.d("ai","11");
            Bitmap bitmapBuf = ImgUtils.getImageFromSD(MyApplication.getUserPhone());
            if (bitmapBuf == null) {
                Log.d("ai","22");
                HttpUtils.pullImage(MyApplication.getUserPhone(), handler);
            }
            else {
                Log.d("ai","33");
                MyApplication.setBitmap(bitmapBuf);
                userImage.setImageBitmap(bitmapBuf);
            }
        }else {
            Log.d("ai","44");
            userImage.setImageBitmap(MyApplication.getBitmap());
        }
        setUserName();
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

    private void setUserName(){
        userName.setText(MyApplication.getUserName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycleBitmap(MyApplication.getBitmap());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ImgUtils.TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24){
                        Uri imageUri= FileProvider.getUriForFile(this,"com.example.ai.dtest.fileprovider",new File(mCameraFile));
                        startPhotoZoom(imageUri);
                    }
                    else {
                        Uri imageUri= Uri.fromFile(new File(mCameraFile));
                        startPhotoZoom(imageUri);
                    }
                }
                break;
            case ImgUtils.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=24) {
                        String imagePath = getImagePath.getPath(MyInformation.this, data.getData());
                        assert imagePath != null;
                        Uri imageUri= FileProvider.getUriForFile(MyInformation.this,"com.example.ai.dtest.fileprovider",new File(imagePath));
                        startPhotoZoom(imageUri);
                    }
                    else {
                        Uri imageUri= data.getData();
                        startPhotoZoom(imageUri);
                    }
                }
                break;
            case ImgUtils.CROP_PHOTO:
                if(resultCode==RESULT_OK) {
                    Log.d("ai","crop");
                    Uri imageUri = Uri.fromFile(new File(mCropFile));
                    handleImage(imageUri);
                }
                break;
            default:
                break;
        }
    }

    private void handleImage(Uri imageUri){
        if(imageUri!=null){
            Log.d("ai","33");
            String imagePath= getImagePath.getPath(MyInformation.this,imageUri);
//            HttpUtils.pushImage(MyApplication.getUserPhone(),imagePath,handler);
            Bitmap bitmapBuf = BitmapFactory.decodeFile(imagePath);
            ImgUtils.saveImageToGallery(MyInformation.this,MyApplication.getUserPhone(),bitmapBuf);
            Bitmap bitmapOrigin= MyApplication.getBitmap();
            recycleBitmap(bitmapOrigin);
            MyApplication.setBitmap(bitmapBuf);
            userImage.setImageBitmap(MyApplication.getBitmap());
        }
    }

    private void recycleBitmap(Bitmap bitmap){
        if(bitmap != null && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
            bitmap=null;
        }
        System.gc();
    }

    private void startPhotoZoom(Uri inputUri) {
        if (inputUri == null) {
            Log.e("ai","The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File outputImage = new File(mCropFile);
        if(outputImage.exists()){
            outputImage.delete();
        }
        Uri outPutUri = Uri.fromFile(outputImage);
        Log.d("ai","crop1");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(inputUri, "image/*");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = getImagePath.getPath(this,inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                assert url != null;
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                intent.setDataAndType(inputUri, "image/*");
            }
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 512);
        intent.putExtra("outputY", 512);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        startActivityForResult(intent, ImgUtils.CROP_PHOTO);//这里就将裁剪后的图片的Uri返回了
    }

//    private byte[] bitmapToBytes(Bitmap bitmap){
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//        return baos.toByteArray();
//    }
}
