package com.example.ai.dtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{

    Button login;

    EditText user_name;

    EditText user_password;

    TextView return_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login= (Button) findViewById(R.id.login);
        user_name= (EditText) findViewById(R.id.name);
        user_password= (EditText) findViewById(R.id.password);
        return_register= (TextView) findViewById(R.id.return_register);
        login.setOnClickListener(this);
        return_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                break;
            case R.id.return_register:
                Intent intent= new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
