package com.example.ai.dtest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.ai.dtest.R;

/**
 * Created by ai on 2017/8/5.
 */

public class loadDialog extends Dialog {

    private load loading;

    public loadDialog(@NonNull Context context) {
        super(context, R.style.loadDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        loading= (load) findViewById(R.id.load);
//        setCanceledOnTouchOutside(false);
//        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        loading.loadAnima();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loading.clearAnima();
    }

}
