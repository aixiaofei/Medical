package com.example.ai.dtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.ai.dtest.base.BaseActivity;
import com.example.ai.dtest.base.MyApplication;
import com.example.ai.dtest.data.FamilyInfo;
import com.example.ai.dtest.data.Usersick;
import com.example.ai.dtest.frag.ConditionShow;
import com.example.ai.dtest.util.HttpUtils;
import com.example.ai.dtest.util.ImgUtils;
import com.example.ai.dtest.util.getImagePath;
import com.example.ai.dtest.view.selectFamilyInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.ai.dtest.base.MyApplication.getContext;
import static com.example.ai.dtest.util.ImgUtils.cameraIdentify;
import static com.example.ai.dtest.util.ImgUtils.identifyPath;

/**
 * Created by ai on 2017/8/11.
 */

public class ReleaseCondition extends BaseActivity implements View.OnClickListener{

    private Usersick lastInfo;

    private EditText conditionDec;

    private TextView name;

    private TextView department;

    private Button addName;

    private Button addDepartment;

    private ImageView addPicture;

    private ImageView[] fig= new ImageView[4];

    private FamilyInfo currentInfo;

    private int currentFig;

    private String[] selectPath= new String[4];

    private int figNumber;

    private static final String[] paths={"first","second","third","forth"};

    private static final int CONPLETE= 100;

    private static final String IMAGEURI= HttpUtils.SOURCEIP+ "/internetmedical/user/getsickpic/";

    private ProgressDialog dialog;

    private int startMode;

    private List<String> choosePhoto= new ArrayList<>();

