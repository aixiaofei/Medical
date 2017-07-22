package com.example.ai.dtest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import commen.MyApplication;

public class MyInformation extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);
        ImageView userImage= (ImageView) findViewById(R.id.user_image);
        TextView userName= (TextView) findViewById(R.id.user_name);
        userImage.setImageResource(R.drawable.defaultuserimage);
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_information:

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
}
