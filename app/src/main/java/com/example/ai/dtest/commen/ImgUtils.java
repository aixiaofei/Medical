package com.example.ai.dtest.commen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class ImgUtils {

    public static final int TAKE_PHOTO=1;

    public static final int CHOOSE_PHOTO=2;

    public static final int CROP_PHOTO=3;

    public static final String mCameraFile = MyApplication.getContext().getExternalCacheDir()+File.separator+MyApplication.getUserPhone()+"1"+".png";

    public static final String mCropFile = MyApplication.getContext().getExternalCacheDir()+File.separator+MyApplication.getUserPhone()+".png";

    private static final String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test";

    //保存文件到指定路径
    public static boolean saveImageToGallery(String userPhone,Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = userPhone+".png";
        File file = new File(appDir, fileName);
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
////             其次把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(), fileName, null);
//
////            保存图片后发送广播通知更新数据库
//            Uri uri = Uri.fromFile(file);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Bitmap getImageFromSD (String userPhone) {
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = userPhone + ".png";
        String Path = storePath+File.separator+fileName;
        File file = new File(appDir, fileName);
        Bitmap bitmap = null;
        try {
            if(file.exists()) {
                try {
                    bitmap = BitmapFactory.decodeFile(Path);
                } catch (OutOfMemoryError e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
                e.printStackTrace();
            }
        return bitmap;
    }

    public static Bitmap zoonBitmap(String imagePath){
        Bitmap bitmapBuf;
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(imagePath,options);
        int width = options.outWidth;
        int height= options.outHeight;
        double widthScale= 1;
        double heightScale= 1;
        if(width>512){
            widthScale= (double) width/512;
        }
        if(height>512){
            heightScale= (double) height/512;
        }
        options.inJustDecodeBounds=false;
        options.inSampleSize=(int) Math.max(widthScale,heightScale);
        bitmapBuf= BitmapFactory.decodeFile(imagePath,options);
        return bitmapBuf;
    }

    public static void getImageFromCamera(Activity activity){
        Uri imageUri;
        File outputImage = new File(mCameraFile);
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent= new Intent("android.media.action.IMAGE_CAPTURE");
        if(Build.VERSION.SDK_INT>=24){
            imageUri = FileProvider.getUriForFile(activity,"com.example.ai.dtest.fileprovider",new File(mCameraFile));
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        else {
            imageUri= Uri.fromFile(outputImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        }
        activity.startActivityForResult(intent,TAKE_PHOTO);
    }

    public static void getImageFromAlbum(Activity activity){
        Intent intent= new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(intent,CHOOSE_PHOTO);
    }

    public static void startPhotoZoom(Uri inputUri,Activity activity) {
        if (inputUri == null) {
            Log.e("ai","The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        File outputImage = new File(mCropFile);
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri outPutUri = Uri.fromFile(outputImage);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("ai","crop1");
            intent.setDataAndType(inputUri, "image/*");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = getImagePath.getPath(activity, inputUri);//这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
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
        activity.startActivityForResult(intent, CROP_PHOTO);//这里就将裁剪后的图片的Uri返回了
    }
}
