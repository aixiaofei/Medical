package com.example.ai.dtest.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import com.example.ai.dtest.R;

/**
 * Created by ai on 2017/8/5.
 */

public class loadDialog extends Dialog {

    private load loading;

    public loadDialog(@NonNull Context context) {
        super(context, R.style.loadDialogP);
    }

    public loadDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        loading= (load) findViewById(R.id.load);
        loading.setVisibility(View.VISIBLE);
        complete complete= (complete) findViewById(R.id.complete);
        complete.setVisibility(View.GONE);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
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