    private Handler handler= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 100:
                    submit();
                    break;
                case HttpUtils.ADDCONDITIONFA:
                    dialog.cancel();
                    if(startMode==0) {
                        Toast.makeText(ReleaseCondition.this, "上传失败", Toast.LENGTH_LONG).show();
                    }else if(startMode==1){
                        Toast.makeText(ReleaseCondition.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                    break;
                case HttpUtils.ADDCONDITIONSU:
                    dialog.cancel();
                    if(startMode==0) {
                        Toast.makeText(ReleaseCondition.this, "上传成功", Toast.LENGTH_LONG).show();
                    }else if(startMode==1){
                        Toast.makeText(ReleaseCondition.this, "修改成功", Toast.LENGTH_LONG).show();
                    }
                    clean();
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
                    Intent intent= new Intent(ReleaseCondition.this,MainActivity.class);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
                case HttpUtils.PULLONECONFA:
                    break;
                case HttpUtils.PULLONECONSU:
                    Bundle bundle= msg.getData();
                    lastInfo= (Usersick) bundle.getSerializable("result");
                    initInfo();
                    break;
                default:
                    break;
            }
        }
    };

    public static void actionStart(Activity activity, String startMode, String id){
        Intent intent= new Intent(activity,ReleaseCondition.class);
        intent.putExtra("mode",startMode);
        intent.putExtra("id",id);
        activity.startActivityForResult(intent,MainActivity.RELEASE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.release_condition);
        Intent intent= getIntent();

        ImageView backFig= (ImageView) findViewById(R.id.design_back_fig);
        TextView backText= (TextView) findViewById(R.id.design_back_text);
        backFig.setOnClickListener(this);
        backText.setOnClickListener(this);

        conditionDec= (EditText) findViewById(R.id.illness_remind);
        name= (TextView) findViewById(R.id.patient_name);
        department= (TextView) findViewById(R.id.department_info);

        addName= (Button) findViewById(R.id.add_patient);
        addDepartment= (Button) findViewById(R.id.add_department);
        addPicture= (ImageView) findViewById(R.id.add_picture);
        addName.setOnClickListener(this);
        addDepartment.setOnClickListener(this);
        addPicture.setOnClickListener(this);

        fig[0]=(ImageView) findViewById(R.id.info_first);
        fig[1]=(ImageView) findViewById(R.id.info_second);
        fig[2]=(ImageView) findViewById(R.id.info_third);
        fig[3]=(ImageView) findViewById(R.id.info_forth);

        TextView submit = (TextView) findViewById(R.id.submit_button);
        fig[0].setOnClickListener(this);
        fig[1].setOnClickListener(this);
        fig[2].setOnClickListener(this);
        fig[3].setOnClickListener(this);
        submit.setOnClickListener(this);

        process(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    private void process(Intent intent){
        String mode= intent.getStringExtra("mode");
        if(mode.equals(ConditionShow.ADD)){
            startMode=0;
        }else if(mode.equals(ConditionShow.CHANGE)){
            String id= intent.getStringExtra("id");
            startMode=1;
            HttpUtils.pullOneCondition(id,handler);
        }
    }

    private void initInfo(){
        conditionDec.setText(lastInfo.getUsersickdesc());
        conditionDec.setSelection(lastInfo.getUsersickdesc().length());
        name.setText(lastInfo.getFamilyname());
        String[] paths= lastInfo.getUsersickpic().split(",");
        for(int i=0;i<paths.length;i++){
            if(!TextUtils.isEmpty(paths[i])){
                loadImage(fig[i],IMAGEURI+paths[i]);
                figNumber+=1;
            }
        }
        if(figNumber==4){
            addPicture.setClickable(false);
        }
    }

    private void loadImage(ImageView imageView,String path){
        Uri uri= Uri.parse(path);
        Glide.with(getContext())
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    private void clean(){
        for (ImageView aFig : fig) {
            if(aFig.getDrawable() instanceof BitmapDrawable){
                Bitmap bitmap= ((BitmapDrawable) aFig.getDrawable()).getBitmap();
                ImgUtils.recycleBitmap(bitmap);
            }
        }
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
            case R.id.add_patient:
                showAddPatient();
                break;
            case R.id.add_department:
                break;
            case R.id.add_picture:
                showListDialog();
                break;
            case R.id.info_first:
                showDeleteDialog(1);
                break;
            case R.id.info_second:
                showDeleteDialog(2);
                break;
            case R.id.info_third:
                showDeleteDialog(3);
                break;
            case R.id.info_forth:
                showDeleteDialog(4);
                break;
            case R.id.submit_button:
                trySubmit();
                break;
            default:
                break;
        }
    }

    private void showAddPatient(){
        final selectFamilyInfo dialog= new selectFamilyInfo(this);
        dialog.setListener(new selectFamilyInfo.selectFamily() {
            @Override
            public void select(FamilyInfo info) {
                dialog.dismiss();
                currentInfo= info;
                name.setText(currentInfo.getFamilyname());
            }
        });
        dialog.show();
    }


    private void showListDialog() {
        final String[] items = { "拍照","从相册选取"};
        final AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("选择图片");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dialog.cancel();
                        ImgUtils.getImageFromCamera(ReleaseCondition.this,cameraIdentify);
                        for(int i=0;i<fig.length;i++){
                            if(fig[i].getDrawable()==null){
                                currentFig=i+1;
                                break;
                            }
                        }
                        break;
                    case 1:
                        dialog.cancel();
                        ImgUtils.getImageFromAlbum(ReleaseCondition.this);
                        for(int i=0;i<fig.length;i++){
                            if(fig[i].getDrawable()==null){
                                currentFig=i+1;
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        listDialog.show();
    }


    private void showDeleteDialog(final int position) {
        if(fig[position-1].getDrawable()!=null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.myDialog);
            builder.setMessage("确认删除此照片!")
                    .setTitle("提示")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fig[position-1].setImageDrawable(null);
                            figNumber-=1;
                            if(figNumber<4){
                                addPicture.setClickable(true);
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

    private void trySubmit(){
        if(TextUtils.isEmpty(conditionDec.getText()) || TextUtils.isEmpty(name.getText())){
            Toast.makeText(this,"请继续填写信息!",Toast.LENGTH_SHORT).show();
            return;
        }
        dialog= new ProgressDialog(this,R.style.myDialog);
        dialog.setMessage("上传中...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();
        saveImage();
    }

    private void submit(){
        Usersick info= new Usersick();
        if(currentInfo!=null) {
            info.setFamliyid(currentInfo.getFamilyid());
        }else {
            info.setFamliyid(lastInfo.getFamliyid());
        }
        if(startMode==1){
            info.setUsersickid(lastInfo.getUsersickid());
        }
        info.setPhone(MyApplication.getUserPhone());
        info.setUsersickdesc(conditionDec.getText().toString());
        Date buf_date= new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(buf_date);
        info.setUsersicktime(date);
        HttpUtils.addCondition(info,selectPath,handler);
    }

    private void saveImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<fig.length;i++){
                    if(fig[i].getDrawable()!=null){
                        final String fileName = MyApplication.getUserPhone()+paths[i];
                        Bitmap bitmap=null;
                        if(fig[i].getDrawable() instanceof BitmapDrawable){
                            bitmap= ((BitmapDrawable) fig[i].getDrawable()).getBitmap();
                        }else if(fig[i].getDrawable() instanceof GlideBitmapDrawable){
                            bitmap=((GlideBitmapDrawable) fig[i].getDrawable()).getBitmap();
                        }
                        ImgUtils.saveImageToGallery(fileName,identifyPath,bitmap);
                        selectPath[i]=(identifyPath + File.separator + MyApplication.getUserPhone()+paths[i]+".png");
                    }
                }
                Message message= new Message();
                message.what= CONPLETE;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void processFig(Uri imageUri){
        figNumber+=1;
        if(figNumber==4){
            addPicture.setClickable(false);
        }
        try {
            final Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            fig[currentFig-1].setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
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
                break;
            case ImgUtils.CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    String imagePath = getImagePath.getPath(ReleaseCondition.this, data.getData());
                    boolean canSelect=true;
                    for(String path:choosePhoto){
                        if(!TextUtils.isEmpty(path) && path.equals(imagePath)){
                            canSelect=false;
                            break;
                        }
                    }
                    if(canSelect) {
                        figNumber+=1;
                        if(figNumber==4){
                            addPicture.setClickable(false);
                        }
                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                        fig[currentFig-1].setImageBitmap(bitmap);
                        choosePhoto.add(imagePath);
                    }
                    else {
                        Toast.makeText(this,"请不要选择重复图片!",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
}
