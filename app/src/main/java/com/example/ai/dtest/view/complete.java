package com.example.ai.dtest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.ai.dtest.R;

/**
 * Created by ai on 2017/8/8.
 */

public class complete extends View {

    private Paint circlePaint;

    private Paint pathPaint;

    private Path path;

    private DashPathEffect effect;

    private float length;

    private float width,height;

    private float defaultWidth;

    private float sureWidth;

    private ValueAnimator animator;

    public complete(Context context) {
        super(context);
        initPaint();
        initData();
    }

    public complete(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initData();
    }

    private void initPaint(){
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(getResources().getColor(R.color.colorPrimary));

        pathPaint= new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setColor(getResources().getColor(R.color.colorBackground));
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeWidth(dp2px(2));
    }

    private void initData(){
        defaultWidth= dp2px(32);
        sureWidth= dp2px(30);
        initPath();
    }

    private void initPath(){
        path= new Path();
        path.moveTo(defaultWidth / 4 * 1.2f, defaultWidth / 2);
        path.lineTo(defaultWidth / 2, defaultWidth / 3 * 2);
        path.lineTo(defaultWidth / 4 * 2.9f, defaultWidth / 4 * 1.5f);
        PathMeasure okMeasure = new PathMeasure(path, false);
        length= okMeasure.getLength();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);

        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode==MeasureSpec.EXACTLY||heightMode==MeasureSpec.AT_MOST){
            height=defaultWidth;
        }
        if(widthMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST){
            width=defaultWidth;
        }
        setMeasuredDimension((int) width,(int) height);
    }

//    public void startAnima(){
//        if(animator!=null&&animator.isRunning()){
//            animator.pause();
//        }
//        animator= ValueAnimator.ofFloat(length,0);
//        animator.setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                effect= new DashPathEffect(new float[]{length,length},(float) valueAnimator.getAnimatedValue());
//                pathPaint.setPathEffect(effect);
//                postInvalidate();
//            }
//        });
//        animator.start();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(defaultWidth/2,defaultWidth/2,sureWidth/2,circlePaint);
        canvas.drawPath(path,pathPaint);
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }
}
