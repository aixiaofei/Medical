package com.example.ai.dtest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ai on 2017/7/24.
 */

public class okView extends View {

    private Paint circlePaint;

    private Paint okPaint;

    private Path circlePath;

    private PathEffect circleEffect;

    private float circleLength;

    private Path okPath;

    private PathEffect okEffect;

    private float okLength;

    private boolean isOK= false;

    private boolean isCircle= false;

    private float width,height;

    private float defaultWidth,defaultHeight;

    private ValueAnimator circleAnima;

    private ValueAnimator delayAnima;

    private ValueAnimator okAnima;

    public okView(Context context) {
        this(context,null);
        initPaint();
    }

    public okView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint(){
        circlePaint= new Paint();
        circlePaint.setColor(0xff969696);
        circlePaint.setStrokeWidth(dp2px(1));
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);

        okPaint= new Paint();
        okPaint.setColor(0xff969696);
        okPaint.setAntiAlias(true);
        okPaint.setStrokeWidth(dp2px(1));
        okPaint.setStyle(Paint.Style.STROKE);

    }

    public void start(){
        circleAnima = ValueAnimator.ofFloat(circleLength, 0);
        circleAnima.setDuration(1000);
        circleAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isCircle=true;
                circleEffect= new DashPathEffect(new float[]{circleLength,circleLength},(float) valueAnimator.getAnimatedValue());
                circlePaint.setPathEffect(circleEffect);
                postInvalidate();
            }
        });
        okAnima = ValueAnimator.ofFloat(okLength, 0);
        okAnima.setDuration(1000);
        okAnima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isOK=true;
                okEffect= new DashPathEffect(new float[]{okLength,okLength},(float) valueAnimator.getAnimatedValue());
                okPaint.setPathEffect(okEffect);
                postInvalidate();
            }
        });
        delayAnima= ValueAnimator.ofFloat(1,0);
        delayAnima.setDuration(5000);
        circleAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                    okAnima.start();
            }
        });
        okAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                delayAnima.start();
            }
        });
        delayAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animator) {
                    okAnima.start();
            }
        });
        circleAnima.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);

        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        int heightMy= dp2px(20);

        if(heightMode==MeasureSpec.EXACTLY||heightMode==MeasureSpec.AT_MOST){
            defaultHeight=heightMy;
            height=dp2px(18);
        }
        if(widthMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST){
            defaultWidth=heightMy;
            width=dp2px(18);
        }
        setMeasuredDimension((int) defaultWidth,(int) defaultHeight);
        initPath();
    }

    private void initPath(){
        circlePath= new Path();
        circlePath.addCircle(defaultWidth/2,defaultHeight/2,height/2, Path.Direction.CCW);
        PathMeasure circleMeasure = new PathMeasure(circlePath, false);
        circleLength= circleMeasure.getLength();

        okPath= new Path();
        okPath.moveTo(defaultWidth / 4 * 1.2f, defaultWidth / 2);
        okPath.lineTo(defaultWidth / 2, defaultWidth / 3 * 2);
        okPath.lineTo(defaultWidth / 4 * 2.9f, defaultWidth / 4 * 1.5f);
        PathMeasure okMeasure = new PathMeasure(okPath, false);
        okLength= okMeasure.getLength();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isCircle) {
            canvas.drawPath(circlePath, circlePaint);
        }
        if(isOK){
            canvas.drawPath(okPath,okPaint);
        }
    }

    public void clear(){
        if(circleAnima!=null && circleAnima.isRunning()){
            circleAnima.pause();
            circleAnima=null;
        }
        if(okAnima!=null && okAnima.isRunning()){
            okAnima.pause();
            okAnima=null;
        }
        if(delayAnima!=null && delayAnima.isRunning()){
            delayAnima.pause();
            delayAnima=null;
        }
        isCircle=false;
        isOK=false;
        postInvalidate();
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
