package com.example.ai.dtest.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by ai on 2017/8/2.
 */

public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    //声明一个LinearLayoutManager
//    private LinearLayoutManager mLinearLayoutManager;

    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

//    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
//        this.mLinearLayoutManager = linearLayoutManager;
//    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager mLinearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
        super.onScrolled(recyclerView, dx, dy);
//        Log.d("ai","11");
        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        if(loading){
//            Log.d("ai","firstVisibleItem: " +firstVisibleItem);
//            Log.d("ai","totalPageCount:" +totalItemCount);
//            Log.d("ai", "visibleItemCount:" + visibleItemCount);
            if(totalItemCount > previousTotal){
                //说明数据已经加载结束
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        //这里需要好好理解
        if (!loading && (totalItemCount-visibleItemCount) <= firstVisibleItem){
            onLoadMore();
            loading = true;
        }
    }

    /**
     * 提供一个抽象方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     * */
    public abstract void onLoadMore();
}
