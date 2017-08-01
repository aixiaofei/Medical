package com.example.ai.dtest.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.example.ai.dtest.frag.DoctorList;

/**
 * Created by ai on 2017/8/1.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Context context;

    final int PAGE_COUNT=5;

    private String[] tabTitles= {"首页","1","2","3","我的"};

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
//        if(position==0) {
            return new DoctorList();
//        }
//        else {
//            return null;
//        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

//    public View getTabView(){}
}
