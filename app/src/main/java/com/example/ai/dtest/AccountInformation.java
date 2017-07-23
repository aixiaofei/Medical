package com.example.ai.dtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ai.dtest.commen.MyApplication;
import com.example.ai.dtest.yanzhengma.FormatCheckUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountInformation extends BaseActivity implements View.OnClickListener{

    CircleImageView userSculpture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_management);
        userSculpture= (CircleImageView) findViewById(R.id.user_sculpture);
        if(MyApplication.getBitmap()!=null){
            userSculpture.setImageBitmap(MyApplication.getBitmap());
        }
        else {
            userSculpture.setImageResource(R.drawable.defaultuserimage);
        }
        ImageView backFig= (ImageView) findViewById(R.id.back_fig);
        TextView backText= (TextView) findViewById(R.id.back_text);
        TextView userNickname = (TextView) findViewById(R.id.user_nickname);
        TextView userPhone= (TextView) findViewById(R.id.user_phone);
        TextView revisepassword= (TextView) findViewById(R.id.revise_password);
        TextView exitLogin= (TextView) findViewById(R.id.exit_login);
        userPhone.setText(MyApplication.getUserPhone());
        userNickname.setText(MyApplication.getUserName());
        backFig.setOnClickListener(this);
        backText.setOnClickListener(this);
        revisepassword.setOnClickListener(this);
        exitLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_fig:
                finish();
                break;
            case R.id.back_text:
                finish();
                break;
            case R.id.user_nickname:

                break;
            case R.id.revise_password:
                break;
            case R.id.exit_login:
                break;
            default:
                break;
        }
    }

    private void showInputDialog() {
        final EditText editText = new EditText(AccountInformation.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(AccountInformation.this);
        inputDialog.setTitle("请输入昵称").setView(editText);
        inputDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(editText.getText().toString())){
                            Toast.makeText(AccountInformation.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                        }else if(FormatCheckUtils.isNumber(editText.getText().toString())){
                            Toast.makeText(AccountInformation.this,"昵称不能全为数字",Toast.LENGTH_SHORT).show();
                        }else {
                            String nickName= editText.getText().toString();

                        }
                    }
                }).show();
    }
}
