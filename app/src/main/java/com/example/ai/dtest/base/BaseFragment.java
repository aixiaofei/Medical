package com.example.ai.dtest.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by ai on 2017/8/11.
 */

public class BaseFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ai","create");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ai","start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("ai","resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("ai","pause");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("ai","changeHidden");
    }
}
