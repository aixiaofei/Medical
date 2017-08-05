package com.example.ai.dtest.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ai.dtest.R;
import com.example.ai.dtest.base.ActivityCollector;
import com.gyf.barlibrary.ImmersionBar;

public class BaseActivity extends AppCompatActivity {

    private ImmersionBar mImmersionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this).statusBarColor(R.color.colorPrimary).fitsSystemWindows(true);
        mImmersionBar.init();   //所有子类都将继承这些相同的属性
        Log.d("ai",getClass().getSimpleName());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImmersionBar.destroy();
        ActivityCollector.removeActivity(this);
    }
}
